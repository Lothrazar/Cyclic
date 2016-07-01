package com.lothrazar.cyclicmagic.util;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.ConcurrentModificationException;
import java.util.List;
import com.lothrazar.cyclicmagic.ModMain;
import net.minecraft.block.Block;
import net.minecraft.block.BlockStone;
import net.minecraft.block.BlockStoneBrick;
import net.minecraft.block.BlockStone.EnumType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class UtilPlaceBlocks {
  public static List<BlockPos> repeatShapeByHeight(List<BlockPos> shape, int height) {
    List<BlockPos> newShape = new ArrayList<BlockPos>();
    newShape.addAll(shape);//copy it
    for (int i = 1; i <= height; i++)
      for (BlockPos p : shape) {
        newShape.add(p.up(i));
      }
    return newShape;
  }
  public static List<BlockPos> circle(BlockPos pos, int diameter) {
    int centerX = pos.getX();
    int centerZ = pos.getZ();
    int height = (int) pos.getY();
    int radius = diameter / 2;
    int z = radius;
    int x = 0;
    int d = 2 - (2 * radius);//dont use Diameter again, for integer roundoff
    List<BlockPos> circleList = new ArrayList<BlockPos>();
    do {
      circleList.add(new BlockPos(centerX + x, height, centerZ + z));
      circleList.add(new BlockPos(centerX + x, height, centerZ - z));
      circleList.add(new BlockPos(centerX - x, height, centerZ + z));
      circleList.add(new BlockPos(centerX - x, height, centerZ - z));
      circleList.add(new BlockPos(centerX + z, height, centerZ + x));
      circleList.add(new BlockPos(centerX + z, height, centerZ - x));
      circleList.add(new BlockPos(centerX - z, height, centerZ + x));
      circleList.add(new BlockPos(centerX - z, height, centerZ - x));
      if (d < 0) {
        d = d + (4 * x) + 6;
      }
      else {
        d = d + 4 * (x - z) + 10;
        z--;
      }
      x++;
    }
    while (x <= z);
    Collections.sort(circleList, new Comparator<BlockPos>() {
      @Override
      public int compare(final BlockPos object1, final BlockPos object2) {
        return (int) object1.getX() - object2.getX();
      }
    });
    return circleList;
  }
  public static List<BlockPos> squareHorizontalHollow(final BlockPos pos, int radius) {
    List<BlockPos> shape = new ArrayList<BlockPos>();
    // search in a cube
    int xMin = pos.getX() - radius;
    int xMax = pos.getX() + radius;
    int zMin = pos.getZ() - radius;
    int zMax = pos.getZ() + radius;
    int y = pos.getY();
    //first, leave x fixed and track along +/- y
    for (int x = xMin; x <= xMax; x++) {
      shape.add(new BlockPos(x, y, zMin));
      shape.add(new BlockPos(x, y, zMax));
    }
    //corners are done so offset
    for (int z = zMin + 1; z < zMax; z++) {
      shape.add(new BlockPos(xMin, y, z));
      shape.add(new BlockPos(xMax, y, z));
    }
    //		for (int x = xMin; x <= xMax; x++) {
    //			for (int z = zMin; z <= zMax; z++) {
    //				shape.add(new BlockPos(x, y, z));
    //			}
    //		} // end of the outer loop
    return shape;
  }
  public static List<BlockPos> stairway(BlockPos position, EnumFacing pfacing, int want, boolean isLookingUp) {
    List<BlockPos> shape = new ArrayList<BlockPos>();
    BlockPos posCurrent = position.down().offset(pfacing);
    boolean goVert = false;
    for (int i = 1; i < want + 1; i++) {
      if (goVert) {
        if (isLookingUp)
          posCurrent = posCurrent.up();
        else
          posCurrent = posCurrent.down();
      }
      else {
        posCurrent = posCurrent.offset(pfacing);
      }
      shape.add(posCurrent);
      goVert = (i % 2 == 0);// alternate between going forward vertical
    }
    return shape;
  }
  public static List<BlockPos> line(BlockPos pos, EnumFacing efacing, int want) {
    List<BlockPos> shape = new ArrayList<BlockPos>();
    int skip = 1;
    for (int i = 1; i < want + 1; i = i + skip) {
      shape.add(pos.offset(efacing, i));
    }
    return shape;
  }
  public static boolean placeStateDetsroy(World world, EntityPlayer player, BlockPos placePos, IBlockState placeState, boolean dropBlock) {
    if (world.destroyBlock(placePos, dropBlock)) { return placeStateSafe(world, player, placePos, placeState); }
    return false;
  }
  public static boolean placeStateOverwrite(World world, EntityPlayer player, BlockPos placePos, IBlockState placeState) {
    if (world.setBlockToAir(placePos)) { return placeStateSafe(world, player, placePos, placeState); }
    return false;
  }
  // from spell range build
  public static boolean placeStateSafe(World world, EntityPlayer player, BlockPos placePos, IBlockState placeState) {
    if (placePos == null) { return false; }
    IBlockState stateHere = null;
    if (world.isAirBlock(placePos) == false) {
      // if there is a block here, we might have to stop
      stateHere = world.getBlockState(placePos);
      if (stateHere != null) {
        Block blockHere = stateHere.getBlock();
        if (blockHere.isReplaceable(world, placePos) == false) {
          // for example, torches, and the top half of a slab if you click
          // in the empty space
          return false;
        }
        // ok its a soft block so try to break it first try to destroy it
        // unless it is liquid, don't try to destroy liquid
        //blockHere.getMaterial(stateHere)
        if (stateHere.getMaterial() != Material.WATER && stateHere.getMaterial() != Material.LAVA) {
          boolean dropBlock = true;
          world.destroyBlock(placePos, dropBlock);
        }
      }
    }
    boolean success = false;
    try {
      // as soon as i added the try catch, it started never (rarely) happening
      // we used to pass a flag as third argument, such as '2'
      // default is '3'
      success = world.setBlockState(placePos, placeState);
      // world.markBlockForUpdate(posMoveToHere);
    }
    catch (ConcurrentModificationException e) {
      ModMain.logger.warn("ConcurrentModificationException");
      ModMain.logger.warn(e.getMessage());// message is null??
      ModMain.logger.warn(e.getStackTrace().toString());
      success = false;
    }
    if (success) {
      if (player != null) {
        UtilSound.playSoundPlaceBlock(player, placePos, placeState.getBlock());
      }
      else {
        //, SoundCategory.BLOCKS
        //isremote seems to always be false here. so playing sounds on server
        UtilSound.playSound(world, placePos, placeState.getBlock().getSoundType().getPlaceSound(), SoundCategory.BLOCKS);
      }
    }
    // either it was air, or it wasnt and we broke it
    return success;
  }
  public static ArrayList<Block> ignoreList = new ArrayList<Block>();

  private static void translateCSV() {
    if (ignoreList.size() == 0) {
      ignoreList.add(Blocks.END_PORTAL_FRAME);
      ignoreList.add(Blocks.END_PORTAL);
      ignoreList.add(Blocks.PORTAL);
      ignoreList.add(Blocks.BED);
      ignoreList.add(Blocks.DARK_OAK_DOOR);
      ignoreList.add(Blocks.ACACIA_DOOR);
      ignoreList.add(Blocks.BIRCH_DOOR);
      ignoreList.add(Blocks.OAK_DOOR);
      ignoreList.add(Blocks.SPRUCE_DOOR);
      ignoreList.add(Blocks.JUNGLE_DOOR);
      ignoreList.add(Blocks.IRON_DOOR);
      ignoreList.add(Blocks.SKULL);
      ignoreList.add(Blocks.DOUBLE_PLANT);
    }
  }
  public static boolean moveBlockTo(World world, EntityPlayer player, BlockPos pos, BlockPos posMoveToHere) {
    IBlockState newStateToPlace = world.getBlockState(pos);
    translateCSV();
    if (newStateToPlace == null || ignoreList.contains(newStateToPlace.getBlock())) { return false; }
    //negative hardness: unbreakable like bedrock
    //if (newStateToPlace.getBlock().getBlockHardness(newStateToPlace, world, posMoveToHere) == -1) { 
    if (newStateToPlace.getBlockHardness(world, posMoveToHere) == -1) { return false; }
    boolean moved = false;
    if (world.isAirBlock(posMoveToHere) && world.isBlockModifiable(player, pos)) {
      //start of break
      //moving tile entitys, like chests
      TileEntity tile = world.getTileEntity(pos);
      NBTTagCompound tileData = null;
      if (tile != null) {
        tileData = new NBTTagCompound();
        tile.writeToNBT(tileData);
        world.removeTileEntity(pos);
      }
      world.setBlockToAir(pos);
      //end of break
      //start move
      moved = UtilPlaceBlocks.placeStateSafe(world, player, posMoveToHere, newStateToPlace);
      if (moved) {
        if (tileData != null) {
          TileEntity newTile = world.getTileEntity(posMoveToHere);
          //thanks for the tip on setting tile entity data from nbt tag: https://github.com/romelo333/notenoughwands1.8.8/blob/master/src/main/java/romelo333/notenoughwands/Items/DisplacementWand.java
          if (newTile != null) {
            tileData.setInteger("x", posMoveToHere.getX());
            tileData.setInteger("y", posMoveToHere.getY());
            tileData.setInteger("z", posMoveToHere.getZ());
            newTile.readFromNBT(tileData);
            newTile.markDirty();
            //  world.markBlockForUpdate(posMoveToHere);
            world.markChunkDirty(posMoveToHere, newTile);
          }
        }
      }
      //end move
    }
    return moved;
  }
  /**
   * wrap moveBlockTo but detect the destination based on the side hit
   * 
   * @param worldIn
   * @param player
   * @param pos
   * @param face
   */
  public static BlockPos pullBlock(World worldIn, EntityPlayer player, BlockPos pos, EnumFacing face) {
    BlockPos posTowardsPlayer = pos.offset(face);
    if (moveBlockTo(worldIn, player, pos, posTowardsPlayer)) {
      return posTowardsPlayer;
    }
    else {
      return null;
    }
  }
  public static BlockPos pushBlock(World worldIn, EntityPlayer player, BlockPos pos, EnumFacing face) {
    BlockPos posAwayPlayer = pos.offset(face.getOpposite());
    if (moveBlockTo(worldIn, player, pos, posAwayPlayer)) {
      return posAwayPlayer;
    }
    else {
      return null;
    }
  }
  @SuppressWarnings({ "rawtypes", "unchecked" })
  public static boolean rotateBlockValidState(BlockPos pos, World worldObj, EnumFacing side, EntityPlayer p) {
    if (pos == null || worldObj.getBlockState(pos) == null || side == null) { return false; }
    IBlockState clicked = worldObj.getBlockState(pos);
    if (clicked.getBlock() == null) { return false; }
    Block clickedBlock = clicked.getBlock();
    //avoiding using the integer values of properties
    //int clickedMeta = clickedBlock.getMetaFromState(clicked);
    //the built in function doues the properties: ("facing")|| ("rotation")
    // for example, BlockMushroom.rotateBlock uses this, and hay bales
    boolean isDone = clickedBlock.rotateBlock(worldObj, pos, side);
    if (isDone) {
      //rotateBlock does not have any sounds attached, so add our own
      UtilSound.playSoundPlaceBlock(p, pos, clickedBlock);
      return true;
    }
    //first handle any special cases 
    IBlockState placeState = null;
    if (clickedBlock == Blocks.STONE) {
      EnumType variant = clicked.getValue(BlockStone.VARIANT);//.getProperties().get(BlockStone.VARIANT);
      //basically we want to toggle the "smooth" property on and off
      //but there is no property 'smooth' its just within the variant
      switch (variant) {
      case ANDESITE:
        placeState = clicked.withProperty(BlockStone.VARIANT, EnumType.ANDESITE_SMOOTH);
        break;
      case ANDESITE_SMOOTH:
        placeState = clicked.withProperty(BlockStone.VARIANT, EnumType.ANDESITE);
        break;
      case DIORITE:
        placeState = clicked.withProperty(BlockStone.VARIANT, EnumType.DIORITE_SMOOTH);
        break;
      case DIORITE_SMOOTH:
        placeState = clicked.withProperty(BlockStone.VARIANT, EnumType.DIORITE);
        break;
      case GRANITE:
        placeState = clicked.withProperty(BlockStone.VARIANT, EnumType.GRANITE_SMOOTH);
        break;
      case GRANITE_SMOOTH:
        placeState = clicked.withProperty(BlockStone.VARIANT, EnumType.GRANITE);
        break;
      case STONE:
        placeState = Blocks.STONEBRICK.getDefaultState();
      default:
        break;
      }
    }
    else if (clickedBlock == Blocks.STONEBRICK) {
      //basically we rotate variant, buut not all of them. and smoothstone is a different block
      if (Blocks.STONEBRICK.getMetaFromState(clicked) == BlockStoneBrick.DEFAULT_META) {
        placeState = Blocks.STONEBRICK.getStateFromMeta(BlockStoneBrick.CHISELED_META);
      }
      else if (Blocks.STONEBRICK.getMetaFromState(clicked) == BlockStoneBrick.CHISELED_META) {
        placeState = Blocks.STONE.getDefaultState();
      }
    }
    //ENDBRICK
    if (placeState != null) {
      isDone = UtilPlaceBlocks.placeStateOverwrite(worldObj, p, pos, placeState);
    }
    if (isDone) { return true; }
    //now try something else if not done
    for (IProperty prop : (com.google.common.collect.ImmutableSet<IProperty<?>>) clicked.getProperties().keySet()) {
      if (isDone) {
        break;//stop looping right away if we are done
      }
      //variant is commonly used, but we dont want to rotate on it
      if (prop.getName().equals("half")) {
        //also exists as object in BlockSlab.HALF
        isDone = UtilPlaceBlocks.placeStateOverwrite(worldObj, p, pos, clicked.cycleProperty(prop));
      }
      else if (prop.getName().equals("seamless")) {
        //http://minecraft.gamepedia.com/Slab#Block_state
        isDone = UtilPlaceBlocks.placeStateOverwrite(worldObj, p, pos, clicked.cycleProperty(prop));
      }
      else if (prop.getName().equals("axis")) {
        //i dont remember what blocks use this. rotateBlock might cover it in some cases
        isDone = UtilPlaceBlocks.placeStateOverwrite(worldObj, p, pos, clicked.cycleProperty(prop));
      }
      else if (prop.getName().equals("type") && (clickedBlock == Blocks.SANDSTONE || clickedBlock == Blocks.RED_SANDSTONE)) {
        isDone = UtilPlaceBlocks.placeStateOverwrite(worldObj, p, pos, clicked.cycleProperty(prop));
      }
    }
    return isDone;
  }
}
