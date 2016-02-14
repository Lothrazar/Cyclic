package com.lothrazar.cyclicmagic.spell;

import com.lothrazar.cyclicmagic.projectile.EntityFishingBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class SpellThrowFishing extends BaseSpellThrown implements ISpell{

	public SpellThrowFishing(int id, String name){

		super.init(id, name);
		this.cost = 30;
		this.cooldown = 10;
	}

	@Override
	public boolean cast(World world, EntityPlayer player, BlockPos pos, EnumFacing side){

		if(world.isRemote == false){
			world.spawnEntityInWorld(new EntityFishingBolt(world, player));
		}

		this.playSound(world, null, player.getPosition());
		this.spawnParticle(world, player, player.getPosition());

		return true;
	}

}
