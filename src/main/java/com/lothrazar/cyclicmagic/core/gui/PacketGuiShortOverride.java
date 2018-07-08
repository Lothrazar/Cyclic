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
package com.lothrazar.cyclicmagic.core.gui;

import com.lothrazar.cyclicmagic.ModCyclic;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.IThreadListener;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * Override / fix for vanilla minecraft using an old server-> client sync system that truncates Integers to Shorts so anything under Short.MAX_VALUE aka 32767 wont work
 * 
 * Look at net.minecraft.inventory.IContainerListener
 * 
 * and you will see
 * 
 * Quote start
 * 
 * Sends two ints to the client-side Container. Used for furnace burning time, smelting progress, brewing progress, and enchanting level. Normally the first int identifies which variable to update,
 * and the second contains the new value. Both are truncated to shorts in non-local SMP.
 * 
 * Quote End
 * 
 * void sendWindowProperty(Container containerIn, int varToUpdate, int newValue);
 * 
 * @author Sam
 *
 */
public class PacketGuiShortOverride implements IMessage, IMessageHandler<PacketGuiShortOverride, IMessage> {

  public int fieldId, value;

  public PacketGuiShortOverride() {}

  public PacketGuiShortOverride(int f, int v) {
    this.fieldId = f;
    this.value = v;
  }

  @Override
  public void fromBytes(ByteBuf buf) {
    fieldId = buf.readInt();
    value = buf.readInt();
  }

  @Override
  public void toBytes(ByteBuf buf) {
    //not writeShort as the legacy vanilla system does
    buf.writeInt(fieldId);
    buf.writeInt(value);
  }

  @Override
  public IMessage onMessage(PacketGuiShortOverride message, MessageContext ctx) {
    final IThreadListener mainThread = Minecraft.getMinecraft();
    mainThread.addScheduledTask(new Runnable() {

      @Override
      public void run() {
        EntityPlayer player = ModCyclic.proxy.getClientPlayer();
        if (player.openContainer != null) {
          player.openContainer.updateProgressBar(message.fieldId, message.value);
        }
      }
    });
    return null;
  }
}
