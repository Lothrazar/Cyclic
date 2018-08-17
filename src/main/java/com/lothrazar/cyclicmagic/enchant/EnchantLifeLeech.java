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
package com.lothrazar.cyclicmagic.enchant;

import java.util.ArrayList;
import com.lothrazar.cyclicmagic.core.enchant.EnchantBase;
import com.lothrazar.cyclicmagic.core.util.UtilParticle;
import com.lothrazar.cyclicmagic.guide.GuideRegistry;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.util.EnumParticleTypes;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EnchantLifeLeech extends EnchantBase {

  public EnchantLifeLeech() {
    super("lifeleech", Rarity.COMMON, EnumEnchantmentType.WEAPON, new EntityEquipmentSlot[] { EntityEquipmentSlot.MAINHAND });
    GuideRegistry.register(this, new ArrayList<String>());
  }

  @Override
  public int getMaxLevel() {
    return 1;
  }

  @SubscribeEvent
  public void onEntityKill(LivingDeathEvent event) {
    if (event.getSource().getTrueSource() instanceof EntityPlayer && event.getEntity() instanceof EntityLivingBase) {
      EntityPlayer attacker = (EntityPlayer) event.getSource().getTrueSource();
      EntityLivingBase target = (EntityLivingBase) event.getEntity();
      int level = getCurrentLevelTool(attacker);
      if (level > 0) {
        // we -1  since potion level 1 is  II
        //so that means enchantment I giving poison I means this
        int restore = (int) Math.max(Math.ceil(target.getMaxHealth() / 5), 4);
        int min = 1;//so if restore starts at 4 the rand will be [min,restore]
        restore = attacker.getEntityWorld().rand.nextInt(restore + 1) + min;
        if (restore > 0) {
          attacker.getFoodStats().setFoodLevel(attacker.getFoodStats().getFoodLevel() + level);
          if (attacker.getHealth() < attacker.getMaxHealth()) {
            attacker.heal(restore);
            UtilParticle.spawnParticle(attacker.getEntityWorld(), EnumParticleTypes.HEART, attacker.getPosition().up(2));
          }
        }
      }
    }
  }

  @SubscribeEvent
  public void onAttackEntity(AttackEntityEvent event) {
    if (event.getTarget() instanceof EntityLivingBase == false) {
      return;
    }
    //    EntityLivingBase target = (EntityLivingBase) event.getTarget();
    EntityPlayer attacker = event.getEntityPlayer();
    int level = getCurrentLevelTool(attacker);
    if (level > 0 && attacker.getHealth() < attacker.getMaxHealth()) {
      UtilParticle.spawnParticle(attacker.getEntityWorld(), EnumParticleTypes.HEART, attacker.getPosition().up(2));
      attacker.heal(level);
    }
  }
}
