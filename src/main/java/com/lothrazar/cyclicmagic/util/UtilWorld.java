package com.lothrazar.cyclicmagic.util;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.dispenser.IPosition;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class UtilWorld {
  public static boolean isNight(World world) {
    long t = world.getWorldTime();
    int timeOfDay = (int) t % 24000;
    return timeOfDay > 12000;
  }
  public static BlockPos convertIposToBlockpos(IPosition here) {
    return new BlockPos(here.getX(), here.getY(), here.getZ());
  }
  public static BlockPos getRandomPos(Random rand, BlockPos here, int hRadius) {
    int x = here.getX();
    int z = here.getZ();
    // search in a square
    int xMin = x - hRadius;
    int xMax = x + hRadius;
    int zMin = z - hRadius;
    int zMax = z + hRadius;
    int posX = MathHelper.getRandomIntegerInRange(rand, xMin, xMax);
    int posZ = MathHelper.getRandomIntegerInRange(rand, zMin, zMax);
    return new BlockPos(posX, here.getY(), posZ);
  }
  
  
  public static boolean tryTpPlayerToBed( World world, EntityPlayer player){
    if(world.isRemote){return false;}
    if (player.dimension != 0) {
      UtilChat.addChatMessage(player, "command.home.overworld");
      return false;
    }
    BlockPos pos = player.getBedLocation(0);
    if (pos == null) {
      // has not been sent in a bed
      UtilChat.addChatMessage(player, "command.gethome.bed");
      return false;
    }
    IBlockState state = world.getBlockState(pos);
    Block block = (state == null) ? null : world.getBlockState(pos).getBlock();
    if (block != null && block.isBed(state, world, pos, player)) {
      // then move over according to how/where the bed wants me to spawn
      pos = block.getBedSpawnPosition(state, world, pos, null);
    }
    else {
      // spawn point was set, so the coords were not null, but player broke the
      // bed (probably recently)
      UtilChat.addChatMessage(player, "command.gethome.bed");
      return false;
    }
    UtilEntity.teleportWallSafe(player, world, pos);
    UtilSound.playSound(player, pos, SoundEvents.ENTITY_ENDERMEN_TELEPORT);
 
    return true;
  }
}
