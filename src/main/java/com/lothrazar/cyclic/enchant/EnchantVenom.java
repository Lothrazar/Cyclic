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
import com.lothrazar.cyclic.data.Const;
import com.lothrazar.cyclic.util.UtilEntity;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class EnchantVenom extends EnchantBase {

  public EnchantVenom(Rarity rarityIn, EnchantmentType typeIn, EquipmentSlotType... slots) {
    super(rarityIn, typeIn, slots);
    MinecraftForge.EVENT_BUS.register(this);
  }

  final int durationTicksPerLevel = 3 * Const.TICKS_PER_SEC;
  public static BooleanValue CFG;
  public static final String ID = "venom";

  @Override
  public boolean isEnabled() {
    return CFG.get();
  }

  @Override
  public int getMaxLevel() {
    return 2;
  }

  @SubscribeEvent
  public void onAttackEntity(AttackEntityEvent event) {
    if (event.getTarget() instanceof LivingEntity == false) {
      return;
    }
    LivingEntity target = (LivingEntity) event.getTarget();
    PlayerEntity attacker = event.getPlayer();
    ItemStack main = attacker.getHeldItemMainhand();
    ItemStack off = attacker.getHeldItemOffhand();
    int mainLevel = -1, offLevel = -1;
    if (main != null && EnchantmentHelper.getEnchantments(main).containsKey(this)) {
      mainLevel = EnchantmentHelper.getEnchantments(main).get(this);
    }
    if (off != null && EnchantmentHelper.getEnchantments(off).containsKey(this)) {
      offLevel = EnchantmentHelper.getEnchantments(off).get(this);
    }
    int level = Math.max(mainLevel, offLevel);
    if (level > 0) {
      // we -1  since potion level 1 is Poison II
      //so that means enchantment I giving poison I means this
      UtilEntity.addOrMergePotionEffect(target, new EffectInstance(Effects.POISON, durationTicksPerLevel * level, level - 1));
    }
  }
}
