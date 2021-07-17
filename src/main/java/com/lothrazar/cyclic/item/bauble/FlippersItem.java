package com.lothrazar.cyclic.item.bauble;

import com.lothrazar.cyclic.util.UtilEntity;
import com.lothrazar.cyclic.util.UtilItemStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class FlippersItem extends ItemBaseToggle {

  private static final float SPEEDFACTOR = 0.11F * 3.5F;

  public FlippersItem(Properties properties) {
    super(properties);
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
        if (entity instanceof PlayerEntity) {
          UtilItemStack.damageItemRandomly(entity, stack);
        }
      }
    }
  }
}
