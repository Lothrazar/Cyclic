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
package com.lothrazar.cyclicmagic.core;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;

public abstract class EnchantBase extends Enchantment {

  protected EnchantBase(String name, Rarity rarityIn, EnumEnchantmentType typeIn, EntityEquipmentSlot[] slots) {
    super(rarityIn, typeIn, slots);
    this.setName(name);
  }

  protected int getCurrentLevelTool(ItemStack stack) {
    if (stack.isEmpty() == false && EnchantmentHelper.getEnchantments(stack).containsKey(this))
      return EnchantmentHelper.getEnchantments(stack).get(this);
    return -1;
  }

  protected int getCurrentLevelTool(EntityLivingBase player) {
    if (player == null) {
      return -1;
    }
    ItemStack main = player.getHeldItemMainhand();
    ItemStack off = player.getHeldItemOffhand();
    return Math.max(getCurrentLevelTool(main), getCurrentLevelTool(off));
  }

  protected int getCurrentArmorLevel(EntityLivingBase player) {
    EntityEquipmentSlot[] armors = new EntityEquipmentSlot[] {
        EntityEquipmentSlot.CHEST, EntityEquipmentSlot.FEET, EntityEquipmentSlot.HEAD, EntityEquipmentSlot.LEGS
    };
    int level = 0;
    for (EntityEquipmentSlot slot : armors) {
      ItemStack armor = player.getItemStackFromSlot(slot);
      if (armor.isEmpty() == false
          && EnchantmentHelper.getEnchantments(armor) != null
          && EnchantmentHelper.getEnchantments(armor).containsKey(this)) {
        int newlevel = EnchantmentHelper.getEnchantments(armor).get(this);
        if (newlevel > level) {
          level = newlevel;
        }
      }
    }
    return level;
  }

  protected int getLevelAll(EntityLivingBase p) {
    return Math.max(getCurrentArmorLevel(p), getCurrentLevelTool(p));
  }

  protected ItemStack getFirstArmorStackWithEnchant(EntityLivingBase player) {
    if (player == null) {
      return ItemStack.EMPTY;
    }
    for (ItemStack main : player.getArmorInventoryList()) {
      if ((main.isEmpty() == false) &&
          EnchantmentHelper.getEnchantments(main).containsKey(this)) {
        return main;// EnchantmentHelper.getEnchantments(main).get(this);
      }
    }
    return ItemStack.EMPTY;
  }
}
