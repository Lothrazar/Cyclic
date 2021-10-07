package com.lothrazar.cyclic.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraftforge.registries.ForgeRegistries;

public class UtilEnchant {

  public static List<MobEffect> getNegativeEffects() {
    return getEffects(MobEffectCategory.HARMFUL);
  }

  public static List<MobEffect> getBeneficialEffects() {
    return getEffects(MobEffectCategory.BENEFICIAL);
  }

  public static List<MobEffect> getNeutralEffects() {
    return getEffects(MobEffectCategory.NEUTRAL);
  }

  public static List<MobEffect> getAllEffects() {
    return getEffects(null);
  }

  public static List<MobEffect> getEffects(MobEffectCategory effectType) {
    Collection<MobEffect> effects = ForgeRegistries.POTIONS.getValues();
    List<MobEffect> effectsList = new ArrayList<>();
    for (MobEffect effect : effects) {
      if (effectType == null || effect.getCategory() == effectType) {
        effectsList.add(effect);
      }
    }
    return effectsList;
  }

  public static boolean doBookEnchantmentsMatch(ItemStack stack1, ItemStack stack2) {
    Item item1 = stack1.getItem();
    Item item2 = stack2.getItem();
    if (item1 == Items.ENCHANTED_BOOK && item2 == Items.ENCHANTED_BOOK) {
      ListTag ench1 = EnchantedBookItem.getEnchantments(stack1);
      ListTag ench2 = EnchantedBookItem.getEnchantments(stack2);
      if (ench1 == null || ench2 == null) {
        return false;
      }
      if (ench1.equals(ench2)) {
        return true;
      }
    }
    return false;
  }
}
