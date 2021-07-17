package com.lothrazar.cyclic.item.bauble;

import com.lothrazar.cyclic.util.UtilEntity;
import com.lothrazar.cyclic.util.UtilItemStack;
import com.lothrazar.cyclic.util.UtilSound;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;

public class GloveItem extends ItemBaseToggle {

  private static final double CLIMB_SPEED = 0.288D;

  public GloveItem(Properties properties) {
    super(properties);
  }

  @Override
  public void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
    //so
    if (!this.isOn(stack)) {
      return;
    }
    if (entityIn instanceof PlayerEntity) {
      PlayerEntity player = (PlayerEntity) entityIn;
      UtilItemStack.damageItemRandomly(player, stack);
      if (player.collidedHorizontally) {
        World world = player.getEntityWorld();
        UtilEntity.tryMakeEntityClimb(world, player, CLIMB_SPEED);
        UtilItemStack.damageItem(player, stack);
        if (worldIn.getGameTime() % 20 == 0) {
          UtilSound.playSound(player, SoundEvents.BLOCK_LADDER_STEP);
        }
      }
    }
  }
}
