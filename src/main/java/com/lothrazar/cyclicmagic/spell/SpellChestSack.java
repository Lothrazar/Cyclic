package com.lothrazar.cyclicmagic.spell;

import com.lothrazar.cyclicmagic.item.ItemChestSack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class SpellChestSack extends BaseSpellExp {
	private final int cooldown = 5;// same cooldown for all potion spells
	@Override
	public int getCastCooldown() {
		return cooldown;
	}
	
	@Override
	public boolean cast(World world, EntityPlayer player, BlockPos pos, EnumFacing side, Entity target) {
 
		//imported from my old mod https://github.com/PrinceOfAmber/SamsPowerups/blob/b02f6b4243993eb301f4aa2b39984838adf482c1/src/main/java/com/lothrazar/samscontent/item/ItemChestSackEmpty.java

		ItemStack drop = ItemChestSack.createStackFromInventory( world,player, pos);
		
		if(drop != null){

			world.setBlockToAir(pos); 
			
			if(world.isRemote == false  ){
				player.entityDropItem(drop, 1); 
			}

			return true;
		}
 
		//Util.spawnParticle(entityPlayer.worldObj, EnumParticleTypes.CRIT, pos);
		//Util.playSoundAt(entityPlayer, "random.wood_click");
		
		return false;
	}
}
