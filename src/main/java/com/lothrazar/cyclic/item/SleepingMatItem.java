package com.lothrazar.cyclic.item;

import java.util.Optional;
import com.lothrazar.library.util.ItemStackUtil;
import com.mojang.datafixers.util.Either;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Unit;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class SleepingMatItem extends ItemBaseCyclic {

  public static final String CYCLIC_SLEEPING = "cyclic_sleeping";

  public SleepingMatItem(Properties properties) {
    super(properties.stacksTo(1).durability(256));
  }

  @Override
  public InteractionResult use(Level worldIn, Player player, InteractionHand handIn) {
    ItemStack itemstack = player.getItemInHand(handIn);
    BlockPos pos = player.blockPosition();
    if (!worldIn.isDay()) {
      trySleep(player, pos, itemstack).ifLeft((bsp) -> {
        if (bsp != null && bsp.getMessage() != null) {
          player.displayClientMessage(bsp.getMessage(), true);
        }
      });
    }
    return InteractionResult.SUCCESS.heldItemTransformedTo(itemstack);
  }

  public Either<Player.BedSleepingProblem, Unit> trySleep(Player player, BlockPos at, ItemStack itemstack) {

    Level world = player.level();
    if (!world.isClientSide()) {
      if (player.isSleeping() || !player.isAlive()) {
        return Either.left(Player.BedSleepingProblem.OTHER_PROBLEM);
      }
      boolean isoverworld = world.dimension() == Level.OVERWORLD;
      if (!isoverworld) {
        return Either.left(Player.BedSleepingProblem.NOT_POSSIBLE_HERE);
      }

      player.startSleeping(at);
      player.getPersistentData().putBoolean(SleepingMatItem.CYCLIC_SLEEPING, true);
      // player.getSleepTimer() = 0; //    ObfuscationReflectionHelper.setPrivateValue(Player.class, player, 0, "sleepCounter");
      if (player.level() instanceof ServerLevel sl) {
        sl.updateSleepingPlayerList();
      }
      ItemStackUtil.damageItem(player, itemstack);
    }
    return Either.right(Unit.INSTANCE);
  }
}
