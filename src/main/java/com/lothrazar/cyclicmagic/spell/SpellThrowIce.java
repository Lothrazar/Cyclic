package com.lothrazar.cyclicmagic.spell;

import com.lothrazar.cyclicmagic.projectile.EntityBlazeBolt;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class SpellThrowIce extends BaseSpellExp implements ISpell {

	private final int cooldown = 25;

	@Override
	public int getCastCooldown() {
		return cooldown;
	}

	@Override
	public void cast(World world, EntityPlayer player, BlockPos pos, EnumFacing side, Entity target) {

		world.spawnEntityInWorld(new EntityBlazeBolt(world, player));
	}
}
