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

import java.util.ArrayList;
import java.util.List;
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.item.crashtestdummy.EntityRobot;
import com.lothrazar.cyclicmagic.item.crashtestdummy.EntityRobot.DmgTracker;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketEntitySyncToClient implements IMessage, IMessageHandler<PacketEntitySyncToClient, IMessage> {

  private int entityId;
  private List<DmgTracker> trackers;

  public PacketEntitySyncToClient() {}

  public PacketEntitySyncToClient(int entityid, List<DmgTracker> trackers) {
    entityId = entityid;
    this.trackers = trackers;
  }

  @Override
  public void fromBytes(ByteBuf buf) {
    NBTTagCompound compound = ByteBufUtils.readTag(buf);
    entityId = compound.getInteger("entityId");
    trackers = new ArrayList<DmgTracker>();
    int saved = compound.getInteger("SAVED");
    for (int i = 0; i < saved; i++) {
      if (compound.hasKey("tmr" + i)) {
        int timer = compound.getInteger("tmr" + i);
        if (timer > 0) {
          trackers.add(new EntityRobot.DmgTracker(timer, compound.getString("strMsg" + i)));
        }
      }
    }
  }

  @Override
  public void toBytes(ByteBuf buf) {
    NBTTagCompound compound = new NBTTagCompound();
    compound.setInteger("entityId", entityId);
    int i = 0;
    for (DmgTracker t : trackers) {
      compound.setInteger("tmr" + i, t.timer);
      compound.setString("strMsg" + i, t.message);
      i++;
    }
    compound.setInteger("SAVED", i);
    ByteBufUtils.writeTag(buf, compound);
  }

  @Override
  public IMessage onMessage(PacketEntitySyncToClient message, MessageContext ctx) {
    if (ctx.side.isClient()) {
      MinecraftServer s = FMLCommonHandler.instance().getMinecraftServerInstance();

      s.addScheduledTask(new Runnable() {

        @Override
        public void run() {
          handle(message, ctx);
        }
      });
    }
    return null;
  }

  private void handle(PacketEntitySyncToClient message, MessageContext ctx) {

    Entity entityTarget = ModCyclic.proxy.getPlayerEntity(ctx).world.getEntityByID(message.entityId);
    if (entityTarget instanceof EntityRobot) {
      ((EntityRobot) entityTarget).setTrackers(message.trackers);
    }

  }
}
