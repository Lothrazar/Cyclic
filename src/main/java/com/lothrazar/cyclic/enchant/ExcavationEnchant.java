package com.lothrazar.cyclic.enchant;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.data.DataTags;
import com.lothrazar.cyclic.registry.EnchantRegistry;
import com.lothrazar.library.util.EnchantUtil;
import com.lothrazar.library.util.ItemStackUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.common.ModConfigSpec.BooleanValue;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.event.EventHooks;
import net.neoforged.neoforge.event.level.BlockEvent.BreakEvent;

public class ExcavationEnchant {

  public static final String ID = "excavate";
  public static BooleanValue CFG;
  public static boolean effectiveToolRequired = true;

  public static boolean isEnabled() {
    return CFG == null || CFG.get();
  }

  private int getHarvestMax(int level) {
    return 26 + 8 * level;
  }

  @SubscribeEvent(priority = EventPriority.LOWEST)
  public void onBreakEvent(BreakEvent event) {
    if (!isEnabled()) { return; }
    LevelAccessor world = event.getLevel();
    Player player = event.getPlayer();
    if (player.swingingArm == null || world.isClientSide()) { return; }
    BlockPos pos = event.getPos();
    BlockState eventState = event.getState();
    Block block = eventState.getBlock();
    ItemStack stackHarvestingWith = player.getItemInHand(player.swingingArm);
    int level = EnchantUtil.getCurrentLevelTool(EnchantUtil.holder(EnchantRegistry.EXCAVATE, player), stackHarvestingWith);
    if (level <= 0) { return; }
    if (effectiveToolRequired && !player.hasCorrectToolForDrops(eventState)) {
      return;
    }
    if (eventState.is(DataTags.EXCAVATE_IGNORED)) {
      ModCyclic.LOGGER.debug("excavate trigger cancelled; see blocktag " + DataTags.EXCAVATE_IGNORED);
      return;
    }
    if (EventHooks.doPlayerHarvestCheck(player, eventState, world, pos)) {
      int harvested = this.harvestSurrounding((Level) world, player, pos, block, 1, level, player.swingingArm);
      if (harvested > 0) {
        ItemStackUtil.damageItem(player, stackHarvestingWith);
      }
    }
  }

  private int harvestSurrounding(final Level world, final Player player, final BlockPos posIn, final Block block, int totalBroken, final int level, InteractionHand swingingHand) {
    if (totalBroken >= this.getHarvestMax(level) || player.getItemInHand(player.swingingArm).isEmpty()) {
      return totalBroken;
    }
    Set<BlockPos> wasHarvested = new HashSet<>();
    Set<BlockPos> theFuture = this.getMatchingSurrounding(world, posIn, block);
    for (BlockPos targetPos : theFuture) {
      BlockState targetState = world.getBlockState(targetPos);
      if (world.isEmptyBlock(targetPos)
          || !player.mayBuild()
          || !player.hasCorrectToolForDrops(targetState)
          || totalBroken >= this.getHarvestMax(level)
          || player.getItemInHand(player.swingingArm).isEmpty()) {
        continue;
      }
      if (world instanceof ServerLevel) {
        Block.dropResources(targetState, world, targetPos, world.getBlockEntity(targetPos), player, player.getItemInHand(player.swingingArm));
      }

      int exp = targetState.getExpDrop(world, targetPos, world.getBlockEntity(targetPos), player,  player.getItemInHand(player.swingingArm));
      if (exp > 0 && world instanceof ServerLevel sl) {
        block.popExperience(sl, targetPos, exp);
      }
      world.destroyBlock(targetPos, false);
      wasHarvested.add(targetPos);
      totalBroken++;
    }
    if (wasHarvested.isEmpty()) { return totalBroken; }
    for (BlockPos targetPos : theFuture) {
      if (totalBroken >= this.getHarvestMax(level) || player.getItemInHand(player.swingingArm).isEmpty()) { break; }
      totalBroken += this.harvestSurrounding(world, player, targetPos, block, totalBroken, level, swingingHand);
    }
    return totalBroken;
  }

  private static final Direction[] VALUES = Direction.values();

  private Set<BlockPos> getMatchingSurrounding(Level world, BlockPos start, Block blockIn) {
    Set<BlockPos> list = new HashSet<>();
    List<Direction> targetFaces = Arrays.asList(VALUES);
    try {
      // cannot replicate this error at all at max level (5 = V)
      // java.lang.StackOverflowError: Exception in server tick loop
      Collections.shuffle(targetFaces);
    }
    catch (Exception e) {
      // java.util shit the bed not my problem
    }
    for (Direction fac : targetFaces) {
      Block target = world.getBlockState(start.relative(fac)).getBlock();
      if (target == blockIn) {
        list.add(start.relative(fac));
      }
    }
    return list;
  }
}
