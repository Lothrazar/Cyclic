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

import java.util.function.Supplier;
import com.lothrazar.cyclic.item.slingshot.LaserItem;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.network.NetworkEvent;

/**
 * Forge docs suggest using a direct packet to keep capabilities, such as power, in sync with the client according to https://mcforge.readthedocs.io/en/latest/datastorage/capabilities/
 */
public class PacketEntityLaser extends PacketBaseCyclic {

  private int entityId;
  private boolean crosshair;

  public PacketEntityLaser(int eid, boolean cross) {
    this.entityId = eid;
    this.crosshair = cross;
  }

  public static void handle(PacketEntityLaser message, Supplier<NetworkEvent.Context> ctx) {
    ctx.get().enqueueWork(() -> {
      ServerPlayer sender = ctx.get().getSender();
      Level level = sender.level;
      Entity target = level.getEntity(message.entityId);
      //validate also covers delay
      ItemStack stack = LaserItem.getIfHeld(sender);
      if (PacketEntityLaser.canShoot(sender, target, stack)) {
        IEnergyStorage storage = stack.getCapability(CapabilityEnergy.ENERGY, null).orElse(null);
        if (storage != null) {
          float dmg = message.crosshair ? LaserItem.DMG_CLOSE : LaserItem.DMG_FAR; // TODO: config/properties/whatever
          if (target.hurt(DamageSource.indirectMagic(sender, sender), dmg)) {
            //DRAIN RF ETC 
            LaserItem.resetStackDamageCool(stack, level.getGameTime());
            storage.extractEnergy(LaserItem.COST, false);
          }
        }
        //        target.causeFallDamage(0, 0, null);
      }
    });
    message.done(ctx);
  }

  private static boolean canShoot(ServerPlayer sender, Entity target, ItemStack stack) {
    if (!sender.isAlive() || !target.isAlive() || target.isInvulnerable()) {
      //somene died or target is invincible
      return false;
    }
    if (stack.isEmpty()) {
      return false;
    }
    IEnergyStorage storage = stack.getCapability(CapabilityEnergy.ENERGY, null).orElse(null);
    return (storage != null && storage.extractEnergy(LaserItem.COST, true) == LaserItem.COST);
    //    int dmgCooldown = LaserItem.getDamageCooldown(lasercannon);
    //    //past time plus interval equals now
    //    // dmg every _ ticks, not every tick
    //    return dmgCooldown + LaserItem.DELAYDAMAGETICKS < sender.level.getGameTime();
  }

  public static PacketEntityLaser decode(FriendlyByteBuf buf) {
    PacketEntityLaser msg = new PacketEntityLaser(buf.readInt(), buf.readBoolean());
    return msg;
  }

  public static void encode(PacketEntityLaser msg, FriendlyByteBuf buf) {
    buf.writeInt(msg.entityId);
    buf.writeBoolean(msg.crosshair);
  }
}
