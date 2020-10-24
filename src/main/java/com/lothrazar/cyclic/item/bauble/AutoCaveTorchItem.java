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

import java.util.Iterator;
import java.util.LinkedHashSet;
import com.lothrazar.cyclic.base.ItemBase;
import com.lothrazar.cyclic.util.UtilItemStack;
import com.lothrazar.cyclic.util.UtilPlaceBlocks;
import com.lothrazar.cyclic.util.UtilShape;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class AutoCaveTorchItem extends ItemBase {

  public AutoCaveTorchItem(Properties properties) {
    super(properties);
  }

  public static final int LIGHT_LIMIT = 9;
  private static final int MAX_DISTANCE_SQ = (int) Math.pow(16, 2);
  private static final int MAX_LIST_SIZE = 200;
  private static int timer = 0;
  private boolean ticking = false;
  private LinkedHashSet<BlockPos> blockHashList = new LinkedHashSet<>();

  @Override
  public void inventoryTick(ItemStack stack, World world, Entity entityIn, int itemSlot, boolean isSelected) {
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
    Iterator<BlockPos> iter;
    if (timer <= 0 && !ticking) {
      ticking = true;
      BlockPos pos = entityIn.getPosition();
      if (world.getLightValue(pos) <= LIGHT_LIMIT) {
        blockHashList.addAll(UtilShape.caveInterior(world, pos, player.getHorizontalFacing(), MAX_LIST_SIZE / 2));
        blockHashList.removeIf(blockPos -> player.getDistanceSq(blockPos.getX(), blockPos.getY(), blockPos.getZ()) > MAX_DISTANCE_SQ);
        if (blockHashList.size() > MAX_LIST_SIZE) { //if we're moving too fast and adding a bunch of torches, trim until under max size
          int i = blockHashList.size();
          iter = blockHashList.iterator();
          while (iter.hasNext()) {
            iter.next();
            iter.remove();
            if (--i <= MAX_LIST_SIZE)
              break;
          }
        }
      }
      iter = blockHashList.iterator();
      while (iter.hasNext()) {
        BlockPos testPos = iter.next();
        if (shouldPlaceTorch(world, testPos)) {
          if (UtilPlaceBlocks.placeTorchSafely(world, testPos))
            UtilItemStack.damageItem(player, stack);
          iter.remove();
          timer = 2; //lag compensation -- wait a tick before trying to place next torch
          break;
        }
      }
      ticking = false;
    }
    if (!ticking) {
      tryRepairWith(stack, player, Blocks.TORCH.asItem());
    }
  }

  private boolean shouldPlaceTorch(World world, BlockPos pos) {
    return world.getLight(pos) <= LIGHT_LIMIT && world.isAirBlock(pos);
  }
}
