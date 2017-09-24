package com.lothrazar.cyclicmagic.util;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import javax.annotation.Nullable;
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.registry.PermissionRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockStone;
import net.minecraft.block.BlockStone.EnumType;
import net.minecraft.block.BlockStoneBrick;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class UtilPlaceBlocks {
  public static boolean placeStateDetsroy(World world, @Nullable EntityPlayer player, BlockPos placePos, IBlockState placeState, boolean dropBlock) {
    if (world.destroyBlock(placePos, dropBlock)) {
      return placeStateSafe(world, player, placePos, placeState);
    }
    return false;
  }
  /*
   * TODO: SHOULD every call to this be in a scheduled task?
   * https://github.com/PrinceOfAmber/Cyclic/issues/143
   */
  public static boolean placeStateOverwrite(World world, @Nullable EntityPlayer player, BlockPos placePos, IBlockState placeState) {
    if (world.setBlockToAir(placePos)) {
      return placeStateSafe(world, player, placePos, placeState);
    }
    return false;
  }
  // from spell range build
  public static boolean placeStateSafe(World world, @Nullable EntityPlayer player, BlockPos placePos, IBlockState placeState) {
    if (placePos == null) {
      return false;
    }
    IBlockState stateHere = null;
    if (player != null && PermissionRegistry.hasPermissionHere(player, placePos) == false) {
      return false;
    }
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
          if (!world.isRemote) {
            world.destroyBlock(placePos, dropBlock);
          }
        }
      }
    }
    boolean success = false;
    try {
      // as soon as i added the try catch, it started never (rarely) happening
      // we used to pass a flag as third argument, such as '2'
      // default is '3'
      // if (!world.isRemote) {
      if (placeState.getBlock() instanceof BlockLeaves) {//dont let them decay
        placeState = placeState.withProperty(BlockLeaves.DECAYABLE, false);
      }
      success = world.setBlockState(placePos, placeState, 3);
      // }
      // else {//this often gets called from only serverside, but not always (structurebuilder)
      if (success) {
        UtilSound.playSoundPlaceBlock(world, placePos, placeState.getBlock());
      }
      // }
      world.markBlockRangeForRenderUpdate(placePos, placePos.up());
      world.markChunkDirty(placePos, null);
    }
    catch (ConcurrentModificationException e) {
      ModCyclic.logger.error("ConcurrentModificationException");
      ModCyclic.logger.error(e.getMessage());// message is null??
      ModCyclic.logger.error(e.getStackTrace().toString());
      success = false;
    }
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
    if (newStateToPlace == null || ignoreList.contains(newStateToPlace.getBlock())) {
      return false;
    }
    //negative hardness: unbreakable like bedrock
    //if (newStateToPlace.getBlock().getBlockHardness(newStateToPlace, world, posMoveToHere) == -1) { 
    if (newStateToPlace.getBlockHardness(world, posMoveToHere) == -1) {
      return false;
    }
    boolean moved = false;
    if (world.isAirBlock(posMoveToHere) && world.isBlockModifiable(player, pos)) {
      //copy tile if exists
      TileEntity tile = world.getTileEntity(pos);
      NBTTagCompound tileData = null;
      if (tile != null) {
        tileData = new NBTTagCompound();
        tile.writeToNBT(tileData);
      }
      //break current location
      destroyBlock(world, pos);
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
  public static void destroyBlock(World world, BlockPos pos) {
    if (world.getTileEntity(pos) != null) {
      world.removeTileEntity(pos);
    }
    world.setBlockToAir(pos);
    world.markChunkDirty(pos, null);//dont forget to update the old pos as well as the new position for server sync
    // IN CASE OF DOUBLE CHESTS
    tryUpdateNeighbour(world, pos.north());
    tryUpdateNeighbour(world, pos.south());
    tryUpdateNeighbour(world, pos.east());
    tryUpdateNeighbour(world, pos.west());
  }
  public static void tryUpdateNeighbour(World world, BlockPos pos) {
    // https://github.com/PrinceOfAmber/Cyclic/issues/119
    //in case its a linked tile entity // double chest, make sure we pass updates along
    TileEntity tile = world.getTileEntity(pos);
    if (tile != null) {
      tile.updateContainingBlockInfo();
      tile.markDirty();
    }
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
  //(worldObj, player, message.pos, message.side);
  @SuppressWarnings({ "rawtypes", "unchecked" })
  public static boolean rotateBlockValidState(World worldObj, @Nullable EntityPlayer p, BlockPos pos, EnumFacing side) {
    if (pos == null || worldObj.getBlockState(pos) == null || side == null) {
      return false;
    }
    IBlockState clicked = worldObj.getBlockState(pos);
    if (clicked.getBlock() == null) {
      return false;
    }
    Block clickedBlock = clicked.getBlock();
    //avoiding using the integer values of properties
    //int clickedMeta = clickedBlock.getMetaFromState(clicked);
    //the built in function doues the properties: ("facing")|| ("rotation")
    // for example, BlockMushroom.rotateBlock uses this, and hay bales
    boolean isDone = clickedBlock.rotateBlock(worldObj, pos, side);
    if (isDone) {
      //rotateBlock does not have any sounds attached, so add our own
      if (p != null)
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
        placeState = UtilItemStack.getStateFromMeta(Blocks.STONEBRICK, BlockStoneBrick.CHISELED_META);
      }
      else if (Blocks.STONEBRICK.getMetaFromState(clicked) == BlockStoneBrick.CHISELED_META) {
        placeState = Blocks.STONE.getDefaultState();
      }
    }
    //ENDBRICK
    if (placeState != null) {
      isDone = UtilPlaceBlocks.placeStateOverwrite(worldObj, p, pos, placeState);
    }
    if (isDone) {
      return true;
    }
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
