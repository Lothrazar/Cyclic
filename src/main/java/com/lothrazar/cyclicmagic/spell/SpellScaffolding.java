package com.lothrazar.cyclicmagic.spell;

import com.lothrazar.cyclicmagic.BlockRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class SpellScaffolding extends BaseSpellExp {
	private final int cooldown = 5;// same cooldown for all potion spells
	@Override
	public int getCastCooldown() {
		return cooldown;
	}
	@Override
	public void cast(World world, EntityPlayer player, BlockPos pos, EnumFacing side, Entity target) {

		BlockPos offset = pos.offset(side);
		
		world.setBlockState(offset, BlockRegistry.block_fragile.getDefaultState());
	}
}
