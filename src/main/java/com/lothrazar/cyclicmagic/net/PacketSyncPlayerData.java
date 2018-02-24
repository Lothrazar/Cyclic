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
import com.lothrazar.cyclicmagic.ModCyclic;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IThreadListener;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class PacketSyncPlayerData implements IMessage, IMessageHandler<PacketSyncPlayerData, IMessage> {
  NBTTagCompound tags = new NBTTagCompound();
  public PacketSyncPlayerData() {}
  public PacketSyncPlayerData(NBTTagCompound ptags) {
    tags = ptags;
  }
  @Override
  public void fromBytes(ByteBuf buf) {
    tags = ByteBufUtils.readTag(buf);
  }
  @Override
  public void toBytes(ByteBuf buf) {
    ByteBufUtils.writeTag(buf, this.tags);
  }
  @Override
  public IMessage onMessage(PacketSyncPlayerData message, MessageContext ctx) {
    if (ctx.side == Side.CLIENT) {
      //update it through client proxy
      PacketSyncPlayerData.checkThreadAndEnqueue(message, ctx);
    }
    return null;
  }
  /**
   * 1.8 +: Ensures that the message is being handled on the main thread https://github.com/coolAlias/Tutorial-Demo/blob/master/src/main/java/ tutorial/network/AbstractMessage.java#L118-L131
   * http://www.minecraftforge.net/forum/index.php?topic=31853.0
   */
  private static final void checkThreadAndEnqueue(final PacketSyncPlayerData message, final MessageContext ctx) {
    IThreadListener thread = ModCyclic.proxy.getThreadFromContext(ctx);
    // pretty much copied straight from vanilla code, see {@link PacketThreadUtil#checkThreadAndEnqueue}
    thread.addScheduledTask(new Runnable() {
      public void run() {
        // msg.process(ModMain.proxy.getPlayerEntity(ctx), ctx.side);
        ModCyclic.proxy.setClientPlayerData(ctx, message.tags);
      }
    });
  }
}
