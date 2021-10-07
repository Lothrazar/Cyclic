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
import com.lothrazar.cyclic.util.UtilItemStack;
import com.lothrazar.cyclic.util.UtilSound;
import com.lothrazar.cyclic.util.UtilWorld;
import java.util.List;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.InteractionResult;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

import net.minecraft.world.item.Item.Properties;

public class IceWand extends ItemBase {

  public IceWand(Properties properties) {
    super(properties);
  }

  private static final int RADIUS = 2;

  @Override
  public InteractionResult useOn(UseOnContext context) {
    Player player = context.getPlayer();
    BlockPos pos = context.getClickedPos();
    Direction side = context.getClickedFace();
    if (side != null) {
      pos = pos.relative(side);
    }
    if (spreadWaterFromCenter(context.getLevel(), pos.relative(side))) {
      //but the real sound
      UtilSound.playSound(player, Blocks.PACKED_ICE.defaultBlockState().getSoundType().getBreakSound());
      UtilItemStack.damageItem(player, context.getItemInHand());
    }
    return super.useOn(context);
  }

  private boolean spreadWaterFromCenter(Level world, BlockPos posCenter) {
    int count = 0;
    List<BlockPos> water = UtilWorld.findBlocks(world, posCenter, Blocks.WATER, RADIUS);
    for (BlockPos pos : water) {
      FluidState fluidState = world.getBlockState(pos).getFluidState();

      if (fluidState != null &&
//          fluidState.getFluidState() != null &&
          fluidState.getAmount() >= 8) { // .getFluidState()
        world.setBlock(pos, Blocks.ICE.defaultBlockState(), 3);
      }
      count++;
    }
    return count > 0;
  }
}
