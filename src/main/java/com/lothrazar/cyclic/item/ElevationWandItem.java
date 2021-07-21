package com.lothrazar.cyclic.item;

import com.lothrazar.cyclic.base.ItemBase;
import com.lothrazar.cyclic.registry.SoundRegistry;
import com.lothrazar.cyclic.util.UtilEntity;
import com.lothrazar.cyclic.util.UtilItemStack;
import com.lothrazar.cyclic.util.UtilSound;
import com.lothrazar.cyclic.util.UtilWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ElevationWandItem extends ItemBase {

  public ElevationWandItem(Properties properties) {
    super(properties);
  }

  @Override
  public ActionResultType itemInteractionForEntity(ItemStack stack, PlayerEntity playerIn, LivingEntity target, Hand hand) {
    return tryTeleport(playerIn.world, playerIn, target, stack) ? ActionResultType.SUCCESS : ActionResultType.CONSUME;
  }

  @Override
  public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
    return tryTeleport(worldIn, playerIn, playerIn, playerIn.getHeldItem(handIn)) ? ActionResult.resultSuccess(playerIn.getHeldItem(handIn)) : ActionResult.resultConsume(playerIn.getHeldItem(handIn));
  }

  private boolean tryTeleport(World world, PlayerEntity playerIn, LivingEntity target, ItemStack stack) {
    if (target == null || stack == null) {
      return false;
    }
    BlockPos destination = UtilWorld.getFirstBlockAbove(world, target.getPosition());
    if (destination != null) {
      //play sound at old locaiton on leaving
      UtilSound.playSound(target, SoundRegistry.WARP_ECHO);
      if (!world.isRemote) {
        UtilEntity.enderTeleportEvent(target, world, destination);
        UtilItemStack.damageItem(playerIn, stack);
      }
      //play sound att new location also, may be far away
      UtilSound.playSound(target, SoundRegistry.WARP_ECHO);
      return true;
    }
    UtilSound.playSound(target, SoundEvents.BLOCK_FIRE_EXTINGUISH, 0.1F, 1F);
    return false;
  }
}
