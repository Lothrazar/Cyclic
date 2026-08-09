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
package com.lothrazar.cyclic.item.animal;

import com.lothrazar.cyclic.item.ItemBaseCyclic;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class StirrupsReverseItem extends ItemBaseCyclic {

  public StirrupsReverseItem(Properties properties) {
    super(properties);
  }

  @Override
  public InteractionResult interactLivingEntity(ItemStack stack, Player playerIn, LivingEntity target, InteractionHand hand) {
    if (playerIn.hasPassenger(target)) {
      playerIn.swing(hand);
      target.removeVehicle();
      return InteractionResult.SUCCESS;
    }
    if (!playerIn.getPassengers().isEmpty()) {
      //already carrying something: dont allow picking up a second entity
      return InteractionResult.PASS;
    }
    playerIn.swing(hand);
    return target.startRiding(playerIn, true) ? InteractionResult.SUCCESS : super.interactLivingEntity(stack, playerIn, target, hand);
  }

  @Override
  public InteractionResultHolder<ItemStack> use(Level level, Player playerIn, InteractionHand hand) {
    //a carried passenger usually rides above/behind the player's own head, so it often can't be
    //re-clicked to drop it; right-clicking with no entity target releases it instead
    if (!playerIn.getPassengers().isEmpty()) {
      playerIn.ejectPassengers();
      playerIn.swing(hand);
      return InteractionResultHolder.success(playerIn.getItemInHand(hand));
    }
    return super.use(level, playerIn, hand);
  }
}
