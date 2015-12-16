package com.lothrazar.cyclicmagic.spell;

import com.lothrazar.cyclicmagic.ProjectileRegistry;
import com.lothrazar.cyclicmagic.projectile.EntityTorchBolt;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class SpellTorchProjectile extends BaseSpellExp implements ISpell{

	
	
	@Override
	public void cast(World world, EntityPlayer player, BlockPos pos, EnumFacing side, Entity target) {

		world.spawnEntityInWorld(new EntityTorchBolt(world,player));
	}
}
