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
package com.lothrazar.cyclicmagic.item.enderbook;

import com.lothrazar.cyclicmagic.util.UtilChat;
import com.lothrazar.cyclicmagic.util.UtilExperience;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketWarpButton implements IMessage, IMessageHandler<PacketWarpButton, IMessage> {

  public int slot;

  public PacketWarpButton() {}

  public PacketWarpButton(int s) {
    slot = s;
  }

  @Override
  public void fromBytes(ByteBuf buf) {
    this.slot = buf.readInt();
  }

  @Override
  public void toBytes(ByteBuf buf) {
    buf.writeInt(slot);
  }

  @Override
  public IMessage onMessage(PacketWarpButton message, MessageContext ctx) {
    EntityPlayer player = ((NetHandlerPlayServer) ctx.netHandler).player;
    int cost = (int) ItemEnderBook.getExpCostPerTeleport(player, ItemEnderBook.getPlayersBook(player), message.slot);
    if (player.isCreative()) {
      ItemEnderBook.teleport(player, message.slot);
    }
    else if (cost > 0 && UtilExperience.getExpTotal(player) < cost) {
      UtilChat.addChatMessage(player, "gui.chatexp");
    }
    else if (ItemEnderBook.teleport(player, message.slot)) {
      //if the teleport worked in non creative, drain it
      UtilExperience.drainExp(player, cost);
    }
    return null;
  }
}
