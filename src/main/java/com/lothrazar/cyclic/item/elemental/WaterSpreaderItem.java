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
package com.lothrazar.cyclic.item.elemental;

import com.lothrazar.cyclic.base.ItemBase;
import com.lothrazar.cyclic.util.UtilEntity;
import com.lothrazar.cyclic.util.UtilShape;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemUseContext;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WaterSpreaderItem extends ItemBase {

  public WaterSpreaderItem(Properties properties) {
    super(properties);
  }

  private static final int COOLDOWN = 28;
  private static final int RADIUS = 3;

  @Override
  public ActionResultType onItemUse(ItemUseContext context) {
    PlayerEntity player = context.getPlayer();
    // 
    BlockPos pos = context.getPos();
    Direction side = context.getFace();
    if (side != null) {
      pos = pos.offset(side);
    }
    spreadWaterFromCenter(context.getWorld(), player, pos);
    player.swingArm(context.getHand());
    return super.onItemUse(context);
  }

  private boolean spreadWaterFromCenter(World world, PlayerEntity player, BlockPos posCenter) {
    int count = 0;
    for (BlockPos pos : UtilShape.squareHorizontalFull(posCenter, RADIUS)) {
      if (world.hasWater(pos) && world.getBlockState(pos).getBlock() == Blocks.WATER) {
        world.setBlockState(pos, Blocks.WATER.getDefaultState());
        count++;
      }
      else {
        BlockState state = world.getBlockState(pos);
        if (state.hasProperty(BlockStateProperties.WATERLOGGED)
            && !state.get(BlockStateProperties.WATERLOGGED).booleanValue()
            && this.isWaterNextdoor(world, pos)) {
          //  flow it into the loggable
          state = state.with(BlockStateProperties.WATERLOGGED, true);
          world.setBlockState(pos, state);
          count++;
        }
      }
    }
    boolean success = count > 0;
    if (success) {//particles are on each location, sound is just once
      UtilEntity.setCooldownItem(player, this, COOLDOWN);
      //      UtilSound.playSound(player, SoundEvents.ENTITY_PLAYER_SPLASH);
    }
    return success;
  }

  private boolean isWaterNextdoor(World world, BlockPos pos) {
    return world.hasWater(pos.north()) || world.hasWater(pos.south()) ||
        world.hasWater(pos.east()) || world.hasWater(pos.west());
  }
}
