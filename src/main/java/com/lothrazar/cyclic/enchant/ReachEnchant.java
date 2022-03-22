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

import com.lothrazar.cyclic.util.UtilPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ReachEnchant extends EnchantmentCyclic {

  private static final String NBT_REACH_ON = "reachon";
  public static final int REACH_BOOST = 11; // 16 = 11 + 5
  public static final String ID = "reach";
  public static BooleanValue CFG;

  public ReachEnchant(Rarity rarityIn, EnchantmentCategory typeIn, EquipmentSlot... slots) {
    super(rarityIn, typeIn, slots);
    MinecraftForge.EVENT_BUS.register(this);
  }

  @Override
  public boolean isEnabled() {
    return CFG.get();
  }

  @Override
  public int getMaxLevel() {
    return 1;
  }

  @Override
  public boolean canEnchant(ItemStack stack) {
    return true;
  }

  @Override
  public boolean canApplyAtEnchantingTable(ItemStack stack) {
    return this.canEnchant(stack);
  }

  private void turnReachOff(Player player) {
    player.getPersistentData().putBoolean(NBT_REACH_ON, false);
    UtilPlayer.removePlayerReach(player);
  }

  private void turnReachOn(Player player) {
    player.getPersistentData().putBoolean(NBT_REACH_ON, true);
    UtilPlayer.addPlayerReach(player, REACH_BOOST);
  }

  @SubscribeEvent
  public void onEntityUpdate(LivingUpdateEvent event) {
    if (!isEnabled()) {
      return;
    }
    //check if NOT holding this harm
    if (event.getEntityLiving() instanceof Player == false) {
      return;
    }
    Player player = (Player) event.getEntityLiving();
    //Ticking
    ItemStack armor = this.getFirstArmorStackWithEnchant(player);
    int level = 0;
    if (armor.isEmpty() == false && EnchantmentHelper.getEnchantments(armor) != null
        && EnchantmentHelper.getEnchantments(armor).containsKey(this)) {
      //todo: maybe any armor?
      level = EnchantmentHelper.getEnchantments(armor).get(this);
    }
    if (level > 0) {
      turnReachOn(player);
    }
    else {
      //was it on before, do we need to do an off hit
      if (player.getPersistentData().contains(NBT_REACH_ON) && player.getPersistentData().getBoolean(NBT_REACH_ON)) {
        turnReachOff(player);
      }
    }
  }
}
