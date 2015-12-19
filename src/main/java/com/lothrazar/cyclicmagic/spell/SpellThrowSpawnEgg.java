package com.lothrazar.cyclicmagic.spell;

import com.lothrazar.cyclicmagic.ItemRegistry;
import com.lothrazar.cyclicmagic.projectile.EntityFishingBolt;
import com.lothrazar.cyclicmagic.projectile.EntityRespawnEgg;
import net.minecraft.entity.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class SpellThrowSpawnEgg extends BaseSpellExp implements ISpell {

	@Override
	public boolean cast(World world, EntityPlayer player, BlockPos pos, EnumFacing side, Entity target) {

		return world.spawnEntityInWorld(new EntityRespawnEgg(world, player));
		/*
		if(target == null){return false;}
	    int entity_id = EntityList.getEntityID(target);

		if(entity_id > 0) 
		{ 
			System.out.println("make this an egg projectile");
			world.removeEntity(target); 
			if(world.isRemote == false){
				ItemStack stack = new ItemStack(ItemRegistry.respawn_egg,1,entity_id);
				
				if(target.hasCustomName()){
					stack.setStackDisplayName(target.getCustomNameTag());
				}
				
				
				player.dropPlayerItemWithRandomChoice(stack,true);
			}

			
			//		ModSpells.spawnParticle(world, EnumParticleTypes.VILLAGER_HAPPY, target.getPosition());

			//ModSpells.playSoundAt(entityPlayer, "mob.zombie.remedy");
			 
	
			return true;
			
		} 
		
		return false;*/
	}

}
