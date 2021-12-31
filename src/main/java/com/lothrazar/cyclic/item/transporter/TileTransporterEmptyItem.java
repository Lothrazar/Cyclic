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

import java.util.List;
import com.lothrazar.cyclic.item.ItemBaseCyclic;
import com.lothrazar.cyclic.registry.ItemRegistry;
import com.lothrazar.cyclic.registry.PacketRegistry;
import com.lothrazar.cyclic.registry.SoundRegistry;
import com.lothrazar.cyclic.util.UtilChat;
import com.lothrazar.cyclic.util.UtilItemStack;
import com.lothrazar.cyclic.util.UtilPlaceBlocks;
import com.lothrazar.cyclic.util.UtilSound;
import com.lothrazar.cyclic.util.UtilString;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;

public class TileTransporterEmptyItem extends ItemBaseCyclic {

  public static ConfigValue<List<? extends String>> IGNORELIST;

  public TileTransporterEmptyItem(Properties prop) {
    super(prop);
  }

  @SuppressWarnings("unchecked")
  @Override
  public InteractionResult useOn(UseOnContext context) {
    Player player = context.getPlayer();
    BlockPos pos = context.getClickedPos();
    Level world = context.getLevel();
    BlockEntity tile = world.getBlockEntity(pos);
    BlockState state = world.getBlockState(pos);
    //
    if (state == null || tile == null || state.getBlock() == null
        || state.getBlock().getRegistryName() == null) {
      UtilChat.sendStatusMessage(player, "chest_sack.error.null");
      return InteractionResult.FAIL;
    }
    ResourceLocation blockId = state.getBlock().getRegistryName();
    if (UtilString.isInList((List<String>) IGNORELIST.get(), blockId)) {
      UtilChat.sendStatusMessage(player, "chest_sack.error.config");
      return InteractionResult.FAIL;
    }
    UtilSound.playSound(player, SoundRegistry.THUNK);
    if (world.isClientSide) {
      PacketRegistry.INSTANCE.sendToServer(new PacketChestSack(pos));
    }
    return InteractionResult.SUCCESS;
  }

  public static void gatherTileEntity(BlockPos pos, Player player, Level world, BlockEntity tile) {
    if (tile == null) {
      return;
    } //was block destroyed before this packet and/or thread resolved? server desync? who knows https://github.com/PrinceOfAmber/Cyclic/issues/487
    BlockState state = world.getBlockState(pos);
    //bedrock returns ZERO for this hardness 
    if (state.getDestroyProgress(player, world, pos) <= 0) {
      return;
    }
    CompoundTag tileData = new CompoundTag();
    //thanks for the tip on setting tile entity data from nbt tag:
    //https://github.com/romelo333/notenoughwands1.8.8/blob/master/src/main/java/romelo333/notenoughwands/Items/DisplacementWand.java
    tile.save(tileData);
    CompoundTag itemData = new CompoundTag();
    itemData.putString(TileTransporterItem.KEY_BLOCKNAME, state.getBlock().getDescriptionId());
    itemData.put(TileTransporterItem.KEY_BLOCKTILE, tileData);
    itemData.putString(TileTransporterItem.KEY_BLOCKID, state.getBlock().getRegistryName().toString());
    itemData.put(TileTransporterItem.KEY_BLOCKSTATE, NbtUtils.writeBlockState(state));
    InteractionHand hand = InteractionHand.MAIN_HAND;
    ItemStack held = player.getItemInHand(hand);
    if (held == null || held.getItem() instanceof TileTransporterEmptyItem == false) {
      hand = InteractionHand.OFF_HAND;
      held = player.getItemInHand(hand);
    }
    if (held != null && held.getCount() > 0) { //https://github.com/PrinceOfAmber/Cyclic/issues/181
      if (held.getItem() instanceof TileTransporterEmptyItem) {
        if (!UtilPlaceBlocks.destroyBlock(world, pos)) {
          //we failed to break the block
          // try to undo the break if we can
          UtilChat.sendStatusMessage(player, "chest_sack.error.pickup");
          world.setBlockAndUpdate(pos, state);
          return; // and dont drop the full item stack or shrink the empty just end
        }
        ItemStack drop = new ItemStack(ItemRegistry.TILE_TRANSPORTER.get());
        drop.setTag(itemData);
        UtilItemStack.drop(world, player.blockPosition(), drop);
        if (player.isCreative() == false && held.getCount() > 0) {
          held.shrink(1);
          if (held.getCount() == 0) {
            held = ItemStack.EMPTY;
            player.setItemInHand(hand, ItemStack.EMPTY);
          }
        }
      }
    }
  }
}
