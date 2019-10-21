package com.lothrazar.cyclic.util;

import java.util.ArrayList;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.LeverBlock;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class UtilWorld {

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

  public static boolean tryTpPlayerToBed(World world, PlayerEntity player) {
    if (world.isRemote) {
      return false;
    }
    if (player.dimension.getId() != 0) {
      //      UtilChat.addChatMessage(player, "command.home.overworld");
      return false;
    }
    BlockPos pos = player.getBedLocation();
    if (pos == null) {
      // has not been sent in a bed
      //      UtilChat.addChatMessage(player, "command.gethome.bed");
      return false;
    }
    //  if player data says your bed location is set, then its set
    //so now. if   spawn was set for any reason (bed/sleepingmat/other) then this TP works the same way as /kill
    UtilEntity.teleportWallSafe(player, world, pos);
    //    UtilSound.playSound(player, pos, SoundEvents.ENTITY_ENDERMEN_TELEPORT);
    return true;
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
    //    worldIn.notifyBlockUpdate(blockPos,blockState,stateNew,3);
    //    worldIn.notifyNeighborsOfStateChange(pos, blockIn);
    worldIn.notifyNeighborsOfStateChange(blockPos, blockState.getBlock());//THIS one works only with true
    //    worldIn.scheduleBlockUpdate(blockPos, stateNew.getBlock(), 3, 3);
    //    worldIn.scheduleUpdate(blockPos, stateNew.getBlock(), 3);
  }
}
