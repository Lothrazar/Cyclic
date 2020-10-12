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

import com.lothrazar.cyclic.base.ItemBase;
import com.lothrazar.cyclic.util.UtilPlaceBlocks;
import com.lothrazar.cyclic.util.UtilShape;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;

public class AutoCaveTorchItem extends ItemBase {

  public AutoCaveTorchItem(Properties properties) {
    super(properties);
  }

  public static final int lightLimit = 9;
  private static final int MAX_DISTANCE_SQ = (int) Math.pow(30, 2);
  private static final int MIN_SPACING_SQ = (int) Math.pow(9, 2);
  private static int timer = 0;

  @Override
  public void inventoryTick(ItemStack stack, World world, Entity entityIn, int itemSlot, boolean isSelected) {
    if (entityIn instanceof PlayerEntity == false) {
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
    if (++timer >= 40) {
      BlockPos pos = entityIn.getPosition();
      List<BlockPos> shape = UtilShape.caveInterior(world, pos, player.getHorizontalFacing());
      BlockPos lastPos = null;
      for (BlockPos blockPos : shape) {
        if (player.getDistanceSq(blockPos.getX(), blockPos.getY(), blockPos.getZ()) < MAX_DISTANCE_SQ
                && world.getLight(blockPos) <= lightLimit
                && world.isAirBlock(blockPos)
                && (lastPos == null || blockPos.distanceSq(lastPos.getX(), lastPos.getY(), lastPos.getZ(), true) >= MIN_SPACING_SQ)) {
          UtilPlaceBlocks.placeTorchSafely(world, blockPos);
          lastPos = blockPos;
        }
      }
      timer = 0;
    }
  }
}
