package com.lothrazar.cyclic.base;

import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import net.minecraft.world.item.enchantment.Enchantment.Rarity;

public abstract class EnchantBase extends Enchantment {

  protected EnchantBase(Rarity rarityIn, EnchantmentCategory typeIn, EquipmentSlot[] slots) {
    super(rarityIn, typeIn, slots);
  }

  public abstract boolean isEnabled();

  public int getCurrentLevelTool(ItemStack stack) {
    if (stack.isEmpty() == false && EnchantmentHelper.getEnchantments(stack).containsKey(this)
        && stack.getItem() != Items.ENCHANTED_BOOK) {
      return EnchantmentHelper.getEnchantments(stack).get(this);
    }
    return -1;
  }

  protected int getCurrentArmorLevelSlot(LivingEntity player, EquipmentSlot type) {
    ItemStack armor = player.getItemBySlot(type);
    int level = 0;
    if (armor.isEmpty() == false && EnchantmentHelper.getEnchantments(armor) != null
        && EnchantmentHelper.getEnchantments(armor).containsKey(this)) {
      level = EnchantmentHelper.getEnchantments(armor).get(this);
    }
    return level;
  }

  protected int getCurrentArmorLevel(LivingEntity player) {
    EquipmentSlot[] armors = new EquipmentSlot[] {
        EquipmentSlot.CHEST, EquipmentSlot.FEET, EquipmentSlot.HEAD, EquipmentSlot.LEGS
    };
    int level = 0;
    for (EquipmentSlot slot : armors) {
      ItemStack armor = player.getItemBySlot(slot);
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

  protected int getLevelAll(LivingEntity p) {
    return Math.max(getCurrentArmorLevel(p), getCurrentLevelTool(p));
  }

  protected ItemStack getFirstArmorStackWithEnchant(LivingEntity player) {
    if (player == null) {
      return ItemStack.EMPTY;
    }
    for (ItemStack main : player.getArmorSlots()) {
      if ((main.isEmpty() == false) && EnchantmentHelper.getEnchantments(main).containsKey(this)) {
        return main;
      }
    }
    return ItemStack.EMPTY;
  }

  protected int getCurrentLevelTool(LivingEntity player) {
    if (player == null) {
      return -1;
    }
    ItemStack main = player.getMainHandItem();
    ItemStack off = player.getOffhandItem();
    return Math.max(getCurrentLevelTool(main), getCurrentLevelTool(off));
  }
}
