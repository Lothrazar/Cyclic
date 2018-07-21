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
package com.lothrazar.cyclicmagic.item.tiletransporter;

import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.core.util.UtilChat;
import com.lothrazar.cyclicmagic.core.util.UtilItemStack;
import com.lothrazar.cyclicmagic.core.util.UtilPlaceBlocks;
import io.netty.buffer.ByteBuf;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IThreadListener;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketChestSack implements IMessage, IMessageHandler<PacketChestSack, IMessage> {

  private BlockPos pos;

  public PacketChestSack() {}

  public PacketChestSack(BlockPos mouseover) {
    pos = mouseover;
  }

  @Override
  public void fromBytes(ByteBuf buf) {
    NBTTagCompound tags = ByteBufUtils.readTag(buf);
    int x = tags.getInteger("x");
    int y = tags.getInteger("y");
    int z = tags.getInteger("z");
    pos = new BlockPos(x, y, z);
  }

  @Override
  public void toBytes(ByteBuf buf) {
    NBTTagCompound tags = new NBTTagCompound();
    tags.setInteger("x", pos.getX());
    tags.setInteger("y", pos.getY());
    tags.setInteger("z", pos.getZ());
    ByteBufUtils.writeTag(buf, tags);
  }

  @Override
  public IMessage onMessage(PacketChestSack message, MessageContext ctx) {
    PacketChestSack.checkThreadAndEnqueue(message, ctx);
    return null;
  }

  private static void checkThreadAndEnqueue(final PacketChestSack message, final MessageContext ctx) {
    if (ctx.side.isServer() && message != null && message.pos != null) {
      IThreadListener thread = ModCyclic.proxy.getThreadFromContext(ctx);
      // pretty much copied straight from vanilla code, see {@link PacketThreadUtil#checkThreadAndEnqueue}
      thread.addScheduledTask(new Runnable() {

        @Override
        public void run() {
          BlockPos position = message.pos;
          EntityPlayer player = ctx.getServerHandler().player;
          World world = player.getEntityWorld();
          TileEntity tile = world.getTileEntity(position);
          if (tile == null) {
            return;
          } //was block destroyed before this packet and/or thread resolved? server desync? who knows https://github.com/PrinceOfAmber/Cyclic/issues/487
          IBlockState state = world.getBlockState(position);
          NBTTagCompound tileData = new NBTTagCompound(); //thanks for the tip on setting tile entity data from nbt tag: https://github.com/romelo333/notenoughwands1.8.8/blob/master/src/main/java/romelo333/notenoughwands/Items/DisplacementWand.java
          tile.writeToNBT(tileData);
          NBTTagCompound itemData = new NBTTagCompound();
          itemData.setString(ItemChestSack.KEY_BLOCKNAME, state.getBlock().getUnlocalizedName());
          itemData.setTag(ItemChestSack.KEY_BLOCKTILE, tileData);
          itemData.setInteger(ItemChestSack.KEY_BLOCKID, Block.getIdFromBlock(state.getBlock()));
          itemData.setInteger(ItemChestSack.KEY_BLOCKSTATE, state.getBlock().getMetaFromState(state));
          EnumHand hand = EnumHand.MAIN_HAND;
          ItemStack held = player.getHeldItem(hand);
          if (held == null || held.getItem() instanceof ItemChestSackEmpty == false) {
            hand = EnumHand.OFF_HAND;
            held = player.getHeldItem(hand);
          }
          if (held != null && held.getCount() > 0) { //https://github.com/PrinceOfAmber/Cyclic/issues/181
            if (held.getItem() instanceof ItemChestSackEmpty) {
              Item chest_sack = ((ItemChestSackEmpty) held.getItem()).getFullSack();
              if (chest_sack != null) {
                if (!UtilPlaceBlocks.destroyBlock(world, position)) {
                  //we failed to break the block
                  // try to undo the break if we can
                  UtilChat.sendStatusMessage(player, "chest_sack.error.pickup");
                  world.setBlockState(position, state);
                  return;// and dont drop the full item stack or shrink the empty just end
                  //TileEntity tileCopy = world.getTileEntity(position);
                  //  if (tileCopy != null) {
                  //    tileCopy.readFromNBT(tileData);
                  //  } 
                }
                ItemStack drop = new ItemStack(chest_sack);
                drop.setTagCompound(itemData);
                UtilItemStack.dropItemStackInWorld(world, player.getPosition(), drop);

                if (player.capabilities.isCreativeMode == false && held.getCount() > 0) {
                  held.shrink(1);
                  if (held.getCount() == 0) {
                    held = ItemStack.EMPTY;
                    player.setHeldItem(hand, ItemStack.EMPTY);
                  }
                }
              }
            }
          }
        }
      });
    }
  }
}
