package com.lothrazar.cyclic.util;

import com.google.common.collect.Sets;
import com.lothrazar.cyclic.api.IHarvesterOverride;
import com.lothrazar.cyclic.compat.CompatConstants;
import com.lothrazar.cyclic.data.DataTags;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.CropsBlock;
import net.minecraft.block.StemBlock;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.Property;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

public class HarvestUtil {

  public static final Set<IHarvesterOverride> HARVEST_OVERRIDES = Sets.newIdentityHashSet();

  public static void harvestShape(World world, BlockPos pos, int radius) {
    // TODO Auto-generated method stub
    List<BlockPos> shape = UtilShape.squareHorizontalFull(pos, radius);
    for (BlockPos p : shape) {
      HarvestUtil.tryHarvestSingle(world, p);
    }
  }

  public static boolean tryHarvestSingle(World world, BlockPos posCurrent) {
    BlockState blockState = world.getBlockState(posCurrent);
    // Try running override logic
    IHarvesterOverride applicable = null;
    for (IHarvesterOverride override : HARVEST_OVERRIDES) {
      if (override.appliesTo(blockState, world, posCurrent)) {
        applicable = override;
        break;
      }
    }
    if (applicable != null) {
      return applicable.attemptHarvest(blockState, world, posCurrent, stack -> UtilItemStack.drop(world, posCurrent, blockState.getBlock()));
    }
    // Fall back to default logic
    if (simpleBreakDrop(blockState)) {
      UtilItemStack.drop(world, posCurrent, blockState.getBlock());
      world.destroyBlock(posCurrent, false);
      return true;
    }
    //don't break stems see Issue #1601
    if (world.getBlockState(posCurrent).getBlock() instanceof StemBlock) {
      return false;
    }
    //growable crops have an age block property
    IntegerProperty propInt = getAgeProp(blockState);
    if (propInt == null || !(world instanceof ServerWorld)) {
      return false;
    }
    final int currentAge = blockState.get(propInt);
    final int minAge = Collections.min(propInt.getAllowedValues());
    final int maxAge = Collections.max(propInt.getAllowedValues());
    if (minAge == maxAge || currentAge < maxAge) {
      //not grown
      return false;
    }
    //we have an age property, so harvest now and run drops
    //update behavior to address Issue #1600
    //
    Item seed = null;
    if (blockState.getBlock() instanceof CropsBlock) {
      CropsBlock crop = (CropsBlock) blockState.getBlock();
      ItemStack defaultSeedDrop = crop.getItem(world, posCurrent, blockState);
      if (!defaultSeedDrop.isEmpty()) {
        seed = defaultSeedDrop.getItem();
      }
    }
    List<ItemStack> drops = Block.getDrops(blockState, (ServerWorld) world, posCurrent, null);
    for (ItemStack dropStack : drops) {
      if (seed != null && dropStack.getItem() == seed) {
        //we found the seed. steal one for replant
        dropStack.shrink(1);
        seed = null;
      }
      //drop the rest
      UtilWorld.dropItemStackInWorld(world, posCurrent, dropStack);
    }
    if (world instanceof ServerWorld) {
      blockState.spawnAdditionalDrops((ServerWorld) world, posCurrent, ItemStack.EMPTY);
    }
    //now update age to zero after harvest
    BlockState newState = blockState.with(propInt, minAge);
    boolean updated = world.setBlockState(posCurrent, newState);
    return updated || drops.size() > 0;
  }

  private static boolean simpleBreakDrop(BlockState blockState) {
    boolean breakit = blockState.isIn(DataTags.VINES) || blockState.isIn(DataTags.CROP_BLOCKS);
    // the list tells all
    return breakit;
  }

  public static IntegerProperty getAgeProp(BlockState blockState) {
    if (blockState.getBlock() instanceof CropsBlock) {
      CropsBlock crops = (CropsBlock) blockState.getBlock();
      //better mod compatibility if they dont use 'age'
      return crops.getAgeProperty();
    }
    String age = CropsBlock.AGE.getName();
    ResourceLocation bid = blockState.getBlock().getRegistryName();
    if (CompatConstants.RESYNTH.equalsIgnoreCase(bid.getNamespace())) {
      //some silly old mods dont use age for compatibility
      // https://github.com/Resynth-Minecraft-Mod/Resynth-Mod/blob/a9f47439d103c1c17ca7a4ffd05c2dc0397e5e5f/src/main/java/com/ki11erwolf/resynth/plant/block/BlockBiochemicalPlant.java#L59
      //so we hack it
      age = CompatConstants.RESYNTH_GROWTH_STAGE;
    }
    for (Property<?> p : blockState.getProperties()) {
      if (p != null && p.getName() != null
          && p instanceof IntegerProperty &&
          p.getName().equalsIgnoreCase(age)) {
        return (IntegerProperty) p;
      }
    }
    //IGrowable is useless here, i tried. no way to tell if its fully grown, or what age/stage its in
    return null;
  }
}
