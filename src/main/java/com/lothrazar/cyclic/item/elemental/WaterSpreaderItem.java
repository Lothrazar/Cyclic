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

import com.lothrazar.cyclic.item.ItemBaseCyclic;
import com.lothrazar.library.util.ShapeUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.network.PacketDistributor;

public class WaterSpreaderItem extends ItemBaseCyclic {

  static final int COOLDOWN = 28;
  public static ModConfigSpec.IntValue RADIUS;

  public WaterSpreaderItem(Properties properties) {
    super(properties);
  }

  @Override
  public InteractionResult useOn(UseOnContext context) {
    BlockPos pos = context.getClickedPos();
    Direction side = context.getClickedFace();
    if (side != null) {
      pos = pos.relative(side);
    }
    if (context.getLevel().isClientSide) {
      PacketDistributor.sendToServer(new PacketWaterFlow(pos, context.getHand()));
      return InteractionResult.SUCCESS;
    }
    return super.useOn(context);
  }

  public static boolean spreadWaterFromCenter(Level world, BlockPos posCenter) {
    int count = 0;
    for (BlockPos pos : ShapeUtil.squareHorizontalFull(posCenter, RADIUS.get())) {
      if (world.isWaterAt(pos) && world.getBlockState(pos).getBlock() == Blocks.WATER) {
        world.setBlockAndUpdate(pos, Blocks.WATER.defaultBlockState());
        count++;
      } else {
        BlockState state = world.getBlockState(pos);
        if (state.hasProperty(BlockStateProperties.WATERLOGGED)
            && !state.getValue(BlockStateProperties.WATERLOGGED).booleanValue()
            && isWaterNextdoor(world, pos)) {
          state = state.setValue(BlockStateProperties.WATERLOGGED, true);
          world.setBlockAndUpdate(pos, state);
          count++;
        }
      }
    }
    return count > 0;
  }

  private static boolean isWaterNextdoor(Level world, BlockPos pos) {
    return world.isWaterAt(pos.north()) || world.isWaterAt(pos.south()) ||
        world.isWaterAt(pos.east()) || world.isWaterAt(pos.west());
  }
}
