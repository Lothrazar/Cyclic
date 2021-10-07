package com.lothrazar.cyclic.item.bauble;

import com.lothrazar.cyclic.util.UtilEntity;
import com.lothrazar.cyclic.util.UtilItemStack;
import com.lothrazar.cyclic.util.UtilSound;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.Level;

import net.minecraft.world.item.Item.Properties;

public class GloveItem extends ItemBaseToggle {

  private static final double CLIMB_SPEED = 0.288D;

  public GloveItem(Properties properties) {
    super(properties);
  }

  @Override
  public void inventoryTick(ItemStack stack, Level worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
    //so
    if (!this.isOn(stack)) {
      return;
    }
    if (entityIn instanceof Player) {
      Player player = (Player) entityIn;
      UtilItemStack.damageItemRandomly(player, stack);
      if (player.horizontalCollision) {
        Level world = player.getCommandSenderWorld();
        UtilEntity.tryMakeEntityClimb(world, player, CLIMB_SPEED);
        UtilItemStack.damageItem(player, stack);
        if (worldIn.getGameTime() % 20 == 0) {
          UtilSound.playSound(player, SoundEvents.LADDER_STEP);
        }
      }
    }
  }
}
