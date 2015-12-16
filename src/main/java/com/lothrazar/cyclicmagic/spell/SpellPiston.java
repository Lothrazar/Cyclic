package com.lothrazar.cyclicmagic.spell;

import com.lothrazar.cyclicmagic.util.UtilMoveBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class SpellPiston extends BaseSpellExp {

	@Override
	public void cast(World world, EntityPlayer player, BlockPos pos, EnumFacing side, Entity target) {

		BlockPos resultPosition = UtilMoveBlock.moveBlock(world, player, pos, side);

		if (resultPosition != null) {
			this.onCastSuccess(world, player, resultPosition);
		}
	}
}
