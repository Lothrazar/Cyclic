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
package com.lothrazar.cyclicmagic.playerupgrade;

import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.util.UtilPlayerInventoryFilestorage;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class PacketSyncExtendedInventory implements IMessage, IMessageHandler<PacketSyncExtendedInventory, IMessage> {

  int slot;
  int playerId;
  ItemStack itemStack = null;

  public PacketSyncExtendedInventory() {}

  public PacketSyncExtendedInventory(EntityPlayer player, int slot) {
    this.slot = slot;
    this.itemStack = UtilPlayerInventoryFilestorage.getPlayerInventoryStack(player, slot);
    this.playerId = player.getEntityId();
  }

  @Override
  public void toBytes(ByteBuf buffer) {
    buffer.writeByte(slot);
    buffer.writeInt(playerId);
    ByteBufUtils.writeItemStack(buffer, itemStack);
  }

  @Override
  public void fromBytes(ByteBuf buffer) {
    slot = buffer.readByte();
    playerId = buffer.readInt();
    itemStack = ByteBufUtils.readItemStack(buffer);
  }

  @SideOnly(Side.CLIENT)
  @Override
  public IMessage onMessage(final PacketSyncExtendedInventory message, MessageContext ctx) {
    Minecraft.getMinecraft().addScheduledTask(new Runnable() {

      public void run() {
        processMessage(message);
      }
    });
    return null;
  }

  @SideOnly(Side.CLIENT)
  void processMessage(PacketSyncExtendedInventory message) {
    World world = ModCyclic.proxy.getClientWorld();
    if (world == null)
      return;
    Entity p = world.getEntityByID(message.playerId);
    if (p != null && p instanceof EntityPlayer) {
      EntityPlayer player = (EntityPlayer) p;
      UtilPlayerInventoryFilestorage.setPlayerInventoryStack(player, slot, message.itemStack);
      //      UtilPlayerInventoryFilestorage.getPlayerInventory(player).stackList[message.slot] = message.itemStack;
    }
    return;
  }
}
