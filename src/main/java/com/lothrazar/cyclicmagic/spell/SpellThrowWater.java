package com.lothrazar.cyclicmagic.spell;

import com.lothrazar.cyclicmagic.projectile.EntityWaterBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class SpellThrowWater extends BaseSpell implements ISpell {

	public SpellThrowWater(){
		super();
		cooldown = 25;
	}
 
	@Override
	public boolean cast(World world, EntityPlayer player, BlockPos pos, EnumFacing side) {

		return world.spawnEntityInWorld(new EntityWaterBolt(world, player));
	}
}
