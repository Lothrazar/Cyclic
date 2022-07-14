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
import com.lothrazar.cyclic.util.ItemStackUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class GlowingSpark extends ItemBaseCyclic {

  public GlowingSpark(Properties properties) {
    super(properties);
  }

  @Override
  public InteractionResult useOn(UseOnContext context) {
    Player player = context.getPlayer();
    InteractionHand hand = context.getHand();
    BlockPos pos = context.getClickedPos().relative(context.getClickedFace());
    ItemStack held = player.getItemInHand(hand);
    //can place
    BlockState blockState = context.getLevel().getBlockState(pos);
    if (blockState.is(Blocks.LIGHT)) {
      context.getLevel().setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
      player.swing(hand);
      held.grow(1);
      return InteractionResult.SUCCESS;
    }
    else if (blockState.isAir()) {
      context.getLevel().setBlock(pos, Blocks.LIGHT.defaultBlockState(), 3);
      ItemStackUtil.shrink(player, held);
      player.swing(hand);
      return InteractionResult.SUCCESS;
    }
    return InteractionResult.PASS;
  }

  @Override
  public InteractionResult interactLivingEntity(ItemStack stack, Player playerIn, LivingEntity target, InteractionHand hand) {
    playerIn.swing(hand);
    target.setGlowingTag(!target.isCurrentlyGlowing());
    return InteractionResult.SUCCESS;
  }
}
