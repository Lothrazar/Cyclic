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
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingSetAttackTargetEvent;
import net.minecraftforge.eventbus.api.Event.Result;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import net.minecraft.world.item.enchantment.Enchantment.Rarity;

public class EnchantBeekeeper extends EnchantBase {

  public EnchantBeekeeper(Rarity rarityIn, EnchantmentCategory typeIn, EquipmentSlot... slots) {
    super(rarityIn, typeIn, slots);
    MinecraftForge.EVENT_BUS.register(this);
  }

  public static BooleanValue CFG;
  public static final String ID = "beekeeper";

  @Override
  public boolean isEnabled() {
    return CFG.get();
  }

  @Override
  public int getMaxLevel() {
    return 2;
  }

  @SubscribeEvent
  public void onLivingSetAttackTargetEvent(LivingSetAttackTargetEvent event) {
    if (event.getTarget() instanceof Player && event.getEntityLiving().getType() == EntityType.BEE) {
      int level = this.getCurrentArmorLevel(event.getTarget());
      if (level > 0) {
        Bee bee = (Bee) event.getEntityLiving();
        bee.setAggressive(false);
        bee.setRemainingPersistentAngerTime(0);
        bee.setPersistentAngerTarget(null);
        event.setResult(Result.DENY);
      }
    }
  }

  @SubscribeEvent(priority = EventPriority.LOWEST)
  public void onLivingDamageEvent(LivingDamageEvent event) {
    int level = this.getCurrentArmorLevel(event.getEntityLiving());
    if (level >= 1 && event.getSource() != null
        && event.getSource().getDirectEntity() != null) {
      // Beekeeper I+
      Entity esrc = event.getSource().getDirectEntity();
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
