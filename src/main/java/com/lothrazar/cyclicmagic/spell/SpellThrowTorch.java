package com.lothrazar.cyclicmagic.spell;

import com.lothrazar.cyclicmagic.projectile.EntityTorchBolt;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class SpellThrowTorch extends BaseSpellExp implements ISpell{

	private final int cooldown = 15;

	@Override
	public int getCastCooldown() {
		return cooldown;
	}
	
	@Override
	public boolean cast(World world, EntityPlayer player, BlockPos pos, EnumFacing side, Entity target) {

		return world.spawnEntityInWorld(new EntityTorchBolt(world,player));
	}
}
