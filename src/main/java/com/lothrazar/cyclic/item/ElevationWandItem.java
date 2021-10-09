package com.lothrazar.cyclic.item;

import com.lothrazar.cyclic.base.ItemBase;
import com.lothrazar.cyclic.registry.SoundRegistry;
import com.lothrazar.cyclic.util.UtilEntity;
import com.lothrazar.cyclic.util.UtilItemStack;
import com.lothrazar.cyclic.util.UtilSound;
import com.lothrazar.cyclic.util.UtilWorld;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class ElevationWandItem extends ItemBase {

  public ElevationWandItem(Properties properties) {
    super(properties);
  }

  @Override
  public InteractionResult interactLivingEntity(ItemStack stack, Player playerIn, LivingEntity target, InteractionHand hand) {
    return tryTeleport(playerIn.level, playerIn, target, stack) ? InteractionResult.SUCCESS : InteractionResult.CONSUME;
  }

  @Override
  public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {
    return tryTeleport(worldIn, playerIn, playerIn, playerIn.getItemInHand(handIn)) ? InteractionResultHolder.success(playerIn.getItemInHand(handIn)) : InteractionResultHolder.consume(playerIn.getItemInHand(handIn));
  }

  private boolean tryTeleport(Level world, Player playerIn, LivingEntity target, ItemStack stack) {
    if (target == null || stack == null) {
      return false;
    }
    BlockPos destination = UtilWorld.getFirstBlockAbove(world, target.blockPosition());
    if (destination != null) {
      //play sound at old locaiton on leaving
      UtilSound.playSound(target, SoundRegistry.WARP_ECHO);
      if (!world.isClientSide) {
        UtilEntity.enderTeleportEvent(target, world, destination);
        UtilItemStack.damageItem(playerIn, stack);
      }
      //play sound att new location also, may be far away
      UtilSound.playSound(target, SoundRegistry.WARP_ECHO);
      return true;
    }
    UtilSound.playSound(target, SoundEvents.FIRE_EXTINGUISH, 0.1F, 1F);
    return false;
  }
}
