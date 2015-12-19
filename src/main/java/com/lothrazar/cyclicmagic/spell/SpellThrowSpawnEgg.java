package com.lothrazar.cyclicmagic.spell;

import com.lothrazar.cyclicmagic.projectile.EntityRespawnEgg;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class SpellThrowSpawnEgg extends BaseSpellExp implements ISpell {
	
	private final int cooldown = 50;

	@Override
	public int getCastCooldown() {
		return cooldown;
	}
	@Override
	public boolean cast(World world, EntityPlayer player, BlockPos pos, EnumFacing side) {

		return world.spawnEntityInWorld(new EntityRespawnEgg(world, player));
	}

}
