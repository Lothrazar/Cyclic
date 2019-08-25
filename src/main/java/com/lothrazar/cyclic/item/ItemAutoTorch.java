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
package com.lothrazar.cyclic.item;

import com.lothrazar.cyclic.base.ItemBase;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemAutoTorch extends ItemBase {

  public ItemAutoTorch(Properties properties) {
    super(properties);
  }

  @Override
  public void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {}

  //  private static final int durability = 256;
  public static final int lightLimit = 8;
  //  @Override
  //  public void onTick(ItemStack stack, PlayerEntity player) {
  //    if (!this.canTick(stack)) {
  //      return;
  //    }
  //    World world = player.world;
  //    BlockPos pos = player.getPosition();
  //    if (world.getLight(pos, true) < lightLimit
  //        && player.isSpectator() == false
  //        && world.isSideSolid(pos.down(), Direction.UP)
  //        && world.isAirBlock(pos)) { // dont overwrite liquids
  //      if (UtilPlaceBlocks.placeStateSafe(world, player, pos, Blocks.TORCH.getDefaultState())) {
  //        super.damageCharm(player, stack);
  //      }
  //    }
  //    else if (stack.isItemDamaged()) {
  //      ItemStack torches = this.findAmmo(player, Item.getItemFromBlock(Blocks.TORCH));
  //      if (!torches.isEmpty()) {
  //        torches.shrink(1);
  //        UtilItemStack.repairItem(player, stack);
  //      }
  //    }
  //  }
}
