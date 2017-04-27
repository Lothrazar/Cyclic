package com.lothrazar.cyclicmagic.enchantment;
import com.lothrazar.cyclicmagic.CyclicGuideBook;
import com.lothrazar.cyclicmagic.registry.GuideRegistry;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;

public abstract class EnchantBase extends Enchantment {
  protected EnchantBase(String name,Rarity rarityIn, EnumEnchantmentType typeIn, EntityEquipmentSlot[] slots) {
    super(rarityIn, typeIn, slots);
    this.setName(name);

    GuideRegistry.register(this);
  }
  protected int getCurrentLevelTool(EntityLivingBase player) {
    if (player == null) { return -1; }
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
