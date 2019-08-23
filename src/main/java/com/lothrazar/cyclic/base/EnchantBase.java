package com.lothrazar.cyclic.base;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.inventory.EquipmentSlotType;

public class EnchantBase extends Enchantment {

  protected EnchantBase(Rarity rarityIn, EnchantmentType typeIn, EquipmentSlotType[] slots) {
    super(rarityIn, typeIn, slots);
  }
}
