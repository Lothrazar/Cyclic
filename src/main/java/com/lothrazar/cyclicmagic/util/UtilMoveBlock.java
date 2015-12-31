package com.lothrazar.cyclicmagic.util;

import java.util.ArrayList;
import com.lothrazar.cyclicmagic.Const;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class UtilMoveBlock {
	public static ArrayList<Block> ignoreList = new ArrayList<Block>();

	private static void translateCSV() {
		// TODO: FROM CONFIG...somehow

		if (ignoreList.size() == 0) {

			ignoreList.add(Blocks.end_portal_frame);
			ignoreList.add(Blocks.end_portal);
			ignoreList.add(Blocks.portal);
			ignoreList.add(Blocks.bed);
			ignoreList.add(Blocks.dark_oak_door);
			ignoreList.add(Blocks.acacia_door);
			ignoreList.add(Blocks.birch_door);
			ignoreList.add(Blocks.oak_door);
			ignoreList.add(Blocks.spruce_door);
			ignoreList.add(Blocks.jungle_door);
			ignoreList.add(Blocks.iron_door);
			ignoreList.add(Blocks.skull);
		}
	}

	public static boolean moveBlockTo(World world, EntityPlayer player, BlockPos pos, BlockPos posMoveToHere) {
		IBlockState hit = world.getBlockState(pos);
		translateCSV();

		if (hit == null || ignoreList.contains(hit.getBlock())) {
			return false;
		}
		if (hit.getBlock().getBlockHardness(world, posMoveToHere) == -1) {
			return false;// unbreakable like bedrock
		}

		if (world.isAirBlock(posMoveToHere) && world.isBlockModifiable(player, pos)) {

			if (world.isRemote == false) {

				world.destroyBlock(pos, false);
			}
			world.setBlockState(posMoveToHere, hit, Const.NOTIFY);

			world.markBlockForUpdate(posMoveToHere);

			return true;
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
}