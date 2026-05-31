package com.lothrazar.cyclic.item.food;

import java.util.List;
import com.lothrazar.cyclic.item.ItemBaseCyclic;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.alchemy.PotionContents;

public class AppleBuffs extends ItemBaseCyclic {

  public AppleBuffs(Properties properties) {
    super(properties, new Settings().noTooltip());
  }

  @Override
  public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
    super.appendHoverText(stack, context, tooltip, flag);
    FoodProperties food = stack.get(DataComponents.FOOD);
    if (food == null || food.effects().isEmpty()) {
      return;
    }
    List<MobEffectInstance> effects = food.effects().stream()
        .map(FoodProperties.PossibleEffect::effect)
        .toList();
    PotionContents.addPotionTooltip(effects, tooltip::add, 1.0F, context.tickRate());
  }

}
