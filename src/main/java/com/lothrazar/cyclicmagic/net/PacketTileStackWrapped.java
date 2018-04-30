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
import com.lothrazar.cyclicmagic.core.ITileStackWrapper;
import com.lothrazar.cyclicmagic.core.gui.StackWrapper;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IThreadListener;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketTileStackWrapped implements IMessage, IMessageHandler<PacketTileStackWrapped, IMessage> {

  private BlockPos pos;
  private int index;
  private StackWrapper stack;

  public PacketTileStackWrapped() {}

  public PacketTileStackWrapped(int index, StackWrapper stack, BlockPos p) {
    pos = p;
    this.stack = stack;
    this.index = index;
  }

  @Override
  public void fromBytes(ByteBuf buf) {
    NBTTagCompound tags = ByteBufUtils.readTag(buf);
    int x = tags.getInteger("x");
    int y = tags.getInteger("y");
    int z = tags.getInteger("z");
    index = tags.getInteger("index");
    pos = new BlockPos(x, y, z);
    stack = StackWrapper.loadStackWrapperFromNBT((NBTTagCompound) tags.getTag("ghostSlot"));
  }

  @Override
  public void toBytes(ByteBuf buf) {
    NBTTagCompound tags = new NBTTagCompound();
    tags.setInteger("x", pos.getX());
    tags.setInteger("y", pos.getY());
    tags.setInteger("z", pos.getZ());
    tags.setInteger("index", index);
    NBTTagCompound stackTag = new NBTTagCompound();
    stack.writeToNBT(stackTag);
    tags.setTag("ghostSlot", stackTag);
    ByteBufUtils.writeTag(buf, tags);
  }

  @Override
  public IMessage onMessage(PacketTileStackWrapped message, MessageContext ctx) {
    PacketTileStackWrapped.checkThreadAndEnqueue(message, ctx);
    return null;
  }

  private static void checkThreadAndEnqueue(final PacketTileStackWrapped message, final MessageContext ctx) {
    IThreadListener thread = ModCyclic.proxy.getThreadFromContext(ctx);
    thread.addScheduledTask(new Runnable() {

      @Override
      public void run() {
        EntityPlayerMP player = ctx.getServerHandler().player;
        World world = player.getEntityWorld();
        TileEntity tile = world.getTileEntity(message.pos);
        if (tile != null && tile instanceof ITileStackWrapper) {
          ((ITileStackWrapper) tile).setStackWrapper(message.index, message.stack);
        }
      }
    });
  }
}
