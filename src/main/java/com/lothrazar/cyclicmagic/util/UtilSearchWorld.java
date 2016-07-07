package com.lothrazar.cyclicmagic.util;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import com.lothrazar.cyclicmagic.ModMain;
import net.minecraft.block.Block;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class UtilSearchWorld {
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
