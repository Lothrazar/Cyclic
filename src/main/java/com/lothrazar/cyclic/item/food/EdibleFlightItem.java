package com.lothrazar.cyclic.item.food;

import com.lothrazar.cyclic.registry.PotionEffectRegistry;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Rarity;
import net.neoforged.neoforge.common.ModConfigSpec;

public class EdibleFlightItem extends FoodItemWithEffects {

  public static ModConfigSpec.IntValue TICKS;

  public EdibleFlightItem(Properties properties) {
    super(properties.rarity(Rarity.RARE).food(new FoodProperties.Builder()
        .nutrition(1).saturationModifier(0).alwaysEdible()
        // Supplier is lazy so TICKS.get() resolves at eat-time, after config has loaded.
        .effect(() -> FoodItemWithEffects.silent(PotionEffectRegistry.FLIGHT, TICKS.get(), 0), 1)
        .build()));
  }
}
