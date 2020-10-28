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
package com.lothrazar.cyclic.enchant;

import com.lothrazar.cyclic.base.EnchantBase;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.BeeEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.BabyEntitySpawnEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingSetAttackTargetEvent;
import net.minecraftforge.eventbus.api.Event.Result;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class EnchantBeekeeper extends EnchantBase {

  public EnchantBeekeeper(Rarity rarityIn, EnchantmentType typeIn, EquipmentSlotType... slots) {
    super(rarityIn, typeIn, slots);
    MinecraftForge.EVENT_BUS.register(this);
  }

  @Override
  public int getMaxLevel() {
    return 2;
  }

  BabyEntitySpawnEvent event;// if nearby beekeeper, more spawn? bee extends animal where this is fired so?
  LivingSetAttackTargetEvent event2;//when bee targets a player.? 
  //  @SubscribeEvent
  //  public void onBabyEntitySpawnEvent(BabyEntitySpawnEvent event) {
  //    // instanceof BeeEntity 
  //    if (event.getChild().getType() == EntityType.BEE && event.getCausedByPlayer() != null) {
  //      //
  //      int level = this.getCurrentArmorLevel(event.getCausedByPlayer());
  //      if (level > 0) {
  //        //spawn new one
  //        ModCyclic.LOGGER.info("Breeed extra bee");
  //        BeeEntity newbee = EntityType.BEE.create(event.getChild().world);
  //        newbee.setLocationAndAngles(event.getChild().getPosX(), event.getChild().getPosY(), event.getChild().getPosZ(), 0, 0);
  //        //        newbee.setLocationAndAngles(x, y, z, yaw, pitch);
  //        //        newbee = new BeeEntity(null, null);
  //        event.getChild().world.addEntity(newbee);
  //      }
  //    }
  //  }

  @SubscribeEvent
  public void onLivingSetAttackTargetEvent(LivingSetAttackTargetEvent event) {
    if (event.getTarget() instanceof PlayerEntity && event.getEntityLiving().getType() == EntityType.BEE) {
      int level = this.getCurrentArmorLevel(event.getTarget());
      if (level > 0) {
        BeeEntity bee = (BeeEntity) event.getEntityLiving();
        bee.setAggroed(false);
        bee.setAngerTime(0);
        bee.setAngerTarget(null);
        //        ModCyclic.LOGGER.info("no hurt me");
        event.setResult(Result.DENY);
      }
    }
  }

  @SubscribeEvent(priority = EventPriority.LOWEST)
  public void onLivingDamageEvent(LivingDamageEvent event) {
    int level = this.getCurrentArmorLevel(event.getEntityLiving());
    if (level >= 1 && event.getSource() != null
        && event.getSource().getImmediateSource() != null) {
      // Beekeeper I+
      Entity esrc = event.getSource().getImmediateSource();
      if (esrc.getType() == EntityType.BEE ||
          esrc.getType() == EntityType.BAT ||
          esrc.getType() == EntityType.LLAMA_SPIT) {
        event.setAmount(0);
      }
      if (level >= 2) {
        //Beekeeper II+
        //all of level I and also
        if (esrc.getType() == EntityType.PHANTOM) {
          event.setAmount(0);
        }
      }
    }
  }
}
