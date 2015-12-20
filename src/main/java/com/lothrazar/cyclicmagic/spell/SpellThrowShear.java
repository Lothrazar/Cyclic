package com.lothrazar.cyclicmagic.spell;

import com.lothrazar.cyclicmagic.projectile.EntityShearingBolt; 
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class SpellThrowShear extends BaseSpell implements ISpell {

	public SpellThrowShear(int id){
		super(id);
		cooldown = 15;
	}
 
	@Override
	public boolean cast(World world, EntityPlayer player, BlockPos pos, EnumFacing side) {

		return world.spawnEntityInWorld(new EntityShearingBolt(world, player));
	}
}
