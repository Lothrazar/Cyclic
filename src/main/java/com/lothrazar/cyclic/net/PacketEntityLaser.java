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
package com.lothrazar.cyclic.net;

import com.lothrazar.cyclic.config.ConfigRegistry;
import com.lothrazar.cyclic.item.LaserItem;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
public class PacketEntityLaser implements net.minecraft.network.protocol.common.custom.CustomPacketPayload {

  public static final net.minecraft.network.protocol.common.custom.CustomPacketPayload.Type<PacketEntityLaser> TYPE = new net.minecraft.network.protocol.common.custom.CustomPacketPayload.Type<>(net.minecraft.resources.ResourceLocation.fromNamespaceAndPath(com.lothrazar.cyclic.ModCyclic.MODID, "packet_entity_laser"));

  public static final net.minecraft.network.codec.StreamCodec<net.minecraft.network.RegistryFriendlyByteBuf, PacketEntityLaser> STREAM_CODEC = net.minecraft.network.codec.StreamCodec.of(PacketEntityLaser::encode, PacketEntityLaser::decode);


  @Override
  public net.minecraft.network.protocol.common.custom.CustomPacketPayload.Type<? extends net.minecraft.network.protocol.common.custom.CustomPacketPayload> type() {
    return TYPE;
  }


  private int entityId;
  private boolean crosshair;

  public PacketEntityLaser(int eid, boolean cross) {
    this.entityId = eid;
    this.crosshair = cross;
  }

  public static void handle(PacketEntityLaser message, net.neoforged.neoforge.network.handling.IPayloadContext ctx) {
    ctx.enqueueWork(() -> {
      net.minecraft.server.level.ServerPlayer sender = (net.minecraft.server.level.ServerPlayer) ctx.player();
      Level level = sender.level();
      Entity target = level.getEntity(message.entityId);
      //validate also covers delay
      ItemStack stack = LaserItem.getIfHeld(sender);
      if (PacketEntityLaser.canShoot(sender, target, stack)) {
        net.neoforged.neoforge.energy.IEnergyStorage storage = stack.getCapability(net.neoforged.neoforge.capabilities.Capabilities.EnergyStorage.ITEM);
        if (storage != null) {
          float dmg = message.crosshair ? ConfigRegistry.LaserItemDamageClose.get() : ConfigRegistry.LaserItemDamageFar.get();
          if (target.hurt(level.damageSources().indirectMagic(sender, sender), dmg)) {
            //DRAIN RF ETC 
            LaserItem.resetStackDamageCool(stack, level.getGameTime());
            storage.extractEnergy(ConfigRegistry.LaserItemEnergy.get(), false);
          }
        }
        //        target.causeFallDamage(0, 0, null);
      }
    });
    
  }

  private static boolean canShoot(ServerPlayer sender, Entity target, ItemStack stack) {
    if (!sender.isAlive() || !target.isAlive() || target.isInvulnerable()) {
      //somene died or target is invincible
      return false;
    }
    if (stack.isEmpty()) {
      return false;
    }
    net.neoforged.neoforge.energy.IEnergyStorage storage = stack.getCapability(net.neoforged.neoforge.capabilities.Capabilities.EnergyStorage.ITEM);
    return (storage != null && storage.extractEnergy(ConfigRegistry.LaserItemEnergy.get(), true) == ConfigRegistry.LaserItemEnergy.get());
  }

  public static PacketEntityLaser decode(net.minecraft.network.RegistryFriendlyByteBuf buf) {
    PacketEntityLaser msg = new PacketEntityLaser(buf.readInt(), buf.readBoolean());
    return msg;
  }

  public static void encode(net.minecraft.network.RegistryFriendlyByteBuf buf, PacketEntityLaser msg) {
    buf.writeInt(msg.entityId);
    buf.writeBoolean(msg.crosshair);
  }
}
