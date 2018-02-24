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
package com.lothrazar.cyclicmagic.item.bauble;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.data.Const;
import com.lothrazar.cyclicmagic.item.base.BaseCharm;
import com.lothrazar.cyclicmagic.registry.PotionEffectRegistry;
import com.lothrazar.cyclicmagic.util.UtilParticle;
import com.lothrazar.cyclicmagic.util.UtilSound;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumParticleTypes;

public class ItemCharmSlowfall extends BaseCharm implements IHasRecipe {
  private final static int seconds = 30;
  private final static int fallDistanceLimit = 6;
  private final static int durability = 64;
  private final static Potion potion = PotionEffectRegistry.SLOWFALL;
  public ItemCharmSlowfall() {
    super(durability);
  }
  @Override
  public void onTick(ItemStack stack, EntityPlayer living) {
    if (!this.canTick(stack)) {
      return;
    }
    if (living.fallDistance >= fallDistanceLimit && !living.isPotionActive(potion)) {
      living.addPotionEffect(new PotionEffect(potion, seconds * Const.TICKS_PER_SEC, Const.Potions.I));
      super.damageCharm(living, stack);
      UtilSound.playSound(living, living.getPosition(), SoundEvents.ITEM_ELYTRA_FLYING, living.getSoundCategory());
      UtilParticle.spawnParticle(living.getEntityWorld(), EnumParticleTypes.SUSPENDED, living.getPosition());
    }
    if (living.isPotionActive(potion)) { //hacky / workaround for a reported issue being stuck at 0:00
      if (living.getActivePotionEffect(potion).getDuration() < 1) {
        living.removePotionEffect(potion);
      }
    }
  }
  @Override
  public IRecipe addRecipe() {
    return super.addRecipeAndRepair(Items.RABBIT_FOOT);
  }
}
