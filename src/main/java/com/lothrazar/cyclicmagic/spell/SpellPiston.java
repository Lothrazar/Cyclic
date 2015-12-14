package com.lothrazar.cyclicmagic.spell;

import com.lothrazar.cyclicmagic.util.UtilMoveBlock;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;

public class SpellPiston extends BaseSpellExp {

	@Override
	public void cast(World world, EntityPlayer player, BlockPos pos) {

		//TODO: get side from source and pass it up through cast
		EnumFacing side = EnumFacing.UP;
		
		BlockPos resultPosition = UtilMoveBlock.moveBlock(world, player, pos, side);

		if(resultPosition != null){
			//then it was a success
			//spawnParticle(worldIn, EnumParticleTypes.CRIT_MAGIC, resultPosition); 
			this.onCastSuccess(world, player, resultPosition);
		}
	}

}
