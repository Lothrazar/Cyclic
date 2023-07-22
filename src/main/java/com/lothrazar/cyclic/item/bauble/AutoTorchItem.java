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

import com.lothrazar.cyclic.util.BlockUtil;
import com.lothrazar.cyclic.util.ItemStackUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;

public class AutoTorchItem extends ItemBaseToggle {

  public static IntValue LIGHT_LEVEL;

  public AutoTorchItem(Properties properties) {
    super(properties);
  }

  @Override
  public void inventoryTick(ItemStack stack, Level world, Entity entityIn, int itemSlot, boolean isSelected) {
    if (!this.isOn(stack)) {
      return;
    }
    if (entityIn instanceof Player == false) {
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
    BlockPos pos = entityIn.blockPosition();
    if (world.getMaxLocalRawBrightness(pos) <= LIGHT_LEVEL.get()
        && world.getBlockState(pos.below()).isFaceSturdy(world, pos, Direction.UP)
        && world.getBlockState(pos.below()).canOcclude()
        && world.isEmptyBlock(pos)) { // dont overwrite liquids
      if (BlockUtil.placeStateSafe(world, player, pos, Blocks.TORCH.defaultBlockState())) {
        ItemStackUtil.damageItem(player, stack);
      }
    }
    else {
      tryRepairWith(stack, player, Blocks.TORCH.asItem());
    }
  }
}
