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
//import com.lothrazar.util.Util;
import com.lothrazar.cyclicmagic.util.UtilParticle;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketParticleAtPosition implements IMessage, IMessageHandler<PacketParticleAtPosition, IMessage> {
  private int x;
  private int y;
  private int z;
  private int particle;
  private int count;
  public PacketParticleAtPosition() {}
  public PacketParticleAtPosition(BlockPos p, int part, int c) {
    x = p.getX();
    y = p.getY();
    z = p.getZ();
    particle = part;
    count = c;
  }
  @Override
  public void fromBytes(ByteBuf buf) {
    NBTTagCompound tags = ByteBufUtils.readTag(buf);
    x = tags.getInteger("x");
    y = tags.getInteger("y");
    z = tags.getInteger("z");
    particle = tags.getInteger("p");
    count = tags.getInteger("c");
  }
  @Override
  public void toBytes(ByteBuf buf) {
    NBTTagCompound tags = new NBTTagCompound();
    tags.setInteger("x", x);
    tags.setInteger("y", y);
    tags.setInteger("z", z);
    tags.setInteger("p", particle);
    tags.setInteger("c", count);
    ByteBufUtils.writeTag(buf, tags);
  }
  @Override
  public IMessage onMessage(PacketParticleAtPosition message, MessageContext ctx) {
    if (ctx.side.isClient() && Minecraft.getMinecraft().player != null) {
      // http://www.minecraftforge.net/forum/index.php?topic=21195.0
      //yes, this being null happened once
      World world = Minecraft.getMinecraft().player.getEntityWorld();
      UtilParticle.spawnParticle(world, EnumParticleTypes.getParticleFromId(message.particle), message.x + 0.5, message.y, message.z + 0.5, message.count);
    }
    return null;
  }
}
