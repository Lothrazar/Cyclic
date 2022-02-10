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

import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.registry.EnchantRegistry;
import com.lothrazar.cyclic.registry.SoundRegistry;
import com.lothrazar.cyclic.util.UtilChat;
import com.lothrazar.cyclic.util.UtilPlayer;
import com.lothrazar.cyclic.util.UtilSound;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class StandEnchant extends EnchantmentCyclic {

  public static final String ID = "laststand";
  public static BooleanValue CFG;
  public static IntValue COST;
  public static IntValue ABS;
  public static IntValue COOLDOWN;

  public StandEnchant(Rarity rarityIn, EnchantmentCategory typeIn, EquipmentSlot... slots) {
    super(rarityIn, typeIn, slots);
    MinecraftForge.EVENT_BUS.register(this);
  }

  @Override
  public boolean checkCompatibility(Enchantment ench) {
    return super.checkCompatibility(ench) && ench != EnchantRegistry.LAUNCH && ench != EnchantRegistry.EXPERIENCE_BOOST
        && ench != Enchantments.MENDING && ench != Enchantments.THORNS;
  }

  @Override
  public boolean isEnabled() {
    return CFG.get();
  }

  @Override
  public int getMaxLevel() {
    return 2;
  }

  @Override
  public boolean canEnchant(ItemStack stack) {
    return (stack.getItem() instanceof ArmorItem)
        && ((ArmorItem) stack.getItem()).getSlot() == EquipmentSlot.LEGS;
  }

  @Override
  public boolean canApplyAtEnchantingTable(ItemStack stack) {
    return this.canEnchant(stack);
  }

  @SubscribeEvent
  public void onEntityUpdate(LivingDamageEvent event) {
    if (!isEnabled()) {
      return;
    }
    final int level = getCurrentArmorLevelSlot(event.getEntityLiving(), EquipmentSlot.LEGS);
    if (level > 0 && event.getEntityLiving().getHealth() - event.getAmount() <= 0 && event.getEntityLiving() instanceof ServerPlayer player) {
      //if enchanted and it would cause death, then we go on
      if (COOLDOWN.get() > 0 &&
          player.getCooldowns().isOnCooldown(player.getItemBySlot(EquipmentSlot.LEGS).getItem())) {
        return; //if equippped enchanted item is on cooldown for any reason, done
      }
      final int xpCost = Math.max(1, COST.get() / level); // min 1.  higher level gives a lower cost. level 1 is 30xp, lvl 3 is 10xp etc
      if (UtilPlayer.getExpTotal(player) < xpCost) {
        return; // POOR
      }
      //survive
      float toSurvive = event.getEntityLiving().getHealth() - 1;
      event.setAmount(toSurvive);
      player.giveExperiencePoints(-1 * xpCost);
      //now the fluff
      UtilSound.playSoundFromServer(player, SoundRegistry.CHAOS_REAPER, 1F, 0.4F);
      UtilChat.sendStatusMessage(player, "enchantment." + ModCyclic.MODID + "." + ID + ".activated");
      if (ABS.get() > 0) {
        player.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, ABS.get(), level - 1));
      }
      if (COOLDOWN.get() > 0) {
        player.getCooldowns().addCooldown(player.getItemBySlot(EquipmentSlot.LEGS).getItem(), COOLDOWN.get());
      }
    }
  }
}
