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

public class ItemScytheSp extends ItemBase {

  public ItemScytheSp(Properties properties) {
    super(properties);
  }

  private static final int RADIUS = 6;//13x13
  private static final int RADIUS_SNEAKING = 2;//2x2

  public enum ScytheType {
    WEEDS, LEAVES, CROPS;
  }

  private ScytheType harvestType;
  //
  //  @Override
  //  public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
  //    ItemStack stack = player.getHeldItem(hand);
  //    BlockPos offset = pos;
  //    if (side != null) {
  //      offset = pos.offset(side);
  //    }
  //    int radius = (player.isSneaking()) ? RADIUS_SNEAKING : RADIUS;
  //    List<BlockPos> shape = getShape(offset, radius);
  //    switch (harvestType) {
  //      case CROPS:
  //        //here we use UtilHarvester, which is the new v2 one
  //        final NonNullList<ItemStack> drops = NonNullList.create();
  //        for (BlockPos p : shape) {//now it lands drops on top of player
  //          drops.addAll(UtilHarvester.harvestSingle(world, p));
  //        }
  //        for (ItemStack d : drops) {
  //          UtilItemStack.dropItemStackInWorld(world, player.getPosition(), d);
  //        }
  //      break;
  //      case LEAVES:
  //      case WEEDS://NO LONGER DOES SAPLINGS
  //        if (world.isRemote) {
  //          ModCyclic.network.sendToServer(new PacketScythe(pos, this.harvestType, radius));
  //        }
  //      break;
  //    }
  //    super.onUse(stack, player, world, hand);
  //    return super.onItemUse(player, world, offset, hand, side, hitX, hitY, hitZ);
  //  }
  //
  //  public static List<BlockPos> getShape(BlockPos center, int radius) {
  //    List<BlockPos> shape = new ArrayList<BlockPos>();
  //    shape.addAll(UtilShape.squareHorizontalFull(center.down().down(), radius));
  //    shape.addAll(UtilShape.squareHorizontalFull(center.down(), radius));
  //    shape.addAll(UtilShape.squareHorizontalFull(center, radius));
  //    shape.addAll(UtilShape.squareHorizontalFull(center.up(), radius));
  //    shape.addAll(UtilShape.squareHorizontalFull(center.up().up(), radius));
  //    return shape;
  //  }
}
