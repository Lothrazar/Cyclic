package com.lothrazar.cyclicmagic.util;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;

import com.lothrazar.cyclicmagic.ModMain;
import com.lothrazar.cyclicmagic.gui.wand.InventoryWand;
import com.lothrazar.cyclicmagic.item.ItemCyclicWand;

import net.minecraft.block.Block;
import net.minecraft.block.BlockStone;
import net.minecraft.block.BlockStone.EnumType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class UtilPlaceBlocks {

	public static void circle(World world, EntityPlayer player, ItemStack heldWand, BlockPos pos) {
		int diameter = ItemCyclicWand.BuildType.getBuildSize(heldWand);
		// based on
		// http://stackoverflow.com/questions/1022178/how-to-make-a-circle-on-a-grid
		// also http://rosettacode.org/wiki/Bitmap/Midpoint_circle_algorithm

		int centerX = pos.getX();
		int centerZ = pos.getZ();

		int height = (int) pos.getY();

		int radius = diameter / 2;

		int z = radius;
		int x = 0;
		int d = 2 - (2*radius);//dont use Diameter again, for integer roundoff

		ArrayList<BlockPos> circleList = new ArrayList<BlockPos>();

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
		} while (x <= z);

		int itemSlot;
		IBlockState state;
		for (BlockPos posCurrent : circleList) {
			itemSlot = InventoryWand.getSlotByBuildType(heldWand, null);
			state = InventoryWand.getToPlaceFromSlot(heldWand, itemSlot);

			if (state == null) { return;	}// then inventory is completely empty
		
			placeAndDecrementFromWand(world, player, heldWand, itemSlot, posCurrent, state);
		}
	}

	public static void square(World world, EntityPlayer player, ItemStack heldWand, BlockPos pos, int radius) {
		// search in a cube
		int xMin = pos.getX() - radius;
		int xMax = pos.getX() + radius;
		int zMin = pos.getZ() - radius;
		int zMax = pos.getZ() + radius;

		int y = pos.getY();

		BlockPos posCurrent;

		int itemSlot;
		IBlockState state;
		for (int x = xMin; x <= xMax; x++) {
			for (int z = zMin; z <= zMax; z++) {
				itemSlot = InventoryWand.getSlotByBuildType(heldWand, null);
				state = InventoryWand.getToPlaceFromSlot(heldWand, itemSlot);

				if (state == null) { return;	}// then inventory is completely empty
			
				posCurrent = new BlockPos(x, y, z);

				if (world.isAirBlock(posCurrent) == false) {
					continue;
				}

				placeAndDecrementFromWand(world, player, heldWand, itemSlot, posCurrent, state);

			}
		} // end of the outer loop

	}

	public static void stairway(World world, EntityPlayer player, ItemStack heldWand, BlockPos position) {
		int want = ItemCyclicWand.BuildType.getBuildSize(heldWand);
		boolean isLookingUp = (player.getLookVec().yCoord >= 0);// TODO: use this
		                                                        // somehow? to place
		                                                        // up/down?

		boolean goVert = true;

		EnumFacing pfacing = UtilEntity.getPlayerFacing(player);

		// it starts at eye level, so do down and forward one first
		BlockPos posCurrent = player.getPosition().down().offset(pfacing);

		int itemSlot;
		IBlockState state;
		for (int i = 1; i < want + 1; i++) {

			itemSlot = InventoryWand.getSlotByBuildType(heldWand, null);
			state = InventoryWand.getToPlaceFromSlot(heldWand, itemSlot);

			if (state == null) { return;	}// then inventory is completely empty
		

			if (goVert) {
				if (isLookingUp)
					posCurrent = posCurrent.up();
				else
					posCurrent = posCurrent.down();
			}
			else {
				posCurrent = posCurrent.offset(pfacing);
			}
			goVert = (i % 2 == 0);// alternate between going forward and going
			                      // vertical

			placeAndDecrementFromWand(world, player, heldWand, itemSlot, posCurrent, state);
		}
	}

	public static void line(World world, EntityPlayer player, ItemStack heldWand, BlockPos pos, EnumFacing efacing) {
		int want = ItemCyclicWand.BuildType.getBuildSize(heldWand);
		int skip = 1;

		BlockPos posCurrent;

		int itemSlot;
		IBlockState state;
		for (int i = 1; i < want + 1; i = i + skip) {
			posCurrent = pos.offset(efacing, i);

			itemSlot = InventoryWand.getSlotByBuildType(heldWand, null);
			state = InventoryWand.getToPlaceFromSlot(heldWand, itemSlot);

			if (state == null) { return;	}// then inventory is completely empty
		

			placeAndDecrementFromWand(world, player, heldWand, itemSlot, posCurrent, state);

		}
	}

	// from command place blocks
	private static boolean placeAndDecrementFromWand(World world, EntityPlayer player, ItemStack heldWand, int itemSlot, BlockPos posCurrent, IBlockState placing) {
		boolean success = placeStateSafe(world, player, posCurrent, placing);

		if (success) {

			if (player.capabilities.isCreativeMode == false) {
				// player.inventory.decrStackSize(player.inventory.currentItem, 1);
				InventoryWand.decrementSlot(heldWand, itemSlot);
			}
		}

		return success;
	}

	public static boolean placeStateDetsroy(World world, EntityPlayer player, BlockPos placePos, IBlockState placeState,boolean dropBlock) {
		
		if(world.destroyBlock(placePos, dropBlock)){
			return placeStateSafe( world,  player,  placePos,  placeState);
		}
		return false;
	}
	public static boolean placeStateOverwrite(World world, EntityPlayer player, BlockPos placePos, IBlockState placeState) {
		
		if(world.setBlockToAir(placePos)){
			return placeStateSafe( world,  player,  placePos,  placeState);
		}
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
		} catch (ConcurrentModificationException e) {
			
			
			ModMain.logger.warn("ConcurrentModificationException");
			ModMain.logger.warn(e.getMessage());// message is null??
			ModMain.logger.warn(e.getStackTrace().toString());
			success = false;
		}
 
		 if(success){
				
			UtilSound.playSoundPlaceBlock(player ,placePos,placeState.getBlock());
		
		 }
	 
		// either it was air, or it wasnt and we broke it
		return success;
	}

	public static ArrayList<Block> ignoreList = new ArrayList<Block>();
	private static boolean ignoreTileEntities=true;

	private static void translateCSV() {

		// TODO: FROM CONFIG...somehow

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
		if (newStateToPlace.getBlockHardness(world, posMoveToHere) == -1) { 
			return false;
		}


		if(world.getTileEntity(pos) != null && ignoreTileEntities){
			return false;
		}
		
		if (world.isAirBlock(posMoveToHere) && world.isBlockModifiable(player, pos)) {

			if (world.isRemote == false) {

				world.destroyBlock(pos, false);
			}

			return UtilPlaceBlocks.placeStateSafe(world, player, posMoveToHere, newStateToPlace);
		}
		else
			return false;
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
	public static boolean rotateBlockValidState(BlockPos pos,World worldObj, EnumFacing side, EntityPlayer p){

		if (pos == null || worldObj.getBlockState(pos) == null || side == null) { return false; }

		IBlockState clicked = worldObj.getBlockState(pos);
		if (clicked.getBlock() == null) { return false; }
		Block clickedBlock = clicked.getBlock();
		
		//avoiding using the integer values of properties
		//int clickedMeta = clickedBlock.getMetaFromState(clicked);

		//the built in function doues the properties: ("facing")|| ("rotation")
		// for example, BlockMushroom.rotateBlock uses this, and hay bales
		boolean isDone = clickedBlock.rotateBlock(worldObj, pos, side);

		if(isDone){
			//rotateBlock does not have any sounds attached, so add our own
			UtilSound.playSoundPlaceBlock(p ,pos, clickedBlock);
			return true;
		}
		

		//first handle any special cases : currently just for stone
		if(clickedBlock == Blocks.STONE){

			EnumType variant = clicked.getValue(BlockStone.VARIANT);//.getProperties().get(BlockStone.VARIANT);
			//basically we want to toggle the "smooth" property on and off
			//but there is no property 'smooth' its just within the variant
			IBlockState placeState = null;
			switch(variant){
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
			default:
				break;
			}
			if(placeState != null){
				isDone = UtilPlaceBlocks.placeStateOverwrite(worldObj, p, pos, placeState);
			}
		}//end special case for 'stone'
		
		if(isDone){
			return true; 
		}
			//now try something else if not done
		
		for (IProperty prop : (com.google.common.collect.ImmutableSet<IProperty<?>>) clicked.getProperties().keySet()) {
			
			if(isDone){
				break;//stop looping right away if we are done
			}
			
			if (prop.getName().equals("half")) {
				//also exists as object in BlockSlab.HALF
				isDone = UtilPlaceBlocks.placeStateOverwrite(worldObj, p, pos,  clicked.cycleProperty(prop));
			
			}
			else if (prop.getName().equals("seamless")) {
				//http://minecraft.gamepedia.com/Slab#Block_state
				
				isDone = UtilPlaceBlocks.placeStateOverwrite(worldObj, p, pos,  clicked.cycleProperty(prop));
			}
			else if (prop.getName().equals("axis")) {
				//i dont remember what blocks use this. rotateBlock might cover it in some cases
				
				isDone = UtilPlaceBlocks.placeStateOverwrite(worldObj, p, pos,  clicked.cycleProperty(prop));
			}
		}
		
		return isDone;
	}
}
