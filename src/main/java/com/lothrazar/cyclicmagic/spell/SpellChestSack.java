package com.lothrazar.cyclicmagic.spell;

import com.lothrazar.cyclicmagic.item.ItemChestSack;
import com.lothrazar.cyclicmagic.util.UtilParticle;
import com.lothrazar.cyclicmagic.util.UtilSound;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;

public class SpellChestSack extends BaseSpell implements ISpell {

	public SpellChestSack(int id,String name){
		super.init(id,name);
		this.cost = 30;
	}

	@Override
	public boolean cast(World world, EntityPlayer player, BlockPos pos, EnumFacing side ) {
 
		//imported from my old mod https://github.com/PrinceOfAmber/SamsPowerups/blob/b02f6b4243993eb301f4aa2b39984838adf482c1/src/main/java/com/lothrazar/samscontent/item/ItemChestSackEmpty.java

		if(pos == null){
			return false;// cannot cast in midair
		}
		
		ItemStack drop = ItemChestSack.createStackFromInventory( world,player, pos);
		
		if(drop != null){

			world.setBlockToAir(pos); 

			this.playSound(world,null,pos);
			
			this.spawnParticle(world, player, pos);
			
			if(world.isRemote == false  ){
				player.entityDropItem(drop, 1); 
			}

			return true;
		}
 
		return false;
	}

	@Override
	public void spawnParticle(World world, EntityPlayer player, BlockPos pos) {

		UtilParticle.spawnParticle(world, EnumParticleTypes.CRIT_MAGIC, pos);
	}

	@Override
	public void playSound(World world, Block block, BlockPos pos) {
		UtilSound.playSound(world,pos, UtilSound.Own.thunk);
	}
}
