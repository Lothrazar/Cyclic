package com.lothrazar.cyclicmagic.spell;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import com.lothrazar.cyclicmagic.projectile.EntityHarvestBolt;
import com.lothrazar.cyclicmagic.util.UtilSound;

public class SpellThrowHarvest extends BaseSpell implements ISpell{

	public SpellThrowHarvest(int id, String n) {
		super.init(id, n);
		this.cost = 30;
	}

	@Override
	public boolean cast(World world, EntityPlayer player, BlockPos pos, EnumFacing side ) {

		if(world.isRemote == false){
			world.spawnEntityInWorld(new EntityHarvestBolt(world, player));
		}
		return true;
	}
	
	@Override
	public void payCost(World world, EntityPlayer player, BlockPos pos) {

		UtilSound.playSoundAt(player, UtilSound.Own.pew);
		super.payCost(world, player, pos);
	}

	@Override
	public void spawnParticle(World world, EntityPlayer player, BlockPos pos) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void playSound(World world, EntityPlayer player, BlockPos pos) {
		// TODO Auto-generated method stub
		
	}
}
