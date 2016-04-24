package com.lothrazar.cyclicmagic.util;

import com.lothrazar.cyclicmagic.ModMain;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.block.BlockMushroom;
import net.minecraft.block.BlockSapling;
import net.minecraft.block.BlockStem;
import net.minecraft.block.BlockTallGrass;
import net.minecraft.block.IGrowable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class UtilHarvestCrops {

	public static boolean doesHarvestStem = false;
	public static boolean doesHarvestTallgrass = true;
	public static boolean doesHarvestSapling = false;
	public static boolean doesHarvestMushroom = true;
	public static boolean doesPumpkinBlocks = true;
	public static boolean doesMelonBlocks = true;

	// flower
	public static int harvestArea(World world, EntityLivingBase player, BlockPos pos, int radius) {

		int x = (int) player.posX;
		// int y = (int)player.posY;
		int z = (int) player.posZ;

		// search in a cube
		int xMin = x - radius;
		int xMax = x + radius;
		int zMin = z - radius;
		int zMax = z + radius;

		int eventy = pos.getY();

		BlockPos posCurrent;

		IBlockState bs;
		IBlockState bsAbove;
		Block blockCheck;
		int countHarvested = 0;
		boolean doBreak, doBreakAbove = false, doReplant;
		for (int xLoop = xMin; xLoop <= xMax; xLoop++) {
			for (int zLoop = zMin; zLoop <= zMax; zLoop++) {
				doBreakAbove = false;
				doBreak = false;
				doReplant = false;

				posCurrent = new BlockPos(xLoop, eventy, zLoop);
				ModMain.logger.info(" === " + UtilChat.blockPosToString(posCurrent));

				bs = world.getBlockState(posCurrent);
				if (bs == null) {
					continue;
				}
				blockCheck = bs.getBlock();
				if (blockCheck == null) {
					continue;
				}

				ModMain.logger.info(
						"blockCheck = " + blockCheck.getClass().getName() + " -> " + blockCheck.getUnlocalizedName());

				bsAbove = world.getBlockState(posCurrent.up());

				doBreak = false;
				doReplant = true;
				if (blockCheck instanceof IGrowable) {
					IGrowable plant = (IGrowable) blockCheck;
					// only if its full grown
					if (plant.canGrow(world, posCurrent, bs, world.isRemote) == false) {

						ModMain.logger.info(" fully grown ");
						doBreak = true;
					}
				} else if ((blockCheck instanceof BlockStem) && doesHarvestStem) {
					ModMain.logger.info(" stem ");
					doBreak = true;
				} else if ((blockCheck instanceof BlockSapling) && doesHarvestSapling) {
					ModMain.logger.info(" sapling ");
					doBreak = true;
				} else if ((blockCheck instanceof BlockTallGrass || blockCheck instanceof BlockDoublePlant)
						&& doesHarvestTallgrass) {
					doBreak = true;

					ModMain.logger.info(" tallgrass ");

					if (blockCheck == Blocks.tallgrass && bsAbove != null && bsAbove.getBlock() == Blocks.tallgrass) {
						doBreakAbove = true;
						ModMain.logger.info("above " + UtilChat.blockPosToString(posCurrent.up()));
					}
				} else if ((blockCheck instanceof BlockMushroom) && doesHarvestMushroom) {
					ModMain.logger.info(" BlockMushroom ");
					doBreak = true;
				} else if (blockCheck == Blocks.pumpkin && doesPumpkinBlocks) {
					ModMain.logger.info(" pumpkin ");
					doBreak = true;
					doReplant = false;
				} else if (blockCheck == Blocks.melon_block && doesMelonBlocks) {
					ModMain.logger.info(" melon_block ");
					doBreak = true;
					doReplant = false;
				}
				// no , for now is fine, do not do blocks

				if (doBreak) {

					if (world.isRemote == false) {
						world.destroyBlock(posCurrent, true);
						if (doBreakAbove) {
							world.destroyBlock(posCurrent.up(), true);
						}
					}

					if (doReplant) {// plant new seed
						world.setBlockState(posCurrent, blockCheck.getDefaultState());
					}

					countHarvested++;
				}
			}
		} // end of the outer loop
		return countHarvested;
	}
}
