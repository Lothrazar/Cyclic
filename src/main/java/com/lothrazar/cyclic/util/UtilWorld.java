package com.lothrazar.cyclic.util;

import java.util.ArrayList;
import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class UtilWorld {

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
}
