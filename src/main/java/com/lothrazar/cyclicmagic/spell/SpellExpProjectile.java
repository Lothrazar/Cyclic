package com.lothrazar.cyclicmagic.spell;

import com.lothrazar.cyclicmagic.projectile.EntityTorchBolt;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class SpellExpProjectile extends BaseSpellExp implements ISpell{

	@Override
	public void cast(World world, EntityPlayer player, BlockPos pos, EnumFacing side, Entity target) {
		//TODO: how to treat projectiles like potions
		//that is , spawn them by ID from proj registry?
		world.spawnEntityInWorld(new EntityTorchBolt(world,player));
	}
}
