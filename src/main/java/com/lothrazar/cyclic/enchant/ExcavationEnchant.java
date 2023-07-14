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

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import com.lothrazar.cyclic.registry.EnchantRegistry;
import com.lothrazar.library.util.ItemStackUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.Tags;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.event.level.BlockEvent.BreakEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ExcavationEnchant extends EnchantmentCyclic {

  public static final String ID = "excavate";
  public static BooleanValue CFG;

  public ExcavationEnchant(Rarity rarityIn, EnchantmentCategory typeIn, EquipmentSlot... slots) {
    super(rarityIn, typeIn, slots);
    MinecraftForge.EVENT_BUS.register(this);
  }

  @Override
  public boolean isEnabled() {
    return CFG.get();
  }

  @Override
  public boolean isTradeable() {
    return isEnabled() && super.isTradeable();
  }

  @Override
  public boolean isDiscoverable() {
    return isEnabled() && super.isDiscoverable();
  }

  @Override
  public boolean isAllowedOnBooks() {
    return isEnabled() && super.isAllowedOnBooks();
  }

  @Override
  public boolean canApplyAtEnchantingTable(ItemStack stack) {
    return isEnabled() && super.canApplyAtEnchantingTable(stack);
  }

  @Override
  public int getMaxLevel() {
    return 5;
  }

  @Override
  public boolean canEnchant(ItemStack stack) {
    return isEnabled() && (super.canEnchant(stack) || stack.is(Tags.Items.SHEARS));
  }

  @Override
  public boolean checkCompatibility(Enchantment ench) {
    return super.checkCompatibility(ench) && ench != EnchantRegistry.EXPERIENCE_BOOST.get();
  }

  private int getHarvestMax(int level) {
    return 26 + 8 * level;
  }

  @SubscribeEvent(priority = EventPriority.LOWEST)
  public void onBreakEvent(BreakEvent event) {
    if (!isEnabled()) {
      return;
    }
    LevelAccessor world = event.getLevel();
    Player player = event.getPlayer();
    if (player.swingingArm == null || world.isClientSide()) {
      return;
    }
    BlockPos pos = event.getPos();
    BlockState eventState = event.getState();
    Block block = eventState.getBlock();
    //is this item stack enchanted with ME?
    ItemStack stackHarvestingWith = player.getItemInHand(player.swingingArm);
    int level = this.getCurrentLevelTool(stackHarvestingWith);
    if (level <= 0) {
      return;
    }
    //if (ForgeHooks.canHarvestBlock(eventState, player, world, pos)) {
    if (ForgeEventFactory.doPlayerHarvestCheck(player, eventState, true)) {
      int harvested = this.harvestSurrounding((Level) world, player, pos, block, 1, level, player.swingingArm);
      if (harvested > 0) {
        //damage but also respect the unbreaking chant  
        ItemStackUtil.damageItem(player, stackHarvestingWith);
      }
    }
  }

  /**
   * WARNING: RECURSIVE function to break all blocks connected up to the maximum total
   *
   * @param swingingHand
   */
  private int harvestSurrounding(final Level world, final Player player, final BlockPos posIn, final Block block, int totalBroken, final int level, InteractionHand swingingHand) {
    if (totalBroken >= this.getHarvestMax(level) || player.getItemInHand(player.swingingArm).isEmpty()) {
      return totalBroken;
    }
    Set<BlockPos> wasHarvested = new HashSet<BlockPos>();
    Set<BlockPos> theFuture = this.getMatchingSurrounding(world, posIn, block);
    for (BlockPos targetPos : theFuture) {
      BlockState targetState = world.getBlockState(targetPos);
      //check canHarvest every time -> permission or any other hooks
      if (world.isEmptyBlock(targetPos)
          || !player.mayBuild()
          || !player.hasCorrectToolForDrops(targetState) //canHarvestBlock
          || totalBroken >= this.getHarvestMax(level)
          || player.getItemInHand(player.swingingArm).isEmpty()
      //          || ForgeEventFactory.doPlayerHarvestCheck(player, targetState, true)
      //          || !ForgeHooks.canHarvestBlock(targetState, player, world, targetPos)
      ) {
        continue;
      }
      if (world instanceof ServerLevel) {
        //important! use the version that takes the item stack. this way it will end up in Block:getDrops that references the LootContext.Builder
        //and since now loot tables are used, fortune and similar things will be respected
        Block.dropResources(targetState, world, targetPos, world.getBlockEntity(targetPos), player, player.getItemInHand(player.swingingArm));
      }
      int bonusLevel = EnchantmentHelper.getTagEnchantmentLevel(Enchantments.BLOCK_FORTUNE, player.getMainHandItem());
      int silklevel = EnchantmentHelper.getTagEnchantmentLevel(Enchantments.SILK_TOUCH, player.getMainHandItem());
      int exp = targetState.getExpDrop(world, world.random, targetPos, bonusLevel, silklevel);
      if (exp > 0 && world instanceof ServerLevel) {
        block.popExperience((ServerLevel) world, targetPos, exp);
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
      if (totalBroken >= this.getHarvestMax(level) || player.getItemInHand(player.swingingArm).isEmpty()) {
        break;
      }
      totalBroken += this.harvestSurrounding(world, player, targetPos, block, totalBroken, level, swingingHand);
    }
    return totalBroken;
  }

  private static final Direction[] VALUES = Direction.values();

  private Set<BlockPos> getMatchingSurrounding(Level world, BlockPos start, Block blockIn) {
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
      Block target = world.getBlockState(start.relative(fac)).getBlock();
      if (target == blockIn) {
        list.add(start.relative(fac));
      }
    }
    return list;
  }
}
