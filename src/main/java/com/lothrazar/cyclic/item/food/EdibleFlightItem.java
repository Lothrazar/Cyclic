package com.lothrazar.cyclic.item.food;

import java.util.List;
import com.lothrazar.cyclic.registry.PotionEffectRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.common.ModConfigSpec;


public class EdibleFlightItem extends AppleBuffs {

  public static ModConfigSpec.IntValue TICKS;

  public EdibleFlightItem(Properties properties) {
    super(properties.rarity(Rarity.RARE).food(new FoodProperties.Builder().nutrition(1).saturationModifier(0).alwaysEdible().build()));
  }

  @Override
  @OnlyIn(Dist.CLIENT)
  public void appendHoverText(ItemStack stack, Item.TooltipContext worldIn, List<Component> tooltip, TooltipFlag flagIn) {
    tooltip.add(Component.translatable(getDescriptionId() + ".tooltip").withStyle(ChatFormatting.GRAY));
  }

  @Override
  public ItemStack finishUsingItem(ItemStack stack, Level worldIn, LivingEntity entity) {
    final var flight = PotionEffectRegistry.FLIGHT;
    if (entity.hasEffect(flight)) {
      MobEffectInstance currentEff = entity.getEffect(flight);
      currentEff.update(new MobEffectInstance(PotionEffectRegistry.FLIGHT, currentEff.getDuration() + TICKS.get())); // update to merge together new and existing timers
    }
    else {
      entity.addEffect(new MobEffectInstance(flight, TICKS.get()));
    }
    return super.finishUsingItem(stack, worldIn, entity);
  }
}
