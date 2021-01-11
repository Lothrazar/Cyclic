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
package com.lothrazar.cyclic.item.carrot;

import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.base.ItemEntityInteractable;
import com.lothrazar.cyclic.util.UtilChat;
import com.lothrazar.cyclic.util.UtilParticle;
import com.lothrazar.cyclic.util.UtilSound;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.horse.AbstractChestedHorseEntity;
import net.minecraft.entity.passive.horse.AbstractHorseEntity;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.SoundEvents;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.EntityInteract;

public class ItemHorseEnder extends ItemEntityInteractable {

  public static final String NBT_KEYACTIVE = ModCyclic.MODID + "_carrot_ender";

  public ItemHorseEnder(Properties prop) {
    super(prop);
  }

  public static void onSuccess(LivingEntity liv) {
    UtilSound.playSound(liv, SoundEvents.ENTITY_GENERIC_DRINK);
    UtilParticle.spawnParticle(liv.world, ParticleTypes.CRIT, liv.getPosition(), 3);
    increment(liv, -1);
    //    int current = ahorse.getPersistentData().getInt(NBT_KEYACTIVE);
    //    UtilChat.addChatMessage(event.getPlayer(), UtilChat.lang("cyclic.carrot_ender.count") + current);
  }

  private static void increment(LivingEntity ahorse, int val) {
    int old = ahorse.getPersistentData().getInt(NBT_KEYACTIVE);
    ahorse.getPersistentData().putInt(NBT_KEYACTIVE, old + val);
  }

  @Override
  public void interactWith(EntityInteract event) {
    if (event.getItemStack().getItem() == this
        && event.getTarget() instanceof AbstractHorseEntity
        && !event.getPlayer().getCooldownTracker().hasCooldown(this)) {
      // lets go 
      AbstractHorseEntity ahorse = (AbstractHorseEntity) event.getTarget();
      if (event.getTarget() instanceof AbstractChestedHorseEntity
          && ahorse.isTame()) {
        AbstractChestedHorseEntity ss = (AbstractChestedHorseEntity) event.getTarget();
        ss.setChested(true);
      }
      //do the thing 
      increment(ahorse, 1);
      event.setCanceled(true);
      event.setCancellationResult(ActionResultType.SUCCESS);
      event.getPlayer().getCooldownTracker().setCooldown(this, 1);
      event.getItemStack().shrink(1);
      int current = ahorse.getPersistentData().getInt(NBT_KEYACTIVE);
      UtilChat.addChatMessage(event.getPlayer(), UtilChat.lang("cyclic.carrot_ender.count") + current);
    }
  }
}
