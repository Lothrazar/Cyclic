package com.lothrazar.cyclicmagic.spell;

import com.lothrazar.cyclicmagic.projectile.EntityFishingBolt;
import com.lothrazar.cyclicmagic.util.UtilSound;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class SpellThrowFishing extends BaseSpell implements ISpell {

	public SpellThrowFishing(int id,String name){
		super(id,name);
		this.cost = 30;
		this.cooldown = 10;
	}

	@Override
	public boolean cast(World world, EntityPlayer player, BlockPos pos, EnumFacing side ) {

		if(world.isRemote == false){
			world.spawnEntityInWorld(new EntityFishingBolt(world, player));
		}
		return true;
	}
	
	@Override
	public void onCastSuccess(World world, EntityPlayer player, BlockPos pos) {
		
		UtilSound.playSoundAt(player, UtilSound.toss);
		super.onCastSuccess(world, player, pos);
	}
}
