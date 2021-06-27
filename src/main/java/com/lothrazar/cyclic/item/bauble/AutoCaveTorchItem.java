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
import com.lothrazar.cyclic.util.UtilShape;
import java.util.List;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class AutoCaveTorchItem extends ItemBaseToggle {

  private static final int TICK_DELAY = 10;
  public static final int LIGHT_LIMIT = 9;
  private static final int MAX_DISTANCE_SQ = (int) Math.pow(16, 2);
  private static final int MAX_LIST_SIZE = 200;
  private int timer = 0;
  private boolean ticking = false;

  public AutoCaveTorchItem(Properties properties) {
    super(properties);
  }

  @Override
  public void inventoryTick(ItemStack stack, World world, Entity entityIn, int itemSlot, boolean isSelected) {
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
    timer--;
    if (timer <= 0 && !ticking) {
      ticking = true;
      BlockPos pos = entityIn.getPosition();
      if (world.getLightValue(pos) <= LIGHT_LIMIT) {
        List<BlockPos> blockHashList = UtilShape.caveInterior(world, pos, player.getHorizontalFacing(), MAX_LIST_SIZE / 2);
        int count = 0;
        for (BlockPos testPos : blockHashList) {
          count++;
          if (count > MAX_LIST_SIZE) {
            break; // break loop.  mimic previous "iterator next and remove" that was causing the ConcurrentModificationExceptions
          }
          if (shouldPlaceTorch(world, player, testPos)) {
            if (UtilPlaceBlocks.placeTorchSafely(world, testPos)) {
              UtilItemStack.damageItem(player, stack);
            }
            timer = TICK_DELAY; //lag compensation -- wait a tick before trying to place next torch
            break;
          }
        }
        ticking = false;
      }
    }
    if (!ticking) {
      tryRepairWith(stack, player, Blocks.TORCH.asItem());
    }
  }

  private boolean shouldPlaceTorch(World world, PlayerEntity player, BlockPos pos) {
    return pos != null &&
        world.getLight(pos) <= LIGHT_LIMIT &&
        world.isAirBlock(pos) &&
        player.getDistanceSq(pos.getX(), pos.getY(), pos.getZ()) < MAX_DISTANCE_SQ;
  }
}
