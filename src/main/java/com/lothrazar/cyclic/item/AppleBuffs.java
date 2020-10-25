package com.lothrazar.cyclic.item;

import com.lothrazar.cyclic.base.ItemBase;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class AppleBuffs extends ItemBase {

  public AppleBuffs(Properties properties) {
    super(properties);
  }

  @Override
  public ItemStack onItemUseFinish(ItemStack stack, World worldIn, LivingEntity entityLiving) {
    if (entityLiving instanceof PlayerEntity) {
      ((PlayerEntity) entityLiving).getCooldownTracker().setCooldown(this, 30);
    }
    return super.onItemUseFinish(stack, worldIn, entityLiving);
  }
}
