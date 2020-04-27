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
package com.lothrazar.cyclic.enchant;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import com.lothrazar.cyclic.base.EnchantBase;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class EnchantExcavation extends EnchantBase {

  private static final int POWER_PER_LEVEL = 7;

  public EnchantExcavation(Rarity rarityIn, EnchantmentType typeIn, EquipmentSlotType... slots) {
    super(rarityIn, typeIn, slots);
  }

  @Override
  public int getMaxLevel() {
    return 3;
  }

  private int getHarvestMax(int level) {
    return level * POWER_PER_LEVEL;
  }

  @SubscribeEvent(priority = EventPriority.LOWEST)
  public void onBreakEvent(BreakEvent event) {
    IWorld world = event.getWorld();
    PlayerEntity player = event.getPlayer();
    if (player.swingingHand == null) {
      return;
    }
    BlockPos pos = event.getPos();
    Block block = event.getState().getBlock();
    // is this item stack enchanted with ME?
    ItemStack stackHarvestingWith = player.getHeldItem(player.swingingHand);
    int level = this.getCurrentLevelTool(stackHarvestingWith);
    if (level <= 0) {
      return;
    }
    // if I am using an axe on stone or dirt, doesn't trigger
    boolean isAnySingleOk = false;// if i am a tool valid on 2 things, and both of 2 blocks are present, we are
    // just ok
    for (ToolType type : stackHarvestingWith.getItem().getToolTypes(stackHarvestingWith)) {
      if (block.isToolEffective(world.getBlockState(pos), type)) {
        isAnySingleOk = true;
      }
    }
    // starts at 1 for current one
    if (isAnySingleOk) {
      this.harvestSurrounding((World) world, player, pos, block, 1, level, player.swingingHand);
    }
  }

  /**
   * WARNING: RECURSIVE function to break all blocks connected up to the maximum total
   *
   * @param swingingHand
   */
  private int harvestSurrounding(final World world, final PlayerEntity player, final BlockPos posIn,
      final Block block, int totalBroken, final int level, Hand swingingHand) {
    if (totalBroken >= this.getHarvestMax(level) || player.getHeldItem(player.swingingHand).isEmpty()) {
      return totalBroken;
    }
    // int fortuneXp = 0;//even if tool has fortune, ignore just to unbalance a bit
    List<BlockPos> theFuture = this.getMatchingSurrounding(world, posIn, block);
    // ModCyclic.LOGGER.info("theFuture harvest " + theFuture.size());
    List<BlockPos> wasHarvested = new ArrayList<BlockPos>();
    for (BlockPos targetPos : theFuture) {
      BlockState targetState = world.getBlockState(targetPos);
      // check canHarvest every time -> permission or any other hooks
      if (world.isAirBlock(targetPos) || player.canHarvestBlock(targetState) == false
          || totalBroken >= this.getHarvestMax(level) || player.getHeldItem(player.swingingHand).isEmpty()) {
        continue;
      }
      if (world instanceof ServerWorld) {
        //this is important to get real drops to consider blockstate and tile entity
        //example: shulkers to keep contents
        Block.spawnDrops(targetState, world, targetPos, world.getTileEntity(targetPos));
      }
      world.destroyBlock(targetPos, false);
      wasHarvested.add(targetPos);
      // damage but also respect the unbreaking chant
      player.getHeldItem(player.swingingHand).attemptDamageItem(1, world.rand, null);
      // UtilItemStack.damageItem(player, player.getHeldItem(player.swingingHand) );
      totalBroken++;
    }
    // AFTER we harvest the close ones only THEN we branch out
    for (BlockPos targetPos : theFuture) {
      if (totalBroken >= this.getHarvestMax(level) || player.getHeldItem(player.swingingHand).isEmpty()) {
        break;
      }
      totalBroken += this.harvestSurrounding(world, player, targetPos, block, totalBroken, level, swingingHand);
    }
    return totalBroken;
  }

  private List<BlockPos> getMatchingSurrounding(World world, BlockPos start, Block blockIn) {
    List<BlockPos> list = new ArrayList<BlockPos>();
    List<Direction> targetFaces = Arrays.asList(Direction.values());
    Collections.shuffle(targetFaces);
    for (Direction fac : targetFaces) {
      BlockPos target = start.offset(fac);
      if (World.isOutsideBuildHeight(target)) {
        continue;
      }
      BlockState state = world.getBlockState(target);
      if (state == null || state.getBlock() == Blocks.AIR) {
        continue;
      }
      if (state.getBlock() == blockIn) {
        list.add(target);
      }
    }
    return list;
  }
}
