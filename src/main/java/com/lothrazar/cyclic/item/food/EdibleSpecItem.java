package com.lothrazar.cyclic.item.food;

import com.lothrazar.cyclic.registry.PotionEffectRegistry;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.ModConfigSpec;

public class EdibleSpecItem extends FoodItemWithEffects {

  public static ModConfigSpec.IntValue TICKS;

  public EdibleSpecItem(Properties properties) {
    super(properties.rarity(Rarity.EPIC).food(new FoodProperties.Builder()
        .nutrition(1).saturationModifier(0).alwaysEdible()
        .build()), new Settings().tooltip());
  }

  // 26.1: FoodProperties.Builder#effect(Supplier<MobEffectInstance>, float) is gone - effects moved onto
  // the immutable Consumable component (baked at registration time, no lazy Supplier possible anymore).
  // TICKS.get() must still resolve after config load, not at registration time, so apply it here instead
  // (finishUsingItem already runs long after the game/config is fully loaded).
  @Override
  public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
    ItemStack result = super.finishUsingItem(stack, level, entity);
    if (!level.isClientSide()) {
      entity.addEffect(FoodItemWithEffects.silent(PotionEffectRegistry.NOCLIP, TICKS.get(), 0));
    }
    return result;
  }
}
