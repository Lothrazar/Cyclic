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
package com.lothrazar.cyclicmagic.potion.effect;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.EnderTeleportEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class PotionEnder extends PotionBase {

  public PotionEnder() {
    super("ender", true, 0x0B4D42);
  }

  @SubscribeEvent
  public void onEnderTeleportEvent(EnderTeleportEvent event) {
    Entity ent = event.getEntity();
    if (ent instanceof EntityLivingBase == false) {
      return;
    }
    EntityLivingBase living = (EntityLivingBase) event.getEntity();
    if (living.isPotionActive(this)) {
      event.setAttackDamage(0);
    }
  }

  @SubscribeEvent
  public void onHurt(LivingHurtEvent event) {
    if (event.getEntityLiving().isPotionActive(this) && event.getSource() == DamageSource.IN_WALL) {
      event.setAmount(0);
    }
  }

  @SubscribeEvent
  public void onLivingKill(LivingDeathEvent event) {
    if (event.getSource().getTrueSource() instanceof EntityPlayer) {
      EntityPlayer player = (EntityPlayer) event.getSource().getTrueSource();
      Entity target = event.getEntity();
      if (player.isPotionActive(this) && target instanceof EntityEnderman) {
        World world = player.getEntityWorld();
        int randMore = world.rand.nextInt(5) + 1;// range[1,5]
        world.spawnEntity(new EntityXPOrb(world, target.posX, target.posY, target.posZ, randMore));
      }
    }
  }
}
