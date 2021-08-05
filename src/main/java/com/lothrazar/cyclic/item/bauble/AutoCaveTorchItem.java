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

import com.lothrazar.cyclic.util.UtilItemStack;
import com.lothrazar.cyclic.util.UtilPlaceBlocks;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FallingBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.LightType;
import net.minecraft.world.World;

public class AutoCaveTorchItem extends ItemBaseToggle {
  private static final int TORCH_LIGHT_LEVEL = 14;
  private static final int TICK_DELAY = 2;
  /**
   * Maximum number of blocks to move the current players' position down by to find a surface block.
   */
  private static final int BLOCKS_TO_MOVE_FEET_DOWN = 2;
  // TODO: Replace the constants below with configurable options.
  private static final int LIGHT_LIMIT = 7;
  private static final int LIGHT_TARGET = 10;
  private static final boolean PREFER_WALLS = true;
  private static final boolean PREFER_LEFT_WALL = false;

  private final AtomicInteger timer = new AtomicInteger();
  private final Lock lock = new ReentrantLock();

  public AutoCaveTorchItem(Properties properties) {
    super(properties);
  }

  @Override
  public void inventoryTick(ItemStack stack, World world, Entity entityIn, int itemSlot, boolean isSelected) {
    if (world.isRemote) {
      return;
    }
    if (!this.isOn(stack)) {
      return;
    }
    if (!(entityIn instanceof PlayerEntity)) {
      return;
    }
    PlayerEntity player = (PlayerEntity) entityIn;
    if (player.isSpectator()) {
      return;
    }
    if (stack.getDamage() >= stack.getMaxDamage()) {
      stack.setDamage(stack.getMaxDamage());
      return;
    }
    // A lock is necessary here as it's possible for multiple torches to be placed at the same time.
    // If the lock is currently held, skip for now - we'll attempt placing a torch in the next tick.
    if (timer.updateAndGet(n -> Math.max(n-1, 0)) == 0 && lock.tryLock()) {
      try {
        placeTorchIfNecessary(stack, world, player);
      } finally {
        lock.unlock();
      }
    }
    tryRepairWith(stack, player, Blocks.TORCH.asItem());
  }

  private void placeTorchIfNecessary(ItemStack stack, World world, PlayerEntity player) {
    BlockPos playerPos = player.getPosition();

    if (world.getBlockState(playerPos).isSolid()) {
      return;
    }

    // Find the surface below the player's feet.
    for (int i = 0; i < BLOCKS_TO_MOVE_FEET_DOWN; i++) {
      BlockPos next = playerPos.down();
      if (world.getBlockState(next).isSolid()) {
        break;
      } else {
        playerPos = next;
      }
    }
    if (!world.getBlockState(playerPos.down()).isSolid()) {
      return;
    }

    final int lightLimit = getLightLimit();
    final int playerPosLight = world.getLight(playerPos);
    world.getLightFor(LightType.BLOCK, playerPos);
    if (playerPosLight > lightLimit) {
      return;
    }

    // We will be doing some slightly expensive calculations below (a BFS over N nodes, sorting and trying to place N
    // torches, where N <= 3303).
    // We should be able to place down a torch here - but if we can't, we should still wait TICK_DELAY ticks before
    // trying to place the next torch anyway as it is likely that subsequent attempts will also fail.
    timer.set(TICK_DELAY);

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
        Comparator.<TorchPos, Boolean>comparing(torchPos -> preferWalls && torchPos.isNotOnGround() && torchPos.relativeHeight >= 0)
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
            .thenComparing(torchPos -> -(torchPos.currentLightLevel + (torchPos.relativeHeight >= 0 ? 2 : 4)*Math.abs(torchPos.relativeHeight)))
            // Prefer torches with a LOWER player light level as a heuristic to light up a bigger area.
            // In other words, torches which are further away from the player.
            .thenComparing(torchPos -> -torchPos.playerLightLevel)
            // Prefer torches which are on walls instead of on the ground.
            .thenComparing(TorchPos::isNotOnGround)
            // Reverse the comparator to get validTorchPositions from best to worst, instead of worst to best.
            .reversed()
    );

    final Direction facing = player.getHorizontalFacing();
    for (TorchPos torchPos : validTorchPositions) {
      if (UtilPlaceBlocks.placeTorchSafely(world, torchPos.pos, torchPos.getPlacementDirection(facing))) {
        UtilItemStack.damageItem(player, stack);
        return;
      }
    }

    // Couldn't find a valid position. Fall back on ANY torch which could increase the player light level above
    // lightLimit + 1.
    validTorchPositions = bfs(world, queue, distances, fallbackTargetDistance, fallbackTargetDistance, playerElevation);
    validTorchPositions.sort(
        // Prefer torches with the HIGHEST player light level, as we should compensate for not being able light up this
        // block to the expected light value.
        Comparator.<TorchPos, Integer>comparing(torchPos -> torchPos.playerLightLevel)
            // Same as above.
            .thenComparing(torchPos -> preferWalls && torchPos.isNotOnGround() && torchPos.relativeHeight >= 0)
            .thenComparing(torchPos -> -(torchPos.currentLightLevel + (torchPos.relativeHeight >= 0 ? 2 : 4)*Math.abs(torchPos.relativeHeight)))
            .thenComparing(TorchPos::isNotOnGround)
            .reversed()
    );

    for (TorchPos torchPos : validTorchPositions) {
      if (UtilPlaceBlocks.placeTorchSafely(world, torchPos.pos, torchPos.getPlacementDirection(facing))) {
        UtilItemStack.damageItem(player, stack);
        return;
      }
    }
  }

  /**
   * @param maxPoppedDist The maximum distance that a pos can be popped off of the BFS.
   * @param maxPushedDist The maximum distance that a pos can be pushed onto the BFS.
   * @param playerElevation The y value of the player's feet.
   * @return Newly found valid torch positions.
   */
  private ArrayList<TorchPos> bfs(World world, Queue<BlockPos> queue, HashMap<BlockPos, Integer> distances, int maxPoppedDist, int maxPushedDist, int playerElevation) {
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
        final BlockPos nextPos = poppedPos.offset(direction);
        final BlockState state = world.getBlockState(nextPos);
        if (state.isSolid()) {
          solidDirections.add(direction);
          if (direction != Direction.UP) {
            isValidTorch = true;
          }
        } else if (poppedDistance < maxPushedDist && !distances.containsKey(nextPos)) {
          distances.put(nextPos, poppedDistance + 1);
          queue.add(nextPos);
        }

        // Check if placing a torch here would cause a floating block to fall.
        // Placing a torch BELOW a floating block is okay, though.
        if (direction != Direction.UP && state.getBlock() instanceof FallingBlock &&
            FallingBlock.canFallThrough(world.getBlockState(nextPos.down()))) {
          wouldUpdateFloatingFallingBlock = true;
        }
      }

      if (isValidTorch && !wouldUpdateFloatingFallingBlock && world.isAirBlock(poppedPos)) {
        validTorchPositions.add(new TorchPos(poppedPos, poppedPos.getY() - playerElevation,
            TORCH_LIGHT_LEVEL - poppedDistance, world.getLight(poppedPos), solidDirections));
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
     * The height of this torch relative to the player's feet. 1 means it is at eye level, -1 means it is at the level
     * of the block the player is standing on.
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
     * @param facing The current direction the player is facing.
     * @return The direction, relative to this block, of a solid block to place on.
     */
    public Direction getPlacementDirection(Direction facing) {
      // To prevent, prefer placing torches in this order:
      // - Ground. If isPreferWalls() is false, this may result in placing a torch on the top of a block that the player
      //   will mine soon (imagine digging a one-block wide tunnel, first mining a few blocks at eye level, then at feet
      //   level), but this is relatively rare and will be replaced once the user mines that block.
      if (solidDirections.contains(Direction.DOWN)) return Direction.DOWN;

      // - Right of player / left of player, depending on isPreferLeftWall().
      final Direction preferredDirection = isPreferLeftWall() ? facing.rotateYCCW() : facing.rotateY();
      if (solidDirections.contains(preferredDirection)) return preferredDirection;

      // - The other direction.
      final Direction otherDirection = preferredDirection.getOpposite();
      if (solidDirections.contains(otherDirection)) return otherDirection;

      // - Behind the player - we'd rather do this than place it in the direction the player is facing.
      final Direction behindPlayer = facing.getOpposite();
      if (solidDirections.contains(behindPlayer)) return behindPlayer;

      // - The direction the player is facing.
      assert solidDirections.contains(facing);
      return facing;
    }
  }

  /**
   * @return The light level at which to start placing down a torch.
   */
  private static int getLightLimit() {
    return Math.min(LIGHT_LIMIT, TORCH_LIGHT_LEVEL - 1);
  }

  /**
   * @return The light level of the current block after placing down a torch. The higher this is, the closer torches
   * will be placed to you.
   * In general, you can walk lightTarget - lightLimit blocks before needing to place down another torch.
   */
  private static int getLightTarget() {
    return Math.min(Math.max(getLightLimit() + 1, LIGHT_TARGET), TORCH_LIGHT_LEVEL);
  }

  /**
   * @return Whether to prioritise placing torches on walls (assuming it is possible to reach the target light level
   * with a wall torch).
   */
  private static boolean isPreferWalls() {
    return PREFER_WALLS;
  }

  /**
   * @return Whether to prioritise placing torches on the left wall of a one-block wide tunnel instead of the right.
   * This is only applicable to one-block wide tunnels where torches could be equivalently placed on either side - in
   * large caves, torches will always be placed in the best position to light up the area regardless of side.
   */
  private static boolean isPreferLeftWall() {
    return PREFER_LEFT_WALL;
  }
}
