package com.lothrazar.cyclicmagic.spell;

import com.lothrazar.cyclicmagic.util.UtilMoveBlock;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class SpellPull extends BaseSpell {
	public SpellPull(int id){
		super(id);
		cooldown = 5;
	}

	@Override
	public boolean cast(World world, EntityPlayer player, BlockPos pos, EnumFacing side ) {

		if(side == null || world.getBlockState(pos) == null){
			return false;
		}
		
		BlockPos resultPosition = UtilMoveBlock.pullBlock(world, player, pos, side);

		//it returns a position if it was moved -> the pos it was moved to
		return (resultPosition != null);
	}
}
