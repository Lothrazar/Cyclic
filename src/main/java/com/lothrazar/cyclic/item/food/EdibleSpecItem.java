package com.lothrazar.cyclic.item.food;

import com.lothrazar.cyclic.registry.PotionEffectRegistry;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Rarity;
import net.neoforged.neoforge.common.ModConfigSpec;

public class EdibleSpecItem extends AppleBuffs {

  public static ModConfigSpec.IntValue TICKS;

  public EdibleSpecItem(Properties properties) {
    super(properties.rarity(Rarity.EPIC).food(new FoodProperties.Builder()
        .nutrition(1).saturationModifier(0).alwaysEdible()
        // Suppliers are lazy so TICKS.get() resolves at eat-time, after config has loaded.
        .effect(() -> AppleBuffs.silent(PotionEffectRegistry.NOCLIP, TICKS.get(), 0), 1)
        .build()), new Settings().tooltip());
  }
}
