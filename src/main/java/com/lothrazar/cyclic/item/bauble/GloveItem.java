package com.lothrazar.cyclic.item.bauble;

import com.lothrazar.cyclic.util.UtilEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class GloveItem extends ItemBaseToggle {

  private static final double CLIMB_SPEED = 0.288D;

  public GloveItem(Properties properties) {
    super(properties.maxStackSize(1));
  }

  @Override
  public void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
    //so
    if (!this.isOn(stack)) {
      return;
    }
    if (entityIn instanceof PlayerEntity) {
      PlayerEntity player = (PlayerEntity) entityIn;
      if (player.collidedHorizontally) {
        World world = player.getEntityWorld();
        UtilEntity.tryMakeEntityClimb(world, player, CLIMB_SPEED);
      }
    }
  }
}
