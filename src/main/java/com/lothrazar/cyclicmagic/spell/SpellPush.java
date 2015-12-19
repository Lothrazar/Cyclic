package com.lothrazar.cyclicmagic.spell;

import com.lothrazar.cyclicmagic.util.UtilMoveBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class SpellPush extends BaseSpellExp {
	private final int cooldown = 5;// same cooldown for all potion spells
	@Override
	public int getCastCooldown() {
		return cooldown;
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
