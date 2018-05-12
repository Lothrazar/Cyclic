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
package com.lothrazar.cyclicmagic.item.cyclicwand;

import javax.annotation.Nullable;
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.registry.SpellRegistry;
import com.lothrazar.cyclicmagic.spell.ISpell;
import com.lothrazar.cyclicmagic.spell.ISpellFromServer;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IThreadListener;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketSpellFromServer implements IMessage, IMessageHandler<PacketSpellFromServer, IMessage> {

  private BlockPos pos;
  private BlockPos posOffset;
  private @Nullable EnumFacing face;
  private int spellID;

  public PacketSpellFromServer() {}

  public PacketSpellFromServer(BlockPos mouseover, BlockPos offset, @Nullable EnumFacing sideMouseover, int spellid) {
    pos = mouseover;
    posOffset = offset;
    spellID = spellid;
    face = sideMouseover;
  }

  @Override
  public void fromBytes(ByteBuf buf) {
    NBTTagCompound tags = ByteBufUtils.readTag(buf);
    int x = tags.getInteger("x");
    int y = tags.getInteger("y");
    int z = tags.getInteger("z");
    pos = new BlockPos(x, y, z);
    x = tags.getInteger("ox");
    y = tags.getInteger("oy");
    z = tags.getInteger("oz");
    posOffset = new BlockPos(x, y, z);
    spellID = tags.getInteger("spell");
    if (tags.hasKey("face"))
      face = EnumFacing.values()[tags.getInteger("face")];
  }

  @Override
  public void toBytes(ByteBuf buf) {
    NBTTagCompound tags = new NBTTagCompound();
    tags.setInteger("x", pos.getX());
    tags.setInteger("y", pos.getY());
    tags.setInteger("z", pos.getZ());
    tags.setInteger("ox", posOffset.getX());
    tags.setInteger("oy", posOffset.getY());
    tags.setInteger("oz", posOffset.getZ());
    tags.setInteger("spell", spellID);
    if (face != null)
      tags.setInteger("face", face.ordinal());
    ByteBufUtils.writeTag(buf, tags);
  }

  @Override
  public IMessage onMessage(PacketSpellFromServer message, MessageContext ctx) {
    checkThreadAndEnqueue(message, ctx);
    return null;
  }

  private void checkThreadAndEnqueue(final PacketSpellFromServer message, final MessageContext ctx) {
    IThreadListener thread = ModCyclic.proxy.getThreadFromContext(ctx);
    thread.addScheduledTask(new Runnable() {

      public void run() {
        if (ctx.side.isServer() && message != null && message.pos != null) {
          EntityPlayer p = ctx.getServerHandler().player;
          ISpell spell = SpellRegistry.getSpellFromID(message.spellID);
          if (spell != null && spell instanceof ISpellFromServer) {
            ((ISpellFromServer) spell).castFromServer(message.pos, message.posOffset, message.face, p);
          }
          else {
            ModCyclic.logger.error("WARNING: Message from server: spell not found" + message.spellID);
          }
        }
      }
    });
  }
}
