package com.lothrazar.cyclicmagic.spell;

import com.lothrazar.cyclicmagic.projectile.EntityRespawnEgg;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class SpellThrowSpawnEgg extends BaseSpell implements ISpell {
	
	public SpellThrowSpawnEgg(int id,String name){
		super(id,name);
		cooldown = 50;
	}

	@Override
	public boolean cast(World world, EntityPlayer player, BlockPos pos, EnumFacing side) {

		if(world.isRemote == false){
			world.spawnEntityInWorld(new EntityRespawnEgg(world, player));
		}
		return true;
	}

}
