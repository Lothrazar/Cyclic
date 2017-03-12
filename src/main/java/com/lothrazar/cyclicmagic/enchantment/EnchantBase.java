package com.lothrazar.cyclicmagic.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;

public abstract class EnchantBase extends Enchantment {

  protected EnchantBase(Rarity rarityIn, EnumEnchantmentType typeIn, EntityEquipmentSlot[] slots) {
    super(rarityIn, typeIn, slots);
  }

  protected int getCurrentLevelTool(EntityLivingBase player) {
   ItemStack main = player.getHeldItemMainhand();
  ItemStack off = player.getHeldItemOffhand();
    int mainLevel = -1, offLevel = -1;
    if (main != null && EnchantmentHelper.getEnchantments(main).containsKey(this)) {
      mainLevel = EnchantmentHelper.getEnchantments(main).get(this);
    }
    if (off != null && EnchantmentHelper.getEnchantments(off).containsKey(this)) {
      offLevel = EnchantmentHelper.getEnchantments(off).get(this);
    }
    int level = Math.max(mainLevel, offLevel);
    return level;
  }
}
