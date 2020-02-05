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
package com.lothrazar.cyclic.item.horse;

import com.lothrazar.cyclic.item.ItemEntityInteractable;
import com.lothrazar.cyclic.util.UtilEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.passive.horse.HorseEntity;
import net.minecraft.util.ActionResultType;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.EntityInteract;

public class ItemHorseRedstoneSpeed extends ItemEntityInteractable {

  public static final int SPEED_MAX = 50;
  private static final double SPEED_AMT = 0.004;

  public ItemHorseRedstoneSpeed(Properties prop) {
    super(prop);
  }

  @Override
  public void interactWith(EntityInteract event) {
    if (event.getItemStack().getItem() == this
        && event.getTarget() instanceof HorseEntity) {
      // lets go 
      HorseEntity ahorse = (HorseEntity) event.getTarget();
      double speed = ahorse.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getValue();
      double newSpeed = speed + SPEED_AMT;
      if (UtilEntity.getSpeedTranslated(newSpeed) < SPEED_MAX) {
        ahorse.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(newSpeed);
        event.setCanceled(true);
        event.setCancellationResult(ActionResultType.SUCCESS);
        event.getItemStack().shrink(1);
        UtilEntity.eatingHorse(ahorse);
      }
    }
  }
}
