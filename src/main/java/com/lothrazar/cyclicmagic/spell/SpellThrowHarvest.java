package com.lothrazar.cyclicmagic.spell;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import com.lothrazar.cyclicmagic.projectile.EntityHarvestBolt;

public class SpellThrowHarvest extends BaseSpell implements ISpell{

	public SpellThrowHarvest(int id, String n) {
		super(id, n);
		this.cooldown = 30;
		this.experience = 200;
	}

	@Override
	public boolean cast(World world, EntityPlayer player, BlockPos pos, EnumFacing side ) {

		if(world.isRemote == false){
			world.spawnEntityInWorld(new EntityHarvestBolt(world, player));
		}
		return true;
	}
}
