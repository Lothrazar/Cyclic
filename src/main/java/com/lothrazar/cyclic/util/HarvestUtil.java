package com.lothrazar.cyclic.util;

import java.util.Collections;
import java.util.List;
import com.lothrazar.cyclic.api.IHarvesterOverride;
import com.lothrazar.cyclic.block.harvester.TileHarvester;
import com.lothrazar.cyclic.compat.CompatConstants;
import com.lothrazar.cyclic.data.DataTags;
import com.lothrazar.cyclic.item.scythe.ScytheType;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.StemBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraftforge.registries.ForgeRegistries;

public class HarvestUtil {

  // hardcoded as MAIN hand
  public static boolean harvestByScytheType(Level world, Player player, BlockPos posCurrent, ScytheType type) {
    boolean doBreak = false;
    BlockState blockState = world.getBlockState(posCurrent);
    if (blockState.hasProperty(DoublePlantBlock.HALF) && blockState.getValue(DoublePlantBlock.HALF).equals(DoubleBlockHalf.LOWER)) {
      //the upper half has the drops, so only do that. avoid glitches and item loss
      // ie: rose, lilac, sunflower
      return false;
    }
    switch (type) {
      case LEAVES:
        doBreak = blockState.is(BlockTags.LEAVES);
      break;
      case BRUSH:
        doBreak = blockState.is(DataTags.PLANTS);
      break;
      case FORAGE:
        doBreak = blockState.is(BlockTags.FLOWERS)
            || blockState.is(BlockTags.CORALS) || blockState.is(BlockTags.WALL_CORALS)
            || blockState.is(DataTags.MUSHROOMS)
            || blockState.is(DataTags.VINES)
            || blockState.is(DataTags.CACTUS)
            || blockState.is(DataTags.CROP_BLOCKS);
      break;
    }
    if (doBreak) {
      if (blockState.is(DataTags.CROP_BLOCKS)) {
        world.destroyBlock(posCurrent, false, player);
        ItemStackUtil.drop(world, posCurrent, blockState.getBlock()); //like shears
      }
      else {
        //harvest block with player context: better mod compatibility
        if (type == ScytheType.BRUSH && EnchantmentHelper.getEnchantmentLevel(Enchantments.SILK_TOUCH, player) > 0) {
          //only brush needed silk override, tree leaves worked regardless
          ItemStackUtil.drop(world, posCurrent, blockState.getBlock());
        }
        else {
          blockState.getBlock().playerDestroy(world, player, posCurrent, blockState, world.getBlockEntity(posCurrent), player.getMainHandItem());
        }
        //sometimes this doesnt work and/or doesnt sync ot client, so force it
        world.destroyBlock(posCurrent, false, player);
        //break with false to disable dropsfor the above versions, dont want to dupe tallflowers
      }
      return true;
    }
    return false;
  }

  public static void harvestShape(Level world, BlockPos pos, int radius) {
    List<BlockPos> shape = ShapeUtil.squareHorizontalFull(pos, radius);
    for (BlockPos p : shape) {
      HarvestUtil.tryHarvestSingle(world, p);
    }
  }

  private static boolean simpleBreakDrop(BlockState blockState) {
    boolean breakit = blockState.is(DataTags.VINES) || blockState.is(DataTags.CROP_BLOCKS);
    // the list tells all
    return breakit;
  }

  public static boolean tryHarvestSingle(Level world, BlockPos posCurrent) {
    BlockState blockState = world.getBlockState(posCurrent);
    // Try running override logic
    IHarvesterOverride applicable = null;
    for (IHarvesterOverride override : TileHarvester.HARVEST_OVERRIDES) {
      if (override.appliesTo(blockState, world, posCurrent)) {
        applicable = override;
        break;
      }
    }
    if (applicable != null) {
      return applicable.attemptHarvest(blockState, world, posCurrent, stack -> ItemStackUtil.drop(world, posCurrent, blockState.getBlock()));
    }
    // Fall back to default logic
    if (simpleBreakDrop(blockState)) {
      ItemStackUtil.drop(world, posCurrent, blockState.getBlock());
      world.destroyBlock(posCurrent, false);
      return true;
    }
    //don't break stems see Issue #1601
    if (world.getBlockState(posCurrent).getBlock() instanceof StemBlock) {
      return false;
    }
    //growable crops have an age block property
    IntegerProperty propInt = HarvestUtil.getAgeProp(blockState);
    if (propInt == null || !(world instanceof ServerLevel)) {
      return false;
    }
    final int currentAge = blockState.getValue(propInt);
    final int minAge = Collections.min(propInt.getPossibleValues());
    final int maxAge = Collections.max(propInt.getPossibleValues());
    if (minAge == maxAge || currentAge < maxAge) {
      //not grown
      return false;
    }
    //we have an age property, so harvest now and run drops
    //update behavior to address Issue #1600
    //
    Item seed = null;
    if (blockState.getBlock() instanceof CropBlock) {
      CropBlock crop = (CropBlock) blockState.getBlock();
      ItemStack defaultSeedDrop = crop.getCloneItemStack(world, posCurrent, blockState);
      if (!defaultSeedDrop.isEmpty()) {
        seed = defaultSeedDrop.getItem();
      }
    }
    List<ItemStack> drops = Block.getDrops(blockState, (ServerLevel) world, posCurrent, null);
    for (ItemStack dropStack : drops) {
      if (seed != null && dropStack.getItem() == seed) {
        //we found the seed. steal one for replant
        dropStack.shrink(1);
        seed = null;
      }
      //drop the rest
      LevelWorldUtil.dropItemStackInWorld(world, posCurrent, dropStack);
    }
    if (world instanceof ServerLevel) {
      boolean isPlayer = true;
      blockState.spawnAfterBreak((ServerLevel) world, posCurrent, ItemStack.EMPTY, isPlayer);
    }
    //now update age to zero after harvest
    BlockState newState = blockState.setValue(propInt, minAge);
    boolean updated = world.setBlockAndUpdate(posCurrent, newState);
    return updated || drops.size() > 0;
  }

  public static IntegerProperty getAgeProp(BlockState blockState) {
    if (blockState.getBlock() instanceof CropBlock) {
      CropBlock crops = (CropBlock) blockState.getBlock();
      //better mod compatibility if they dont use 'age'
      return CropBlock.AGE; // protected :(  crops.getAgeProperty();
    }
    String age = CropBlock.AGE.getName();
    ResourceLocation bid = ForgeRegistries.BLOCKS.getKey(blockState.getBlock());
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
