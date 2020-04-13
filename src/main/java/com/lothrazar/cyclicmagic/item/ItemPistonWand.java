/*******************************************************************************
 * The MIT License (MIT)
 * 
 * Copyright (C) 2014-2018 Sam Bassett (aka Lothrazar)
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 ******************************************************************************/
package com.lothrazar.cyclicmagic.item;

import java.util.List;
import com.google.common.collect.Lists;
import com.lothrazar.cyclicmagic.IContent;
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.data.IHasRecipe;
import com.lothrazar.cyclicmagic.item.core.BaseTool;
import com.lothrazar.cyclicmagic.net.PacketMoveBlock;
import com.lothrazar.cyclicmagic.registry.ItemRegistry;
import com.lothrazar.cyclicmagic.registry.LootTableRegistry;
import com.lothrazar.cyclicmagic.registry.RecipeRegistry;
import com.lothrazar.cyclicmagic.registry.SoundRegistry;
import com.lothrazar.cyclicmagic.util.Const;
import com.lothrazar.cyclicmagic.util.UtilChat;
import com.lothrazar.cyclicmagic.util.UtilNBT;
import com.lothrazar.cyclicmagic.util.UtilSound;
import net.minecraft.block.BlockSnow;
import net.minecraft.block.state.BlockPistonStructureHelper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemPistonWand extends BaseTool implements IHasRecipe, IContent {

  private static final int durability = 5000;

  public ItemPistonWand() {
    super(durability);
  }

  public enum ActionType {

    PUSH, PULL, ROTATE;

    private final static String NBT = "ActionType";
    private final static String NBTTIMEOUT = "timeout";

    public static int getTimeout(ItemStack wand) {
      return UtilNBT.getItemStackNBT(wand).getInteger(NBTTIMEOUT);
    }

    public static void setTimeout(ItemStack wand) {
      UtilNBT.getItemStackNBT(wand).setInteger(NBTTIMEOUT, 15);//less than one tick
    }

    public static void tickTimeout(ItemStack wand) {
      NBTTagCompound tags = UtilNBT.getItemStackNBT(wand);
      int t = tags.getInteger(NBTTIMEOUT);
      if (t > 0) {
        UtilNBT.getItemStackNBT(wand).setInteger(NBTTIMEOUT, t - 1);
      }
    }

    public static ActionType get(ItemStack wand) {
      if (wand == null) {
        return ActionType.PUSH;
      }
      NBTTagCompound tags = UtilNBT.getItemStackNBT(wand);
      int get = tags.getInteger(NBT);
      if (get >= ActionType.values().length) {
        return ActionType.PUSH;
      }
      return ActionType.values()[get];
    }

    public static String getName(ItemStack wand) {
      return "tool.action." + ActionType.get(wand).toString().toLowerCase();
    }

    public static void toggle(ItemStack wand) {
      NBTTagCompound tags = UtilNBT.getItemStackNBT(wand);
      int type = tags.getInteger(NBT);
      type++;
      if (type > PULL.ordinal()) {
        type = PUSH.ordinal();
      }
      tags.setInteger(NBT, type);
      wand.setTagCompound(tags);
    }
  }

  @Override
  public String getContentName() {
    return "tool_push";
  }

  @Override
  public void register() {
    ItemRegistry.register(this, getContentName());
    ModCyclic.instance.events.register(this);
    LootTableRegistry.registerLoot(this);
  }

  private boolean enabled;

  @Override
  public boolean enabled() {
    return enabled;
  }

  @Override
  public void syncConfig(Configuration config) {
    enabled = config.getBoolean("PistonScepter", Const.ConfigCategory.content, true, getContentName() + Const.ConfigCategory.contentDefaultText);
  }

  @SubscribeEvent
  public void onHit(PlayerInteractEvent.LeftClickBlock event) {
    EntityPlayer player = event.getEntityPlayer();
    ItemStack held = player.getHeldItem(event.getHand());
    if (held != null && held.getItem() == this) {
      if (ActionType.getTimeout(held) > 0) {
        //without a timeout, this fires every tick. so you 'hit once' and get this happening 6 times
        return;
      }
      ActionType.setTimeout(held);
      event.setCanceled(true);
      UtilSound.playSound(player, player.getPosition(), SoundRegistry.tool_mode, SoundCategory.PLAYERS);
      if (!player.getEntityWorld().isRemote) { // server side
        ActionType.toggle(held);
        UtilChat.sendStatusMessage(player, UtilChat.lang(ActionType.getName(held)));
      }
    }
  }

  @Override
  public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
    ItemStack stack = player.getHeldItem(hand);
    //    TileEntityPiston x;
    ActionType action = ActionType.get(stack);
    switch (action) {
      case PUSH:
        UtilSound.playSound(player, SoundEvents.BLOCK_PISTON_EXTEND);
        EnumFacing direction = side.getOpposite();
        boolean extending = true;
        BlockPistonStructureHelper blockpistonstructurehelper = new BlockPistonStructureHelper(
            worldIn, pos.offset(side), direction, extending);
        if (blockpistonstructurehelper.canMove()) {
          //
          List<BlockPos> blocksToMove = blockpistonstructurehelper.getBlocksToMove();
          List<IBlockState> copyStates = Lists.<IBlockState> newArrayList();
          for (int i = 0; i < blocksToMove.size(); ++i) {
            BlockPos blockpos = blocksToMove.get(i);
            copyStates.add(worldIn.getBlockState(blockpos).getActualState(worldIn, blockpos));
          }
          List<BlockPos> toDestroy = blockpistonstructurehelper.getBlocksToDestroy();///new ArrayList(list);// 
          int k = blocksToMove.size() + toDestroy.size();
          IBlockState[] aiblockstate = new IBlockState[k];
          EnumFacing enumfacing = extending ? direction : direction.getOpposite();
          for (int j = toDestroy.size() - 1; j >= 0; --j) {
            BlockPos blockpos1 = toDestroy.get(j);
            IBlockState iblockstate = worldIn.getBlockState(blockpos1);
            // Forge: With our change to how snowballs are dropped this needs to disallow to mimic vanilla behavior.
            float chance = iblockstate.getBlock() instanceof BlockSnow ? -1.0f : 1.0f;
            iblockstate.getBlock().dropBlockAsItemWithChance(worldIn, blockpos1, iblockstate, chance, 0);
            worldIn.setBlockState(blockpos1, Blocks.AIR.getDefaultState(), 4);
            --k;
            aiblockstate[k] = iblockstate;
          }
          for (int l = blocksToMove.size() - 1; l >= 0; --l) {
            BlockPos blockpos3 = blocksToMove.get(l);
            IBlockState iblockstate2 = worldIn.getBlockState(blockpos3);
            worldIn.setBlockState(blockpos3, Blocks.AIR.getDefaultState(), 2);
            blockpos3 = blockpos3.offset(enumfacing);
            if (!player.canPlayerEdit(blockpos3, side, ItemStack.EMPTY)) {
              ModCyclic.logger.log("piston scepter: cannot edit");
            }
            else
              worldIn.setBlockState(blockpos3, iblockstate2, 4);
            //            worldIn.setTileEntity(blockpos3, BlockPistonMoving.createTilePiston(list1.get(l), direction, extending, false));
            //            --k;
            //            aiblockstate[k] = iblockstate2;
          }
        }
      break;
      case PULL:
        UtilSound.playSound(player, SoundEvents.BLOCK_PISTON_EXTEND);
        ModCyclic.network.sendToServer(new PacketMoveBlock(pos, action, side));
      break;
      default:
      break;
    }
    onUse(stack, player, worldIn, hand);
    return EnumActionResult.SUCCESS;
  }

  @Override
  @SideOnly(Side.CLIENT)
  public void addInformation(ItemStack stack, World playerIn, List<String> tooltip, net.minecraft.client.util.ITooltipFlag advanced) {
    tooltip.add(TextFormatting.GREEN + UtilChat.lang(ActionType.getName(stack)));
  }

  @Override
  public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
    ActionType.tickTimeout(stack);
    super.onUpdate(stack, worldIn, entityIn, itemSlot, isSelected);
  }

  @Override
  public IRecipe addRecipe() {
    return RecipeRegistry.addShapedRecipe(new ItemStack(this),
        " gp",
        " bg",
        "b  ",
        'b', Items.BLAZE_ROD,
        'g', "gemQuartz",
        'p', Blocks.STICKY_PISTON);
  }
}
