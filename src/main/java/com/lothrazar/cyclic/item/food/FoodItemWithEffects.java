package com.lothrazar.cyclic.item.food;

import java.util.List;
import java.util.function.Consumer;
import com.lothrazar.cyclic.item.ItemBaseCyclic;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.alchemy.PotionContents;

public class FoodItemWithEffects extends ItemBaseCyclic {

  public FoodItemWithEffects(Properties properties) {
    this(properties, new Settings().noTooltip()); // disable tooltip since we use potion effects instead
  }

  public FoodItemWithEffects(Properties food, Settings settings) {
    super(food, settings);
  }

  @Override
  public void appendHoverText(ItemStack stack, Item.TooltipContext context, TooltipDisplay tooltipDisplay, Consumer<Component> tooltip, TooltipFlag flag) {
    super.appendHoverText(stack, context, tooltipDisplay, tooltip, flag);
    FoodProperties food = stack.get(DataComponents.FOOD);
    if (food == null || food.effects().isEmpty()) {
      return;
    }
    List<MobEffectInstance> effects = food.effects().stream()
        .map(FoodProperties.PossibleEffect::effect)
        .toList();
    PotionContents.addPotionTooltip(effects, tooltip::add, 1.0F, context.tickRate());
  }

  /**
   * Convenience: build a MobEffectInstance that grants the effect without spawning particles.
   * ambient=false, visible=false (no swirly particles), showIcon=true (HUD timer still appears).
   * Tooltip is still rendered via {@link #appendHoverText} so the player knows what they're getting.
   */
  public static MobEffectInstance silent(Holder<MobEffect> effect, int duration, int amplifier) {
    return new MobEffectInstance(effect, duration, amplifier, false, false, true);
  }
}
