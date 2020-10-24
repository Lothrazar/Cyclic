package com.lothrazar.cyclic.item;

import com.lothrazar.cyclic.base.ItemBase;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class AppleChocolate extends ItemBase {

  public AppleChocolate(Properties properties) {
    super(properties);
  }

  @Override
  public ItemStack onItemUseFinish(ItemStack stack, World worldIn, LivingEntity entityLiving) {
    if (!worldIn.isRemote) entityLiving.curePotionEffects(stack); // FORGE - move up so stack.shrink does not turn stack into air
    return super.onItemUseFinish(stack, worldIn, entityLiving);
  }
}
