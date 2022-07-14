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
import com.lothrazar.cyclic.util.AttributesUtil;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class StepEnchant extends EnchantmentCyclic {

  private static final String NBT_ON = ModCyclic.MODID + "_stepenchant";
  public static final String ID = "step";
  public static BooleanValue CFG;

  public StepEnchant(Rarity rarityIn, EnchantmentCategory typeIn, EquipmentSlot... slots) {
    super(rarityIn, typeIn, slots);
    if (isEnabled()) MinecraftForge.EVENT_BUS.register(this);
  }

  @Override
  public boolean isTradeable() {
    return isEnabled() && super.isTradeable();
  }

  @Override
  public boolean isDiscoverable() {
    return isEnabled() && super.isDiscoverable();
  }

  @Override
  public boolean isAllowedOnBooks() {
    return isEnabled() && super.isAllowedOnBooks();
  }

  @Override
  public boolean canApplyAtEnchantingTable(ItemStack stack) {
    return isEnabled() && super.canApplyAtEnchantingTable(stack);
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
    //anything that goes on your feet
    boolean yes = isEnabled()
        && (stack.getItem() instanceof ArmorItem)
        && ((ArmorItem) stack.getItem()).getSlot() == EquipmentSlot.LEGS;
    return yes;
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
    ItemStack armor = player.getItemBySlot(EquipmentSlot.LEGS);
    int level = 0;
    if (armor.isEmpty() == false && EnchantmentHelper.getEnchantments(armor) != null
        && EnchantmentHelper.getEnchantments(armor).containsKey(this)) {
      //todo: maybe any armor?
      level = EnchantmentHelper.getEnchantments(armor).get(this);
    }
    if (level > 0) {
      turnOn(player, armor);
    }
    else {
      //      ModCyclic.log(" level " + level + " and " + armor.getOrCreateTag().getBoolean(NBT_ON));
      turnOff(player, armor);
    }
  }

  private void turnOn(Player player, ItemStack armor) {
    player.getPersistentData().putBoolean(NBT_ON, true);
    AttributesUtil.enableStepHeight(player);
    //    ModCyclic.log("ON " + player.getPersistentData().getBoolean(NBT_ON));
  }

  private void turnOff(Player player, ItemStack armor) {
    //skip if lofty stature has override
    //was it on before, do we need to do an off hit
    if (player.getPersistentData().contains(NBT_ON) && player.getPersistentData().getBoolean(NBT_ON)) {
      AttributesUtil.disableStepHeight(player);
      player.getPersistentData().putBoolean(NBT_ON, false);
    }
  }
}
