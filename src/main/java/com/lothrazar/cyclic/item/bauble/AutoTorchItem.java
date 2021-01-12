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

import com.lothrazar.cyclic.base.IHasClickToggle;
import com.lothrazar.cyclic.base.ItemBase;
import com.lothrazar.cyclic.util.UtilItemStack;
import com.lothrazar.cyclic.util.UtilPlaceBlocks;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class AutoTorchItem extends ItemBase implements IHasClickToggle {

  public AutoTorchItem(Properties properties) {
    super(properties);
  }

  //TODO: config
  public static final int LIGHT_LIMIT = 9;

  @Override
  public void inventoryTick(ItemStack stack, World world, Entity entityIn, int itemSlot, boolean isSelected) {
    if (!this.isOn(stack)) {
      return;
    }
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
    BlockPos pos = entityIn.getPosition();
    if (world.getLight(pos) <= LIGHT_LIMIT
        //            && player.isSpectator() == false
        //            && world.isSideSolid(pos.down(), Direction.UP)
        && world.getBlockState(pos.down()).isSolid()
        && world.isAirBlock(pos)) { // dont overwrite liquids
      if (UtilPlaceBlocks.placeStateSafe(world, player, pos, Blocks.TORCH.getDefaultState())) {
        UtilItemStack.damageItem(player, stack);
      }
    }
    else {
      tryRepairWith(stack, player, Blocks.TORCH.asItem());
    }
  }

  @Override
  public void toggle(PlayerEntity player, ItemStack held) {
    CompoundNBT tag = held.getOrCreateTag();
    tag.putInt(NBT_STATUS, (tag.getInt(NBT_STATUS) + 1) % 2);
    held.setTag(tag);
  }

  @Override
  public boolean isOn(ItemStack held) {
    return held.getOrCreateTag().getInt(NBT_STATUS) == 0;
  }
}
