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
import com.lothrazar.cyclic.base.EnchantBase;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class EnchantStep extends EnchantBase {

  private static final String NBT_ON = ModCyclic.MODID + "_stepenchant";

  public EnchantStep(Rarity rarityIn, EnchantmentType typeIn, EquipmentSlotType... slots) {
    super(rarityIn, typeIn, slots);
    MinecraftForge.EVENT_BUS.register(this);
  }

  @Override
  public int getMaxLevel() {
    return 1;
  }

  @Override
  public boolean canApply(ItemStack stack) {
    //anything that goes on your feet
    boolean yes = stack.getItem() == Items.BOOK ||
        (stack.getItem() instanceof ArmorItem)
            && ((ArmorItem) stack.getItem()).getEquipmentSlot() == EquipmentSlotType.LEGS;
    return yes;
  }

  @Override
  public boolean canApplyAtEnchantingTable(ItemStack stack) {
    return this.canApply(stack);
  }

  @SubscribeEvent
  public void onEntityUpdate(LivingUpdateEvent event) {
    //check if NOT holding this harm
    if (event.getEntityLiving() instanceof PlayerEntity == false) {
      return;
    }
    PlayerEntity player = (PlayerEntity) event.getEntityLiving();
    //Ticking
    ItemStack armor = player.getItemStackFromSlot(EquipmentSlotType.LEGS);
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

  private void turnOn(PlayerEntity player, ItemStack armor) {
    player.getPersistentData().putBoolean(NBT_ON, true);
    if (player.isCrouching()) {
      //make sure that, when sneaking, dont fall off!!
      player.stepHeight = 0.9F;
    }
    else {
      player.stepHeight = 1.0F + (1F / 16F);//PATH BLOCKS etc are 1/16th downif MY feature turns this on, then do it
    }
    //    ModCyclic.log("ON " + player.getPersistentData().getBoolean(NBT_ON));
  }

  private void turnOff(PlayerEntity player, ItemStack armor) {
    //was it on before, do we need to do an off hit
    if (player.getPersistentData().contains(NBT_ON) && player.getPersistentData().getBoolean(NBT_ON)) {
      player.stepHeight = 0.6F;// LivingEntity.class constructor defaults to this
      player.getPersistentData().putBoolean(NBT_ON, false);
    }
  }
}
