package com.lothrazar.cyclic.item;

import java.util.Optional;
import com.lothrazar.cyclic.base.ItemBase;
import com.lothrazar.cyclic.util.UtilItemStack;
import com.mojang.datafixers.util.Either;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.Unit;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

public class SleepingMatItem extends ItemBase {

  public SleepingMatItem(Properties properties) {
    super(properties.maxStackSize(1).maxDamage(256));
  }

  @Override
  public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity player, Hand handIn) {
    ItemStack itemstack = player.getHeldItem(handIn);
    //    playerIn.setActiveHand(handIn);
    BlockPos pos = player.getPosition();
    if (!worldIn.isDaytime()) {
      trySleep(player, pos, itemstack).ifLeft((p) -> {
        if (p != null) {
          player.sendStatusMessage(p.getMessage(), true);
        }
      });
    }
    return new ActionResult<>(ActionResultType.SUCCESS, itemstack);
  }

  public Either<PlayerEntity.SleepResult, Unit> trySleep(PlayerEntity player, BlockPos at, ItemStack itemstack) {
    Optional<BlockPos> optAt = Optional.of(at);
    PlayerEntity.SleepResult ret = net.minecraftforge.event.ForgeEventFactory.onPlayerSleepInBed(player, optAt);
    if (ret != null) {
      return Either.left(ret);
    }
    if (!player.world.isRemote) {
      if (player.isSleeping() || !player.isAlive()) {
        return Either.left(PlayerEntity.SleepResult.OTHER_PROBLEM);
      }
      if (!player.world.dimension.isSurfaceWorld()) {
        return Either.left(PlayerEntity.SleepResult.NOT_POSSIBLE_HERE);
      }
      if (!net.minecraftforge.event.ForgeEventFactory.fireSleepingTimeCheck(player, optAt)) {
        player.func_226560_a_(at, false, true);
        return Either.left(PlayerEntity.SleepResult.NOT_POSSIBLE_NOW);
      }
    }
    player.startSleeping(at);
    player.getPersistentData().putBoolean("cyclic_sleeping", true);
    //    tthis.sleepTimer = 0;
    ObfuscationReflectionHelper.setPrivateValue(PlayerEntity.class, player, 0, "field_71076_b");
    if (player.world instanceof ServerWorld) {
      ((ServerWorld) player.world).updateAllPlayersSleepingFlag();
    }
    UtilItemStack.damageItem(itemstack);
    return Either.right(Unit.INSTANCE);
  }
}
