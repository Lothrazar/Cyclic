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

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.core.util.UtilChat;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketEntityDropRandom implements IMessage, IMessageHandler<PacketEntityDropRandom, IMessage> {

  private int entityId;

  public PacketEntityDropRandom() {}

  public PacketEntityDropRandom(int itemSlot) {
    entityId = itemSlot;
  }

  @Override
  public void fromBytes(ByteBuf buf) {
    NBTTagCompound tags = ByteBufUtils.readTag(buf);
    entityId = tags.getInteger("entityId");
  }

  @Override
  public void toBytes(ByteBuf buf) {
    NBTTagCompound tags = new NBTTagCompound();
    tags.setInteger("entityId", entityId);
    ByteBufUtils.writeTag(buf, tags);
  }

  @Override
  public IMessage onMessage(PacketEntityDropRandom message, MessageContext ctx) {
    if (ctx.side.isServer()) {
      EntityPlayer player = ctx.getServerHandler().player;
      World world = player.getEntityWorld();
      Entity entityTarget = world.getEntityByID(message.entityId);
      if (entityTarget != null && entityTarget instanceof EntityLivingBase) {
        EntityLivingBase entity = (EntityLivingBase) entityTarget;
        ItemStack stack;
        List<EntityEquipmentSlot> slots = Arrays.asList(EntityEquipmentSlot.values());
        Collections.shuffle(slots);
        for (EntityEquipmentSlot slot : slots) {
          stack = entity.getItemStackFromSlot(slot);
          if (stack.isEmpty() == false) {
            ModCyclic.logger.log("DROP SLOT " + slot + " on world isREmote==" + world.isRemote);
            //if (world.isRemote == false) {
            entity.entityDropItem(stack.copy(), 0.9F);
            if (entity instanceof EntityPlayer) {
              UtilChat.sendStatusMessage((EntityPlayer) entity, "potion.butter.oops");
            }
            //}
            entity.setItemStackToSlot(slot, ItemStack.EMPTY);
            break;
          }
        }
      }
    }
    return null;
  }
}
