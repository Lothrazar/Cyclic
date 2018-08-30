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
package com.lothrazar.cyclicmagic.net;

import com.lothrazar.cyclicmagic.util.UtilPlayerInventoryFilestorage;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketSwapPlayerStack implements IMessage, IMessageHandler<PacketSwapPlayerStack, IMessage> {

  public PacketSwapPlayerStack() {}

  private int indexDest;
  private int hotbarSource;

  public PacketSwapPlayerStack(int indx, int hotbar) {
    indexDest = indx;
    hotbarSource = hotbar;
  }

  @Override
  public void fromBytes(ByteBuf buf) {
    NBTTagCompound tags = ByteBufUtils.readTag(buf);
    this.indexDest = tags.getInteger("ix");
    this.hotbarSource = tags.getInteger("hot");
  }

  @Override
  public void toBytes(ByteBuf buf) {
    NBTTagCompound tags = new NBTTagCompound();
    tags.setInteger("ix", this.indexDest);
    tags.setInteger("hot", this.hotbarSource);
    ByteBufUtils.writeTag(buf, tags);
  }

  @Override
  public IMessage onMessage(PacketSwapPlayerStack message, MessageContext ctx) {
    EntityPlayer player = ctx.getServerHandler().player;
    if (message.hotbarSource >= 0) {
      ItemStack hotbarItem = player.inventory.getStackInSlot(message.hotbarSource);
      ItemStack onStorage = UtilPlayerInventoryFilestorage.getPlayerInventoryStack(player, message.indexDest);

      player.inventory.setInventorySlotContents(message.hotbarSource, onStorage);
      UtilPlayerInventoryFilestorage.setPlayerInventoryStack(player, message.indexDest, hotbarItem);
    }
    return null;
  }
}
