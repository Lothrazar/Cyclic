package com.lothrazar.cyclic.item;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Food;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemExp extends Item {

  public static final int EXP_PER_FOOD = 100;

  public ItemExp(Properties properties) {
    super(properties.food(new Food.Builder().setAlwaysEdible().fastToEat().build()));
  }

  @Override
  public ItemStack onItemUseFinish(ItemStack stack, World worldIn, LivingEntity entityLiving) {
    if (entityLiving instanceof PlayerEntity) {
      PlayerEntity p = (PlayerEntity) entityLiving;
      p.giveExperiencePoints(EXP_PER_FOOD);
    }
    return super.onItemUseFinish(stack, worldIn, entityLiving);
  }
}
