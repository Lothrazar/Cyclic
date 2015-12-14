package com.lothrazar.cyclicmagic.projectile; 

import net.minecraft.block.Block;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.block.BlockMushroom;
import net.minecraft.block.BlockSapling;
import net.minecraft.block.BlockStem;
import net.minecraft.block.BlockTallGrass;
import net.minecraft.block.IGrowable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World; 

public class EntityHarvestBolt extends EntityThrowable//EntitySnowball
{ 
	public static int range_main = 6;
	public static int range_offset = 4;
	public static boolean doesHarvestStem;
	public static boolean doesHarvestTallgrass;
	public static boolean doesHarvestSapling;
	public static boolean doesHarvestMushroom;
	public static boolean doesPumpkinBlocks;
	public static boolean doesMelonBlocks;
    public EntityHarvestBolt(World worldIn)
    {
        super(worldIn);
    }

    public EntityHarvestBolt(World worldIn, EntityLivingBase ent)
    {
        super(worldIn, ent);
    }

    public EntityHarvestBolt(World worldIn, double x, double y, double z)
    {
        super(worldIn, x, y, z);
    }

    @Override
    protected void onImpact(MovingObjectPosition mop)
    {
    	if(this.getThrower() != null && mop.sideHit != null)
    	{
        	BlockPos offset = mop.getBlockPos().offset(mop.sideHit);
        	
    		//it harvests a horizontal slice each time
        	harvestArea(this.worldObj, this.getThrower(), mop.getBlockPos(),range_main);
        	harvestArea(this.worldObj, this.getThrower(), offset,range_main);
        	harvestArea(this.worldObj, this.getThrower(), offset.up(),range_offset);
        	harvestArea(this.worldObj, this.getThrower(), offset.down(),range_offset);
    	}
		 
        this.setDead();
 
    }  
    public static int harvestArea(World world, EntityLivingBase player, BlockPos pos, int radius)
	{
		int x = (int)player.posX;
		//int y = (int)player.posY;
		int z = (int)player.posZ;
		
		//search in a cube
		int xMin = x - radius;
		int xMax = x + radius; 
		int zMin = z - radius;
		int zMax = z + radius;
		
		int eventy = pos.getY();
		
		BlockPos posCurrent;
		
		int countHarvested = 0;
		boolean doBreak,doReplant;
		for (int xLoop = xMin; xLoop <= xMax; xLoop++)
		{ 
			for (int zLoop = zMin; zLoop <= zMax; zLoop++)
			{
				posCurrent = new BlockPos(xLoop, eventy, zLoop);
				IBlockState bs = world.getBlockState(posCurrent);
				Block blockCheck = bs.getBlock(); 
				
				doBreak = false;
				doReplant=true;
				if(blockCheck instanceof IGrowable)
				{ 
					IGrowable plant = (IGrowable) blockCheck;
//TODO: this if else could be structured better??? oh well
					if(plant.canGrow(world, posCurrent, bs, world.isRemote) == false )//it is fully grown
					{  
						if( (blockCheck instanceof BlockStem) == true && doesHarvestStem==false)
						{
							continue;//disabled harvesting pumpkin/melon/similar stems
						}
						if( (blockCheck instanceof BlockSapling) == true && doesHarvestSapling==false)
						{
							continue;//disabled harvesting
						}
						if( (blockCheck instanceof BlockTallGrass || blockCheck instanceof BlockDoublePlant) == true && doesHarvestTallgrass==false)
						{
							continue;//disabled harvesting 
						}
						if( (blockCheck instanceof BlockMushroom) == true && doesHarvestMushroom==false)
						{
							continue;//disabled harvesting 
						}
						
						doBreak = true;
					} 
				}
				else if(blockCheck ==Blocks.pumpkin && doesPumpkinBlocks)
				{
					doBreak = true;
					doReplant = false;
				}
				else if(blockCheck ==Blocks.melon_block && doesMelonBlocks)
				{
					doBreak = true;
					doReplant = false;
				}
				//no , for now is fine, do not do blocks
				
				if(doBreak)//break fully grown,
				{
					if(world.isRemote == false)  //only drop items in serverside
						world.destroyBlock(posCurrent, true);
					 
					if(doReplant)//plant new seed
						world.setBlockState(posCurrent, blockCheck.getDefaultState());//this plants a seed. it is not 'hay_block'
				
					countHarvested++;
					
				}
			}  
		} //end of the outer loop
		return countHarvested;
	}

}