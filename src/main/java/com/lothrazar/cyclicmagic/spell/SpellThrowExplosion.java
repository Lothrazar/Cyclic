package com.lothrazar.cyclicmagic.spell;

import com.lothrazar.cyclicmagic.projectile.EntityDynamite;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class SpellThrowExplosion extends BaseSpell implements ISpell {
	public SpellThrowExplosion(int id,String name){
		super(id,name);
		this.cooldown = 60;
		this.cost = 30;
	}

	@Override
	public boolean cast(World world, EntityPlayer player, BlockPos pos, EnumFacing side ) {
 
		if(world.isRemote == false){
			world.spawnEntityInWorld(new EntityDynamite(world, player,EntityDynamite.LEVEL_CREEPER));
		}
		
		return true;//even client side we want to say this is true
	}
}
