package com.lothrazar.cyclic.item.food;

import java.util.List;
import com.lothrazar.cyclic.registry.PotionEffectRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;

public class EdibleFlightItem extends AppleBuffs {

  public static IntValue TICKS;

  public EdibleFlightItem(Properties properties) {
    super(properties.rarity(Rarity.RARE).food(new FoodProperties.Builder().nutrition(3).saturationMod(0).alwaysEat().build()));
  }

  @Override
  @OnlyIn(Dist.CLIENT)
  public void appendHoverText(ItemStack stack, Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
    tooltip.add(new TranslatableComponent(getDescriptionId() + ".tooltip").withStyle(ChatFormatting.GRAY));
  }

  @Override
  public ItemStack finishUsingItem(ItemStack stack, Level worldIn, LivingEntity entityLiving) {
    entityLiving.addEffect(new MobEffectInstance(PotionEffectRegistry.FLIGHT.get(), TICKS.get()));
    return super.finishUsingItem(stack, worldIn, entityLiving);
  }
}
