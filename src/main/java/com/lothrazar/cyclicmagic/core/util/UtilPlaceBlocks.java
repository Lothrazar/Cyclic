/*******************************************************************************
 * The MIT License (MIT)
 * 
 * Copyright (C) 2014-2018 Sam Bassett (aka Lothrazar)
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 ******************************************************************************/
package com.lothrazar.cyclicmagic.core.util;

import java.util.ArrayList;
import javax.annotation.Nullable;
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.registry.PermissionRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockStone;
import net.minecraft.block.BlockStone.EnumType;
import net.minecraft.block.BlockStoneBrick;
import net.minecraft.block.SoundType;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class UtilPlaceBlocks {

  public static boolean placeStateDestroy(World world, @Nullable EntityPlayer player, BlockPos placePos, IBlockState placeState, boolean dropBlock) {
    if (world.destroyBlock(placePos, dropBlock)) {
      return placeStateSafe(world, player, placePos, placeState);
    }
    return false;
  }

  /* TODO: SHOULD every call to this be in a scheduled task? https://github.com/PrinceOfAmber/Cyclic/issues/143 */
  public static boolean placeStateOverwrite(World world, @Nullable EntityPlayer player, BlockPos placePos, IBlockState placeState) {
    if (world.setBlockToAir(placePos)) {
      return placeStateSafe(world, player, placePos, placeState);
    }
    return false;
  }

  /**
   * overloaded version to disable sound
   */
  public static boolean placeStateSafe(World world, @Nullable EntityPlayer player,
      BlockPos placePos, IBlockState placeState) {
    return placeStateSafe(world, player, placePos, placeState, false);
  }

  /**
   * Thanks ot ItemBlock inspiration and reminder from betterwithmods https://github.com/BetterWithMods/BetterWithMods/issues/940
   * 
   * TODO: down the road we want to re-use this with fake-player blocks.
   * 
   * also non integer x/y/z implementations are possible
   * 
   * 
   * @param world
   * @param placePos
   * @param stack
   * @return
   */
  public static boolean placeItemblock(World world, BlockPos placePos, ItemStack stack) {
    ItemBlock itemblock = (ItemBlock) stack.getItem();
    EntityPlayer fake = null;
    //client only hmm || itemblock.canPlaceBlockOnSide(world, placePos.down(), EnumFacing.DOWN, fake, stack)
    if (world.isAirBlock(placePos)) {
      Block block = itemblock.getBlock();
      //        boolean blockAcross = ;
      IBlockState state = block.getStateForPlacement(world, placePos, EnumFacing.DOWN, placePos.getX(), placePos.getY(), placePos.getZ(),
          stack.getItemDamage(), fake, EnumHand.MAIN_HAND);
      if (block.canPlaceBlockAt(world, placePos)) {
        if (itemblock.placeBlockAt(stack, fake, world, placePos, EnumFacing.DOWN, placePos.getX(), placePos.getY(), placePos.getZ(),
            state)) {
          world.playSound(null, placePos, state.getBlock().getSoundType(state, world, placePos, fake).getPlaceSound(), SoundCategory.BLOCKS, 0.7F, 1.0F);
          stack.shrink(1);
          return true;// stack.isEmpty() ? ItemStack.EMPTY : stack;
        }
      }
    }
    return false;
  }

  /**
   * This will return true only if world.setBlockState(..) returns true or if the block here is already identical
   * 
   * @param world
   * @param player
   * @param placePos
   * @param placeState
   * @param playSound
   * @return
   */
  public static boolean placeStateSafe(World world, @Nullable EntityPlayer player, BlockPos placePos, IBlockState placeState, boolean playSound) {
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
        // ok its a soft (isReplaceable == true) block so try to break it first try to destroy it
        // unless it is liquid, don't try to destroy liquid
        //blockHere.getMaterial(stateHere)
        if (stateHere.getMaterial().isLiquid() == false) {
          boolean dropBlock = true;
          if (world.isRemote == false) {
            world.destroyBlock(placePos, dropBlock);
          }
        }
      }
    }
    if (placeState.getBlock() instanceof BlockLeaves) { //dont let them decay
      placeState = placeState.withProperty(BlockLeaves.DECAYABLE, false);
    }
    boolean success = false;
    try {
      // flags specifies what to update, '3' means notify client & neighbors
      // isRemote to make sure we are in a server thread
      if (world.isRemote == false) {
        success = world.setBlockState(placePos, placeState, 3); // returns false when placement failed
      }
    }
    catch (Exception e) {
      // PR for context https://github.com/PrinceOfAmber/Cyclic/pull/577/files
      // and  https://github.com/PrinceOfAmber/Cyclic/pull/579/files
      // show exception from above, possibly failed placement
      ModCyclic.logger.error("Error attempting to place block ");
      e.printStackTrace();
    }
    // play sound to area when placement is a success
    if (success && playSound) {
      SoundType type = UtilSound.getSoundFromBlockstate(placeState, world, placePos);
      if (type != null && type.getPlaceSound() != null) {
        UtilSound.playSoundFromServer(type.getPlaceSound(), SoundCategory.BLOCKS, placePos, world.provider.getDimension(), UtilSound.RANGE_DEFAULT);
      }
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
    try {
      boolean setToAirSuccess = world.setBlockToAir(pos);
      if (setToAirSuccess == false) {
        setToAirSuccess = world.destroyBlock(pos, false);//destroy with no drops if setToAir failed
      }
    }
    catch (Exception e) {
      ModCyclic.logger.error("Error thrown by a tile entity when removing the block: " + e.getMessage());
      e.printStackTrace();
    }
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
    for (IProperty prop : clicked.getProperties().keySet()) {
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
