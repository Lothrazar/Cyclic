package com.lothrazar.cyclic.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.annotation.Nullable;
import com.lothrazar.cyclic.data.Const;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.LeverBlock;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class UtilWorld {

  public static String dimensionToString(World w) {
    //example: returns "minecraft:overworld" resource location
    return w.getDimensionKey().getRegistryName().toString();
    //RegistryKey.func_240903_a_(Registry.WORLD_KEY, new ResourceLocation("twilightforest", "twilightforest"));
  }

  public static double distanceBetweenHorizontal(BlockPos start, BlockPos end) {
    int xDistance = Math.abs(start.getX() - end.getX());
    int zDistance = Math.abs(start.getZ() - end.getZ());
    // ye olde pythagoras
    return Math.sqrt(xDistance * xDistance + zDistance * zDistance);
  }

  public static BlockPos nextReplaceableInDirection(World world, BlockPos posIn, Direction facing, int max, @Nullable Block blockMatch) {
    BlockPos posToPlaceAt = new BlockPos(posIn);
    BlockPos posLoop = new BlockPos(posIn);
    //    world.getBlockState(posLoop).getBlock().isReplaceable(state, useContext)
    for (int i = 0; i < max; i++) {
      BlockState state = world.getBlockState(posLoop);
      if (state.getBlock() != null
          && world.getBlockState(posLoop).getBlock() == Blocks.AIR//.isAir(state)//.isReplaceable(world, posLoop)
      ) {
        posToPlaceAt = posLoop;
        break;
      }
      else {
        posLoop = posLoop.offset(facing);
      }
    }
    return posToPlaceAt;
  }

  public static ItemEntity dropItemStackInWorld(World world, BlockPos pos, ItemStack stack) {
    if (pos == null || world == null || stack.isEmpty()) {
      return null;
    }
    ItemEntity entityItem = new ItemEntity(world, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, stack);
    if (world.isRemote == false) {
      world.addEntity(entityItem);
    }
    return entityItem;
  }

  public static BlockPos getRandomPos(Random rand, BlockPos here, int hRadius) {
    int x = here.getX();
    int z = here.getZ();
    // search in a square
    int xMin = x - hRadius;
    int xMax = x + hRadius;
    int zMin = z - hRadius;
    int zMax = z + hRadius;
    int posX = MathHelper.nextInt(rand, xMin, xMax);
    int posZ = MathHelper.nextInt(rand, zMin, zMax);
    return new BlockPos(posX, here.getY(), posZ);
  }

  public static ArrayList<BlockPos> findBlocks(World world, BlockPos start, Block blockHunt, int RADIUS) {
    ArrayList<BlockPos> found = new ArrayList<BlockPos>();
    int xMin = start.getX() - RADIUS;
    int xMax = start.getX() + RADIUS;
    int yMin = start.getY() - RADIUS;
    int yMax = start.getY() + RADIUS;
    int zMin = start.getZ() - RADIUS;
    int zMax = start.getZ() + RADIUS;
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

  public static void toggleLeverPowerState(World worldIn, BlockPos blockPos, BlockState blockState) {
    boolean hasPowerHere = blockState.get(LeverBlock.POWERED).booleanValue();//this.block.getStrongPower(blockState, worldIn, pointer, EnumFacing.UP) > 0;
    BlockState stateNew = blockState.with(LeverBlock.POWERED, !hasPowerHere);
    boolean success = worldIn.setBlockState(blockPos, stateNew);
    if (success) {
      flagUpdate(worldIn, blockPos, blockState, stateNew);
      flagUpdate(worldIn, blockPos.down(), blockState, stateNew);
      flagUpdate(worldIn, blockPos.up(), blockState, stateNew);
      flagUpdate(worldIn, blockPos.west(), blockState, stateNew);
      flagUpdate(worldIn, blockPos.east(), blockState, stateNew);
      flagUpdate(worldIn, blockPos.north(), blockState, stateNew);
      flagUpdate(worldIn, blockPos.south(), blockState, stateNew);
    }
  }

  public static void flagUpdate(World worldIn, BlockPos blockPos, BlockState blockState, BlockState stateNew) {
    worldIn.notifyBlockUpdate(blockPos, blockState, stateNew, 3);
    worldIn.notifyNeighborsOfStateChange(blockPos, stateNew.getBlock());
    worldIn.notifyNeighborsOfStateChange(blockPos, blockState.getBlock());//THIS one works only with true
    //        worldIn.scheduleBlockUpdate(blockPos, stateNew.getBlock(), 3, 3);
    //        worldIn.scheduleUpdate(blockPos, stateNew.getBlock(), 3);
  }

  public static BlockPos findClosestBlock(PlayerEntity player, Block blockHunt, int RADIUS) {
    BlockPos found = null;
    int xMin = (int) player.getPosX() - RADIUS;
    int xMax = (int) player.getPosX() + RADIUS;
    int yMin = (int) player.getPosY() - RADIUS;
    int yMax = (int) player.getPosY() + RADIUS;
    int zMin = (int) player.getPosZ() - RADIUS;
    int zMax = (int) player.getPosZ() + RADIUS;
    int distance = 0, distanceClosest = RADIUS * RADIUS;
    BlockPos posCurrent = null;
    World world = player.getEntityWorld();
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
              distance = (int) distanceBetweenHorizontal(player.getPosition(), posCurrent);
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
    for (int x = xMin; x <= xMax; x++)
      for (int y = yMin; y <= yMax; y++)
        for (int z = zMin; z <= zMax; z++) {
          found.add(new BlockPos(x, y, z));
        }
    return found;
  }

  public static boolean doBlockStatesMatch(BlockState replacedBlockState, BlockState newToPlace) {
    //    replacedBlockState.eq?
    return replacedBlockState.equals(newToPlace);
  }

  public static BlockPos getFirstBlockAbove(World world, BlockPos pos) {
    //similar to vanilla fn getTopSolidOrLiquidBlock
    BlockPos posCurrent = null;
    for (int y = pos.getY() + 1; y < Const.WORLDHEIGHT; y++) {
      posCurrent = new BlockPos(pos.getX(), y, pos.getZ());
      if (world.getBlockState(posCurrent).getBlock() == Blocks.AIR &&
          world.getBlockState(posCurrent.up()).getBlock() == Blocks.AIR &&
          world.getBlockState(posCurrent.down()).getBlock() != Blocks.AIR) {
        return posCurrent;
      }
    }
    return null;
  }
}
