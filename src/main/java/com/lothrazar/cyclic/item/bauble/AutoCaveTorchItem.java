/*******************************************************************************
 * The MIT License (MIT)
 *
 * Copyright (C) 2014-2018 Sam Bassett (aka Lothrazar)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 ******************************************************************************/
package com.lothrazar.cyclic.item.bauble;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import com.lothrazar.cyclic.util.UtilItemStack;
import com.lothrazar.cyclic.util.UtilPlaceBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;

public class AutoCaveTorchItem extends ItemBaseToggle {

  private static final int TORCH_LIGHT_LEVEL = 14;
  private static final int TICK_DELAY = 2;
  /**
   * Maximum number of blocks to move the current players' position down by to find a surface block.
   */
  private static final int BLOCKS_TO_MOVE_FEET_DOWN = 2;
  public static IntValue LIGHT_LIMIT;
  public static IntValue LIGHT_TARGET;
  public static BooleanValue PREFER_WALLS;
  public static BooleanValue PREFER_LEFT_WALL;
  private final AtomicInteger timer = new AtomicInteger();
  private final Lock lock = new ReentrantLock();

  public AutoCaveTorchItem(Properties properties) {
    super(properties);
  }

  @Override
  public void inventoryTick(ItemStack stack, Level world, Entity entityIn, int itemSlot, boolean isSelected) {
    if (world.isClientSide) {
      return;
    }
    if (!this.isOn(stack)) {
      return;
    }
    if (!(entityIn instanceof Player)) {
      return;
    }
    Player player = (Player) entityIn;
    if (player.isSpectator()) {
      return;
    }
    if (stack.getDamageValue() >= stack.getMaxDamage()) {
      stack.setDamageValue(stack.getMaxDamage());
      return;
    }
    // A lock is necessary here as it's possible for multiple torches to be placed at the same time.
    // If the lock is currently held, skip for now - we'll attempt placing a torch in the next tick.
    if (timer.updateAndGet(n -> Math.max(n - 1, 0)) == 0 && lock.tryLock()) {
      try {
        placeTorchIfNecessary(stack, world, player);
      }
      finally {
        lock.unlock();
      }
    }
    tryRepairWith(stack, player, Blocks.TORCH.asItem());
  }

  private void placeTorchIfNecessary(ItemStack stack, Level world, Player player) {
    BlockPos playerPos = player.blockPosition();
    if (world.getBlockState(playerPos).canOcclude()) {
      return;
    }
    // Find the surface below the player's feet.
    for (int i = 0; i < BLOCKS_TO_MOVE_FEET_DOWN; i++) {
      BlockPos next = playerPos.below();
      if (world.getBlockState(next).canOcclude()) {
        break;
      }
      else {
        playerPos = next;
      }
    }
    if (!world.getBlockState(playerPos.below()).canOcclude()) {
      return;
    }
    final int lightLimit = getLightLimit();
    final int playerPosLight = world.getMaxLocalRawBrightness(playerPos);
    world.getBrightness(LightLayer.BLOCK, playerPos);
    if (playerPosLight > lightLimit) {
      return;
    }
    // Increase the light target the darker the current light level is, compared to the light limit.
    // For example, if the player was moving very quickly away from a light source, it's possible that
    // `playerPosLight == lightLimit-1`, i.e. they moved over the block which would have `playerPosLight == lightLimit`.
    // To light up the "skipped" block to `lightTarget`, we need to light this block up to `lightTarget+1`.
    // Note that lightTarget, as a result, can exceed TORCH_LIGHT_LEVEL. This is dealt with below (by falling back to
    // maximising the current position's light value if lightTarget is unreachable).
    final int lightTarget = getLightTarget() + lightLimit - playerPosLight;
    // Do an initial BFS to try hitting lightTarget.
    final int targetDistance = TORCH_LIGHT_LEVEL - lightTarget;
    // The worst case is that we need to BFS to try hitting lightLimit + 1, so never push those onto the BFS.
    final int fallbackTargetDistance = TORCH_LIGHT_LEVEL - (lightLimit + 1);
    assert targetDistance <= fallbackTargetDistance;
    Queue<BlockPos> queue = new ArrayDeque<>();
    HashMap<BlockPos, Integer> distances = new HashMap<>();
    queue.add(playerPos);
    distances.put(playerPos, 0);
    final int playerElevation = playerPos.getY();
    ArrayList<TorchPos> validTorchPositions = bfs(world, queue, distances, targetDistance, fallbackTargetDistance, playerElevation);
    final boolean preferWalls = isPreferWalls();
    validTorchPositions.sort(
        // If preferWalls is enabled, always prefer torches that are not on the ground and are at feet level or above.
        // This is to prevent torches from being placed on the edge of platforms / cliffs.
        Comparator.<TorchPos, Boolean> comparing(torchPos -> preferWalls && torchPos.isNotOnGround() && torchPos.isNotBelowFeet())
            // Prefer torch positions which are currently darker.
            // This needs to be before the below. If the two were swapped, torches would be placed CLOSER to existing
            // light sources when digging a tunnel!
            // Additionally, prefer torches which are closer in elevation to the current position.
            // This is a naive heuristic to prevent torches from being placed too high from the ground, therefore
            // lighting up less of the ground.
            // The two heuristics are combined, with the elevation being weighted double (or quadruple if it's beneath
            // the player). Separating the two heuristics either results in torches being redundantly placed too high
            // up, or torches being placed in brighter areas just because they have a lower elevation. Using a weighted
            // mix fixes both of these problems.
            .thenComparing(torchPos -> -(torchPos.currentLightLevel + (torchPos.isNotBelowFeet() ? 2 : 4) * Math.abs(torchPos.relativeHeight)))
            // Prefer torches with a LOWER player light level as a heuristic to light up a bigger area.
            // In other words, torches which are further away from the player.
            .thenComparing(torchPos -> -torchPos.playerLightLevel)
            // Prefer torches which are on walls instead of on the ground.
            .thenComparing(TorchPos::isNotOnGround)
            // Reverse the comparator to get validTorchPositions from best to worst, instead of worst to best.
            .reversed());
    final Direction facing = player.getDirection();
    for (TorchPos torchPos : validTorchPositions) {
      if (UtilPlaceBlocks.placeTorchSafely(world, torchPos.pos, torchPos.getPlacementDirection(facing))) {
        UtilItemStack.damageItem(player, stack);
        timer.set(TICK_DELAY);
        return;
      }
    }
    // Couldn't find a valid position. Fall back on ANY torch which could increase the player light level above
    // lightLimit + 1.
    validTorchPositions = bfs(world, queue, distances, fallbackTargetDistance, fallbackTargetDistance, playerElevation);
    validTorchPositions.sort(
        // Prefer torches with the HIGHEST player light level, as we should compensate for not being able light up this
        // block to the expected light value.
        Comparator.<TorchPos, Integer> comparing(torchPos -> torchPos.playerLightLevel)
            // Same as above.
            .thenComparing(torchPos -> preferWalls && torchPos.isNotOnGround() && torchPos.isNotBelowFeet())
            .thenComparing(torchPos -> -(torchPos.currentLightLevel + (torchPos.isNotBelowFeet() ? 2 : 4) * Math.abs(torchPos.relativeHeight)))
            .thenComparing(TorchPos::isNotOnGround)
            .reversed());
    for (TorchPos torchPos : validTorchPositions) {
      if (UtilPlaceBlocks.placeTorchSafely(world, torchPos.pos, torchPos.getPlacementDirection(facing))) {
        UtilItemStack.damageItem(player, stack);
        timer.set(TICK_DELAY);
        return;
      }
    }
    // Didn't find a match. We'll probably get the same result next time, so delay the next placement attempt.
    timer.set(TICK_DELAY);
  }

  /**
   * @param maxPoppedDist
   *          The maximum distance that a pos can be popped off of the BFS.
   * @param maxPushedDist
   *          The maximum distance that a pos can be pushed onto the BFS.
   * @param playerElevation
   *          The y value of the player's feet.
   * @return Newly found valid torch positions.
   */
  private ArrayList<TorchPos> bfs(Level world, Queue<BlockPos> queue, HashMap<BlockPos, Integer> distances, int maxPoppedDist, int maxPushedDist, int playerElevation) {
    ArrayList<TorchPos> validTorchPositions = new ArrayList<>();
    while (!queue.isEmpty()) {
      final BlockPos poppedPos = queue.peek();
      final int poppedDistance = distances.get(poppedPos);
      if (poppedDistance > maxPoppedDist) {
        break;
      }
      queue.remove();
      // Enumerate all successors while keeping track whether a torch can be placed here.
      boolean isValidTorch = false;
      boolean wouldUpdateFloatingFallingBlock = false;
      EnumSet<Direction> solidDirections = EnumSet.noneOf(Direction.class);
      for (Direction direction : Direction.values()) {
        final BlockPos nextPos = poppedPos.relative(direction);
        final BlockState state = world.getBlockState(nextPos);
        if (state.canOcclude()) {
          solidDirections.add(direction);
          if (direction != Direction.UP) {
            isValidTorch = true;
          }
        }
        else if (poppedDistance < maxPushedDist && !distances.containsKey(nextPos)) {
          distances.put(nextPos, poppedDistance + 1);
          queue.add(nextPos);
        }
        // Check if placing a torch here would cause a floating block to fall.
        // Placing a torch BELOW a floating block is okay, though.
        if (direction != Direction.UP && state.getBlock() instanceof FallingBlock &&
            FallingBlock.isFree(world.getBlockState(nextPos.below()))) {
          wouldUpdateFloatingFallingBlock = true;
        }
      }
      if (isValidTorch && !wouldUpdateFloatingFallingBlock && world.isEmptyBlock(poppedPos)) {
        validTorchPositions.add(new TorchPos(poppedPos, poppedPos.getY() - playerElevation,
            TORCH_LIGHT_LEVEL - poppedDistance, world.getMaxLocalRawBrightness(poppedPos), solidDirections));
      }
    }
    return validTorchPositions;
  }

  /**
   * Class for storing information about torch positions for placeTorchIfNecessary.
   */
  private static class TorchPos {

    final BlockPos pos;
    /**
     * The height of this torch relative to the player's feet. 1 means it is at eye level, -1 means it is at the level of the block the player is standing on.
     */
    final int relativeHeight;
    /**
     * The expected light level at playerPos if this position is used.
     */
    final int playerLightLevel;
    /**
     * The current light level of the block in the world.
     */
    final int currentLightLevel;
    /**
     * Which directions, relative to this position, have solid blocks.
     */
    final EnumSet<Direction> solidDirections;

    public TorchPos(BlockPos pos, int relativeHeight, int playerLightLevel, int currentLightLevel, EnumSet<Direction> solidDirections) {
      assert !solidDirections.isEmpty();
      this.pos = pos;
      this.relativeHeight = relativeHeight;
      this.playerLightLevel = playerLightLevel;
      this.currentLightLevel = currentLightLevel;
      this.solidDirections = solidDirections;
    }

    /**
     * @return Whether the block below this is not solid.
     */
    public boolean isNotOnGround() {
      return !solidDirections.contains(Direction.DOWN);
    }

    /**
     * @return Whether the torch is at feet level or higher.
     */
    public boolean isNotBelowFeet() {
      return relativeHeight >= 0;
    }

    /**
     * @param facing
     *          The current direction the player is facing.
     * @return The direction, relative to this block, of a solid block to place on.
     */
    public Direction getPlacementDirection(Direction facing) {
      // To prevent, prefer placing torches in this order:
      // - Ground. If isPreferWalls() is false, this may result in placing a torch on the top of a block that the player
      //   will mine soon (imagine digging a one-block wide tunnel, first mining a few blocks at eye level, then at feet
      //   level), but this is relatively rare and will be replaced once the user mines that block.
      if (solidDirections.contains(Direction.DOWN)) {
        return Direction.DOWN;
      }
      // - Right of player / left of player, depending on isPreferLeftWall().
      final Direction preferredDirection = isPreferLeftWall() ? facing.getCounterClockWise() : facing.getClockWise();
      if (solidDirections.contains(preferredDirection)) {
        return preferredDirection;
      }
      // - The other direction.
      final Direction otherDirection = preferredDirection.getOpposite();
      if (solidDirections.contains(otherDirection)) {
        return otherDirection;
      }
      // - Behind the player - we'd rather do this than place it in the direction the player is facing.
      final Direction behindPlayer = facing.getOpposite();
      if (solidDirections.contains(behindPlayer)) {
        return behindPlayer;
      }
      // - The direction the player is facing.
      assert solidDirections.contains(facing);
      return facing;
    }
  }

  /**
   * @return The light level at which to start placing down a torch.
   */
  private static int getLightLimit() {
    return Math.min(LIGHT_LIMIT.get(), TORCH_LIGHT_LEVEL - 1);
  }

  /**
   * @return The light level of the current block after placing down a torch. The higher this is, the closer torches will be placed to you. In general, you can walk at least lightTarget - lightLimit
   *         blocks before needing to place down another torch.
   */
  private static int getLightTarget() {
    return Math.min(Math.max(getLightLimit() + 1, LIGHT_TARGET.get()), TORCH_LIGHT_LEVEL);
  }

  /**
   * @return Whether to prioritise placing torches on walls (assuming it is possible to reach the target light level with a wall torch).
   */
  private static boolean isPreferWalls() {
    return PREFER_WALLS.get();
  }

  /**
   * @return Whether to prioritise placing torches on the left wall of a one-block wide tunnel instead of the right. This is only applicable to one-block wide tunnels where torches could be
   *         equivalently placed on either side - in large caves, torches will always be placed in the best position to light up the area regardless of side.
   */
  private static boolean isPreferLeftWall() {
    return PREFER_LEFT_WALL.get();
  }
}
