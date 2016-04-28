package com.lothrazar.cyclicmagic.world.gen;

import java.util.Random;

import net.minecraft.block.BlockCrops;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

public class GeneratePlant extends WorldGenerator {
	
	private BlockCrops crop;

	public GeneratePlant(BlockCrops state) {
		crop = state;
	}

	public boolean generate(World worldIn, Random rand, BlockPos pos) {
		//   fullgrown or a randomness to the state
		int fullGrownMeta = (crop).func_185526_g();

		for (int i = 0; i < 26; ++i) {
			BlockPos blockpos = pos.add(rand.nextInt(8) - rand.nextInt(8), rand.nextInt(4) - rand.nextInt(4),
					rand.nextInt(8) - rand.nextInt(8));

			if (worldIn.isAirBlock(blockpos) && (!worldIn.provider.getHasNoSky() || blockpos.getY() < 255)) {
				IBlockState soil = worldIn.getBlockState(blockpos.down());
				// if(soil.getBlock().canSustainPlant(soil, worldIn, pos.down(),
				// net.minecraft.util.EnumFacing.UP, this)
				if (soil.getBlock() == Blocks.grass || soil.getBlock() == Blocks.dirt) {

					worldIn.setBlockState(blockpos, this.crop.getStateFromMeta(MathHelper.getRandomIntegerInRange(rand, fullGrownMeta/2-1, fullGrownMeta)), 2);
					worldIn.setBlockState(blockpos.down(), Blocks.farmland.getDefaultState(), 2);
				}
			}
		}

		return true;
	}
}
