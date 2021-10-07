package com.lothrazar.cyclic.item;

import com.lothrazar.cyclic.base.ItemBase;
import com.lothrazar.cyclic.util.UtilItemStack;
import com.mojang.datafixers.util.Either;
import java.util.Optional;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.util.Unit;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

import net.minecraft.world.item.Item.Properties;

public class SleepingMatItem extends ItemBase {

  public SleepingMatItem(Properties properties) {
    super(properties.stacksTo(1).durability(256));
  }

  @Override
  public InteractionResultHolder<ItemStack> use(Level worldIn, Player player, InteractionHand handIn) {
    ItemStack itemstack = player.getItemInHand(handIn);
    BlockPos pos = player.blockPosition();
    if (!worldIn.isDay()) {
      trySleep(player, pos, itemstack).ifLeft((p) -> {
        if (p != null) {
          player.displayClientMessage(p.getMessage(), true);
        }
      });
    }
    return new InteractionResultHolder<>(InteractionResult.SUCCESS, itemstack);
  }

  public Either<Player.BedSleepingProblem, Unit> trySleep(Player player, BlockPos at, ItemStack itemstack) {
    Optional<BlockPos> optAt = Optional.of(at);
    Player.BedSleepingProblem ret = net.minecraftforge.event.ForgeEventFactory.onPlayerSleepInBed(player, optAt);
    if (ret != null) {
      return Either.left(ret);
    }
    Level world = player.level;
    if (!world.isClientSide) {
      if (player.isSleeping() || !player.isAlive()) {
        return Either.left(Player.BedSleepingProblem.OTHER_PROBLEM);
      }
      boolean isoverworld = world.dimension() == Level.OVERWORLD;
      if (!isoverworld) {
        return Either.left(Player.BedSleepingProblem.NOT_POSSIBLE_HERE);
      }
      if (!net.minecraftforge.event.ForgeEventFactory.fireSleepingTimeCheck(player, optAt)) {
        player.setSleepingPos(at);
        return Either.left(Player.BedSleepingProblem.NOT_POSSIBLE_NOW);
      }
    }
    player.startSleeping(at);
    player.getPersistentData().putBoolean("cyclic_sleeping", true);
    //    tthis.sleepTimer = 0;
    ObfuscationReflectionHelper.setPrivateValue(Player.class, player, 0, "sleepCounter");
    if (player.level instanceof ServerLevel) {
      ((ServerLevel) player.level).updateSleepingPlayerList();
    }
    UtilItemStack.damageItem(player, itemstack);
    return Either.right(Unit.INSTANCE);
  }
}
