package com.lothrazar.cyclic.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import net.minecraft.item.EnchantedBookItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.ListNBT;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;
import net.minecraftforge.registries.ForgeRegistries;

public class UtilEnchant {

  public static List<Effect> getNegativeEffects() {
    return getEffects(EffectType.HARMFUL);
  }

  public static List<Effect> getBeneficialEffects() {
    return getEffects(EffectType.BENEFICIAL);
  }

  public static List<Effect> getNeutralEffects() {
    return getEffects(EffectType.NEUTRAL);
  }

  public static List<Effect> getAllEffects() {
    return getEffects(null);
  }

  public static List<Effect> getEffects(EffectType effectType) {
    Collection<Effect> effects = ForgeRegistries.POTIONS.getValues();
    List<Effect> effectsList = new ArrayList<>();
    for (Effect effect : effects) {
      if (effectType == null || effect.getEffectType() == effectType) {
        effectsList.add(effect);
      }
    }
    return effectsList;
  }

  public static boolean doBookEnchantmentsMatch(ItemStack stack1, ItemStack stack2) {
    Item item1 = stack1.getItem();
    Item item2 = stack2.getItem();
    if (item1 == Items.ENCHANTED_BOOK && item2 == Items.ENCHANTED_BOOK) {
      ListNBT ench1 = EnchantedBookItem.getEnchantments(stack1);
      ListNBT ench2 = EnchantedBookItem.getEnchantments(stack2);
      if (ench1 == null || ench2 == null) {
        return false;
      }
      return ench1.equals(ench2);
    }
    return false;
  }
}
