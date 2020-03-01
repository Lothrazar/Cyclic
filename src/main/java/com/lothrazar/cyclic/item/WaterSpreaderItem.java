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
import com.lothrazar.cyclic.util.UtilEntity;
import com.lothrazar.cyclic.util.UtilWorld;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
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
    ItemStack stack = context.getItem();
    BlockPos pos = context.getPos();
    Direction side = context.getFace();
    if (side != null) {
      pos = pos.offset(side);
    }
    spreadWaterFromCenter(context.getWorld(), player, pos);
    return super.onItemUse(context);
  }
  //
  //  @Override
  //  public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
  //    ItemStack stack = player.getHeldItem(hand);
  //    if (pos == null) {
  //      return super.onItemUse(player, world, pos, hand, side, hitX, hitY, hitZ);
  //    }
  //    if (side != null) {
  //      pos = pos.offset(side);
  //    }
  //    if (spreadWaterFromCenter(world, player, pos))
  //      super.onUse(stack, player, world, hand);
  //    return super.onItemUse(player, world, pos, hand, side, hitX, hitY, hitZ);
  //  }
  //
  //  @Override
  //  public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
  //    ItemStack stack = player.getHeldItem(hand);
  //    if (spreadWaterFromCenter(world, player, player.getPosition()))
  //      super.onUse(stack, player, world, hand);
  //    return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, stack);
  //  }

  private boolean spreadWaterFromCenter(World world, PlayerEntity player, BlockPos posCenter) {
    int count = 0;
    for (BlockPos pos : UtilWorld.findBlocks(world, posCenter, Blocks.WATER, RADIUS)) {
      if (world.hasWater(pos)) {
        world.setBlockState(pos, Blocks.WATER.getDefaultState());
      }
      //      world.setBlockState(pos, world.getBlockState(pos)
      //          .with(WaterBlock.LEVEL, 0)); // , state.withProperty(LEVEL, 0)
      //instead of just setBlockState, get the correct state for max level and for this fluid material, then schedule a tick update.
      //      //this way, it sends correct block update and avoids 'stuck' water that doesnt flow
      //      BlockDynamicLiquid blockdynamicliquid = BlockLiquid.getFlowingBlock(Material.WATER);
      //      IBlockState state = blockdynamicliquid.getDefaultState();
      //      world.setBlockState(pos, blockdynamicliquid.getDefaultState().withProperty(BlockLiquid.LEVEL, state.getValue(BlockLiquid.LEVEL)), 2);
      //      world.scheduleUpdate(pos, blockdynamicliquid, blockdynamicliquid.tickRate(world));
      //      UtilParticle.spawnParticle(world, EnumParticleTypes.WATER_SPLASH, pos);
      //      UtilParticle.spawnParticle(world, EnumParticleTypes.WATER_SPLASH, pos.up());
      count++;
    }
    boolean success = count > 0;
    if (success) {//particles are on each location, sound is just once
      UtilEntity.setCooldownItem(player, this, COOLDOWN);
      //      UtilSound.playSound(player, SoundEvents.ENTITY_PLAYER_SPLASH);
    }
    return success;
  }
}
