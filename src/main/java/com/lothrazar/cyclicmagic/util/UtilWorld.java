package com.lothrazar.cyclicmagic.util;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.command.ICommandSender;
import net.minecraft.dispenser.IPosition;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
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
  public static List<BlockPos> getPositionsInRange(BlockPos center, int xMin, int xMax, int yMin, int yMax, int zMin, int zMax) {
    List<BlockPos> found = new ArrayList<BlockPos>();
    for (int x = xMin; x <= xMax; x++)
      for (int y = yMin; y <= yMax; y++)
        for (int z = zMin; z <= zMax; z++) {
          found.add(new BlockPos(x, y, z));
        }
    return found;
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
  public static boolean tryTpPlayerToBed(World world, EntityPlayer player) {
    if (world.isRemote) { return false; }
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
  public static Map<IInventory, BlockPos> findTileEntityInventories(ICommandSender player, int RADIUS) {
    // function imported
    // https://github.com/PrinceOfAmber/SamsPowerups/blob/master/Commands/src/main/java/com/lothrazar/samscommands/ModCommands.java#L193
    Map<IInventory, BlockPos> found = new HashMap<IInventory, BlockPos>();
    int xMin = (int) player.getPosition().getX() - RADIUS;
    int xMax = (int) player.getPosition().getX() + RADIUS;
    int yMin = (int) player.getPosition().getY() - RADIUS;
    int yMax = (int) player.getPosition().getY() + RADIUS;
    int zMin = (int) player.getPosition().getZ() - RADIUS;
    int zMax = (int) player.getPosition().getZ() + RADIUS;
    BlockPos posCurrent = null;
    for (int xLoop = xMin; xLoop <= xMax; xLoop++) {
      for (int yLoop = yMin; yLoop <= yMax; yLoop++) {
        for (int zLoop = zMin; zLoop <= zMax; zLoop++) {
          posCurrent = new BlockPos(xLoop, yLoop, zLoop);
          if (player.getEntityWorld().getTileEntity(posCurrent) instanceof IInventory) {
            found.put((IInventory) player.getEntityWorld().getTileEntity(posCurrent), posCurrent);
          }
        }
      }
    }
    return found;
  }
  public static int searchTileInventory(String search, IInventory inventory) {
    int foundQty;
    foundQty = 0;
    for (int slot = 0; slot < inventory.getSizeInventory(); slot++) {
      ItemStack invItem = inventory.getStackInSlot(slot);
      if (invItem == null) {
        continue;
      } // empty slot in chest (or container)
      String invItemName = invItem.getDisplayName().toLowerCase();
      // find any overlap: so if x ==y , or if x substring of y, or y substring
      // of x
      if (search.equals(invItemName) || search.contains(invItemName) || invItemName.contains(search)) {
        foundQty += invItem.stackSize;
      }
    } // end loop on current tile entity
    return foundQty;
  }
  public static BlockPos findClosestBlock(EntityPlayer player, Block blockHunt, int RADIUS) {
    BlockPos found = null;
    int xMin = (int) player.posX - RADIUS;
    int xMax = (int) player.posX + RADIUS;
    int yMin = (int) player.posY - RADIUS;
    int yMax = (int) player.posY + RADIUS;
    int zMin = (int) player.posZ - RADIUS;
    int zMax = (int) player.posZ + RADIUS;
    int distance = 0, distanceClosest = RADIUS * RADIUS;
    BlockPos posCurrent = null;
    for (int xLoop = xMin; xLoop <= xMax; xLoop++) {
      for (int yLoop = yMin; yLoop <= yMax; yLoop++) {
        for (int zLoop = zMin; zLoop <= zMax; zLoop++) {
          posCurrent = new BlockPos(xLoop, yLoop, zLoop);
          if (player.worldObj.getBlockState(posCurrent).getBlock().equals(blockHunt)) {
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
  public static ArrayList<BlockPos> findBlocks(World world, BlockPos start, Block blockHunt, int RADIUS) {
    ArrayList<BlockPos> found = new ArrayList<BlockPos>();
    int xMin = (int) start.getX() - RADIUS;
    int xMax = (int) start.getX() + RADIUS;
    int yMin = (int) start.getY() - RADIUS;
    int yMax = (int) start.getY() + RADIUS;
    int zMin = (int) start.getZ() - RADIUS;
    int zMax = (int) start.getZ() + RADIUS;
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
  public static double distanceBetweenHorizontal(BlockPos start, BlockPos end) {
    int xDistance = Math.abs(start.getX() - end.getX());
    int zDistance = Math.abs(start.getZ() - end.getZ());
    // ye olde pythagoras
    return Math.sqrt(xDistance * xDistance + zDistance * zDistance);
  }
  public static double distanceBetweenVertical(BlockPos start, BlockPos end) {
    return Math.abs(start.getY() - end.getY());
  }
}
