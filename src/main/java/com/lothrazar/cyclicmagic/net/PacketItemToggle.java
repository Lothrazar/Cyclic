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
import com.lothrazar.cyclicmagic.item.base.IHasClickToggle;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketItemToggle implements IMessage, IMessageHandler<PacketItemToggle, IMessage> {
  private int slot;
  public PacketItemToggle() {}
  public PacketItemToggle(int itemSlot) {
    slot = itemSlot;
  }
  @Override
  public void fromBytes(ByteBuf buf) {
    NBTTagCompound tags = ByteBufUtils.readTag(buf);
    slot = tags.getInteger("slot");
  }
  @Override
  public void toBytes(ByteBuf buf) {
    NBTTagCompound tags = new NBTTagCompound();
    tags.setInteger("slot", slot);
    ByteBufUtils.writeTag(buf, tags);
  }
  @Override
  public IMessage onMessage(PacketItemToggle message, MessageContext ctx) {
    if (ctx.side.isServer()) {
      EntityPlayer player = ctx.getServerHandler().player;
      if (player.openContainer == null) {
        return null;
      }
      int scount = player.openContainer.inventorySlots.size();
      //this is an edge case but it DID happen: put charmin your hotbar and then open a creative inventory tab. avoid index OOB
      if (message.slot >= scount) {
        return null;
      }
      Slot slotObj = player.openContainer.getSlot(message.slot);
      if (slotObj != null
          && !slotObj.getStack().isEmpty()) {
        ItemStack maybeCharm = slotObj.getStack();
        if (maybeCharm.getItem() instanceof IHasClickToggle) {
          //example: is a charm or something
          IHasClickToggle c = (IHasClickToggle) maybeCharm.getItem();
          c.toggle(player, maybeCharm);
        }
      }
    }
    return null;
  }
}
