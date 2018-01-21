package com.lothrazar.cyclicmagic.enchantment;
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
