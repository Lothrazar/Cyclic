package com.lothrazar.cyclic.util;

import java.util.ArrayList;
import java.util.List;
import com.lothrazar.cyclic.data.BlockPosDim;
import com.lothrazar.cyclic.data.Const;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BucketPickup;
import net.minecraft.world.level.block.LeverBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class LevelWorldUtil {

  public static Direction getRandomDirection(RandomSource rand) {
    int index = Mth.nextInt(rand, 0, Direction.values().length - 1);
    return Direction.values()[index];
  }

  public static boolean removeFlowingLiquid(Level world, BlockPos pos, boolean nukeOption) {
    BlockState blockHere = world.getBlockState(pos);
    if (blockHere.hasProperty(BlockStateProperties.WATERLOGGED)) {
      // un-water log
      return world.setBlock(pos, blockHere.setValue(BlockStateProperties.WATERLOGGED, false), 18);
    }
    if (blockHere.getBlock() instanceof BucketPickup) {
      BucketPickup block = (BucketPickup) blockHere.getBlock();
      //
      ItemStack res = block.pickupBlock(world, pos, blockHere);
      if (!res.isEmpty()) {
        // flowing block
        return world.setBlock(pos, Blocks.AIR.defaultBlockState(), 18);
      }
      else {
        return true; // was source block
      }
    }
    else if (nukeOption) {
      //ok just nuke it 
      return world.setBlock(pos, Blocks.AIR.defaultBlockState(), 18);
    }
    return false;
  }

  public static String dimensionToString(Level world) {
    //example: returns "minecraft:overworld" resource location
    return world.dimension().location().toString();
    //RegistryKey.create(Registry.WORLD_KEY, new ResourceLocation("twilightforest", "twilightforest"));
  }

  public static ResourceKey<Level> stringToDimension(String key) {
    return ResourceKey.create(Registry.DIMENSION_REGISTRY, ResourceLocation.tryParse(key));
  }

  public static double distanceBetweenHorizontal(BlockPos start, BlockPos end) {
    int xDistance = Math.abs(start.getX() - end.getX());
    int zDistance = Math.abs(start.getZ() - end.getZ());
    // ye olde pythagoras
    return Math.sqrt(xDistance * xDistance + zDistance * zDistance);
  }

  public static BlockPos nextReplaceableInDirection(Level world, BlockPos posIn, Direction facing, int max, Block blockMatch) {
    BlockPos posToPlaceAt = new BlockPos(posIn);
    BlockPos posLoop = new BlockPos(posIn);
    //    world.getBlockState(posLoop).getBlock().isReplaceable(state, useContext)
    for (int i = 0; i < max; i++) {
      BlockState state = world.getBlockState(posLoop);
      if (state.getBlock() != null
          && world.getBlockState(posLoop).isAir()) {
        posToPlaceAt = posLoop;
        break;
      }
      else {
        posLoop = posLoop.relative(facing);
      }
    }
    return posToPlaceAt;
  }

  /**
   * Check if empty, then if not drop the stack safely in a new ItemEntity
   */
  public static ItemEntity dropItemStackInWorld(Level world, BlockPos pos, ItemStack stack) {
    if (pos == null || world == null || stack.isEmpty()) {
      return null;
    }
    ItemEntity entityItem = new ItemEntity(world, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, stack);
    if (world.isClientSide == false) {
      world.addFreshEntity(entityItem);
    }
    return entityItem;
  }
  //  public static BlockPos getRandomPos(Random rand, BlockPos here, int hRadius) {
  //    int x = here.getX();
  //    int z = here.getZ();
  //    // search in a square
  //    int xMin = x - hRadius;
  //    int xMax = x + hRadius;
  //    int zMin = z - hRadius;
  //    int zMax = z + hRadius;
  //    int posX = MathHelper.nextInt(rand, xMin, xMax);
  //    int posZ = MathHelper.nextInt(rand, zMin, zMax);
  //    return new BlockPos(posX, here.getY(), posZ);
  //  }

  public static boolean doesBlockExist(Level world, BlockPos start, BlockState blockHunt, final int radius, final int height) {
    int xMin = start.getX() - radius;
    int xMax = start.getX() + radius;
    int yMin = start.getY();
    int yMax = start.getY() + height;
    int zMin = start.getZ() - radius;
    int zMax = start.getZ() + radius;
    BlockPos posCurrent = null;
    for (int xLoop = xMin; xLoop <= xMax; xLoop++) {
      for (int yLoop = yMin; yLoop <= yMax; yLoop++) {
        for (int zLoop = zMin; zLoop <= zMax; zLoop++) {
          posCurrent = new BlockPos(xLoop, yLoop, zLoop);
          if (world.getBlockState(posCurrent) == blockHunt) {
            return true;
          }
        }
      }
    }
    return false;
  }

  public static ArrayList<BlockPos> findBlocksByTag(Level world, BlockPos start, TagKey<Block> blockHunt, final int radius) {
    ArrayList<BlockPos> found = new ArrayList<BlockPos>();
    int xMin = start.getX() - radius;
    int xMax = start.getX() + radius;
    int yMin = start.getY() - radius;
    int yMax = start.getY() + radius;
    int zMin = start.getZ() - radius;
    int zMax = start.getZ() + radius;
    BlockPos posCurrent = null;
    for (int xLoop = xMin; xLoop <= xMax; xLoop++) {
      for (int yLoop = yMin; yLoop <= yMax; yLoop++) {
        for (int zLoop = zMin; zLoop <= zMax; zLoop++) {
          posCurrent = new BlockPos(xLoop, yLoop, zLoop);
          if (world.getBlockState(posCurrent).is(blockHunt)) {
            found.add(posCurrent);
          }
        }
      }
    }
    return found;
  }

  public static ArrayList<BlockPos> findBlocks(Level world, BlockPos start, Block blockHunt, final int radius) {
    ArrayList<BlockPos> found = new ArrayList<BlockPos>();
    int xMin = start.getX() - radius;
    int xMax = start.getX() + radius;
    int yMin = start.getY() - radius;
    int yMax = start.getY() + radius;
    int zMin = start.getZ() - radius;
    int zMax = start.getZ() + radius;
    BlockPos posCurrent = null;
    for (int xLoop = xMin; xLoop <= xMax; xLoop++) {
      for (int yLoop = yMin; yLoop <= yMax; yLoop++) {
        for (int zLoop = zMin; zLoop <= zMax; zLoop++) {
          posCurrent = new BlockPos(xLoop, yLoop, zLoop);
          if (world.getBlockState(posCurrent).getBlock().equals(blockHunt)) {
            found.add(posCurrent);
          }
        }
      }
    }
    return found;
  }

  public static void toggleLeverPowerState(Level worldIn, BlockPos blockPos, BlockState blockState) {
    boolean hasPowerHere = blockState.getValue(LeverBlock.POWERED).booleanValue();
    BlockState stateNew = blockState.setValue(LeverBlock.POWERED, !hasPowerHere);
    boolean success = worldIn.setBlockAndUpdate(blockPos, stateNew);
    if (success) {
      flagUpdate(worldIn, blockPos, blockState, stateNew);
      flagUpdate(worldIn, blockPos.below(), blockState, stateNew);
      flagUpdate(worldIn, blockPos.above(), blockState, stateNew);
      flagUpdate(worldIn, blockPos.west(), blockState, stateNew);
      flagUpdate(worldIn, blockPos.east(), blockState, stateNew);
      flagUpdate(worldIn, blockPos.north(), blockState, stateNew);
      flagUpdate(worldIn, blockPos.south(), blockState, stateNew);
    }
  }

  public static void flagUpdate(Level worldIn, BlockPos blockPos, BlockState blockState, BlockState stateNew) {
    worldIn.sendBlockUpdated(blockPos, blockState, stateNew, 3);
    worldIn.updateNeighborsAt(blockPos, stateNew.getBlock());
    worldIn.updateNeighborsAt(blockPos, blockState.getBlock());
    //        worldIn.scheduleBlockUpdate(blockPos, stateNew.getBlock(), 3, 3);
    //        worldIn.scheduleUpdate(blockPos, stateNew.getBlock(), 3);
  }

  @SuppressWarnings("deprecation")
  public static BlockPos findClosestBlock(final Player player, final Block blockHunt, final int radiusIn) {
    BlockPos found = null;
    int xMin = (int) player.getX() - radiusIn;
    int xMax = (int) player.getX() + radiusIn;
    int yMin = (int) player.getY() - radiusIn;
    int yMax = (int) player.getY() + radiusIn;
    int zMin = (int) player.getZ() - radiusIn;
    int zMax = (int) player.getZ() + radiusIn;
    int distance = 0, distanceClosest = radiusIn * radiusIn;
    BlockPos posCurrent = null;
    Level world = player.getCommandSenderWorld();
    for (int xLoop = xMin; xLoop <= xMax; xLoop++) {
      for (int yLoop = yMin; yLoop <= yMax; yLoop++) {
        for (int zLoop = zMin; zLoop <= zMax; zLoop++) {
          posCurrent = new BlockPos(xLoop, yLoop, zLoop);
          if (world.isAreaLoaded(posCurrent, 1) == false) {
            continue;
          }
          if (world.getBlockState(posCurrent).getBlock().equals(blockHunt)) {
            // find closest?
            if (found == null) {
              found = posCurrent;
            }
            else {
              distance = (int) distanceBetweenHorizontal(player.blockPosition(), posCurrent);
              if (distance < distanceClosest) {
                found = posCurrent;
                distanceClosest = distance;
              }
            }
          }
        }
      }
    }
    return found;
  }

  public static List<BlockPos> getPositionsInRange(BlockPos pos, int xMin, int xMax, int yMin, int yMax, int zMin, int zMax) {
    List<BlockPos> found = new ArrayList<BlockPos>();
    for (int x = xMin; x <= xMax; x++) {
      for (int y = yMin; y <= yMax; y++) {
        for (int z = zMin; z <= zMax; z++) {
          found.add(new BlockPos(x, y, z));
        }
      }
    }
    return found;
  }

  public static boolean doBlockStatesMatch(BlockState replacedBlockState, BlockState newToPlace) {
    //    replacedBlockState.eq?
    return replacedBlockState.equals(newToPlace);
  }

  public static BlockPos getFirstBlockAbove(Level world, BlockPos pos) {
    //similar to vanilla fn getTopSolidOrLiquidBlock
    BlockPos posCurrent = null;
    for (int y = pos.getY() + 1; y < Const.WORLDHEIGHT; y++) {
      posCurrent = new BlockPos(pos.getX(), y, pos.getZ());
      if (world.getBlockState(posCurrent).isAir() &&
          world.getBlockState(posCurrent.above()).isAir() &&
          !world.getBlockState(posCurrent.below()).isAir()) {
        return posCurrent;
      }
    }
    return null;
  }

  public static BlockPos getLastAirBlockAbove(Level world, BlockPos pos) {
    //keep going up until you hit something that isn't air, then return the last air block
    return getLastAirBlock(world, pos, Direction.UP);
  }

  public static BlockPos getLastAirBlockBelow(Level world, BlockPos pos) {
    return getLastAirBlock(world, pos, Direction.DOWN);
  }

  public static BlockPos getLastAirBlock(Level world, BlockPos pos, Direction direction) {
    int increment;
    if (direction == Direction.DOWN) {
      increment = -1;
    }
    else {
      increment = 1;
    }
    BlockPos posCurrent;
    BlockPos posPrevious = pos;
    for (int y = pos.getY(); y < Const.WORLDHEIGHT && y > 0; y += increment) {
      posCurrent = new BlockPos(pos.getX(), y, pos.getZ());
      if (!world.isEmptyBlock(posCurrent)) {
        return posPrevious;
      }
      posPrevious = posCurrent;
    }
    return pos;
  }

  public static boolean dimensionIsEqual(BlockPosDim targetPos, Level world) {
    if (targetPos == null || targetPos.getDimension() == null) {
      return false;
    }
    return targetPos.getDimension().equalsIgnoreCase(LevelWorldUtil.dimensionToString(world));
  }

  public static BlockPos returnClosest(BlockPos playerPos, BlockPos pos1, BlockPos pos2) {
    if (pos1 == null && pos2 == null) {
      return null;
    }
    else if (pos1 == null) {
      return pos2;
    }
    else if (pos2 == null) {
      return pos1;
    }
    else if (LevelWorldUtil.distanceBetweenHorizontal(playerPos, pos1) <= LevelWorldUtil.distanceBetweenHorizontal(playerPos, pos2)) {
      return pos1;
    }
    return pos2;
  }
}
