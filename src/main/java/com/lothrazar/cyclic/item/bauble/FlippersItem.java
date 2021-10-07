package com.lothrazar.cyclic.item.bauble;

import com.lothrazar.cyclic.util.UtilEntity;
import com.lothrazar.cyclic.util.UtilItemStack;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import net.minecraft.world.item.Item.Properties;

public class FlippersItem extends ItemBaseToggle {

  private static final float SPEEDFACTOR = 0.11F * 3.5F;

  public FlippersItem(Properties properties) {
    super(properties);
  }

  @Override
  public void inventoryTick(ItemStack stack, Level worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
    //so  
    if (!this.isOn(stack)) {
      return;
    }
    if (entityIn instanceof LivingEntity) {
      LivingEntity entity = (LivingEntity) entityIn;
      if (entity.isInWater()) {
        UtilEntity.speedupEntityIfMoving(entity, SPEEDFACTOR);
        if (entity instanceof Player) {
          UtilItemStack.damageItemRandomly(entity, stack);
        }
      }
    }
  }
}
