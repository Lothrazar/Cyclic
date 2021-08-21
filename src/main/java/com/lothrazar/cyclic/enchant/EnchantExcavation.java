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

import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.base.EnchantBase;
import com.lothrazar.cyclic.registry.EnchantRegistry;
import com.lothrazar.cyclic.util.UtilItemStack;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.Tags;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class EnchantExcavation extends EnchantBase {

  private static final int POWER_PER_LEVEL = 8;

  public EnchantExcavation(Rarity rarityIn, EnchantmentType typeIn, EquipmentSlotType... slots) {
    super(rarityIn, typeIn, slots);
    MinecraftForge.EVENT_BUS.register(this);
  }

  public static BooleanValue CFG;
  public static final String ID = "excavate";

  @Override
  public boolean isEnabled() {
    return CFG.get();
  }

  @Override
  public int getMaxLevel() {
    return 5;
  }

  @Override
  public boolean canApply(ItemStack stack) {
    return super.canApply(stack) || stack.getItem().isIn(Tags.Items.SHEARS);
  }

  @Override
  public boolean canApplyTogether(Enchantment ench) {
    return super.canApplyTogether(ench) && ench != EnchantRegistry.EXPERIENCE_BOOST;
  }

  private int getHarvestMax(int level) {
    return (level + 2) * POWER_PER_LEVEL + 10;
  }

  @SubscribeEvent(priority = EventPriority.LOWEST)
  public void onBreakEvent(BreakEvent event) {
    IWorld world = event.getWorld();
    PlayerEntity player = event.getPlayer();
    if (player.swingingHand == null || world.isRemote()) {
      return;
    }
    BlockPos pos = event.getPos();
    BlockState eventState = event.getState();
    Block block = eventState.getBlock();
    //is this item stack enchanted with ME?
    ItemStack stackHarvestingWith = player.getHeldItem(player.swingingHand);
    int level = this.getCurrentLevelTool(stackHarvestingWith);
    if (level <= 0) {
      return;
    }
    if (ForgeHooks.canHarvestBlock(eventState, player, world, pos)) {
      int harvested = this.harvestSurrounding((World) world, player, pos, block, 1, level, player.swingingHand);
      if (harvested > 0) {
        //damage but also respect the unbreaking chant  
        UtilItemStack.damageItem(player, stackHarvestingWith);
      }
    }
    else {
      ModCyclic.LOGGER.info(stackHarvestingWith + "  TOOL NOT EFFECTIVE ON " + world.getBlockState(pos));
    }
    //else wtf why is this false for redstone ore
  }

  /**
   * WARNING: RECURSIVE function to break all blocks connected up to the maximum total
   *
   * @param swingingHand
   */
  private int harvestSurrounding(final World world, final PlayerEntity player, final BlockPos posIn, final Block block, int totalBroken, final int level, Hand swingingHand) {
    if (totalBroken >= this.getHarvestMax(level) || player.getHeldItem(player.swingingHand).isEmpty()) {
      return totalBroken;
    }
    Set<BlockPos> wasHarvested = new HashSet<BlockPos>();
    Set<BlockPos> theFuture = this.getMatchingSurrounding(world, posIn, block);
    for (BlockPos targetPos : theFuture) {
      BlockState targetState = world.getBlockState(targetPos);
      //check canHarvest every time -> permission or any other hooks
      if (world.isAirBlock(targetPos)
          || !player.isAllowEdit()
          || !player.func_234569_d_(targetState) //canHarvestBlock
          || totalBroken >= this.getHarvestMax(level)
          || player.getHeldItem(player.swingingHand).isEmpty()
          || !ForgeHooks.canHarvestBlock(targetState, player, world, targetPos)) {
        continue;
      }
      if (world instanceof ServerWorld) {
        //important! use the version that takes the item stack. this way it will end up in Block:getDrops that references the LootContext.Builder
        //and since now loot tables are used, fortune and similar things will be respected
        Block.spawnDrops(targetState, world, targetPos, world.getTileEntity(targetPos), player, player.getHeldItem(player.swingingHand));
      }
      int bonusLevel = EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, player.getHeldItemMainhand());
      int silklevel = EnchantmentHelper.getEnchantmentLevel(Enchantments.SILK_TOUCH, player.getHeldItemMainhand());
      int exp = targetState.getExpDrop(world, targetPos, bonusLevel, silklevel);
      if (exp > 0 && world instanceof ServerWorld) {
        block.dropXpOnBlockBreak((ServerWorld) world, targetPos, exp);
      }
      world.destroyBlock(targetPos, false);
      wasHarvested.add(targetPos);
      totalBroken++;
    }
    if (wasHarvested.size() == 0) {
      //nothing was harvested here, dont move on
      return totalBroken;
    }
    //AFTER we harvest the close ones only THEN we branch out
    for (BlockPos targetPos : theFuture) {
      if (totalBroken >= this.getHarvestMax(level) || player.getHeldItem(player.swingingHand).isEmpty()) {
        break;
      }
      totalBroken += this.harvestSurrounding(world, player, targetPos, block, totalBroken, level, swingingHand);
    }
    return totalBroken;
  }

  private static final Direction[] VALUES = Direction.values();

  private Set<BlockPos> getMatchingSurrounding(World world, BlockPos start, Block blockIn) {
    Set<BlockPos> list = new HashSet<BlockPos>();
    List<Direction> targetFaces = Arrays.asList(VALUES);
    try {
      // cannot replicate this error at all at max level (5 = V)
      // java.lang.StackOverflowError: Exception in server tick loop
      //      at java.util.Collections.swap(Unknown Source) ~[?:1.8.0_201] {}
      //      at java.util.Collections.shuffle(Unknown Source) ~[?:1.8.0_201] {}
      //      at java.util.Collections.shuffle(Unknown Source) ~[?:1.8.0_201] {}
      Collections.shuffle(targetFaces);
    }
    catch (Exception e) {
      // java.util shit the bed not my problem 
    }
    for (Direction fac : targetFaces) {
      Block target = world.getBlockState(start.offset(fac)).getBlock();
      if (target == blockIn) {
        list.add(start.offset(fac));
      }
    }
    return list;
  }
}
