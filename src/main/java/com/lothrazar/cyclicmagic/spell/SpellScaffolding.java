package com.lothrazar.cyclicmagic.spell;

import com.lothrazar.cyclicmagic.BlockRegistry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class SpellScaffolding extends BaseSpell {
	public SpellScaffolding(int id){
		super(id);
		cooldown = 5;
	}
 
	@Override
	public boolean cast(World world, EntityPlayer player, BlockPos pos, EnumFacing side ) {

		BlockPos offset = pos.offset(side);
		
		if(world.isAirBlock(offset)){
			world.setBlockState(offset, BlockRegistry.block_fragile.getDefaultState());
			
			return true;
		}
		
		return false;
	}
}
