package com.lothrazar.cyclicmagic.spell;

import com.lothrazar.cyclicmagic.projectile.EntityDynamite;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class SpellThrowExplosion extends BaseSpellExp implements ISpell {

	private final int cooldown = 8;

	@Override
	public int getCastCooldown() {
		return cooldown;
	}

	@Override
	public boolean cast(World world, EntityPlayer player, BlockPos pos, EnumFacing side, Entity target) {

		//in theory, only false if chunk is unloaded
		//or if we hit a spawn limit and forceSpawn is false
		return world.spawnEntityInWorld(new EntityDynamite(world, player,EntityDynamite.LEVEL_CREEPER));
	}
}
