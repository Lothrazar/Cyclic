package com.lothrazar.cyclic.base;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

public abstract class EnchantBase extends Enchantment {

  protected EnchantBase(Rarity rarityIn, EnchantmentType typeIn, EquipmentSlotType[] slots) {
    super(rarityIn, typeIn, slots);
  }

  public abstract boolean isEnabled();

  public int getCurrentLevelTool(ItemStack stack) {
    if (!stack.isEmpty() && EnchantmentHelper.getEnchantments(stack).containsKey(this)
        && stack.getItem() != Items.ENCHANTED_BOOK) {
      return EnchantmentHelper.getEnchantments(stack).get(this);
    }
    return -1;
  }

  protected int getCurrentArmorLevelSlot(LivingEntity player, EquipmentSlotType type) {
    ItemStack armor = player.getItemStackFromSlot(type);
    int level = 0;
    if (!armor.isEmpty() && EnchantmentHelper.getEnchantments(armor) != null
        && EnchantmentHelper.getEnchantments(armor).containsKey(this)) {
      level = EnchantmentHelper.getEnchantments(armor).get(this);
    }
    return level;
  }

  protected int getCurrentArmorLevel(LivingEntity player) {
    EquipmentSlotType[] armors = new EquipmentSlotType[] {
        EquipmentSlotType.CHEST, EquipmentSlotType.FEET, EquipmentSlotType.HEAD, EquipmentSlotType.LEGS
    };
    int level = 0;
    for (EquipmentSlotType slot : armors) {
      ItemStack armor = player.getItemStackFromSlot(slot);
      if (!armor.isEmpty()
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
    for (ItemStack main : player.getArmorInventoryList()) {
      if (!main.isEmpty() && EnchantmentHelper.getEnchantments(main).containsKey(this)) {
        return main;
      }
    }
    return ItemStack.EMPTY;
  }

  protected int getCurrentLevelTool(LivingEntity player) {
    if (player == null) {
      return -1;
    }
    ItemStack main = player.getHeldItemMainhand();
    ItemStack off = player.getHeldItemOffhand();
    return Math.max(getCurrentLevelTool(main), getCurrentLevelTool(off));
  }
}
