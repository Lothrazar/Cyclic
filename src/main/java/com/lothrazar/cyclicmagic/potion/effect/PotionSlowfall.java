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
import net.minecraft.entity.player.EntityPlayer;

public class PotionSlowfall extends PotionBase {

  public static final float slowfallSpeed = 0.41F;

  public PotionSlowfall() {
    super("slowfall", true, 0xF46F20);
  }

  @Override
  public void tick(EntityLivingBase entityLiving) {
    if (entityLiving instanceof EntityPlayer) {
      EntityPlayer p = (EntityPlayer) entityLiving;
      if (p.isSneaking()) {
        return;
      }
    }
    // else: so we are either a non-sneaking player, or a non player
    // entity
    // a normal fall seems to go up to 0, -1.2, -1.4, -1.6, then
    // flattens out at -0.078
    if (entityLiving.motionY < 0) {
      entityLiving.motionY *= slowfallSpeed;
      entityLiving.fallDistance = 0f; // for no fall damage
    }
  }
}
