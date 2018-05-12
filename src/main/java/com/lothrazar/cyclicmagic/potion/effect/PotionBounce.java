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

import com.lothrazar.cyclicmagic.core.util.UtilEntity;
import com.lothrazar.cyclicmagic.core.util.UtilParticle;
import com.lothrazar.cyclicmagic.core.util.UtilSound;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class PotionBounce extends PotionBase {

  private static final double VERTICAL_MOMENTUM_FACTOR = 0.917;
  private static final float DAMAGE_REDUCTION = 0.1f;
  private static final int MIN_HEIGHT_START_BOUNCE = 3;
  private static final double PERCENT_HEIGHT_BOUNCED = 0.95;
  private static final String NBT_MOTIONY = "motionY";//is float stored as int so we use 100 factor each way
  private static final String NBT_TICK = "ticksExisted";

  public PotionBounce() {
    super("bounce", true, 0x91E459);
  }

  @SubscribeEvent
  public void onFall(LivingFallEvent event) {
    EntityLivingBase entity = event.getEntityLiving();
    if (entity == null || entity instanceof EntityPlayer == false || entity.isSneaking()
        || entity.isPotionActive(this) == false) {
      return;
    }
    EntityPlayer player = (EntityPlayer) entity;
    if (event.getDistance() >= MIN_HEIGHT_START_BOUNCE) {
      event.setDamageMultiplier(0);
      if (entity.getEntityWorld().isRemote == false) {
        event.setCanceled(true); //nada serverside
      }
      else {
        UtilSound.playSound(player, player.getPosition(), SoundEvents.BLOCK_SLIME_FALL, SoundCategory.PLAYERS, UtilSound.VOLUME / event.getDistance());
        UtilParticle.spawnParticle(player.world, EnumParticleTypes.SLIME, player.getPosition());
        event.setDistance(0);// fall distance
        if (player.isElytraFlying() == false) {
          player.motionY *= -PERCENT_HEIGHT_BOUNCED;
          player.isAirBorne = true;
          player.onGround = false;
          //postpone until one tick later. otherwise there is vanilla code internally that says "ok you finished falldamage so motionY=0;"
          player.posY += 0.01;
          player.getEntityData().setInteger(NBT_TICK, player.ticksExisted + 1);
          player.getEntityData().setInteger(NBT_MOTIONY, (int) (player.motionY * 100f));
        }
      }
    }
    else if (!entity.getEntityWorld().isRemote && entity.isSneaking()) {
      event.setDamageMultiplier(DAMAGE_REDUCTION);
    }
  }

  @SubscribeEvent
  public void rebounceTick(TickEvent.PlayerTickEvent event) {
    //catch a rebounce that was postponed from last tick
    if (event.player.isPotionActive(this) && event.player.isElytraFlying() == false) {
      EntityPlayer player = event.player;
      if (player.isElytraFlying() || event.phase != TickEvent.Phase.END) {
        return;
      }
      float motionY = (player.getEntityData().getInteger(NBT_MOTIONY)) / 100f;
      if (player.getEntityData().getInteger(NBT_TICK) == player.ticksExisted && motionY > 0) {
        player.getEntityData().setInteger(NBT_TICK, -1);
        player.motionY = motionY;
      }
    }
  }

  @Override
  public void tick(EntityLivingBase entity) {
    if (entity.onGround == false && entity.isPotionActive(this)
        && entity.isElytraFlying() == false) {//preserve momentum, otherwise it will be like regular falling/gravity
      //yes this works if drank potion and not just from launcher but is ok
      UtilEntity.dragEntityMomentum(entity, VERTICAL_MOMENTUM_FACTOR);
    }
  };
}
