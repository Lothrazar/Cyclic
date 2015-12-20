package com.lothrazar.cyclicmagic.spell;

import com.lothrazar.cyclicmagic.projectile.EntityFishingBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class SpellThrowFishing extends BaseSpell implements ISpell {

	public SpellThrowFishing(int id,String name){
		super(id,name);
		this.cooldown = 100;
	}

	@Override
	public boolean cast(World world, EntityPlayer player, BlockPos pos, EnumFacing side ) {

		return world.spawnEntityInWorld(new EntityFishingBolt(world, player));
	}
}
