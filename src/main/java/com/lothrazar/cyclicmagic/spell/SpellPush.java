package com.lothrazar.cyclicmagic.spell;

import com.lothrazar.cyclicmagic.util.UtilMoveBlock;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class SpellPush extends BaseSpell {
	public SpellPush(int id,String name){
		super(id,name);
		this.cooldown = 1;
	}

	@Override
	public boolean cast(World world, EntityPlayer player, BlockPos pos, EnumFacing side ) {

		if(side == null || world.getBlockState(pos) == null){
			return false;
		}
		
		BlockPos resultPosition = UtilMoveBlock.pushBlock(world, player, pos, side);

		return (resultPosition != null);
	}
}
