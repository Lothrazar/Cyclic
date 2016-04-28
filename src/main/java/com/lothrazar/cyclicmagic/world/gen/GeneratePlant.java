package com.lothrazar.cyclicmagic.world.gen;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFlower;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

public class GeneratePlant extends WorldGenerator
{
   // private Block flower;
    private IBlockState blockState;
    public GeneratePlant(IBlockState state)
    {
    	blockState = state;
    }

    public boolean generate(World worldIn, Random rand, BlockPos pos)
    {
        for (int i = 0; i < 32; ++i)
        {
            BlockPos blockpos = pos.add(rand.nextInt(8) - rand.nextInt(8), rand.nextInt(4) - rand.nextInt(4), rand.nextInt(8) - rand.nextInt(8));

            if (worldIn.isAirBlock(blockpos) && (!worldIn.provider.getHasNoSky() || blockpos.getY() < 255))
            {
            	IBlockState soil = worldIn.getBlockState(blockpos.down());
           // if(soil.getBlock().canSustainPlant(soil, worldIn, pos.down(), net.minecraft.util.EnumFacing.UP, this)
            	if( soil.getBlock() == Blocks.grass || soil.getBlock() == Blocks.dirt){
            	
	                worldIn.setBlockState(blockpos, this.blockState, 2);
	                worldIn.setBlockState(blockpos.down(), Blocks.farmland.getDefaultState(), 2);
            	}
            }
        }

        return true;
    }
}
