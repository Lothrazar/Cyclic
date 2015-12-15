package com.lothrazar.cyclicmagic.spell;

import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class SpellRotate extends BaseSpellExp {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void cast(World world, EntityPlayer player, BlockPos pos, EnumFacing side, Entity target) {

		IBlockState clicked = world.getBlockState(pos);
		if(pos == null || clicked == null || clicked.getBlock() == null){return;}
	
		if(clicked.getBlock().rotateBlock(world, pos, side)){
			//for example, BlockMushroom.rotateBlock uses this, and hay bales use it to swap the 'axis'
			System.out.println("rotateBlock success");
			this.onCastSuccess(world, player, pos);
		}
		else{
			System.out.println("rotateBlock FAILS -> look into properties");
			//any property that is not variant?
			for (IProperty prop : (java.util.Set<IProperty>)clicked.getProperties().keySet())
	        {
				//since slabs do not use rotateBlock, swap the up or down half being used
	            if (prop.getName().equals("half"))
	            {
	                world.setBlockState(pos, clicked.cycleProperty(prop));
	    			this.onCastSuccess(world, player, pos);
	                return;
	            }
	            //do not do variant, color, wet,  check_decay, decayable, stage, type
	            //TODO: add a whitelist where "variant" is allowed, such as sandstone ?
	        }
			this.onCastFailure(world, player, pos);
		}
	}
}
