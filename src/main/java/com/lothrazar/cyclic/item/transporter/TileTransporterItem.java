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
package com.lothrazar.cyclic.item.transporter;

import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.base.ItemBase;
import com.lothrazar.cyclic.registry.ItemRegistry;
import com.lothrazar.cyclic.registry.SoundRegistry;
import com.lothrazar.cyclic.util.UtilChat;
import com.lothrazar.cyclic.util.UtilItemStack;
import com.lothrazar.cyclic.util.UtilSound;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.ForgeRegistries;

public class TileTransporterItem extends ItemBase {

  public static final String KEY_BLOCKID = "block";
  public static final String KEY_BLOCKTILE = "tile";
  public static final String KEY_BLOCKNAME = "blockname";
  public static final String KEY_BLOCKSTATE = "blockstate";

  public TileTransporterItem(Properties prop) {
    super(prop);
  }

  /**
   * Called when a Block is right-clicked with this Item
   */
  @Override
  public InteractionResult useOn(UseOnContext context) {
    Player player = context.getPlayer();
    //
    ItemStack stack = context.getItemInHand();
    BlockPos pos = context.getClickedPos();
    Direction side = context.getClickedFace();
    Level world = context.getLevel();
    BlockPos offset = pos.relative(side);
    if (world.isEmptyBlock(offset) == false) {
      return InteractionResult.FAIL;
    }
    if (placeStoredTileEntity(player, stack, offset)) {
      player.setItemInHand(context.getHand(), ItemStack.EMPTY);
      UtilSound.playSound(player, SoundRegistry.THUNK);
      if (player.isCreative() == false) {
        UtilItemStack.drop(world, player.blockPosition(), new ItemStack(ItemRegistry.tile_transporterempty));
      }
    }
    return InteractionResult.SUCCESS;
  }

  private boolean placeStoredTileEntity(Player player, ItemStack heldChestSack, BlockPos pos) {
    CompoundTag itemData = heldChestSack.getOrCreateTag();
    ResourceLocation res = new ResourceLocation(itemData.getString(KEY_BLOCKID));
    Block block = ForgeRegistries.BLOCKS.getValue(res);
    if (block == null) {
      heldChestSack = ItemStack.EMPTY;
      UtilChat.addChatMessage(player, "Invalid block id " + res);
      return false;
    }
    BlockState toPlace = NbtUtils.readBlockState(itemData.getCompound(KEY_BLOCKSTATE));
    //maybe get from player direction or offset face, but instead rely on that from saved data
    Level world = player.getCommandSenderWorld();
    try {
      world.setBlockAndUpdate(pos, toPlace);
      BlockEntity tile = world.getBlockEntity(pos);
      if (tile != null) {
        CompoundTag tileData = itemData.getCompound(TileTransporterItem.KEY_BLOCKTILE);
        tileData.putInt("x", pos.getX());
        tileData.putInt("y", pos.getY());
        tileData.putInt("z", pos.getZ());
        tile.load(tileData); // can cause errors in 3rd party mod
        //example at extracells.tileentity.TileEntityFluidFiller.func_145839_a(TileEntityFluidFiller.java:302) ~
        tile.setChanged();
        world.blockEntityChanged(pos);
      }
    }
    catch (Exception e) {
      ModCyclic.LOGGER.error("Error attempting to place block in world", e);
      UtilChat.sendStatusMessage(player, "chest_sack.error.place");
      world.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
      return false;
    }
    //    heldChestSack.stackSize = 0;
    heldChestSack = ItemStack.EMPTY;
    heldChestSack.setTag(null);
    return true;
  }

  @OnlyIn(Dist.CLIENT)
  @Override
  public void appendHoverText(ItemStack itemStack, Level worldIn, List<Component> list, TooltipFlag flagIn) {
    if (itemStack.getTag() != null && itemStack.getTag().contains(KEY_BLOCKNAME)) {
      String blockname = itemStack.getTag().getString(KEY_BLOCKNAME);
      if (blockname != null && blockname.length() > 0) {
        TranslatableComponent t = new TranslatableComponent(UtilChat.lang(blockname));
        t.withStyle(ChatFormatting.DARK_GREEN);
        list.add(t);
      }
    }
    else {
      TranslatableComponent t = new TranslatableComponent(UtilChat.lang("invalid"));
      t.withStyle(ChatFormatting.DARK_RED);
      list.add(t);
    }
  }

  @Override
  @OnlyIn(Dist.CLIENT)
  public boolean isFoil(ItemStack stack) {
    return true;
  }
}
