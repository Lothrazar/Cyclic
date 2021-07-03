package com.lothrazar.cyclic.item.bauble;

import com.lothrazar.cyclic.util.UtilEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class FlippersItem extends ItemBaseToggle {

  private static final float SPEEDFACTOR = 0.11F * 3.5F;

  public FlippersItem(Properties properties) {
    super(properties.maxStackSize(1));
  }

  @Override
  public void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
    //so
    if (!this.isOn(stack)) {
      return;
    }
    if (entityIn instanceof LivingEntity) {
      LivingEntity entity = (LivingEntity) entityIn;
      if (entity.isInWater()) {
        UtilEntity.speedupEntityIfMoving(entity, SPEEDFACTOR);
      }
    }
  }
}
