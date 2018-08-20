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

import net.minecraft.entity.EntityLivingBase;

public class PotionStunned extends PotionBase {

  public PotionStunned() {
    super("stunned", true, 0xC2C4F7);
  }

  @Override
  public void tick(EntityLivingBase entity) {
    // if (entity.world.rand.nextDouble() < 0.8)
    //UtilParticle.spawnParticle(entity.world, EnumParticleTypes.SPIT, entity);
    entity.posX = entity.prevPosX;
    entity.posZ = entity.prevPosZ;
    entity.rotationYaw = (float) (entity.world.rand.nextInt(180) - 360.0);
    entity.rotationPitch = (float) (entity.world.rand.nextInt(90) - 180.0);
    //    entity.addVelocity(x, y, z);
    if (entity.world.isRemote) {
      entity.setVelocity(0, entity.motionY, 0);
    }
  }
}
