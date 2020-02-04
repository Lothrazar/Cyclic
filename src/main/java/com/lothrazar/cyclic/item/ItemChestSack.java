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
package com.lothrazar.cyclic.item;

import java.util.List;
import javax.annotation.Nullable;
import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.base.ItemBase;
import com.lothrazar.cyclic.registry.ItemRegistry;
import com.lothrazar.cyclic.util.UtilChat;
import com.lothrazar.cyclic.util.UtilItemStack;
import com.lothrazar.cyclic.util.UtilSound;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.ForgeRegistries;

public class ItemChestSack extends ItemBase {

  public static final String name = "chest_sack";
  public static final String KEY_BLOCKID = "block";
  public static final String KEY_BLOCKTILE = "tile";
  public static final String KEY_BLOCKNAME = "blockname";
  public static final String KEY_BLOCKSTATE = "blockstate";

  public ItemChestSack(Properties prop) {
    super(prop);
  }

  /**
   * Called when a Block is right-clicked with this Item
   */
  @Override
  public ActionResultType onItemUse(ItemUseContext context) {
    PlayerEntity player = context.getPlayer();
    //
    ItemStack stack = context.getItem();
    BlockPos pos = context.getPos();
    Direction side = context.getFace();
    World world = context.getWorld();
    //    TileEntity tile = world.getTileEntity(pos);
    //    BlockState state = world.getBlockState(pos);
    BlockPos offset = pos.offset(side);
    if (world.isAirBlock(offset) == false) {
      return ActionResultType.FAIL;
    }
    if (placeStoredTileEntity(player, stack, offset)) {
      player.setHeldItem(context.getHand(), ItemStack.EMPTY);
      UtilSound.playSound(player, pos, SoundEvents.BLOCK_ANVIL_HIT);//SoundRegistry.chest_sack_capture);
      if (player.isCreative() == false) {//its never really null tho
        UtilItemStack.drop(world, player.getPosition(), new ItemStack(ItemRegistry.tile_transporterempty));
      }
    }
    return ActionResultType.SUCCESS;
  }

  private boolean placeStoredTileEntity(PlayerEntity player, ItemStack heldChestSack, BlockPos pos) {
    CompoundNBT itemData = heldChestSack.getOrCreateTag();
    ResourceLocation res = new ResourceLocation(itemData.getString(KEY_BLOCKID));
    Block block = ForgeRegistries.BLOCKS.getValue(res);
    //    Block.regi
    if (block == null) {
      heldChestSack = ItemStack.EMPTY;
      UtilChat.addChatMessage(player, "Invalid block id " + res);
      return false;
    }
    BlockState toPlace = NBTUtil.readBlockState(itemData.getCompound(KEY_BLOCKSTATE));
    //maybe get from player direction or offset face, but instead rely on that from saved data
    World world = player.getEntityWorld();
    try {
      world.setBlockState(pos, toPlace);
      TileEntity tile = world.getTileEntity(pos);
      if (tile != null) {
        CompoundNBT tileData = itemData.getCompound(ItemChestSack.KEY_BLOCKTILE);
        tileData.putInt("x", pos.getX());
        tileData.putInt("y", pos.getY());
        tileData.putInt("z", pos.getZ());
        tile.read(tileData);// can cause errors in 3rd party mod
        //example at extracells.tileentity.TileEntityFluidFiller.func_145839_a(TileEntityFluidFiller.java:302) ~
        tile.markDirty();
        world.markChunkDirty(pos, tile);
      }
    }
    catch (Exception e) {
      ModCyclic.LOGGER.error("Error attempting to place block in world", e);
      UtilChat.sendStatusMessage(player, "chest_sack.error.place");
      world.setBlockState(pos, Blocks.AIR.getDefaultState());
      return false;
    }
    //    heldChestSack.stackSize = 0;
    heldChestSack = ItemStack.EMPTY;
    heldChestSack.setTag(null);
    return true;
  }

  @OnlyIn(Dist.CLIENT)
  @Override
  public void addInformation(ItemStack itemStack, @Nullable World worldIn, List<ITextComponent> list, ITooltipFlag flagIn) {
    if (itemStack.getTag() != null && itemStack.getTag().contains(KEY_BLOCKNAME)) {
      String blockname = itemStack.getTag().getString(KEY_BLOCKNAME);
      if (blockname != null && blockname.length() > 0) {
        TranslationTextComponent t = new TranslationTextComponent(
            UtilChat.lang(blockname));//.replace("block.", "item.") + ".name"
        t.applyTextStyle(TextFormatting.DARK_GREEN);
        list.add(t);
      }
    }
    else {
      TranslationTextComponent t = new TranslationTextComponent(
          UtilChat.lang("invalid"));//.replace("block.", "item.") + ".name"
      t.applyTextStyle(TextFormatting.DARK_RED);
      list.add(t);
    }
    //super.addInformation(itemStack, player, list, advanced);
  }

  @Override
  @OnlyIn(Dist.CLIENT)
  public boolean hasEffect(ItemStack stack) {
    return true;
  }
}
