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
import java.util.List;
import com.lothrazar.cyclicmagic.item.ItemScythe;
import com.lothrazar.cyclicmagic.util.UtilScythe;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketScythe implements IMessage, IMessageHandler<PacketScythe, IMessage> {
  private BlockPos pos;
  private ItemScythe.ScytheType type;
  private int radius;
  public PacketScythe() {}
  public PacketScythe(BlockPos mouseover, ItemScythe.ScytheType t, int r) {
    pos = mouseover;
    type = t;
    radius = r;
  }
  @Override
  public void fromBytes(ByteBuf buf) {
    NBTTagCompound tags = ByteBufUtils.readTag(buf);
    int x = tags.getInteger("x");
    int y = tags.getInteger("y");
    int z = tags.getInteger("z");
    pos = new BlockPos(x, y, z);
    int t = tags.getInteger("t");
    type = ItemScythe.ScytheType.values()[t];
    radius = tags.getInteger("s");
  }
  @Override
  public void toBytes(ByteBuf buf) {
    NBTTagCompound tags = new NBTTagCompound();
    tags.setInteger("x", pos.getX());
    tags.setInteger("y", pos.getY());
    tags.setInteger("z", pos.getZ());
    tags.setInteger("t", type.ordinal());
    tags.setInteger("s", radius);
    ByteBufUtils.writeTag(buf, tags);
  }
  @Override
  public IMessage onMessage(final PacketScythe message, final MessageContext ctx) {
    MinecraftServer s = FMLCommonHandler.instance().getMinecraftServerInstance();
    if (s == null) {//this is never happening. ill keep it just in case
      handle(message, ctx);
    }
    else {
      //only java 8
      //s.addScheduledTask(() -> handle(message, ctx));
      s.addScheduledTask(new Runnable() {
        public void run() {
          handle(message, ctx);
        }
      });
    }
    return null;
  }
  private void handle(PacketScythe message, MessageContext ctx) {
    if (ctx.side.isServer() && message != null && message.pos != null) {
      EntityPlayer player = ctx.getServerHandler().player;
      World world = player.getEntityWorld();
      List<BlockPos> shape = ItemScythe.getShape(message.pos, message.radius);
      for (BlockPos p : shape) {
        UtilScythe.harvestSingle(world, p, message.type);
      }
    }
  }
}
