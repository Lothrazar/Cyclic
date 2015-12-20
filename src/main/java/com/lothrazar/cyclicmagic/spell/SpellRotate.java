package com.lothrazar.cyclicmagic.spell;

import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class SpellRotate extends BaseSpell {
	public SpellRotate(int id,String name){
		super(id,name);
		cooldown = 5;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public boolean cast(World world, EntityPlayer player, BlockPos pos, EnumFacing side ) {

		if (pos == null || world.getBlockState(pos) == null || side == null) {
			return false;
		}
		IBlockState clicked = world.getBlockState(pos);
		if(clicked.getBlock()==null){
			return false;
		}

		if (clicked.getBlock().rotateBlock(world, pos, side)) {
			// for example, BlockMushroom.rotateBlock uses this, and hay bales
			// use it to swap the 'axis'
			return true;
		} else {
			// any property that is not variant?
			for (IProperty prop : (java.util.Set<IProperty>) clicked.getProperties().keySet()) {
				// since slabs do not use rotateBlock, swap the up or down half
				// being used
				if (prop.getName().equals("half")) {
					world.setBlockState(pos, clicked.cycleProperty(prop));
					//this.onCastSuccess(world, player, pos);
					return true;
				}
				// do not do variant, color, wet, check_decay, decayable, stage,
				// type
				// TODO: add a whitelist where "variant" is allowed, such as
				// sandstone ?
			}
			
			return false;
		}
	}
}
