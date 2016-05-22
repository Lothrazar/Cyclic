package com.lothrazar.cyclicmagic.util;

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
 
	public static class HarestCropsConfig{ 
		public HarestCropsConfig(){
		}

		public boolean doesHarvestStem = false;
		public boolean doesHarvestSapling = false;
		public boolean doesHarvestMushroom = true;
		public boolean doesPumpkinBlocks = true;
		public boolean doesMelonBlocks = true;
		public boolean doesFlowers = true;
		public boolean doesLeaves = true;

		//TODO: this hits both the short regular grass, and tall grass, and 2 high flowers. split it up
		public boolean doesHarvestTallgrass = true;

		// red_flower and yellow_flower TODO--BlockYellowFlower,BlockRedFlower
		
	}

	public static int harvestArea(World world, EntityLivingBase player, BlockPos pos, int radius, HarestCropsConfig conf) {

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
		IBlockState bsAbove,bsBelow;
		Block blockCheck;
		int countHarvested = 0;
		boolean doBreak, doBreakAbove = false,doBreakBelow = true, doReplant;
		
		for (int xLoop = xMin; xLoop <= xMax; xLoop++) {
			for (int zLoop = zMin; zLoop <= zMax; zLoop++) {

				posCurrent = new BlockPos(xLoop, eventy, zLoop);

				if(world.isAirBlock(posCurrent)){continue;}

				bs = world.getBlockState(posCurrent);
				if (bs == null) {	continue;	}
				blockCheck = bs.getBlock();
				if (blockCheck == null) {	continue;  }
				
				bsAbove = world.getBlockState(posCurrent.up());
				bsBelow = world.getBlockState(posCurrent.down());
				//ModMain.logger.info(" === " + UtilChat.blockPosToString(posCurrent));

				//ModMain.logger.info("blockCheck = " + blockCheck.getClass().getName() + " -> " + blockCheck.getUnlocalizedName());

				doBreakAbove = false;
				doBreakBelow = false;
				doBreak = false;
				doReplant = false;
				 
				if (blockCheck instanceof IGrowable) {
					IGrowable plant = (IGrowable) blockCheck;
					//ModMain.logger.info(" IGrowable ");
					// only if its full grown
					if (plant.canGrow(world, posCurrent, bs, world.isRemote) == false) {

						//ModMain.logger.info(" fully grown ");
						doBreak = true;
						doReplant = true; 
					}
				} 
				if ((blockCheck instanceof BlockStem) && conf.doesHarvestStem) {
					//ModMain.logger.info(" stem ");
					doBreak = true;
				} else if ((blockCheck instanceof BlockSapling) && conf.doesHarvestSapling) {
					//ModMain.logger.info(" sapling ");
					doBreak = true;
				} else if ((blockCheck instanceof BlockTallGrass || blockCheck instanceof BlockDoublePlant)
						&& conf.doesHarvestTallgrass) {
					doBreak = true;
					doReplant = false;
					//ModMain.logger.info(posCurrent.toString()+" tallgrass ");

					if (blockCheck instanceof BlockDoublePlant && bsAbove != null && bsAbove.getBlock() instanceof BlockDoublePlant) {
						doBreakAbove = true;
						//ModMain.logger.info("above " + UtilChat.blockPosToString(posCurrent.up()));
					}
					if (bsBelow instanceof BlockDoublePlant && bsBelow != null && bsBelow.getBlock() instanceof BlockDoublePlant) {
						doBreakBelow = true;
						//ModMain.logger.info("above " + UtilChat.blockPosToString(posCurrent.up()));
					}
				} else if ((blockCheck instanceof BlockMushroom) && conf.doesHarvestMushroom) {
					//ModMain.logger.info(" BlockMushroom ");
					doBreak = true;
				} else if (blockCheck == Blocks.PUMPKIN && conf.doesPumpkinBlocks) {
					//ModMain.logger.info(" pumpkin ");
					doBreak = true;
					doReplant = false;
				} else if (blockCheck == Blocks.MELON_BLOCK && conf.doesMelonBlocks) {
					//ModMain.logger.info(" melon_block ");
					doBreak = true;
					doReplant = false;
				}
				else if(blockCheck == Blocks.RED_FLOWER || blockCheck == Blocks.YELLOW_FLOWER && conf.doesFlowers){
					doBreak = true;
					doReplant = false;
				}
				else if(blockCheck == Blocks.LEAVES || blockCheck == Blocks.LEAVES2 && conf.doesLeaves){
					doBreak = true;
					doReplant = false;
				}
				
				// no , for now is fine, do not do blocks

				if (doBreak) {
				
					world.destroyBlock(posCurrent, true);
					//break above first BECAUSE 2 high tallgrass otherwise will bug out if you break bottom first
					if (doBreakAbove) {
						world.destroyBlock(posCurrent.up(), false);
					}
					if (doBreakBelow) {
						world.destroyBlock(posCurrent.down(), false);
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
