package com.lothrazar.cyclic.item;

import com.lothrazar.cyclic.util.UtilStuff;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;

public class GloveItem extends Item {

  private static final int TICKS_FALLDIST_SYNC = 22;//tick every so often
  private static final double CLIMB_SPEED = 0.288D;

  public GloveItem(Properties properties) {
    super(properties.maxStackSize(1));
  }

  @Override
  public void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
    //so
    if (!this.canTick(stack)) {
      return;
    }
    if (entityIn instanceof PlayerEntity) {
      PlayerEntity player = (PlayerEntity) entityIn;
      if (player.collidedHorizontally) {
        World world = player.getEntityWorld();
        UtilStuff.tryMakeEntityClimb(world, player, CLIMB_SPEED);
        //        stack.damageItem(1, player);
        if (world.isRemote &&
            player.ticksExisted % TICKS_FALLDIST_SYNC == 0) {
          UtilStuff.playSound(player, SoundEvents.BLOCK_LADDER_STEP);
        }
      }
    }
  }

  private boolean canTick(ItemStack stack) {
    return true;//TODO
  }
}
