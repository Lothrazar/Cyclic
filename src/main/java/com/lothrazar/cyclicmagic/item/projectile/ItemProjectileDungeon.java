package com.lothrazar.cyclicmagic.item.projectile;

import com.lothrazar.cyclicmagic.IHasConfig;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.entity.projectile.EntityDungeonEye;
import com.lothrazar.cyclicmagic.registry.ItemRegistry;
import com.lothrazar.cyclicmagic.util.UtilEntity;
import com.lothrazar.cyclicmagic.util.UtilSearchWorld;
import com.lothrazar.cyclicmagic.util.UtilSound;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.init.Items;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ItemProjectileDungeon extends BaseItemProjectile implements IHasRecipe, IHasConfig{

	@Override
	public void syncConfig(Configuration config) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addRecipe() {
		GameRegistry.addShapelessRecipe(new ItemStack(this, 6), new ItemStack(Items.ender_pearl), new ItemStack(Blocks.mossy_cobblestone), new ItemStack(Items.nether_wart));// Blocks.iron_bars
		 
	}

	@Override
	void onItemThrow(ItemStack held, World world, EntityPlayer player, EnumHand hand) {
		BlockPos blockpos = UtilSearchWorld.findClosestBlock(player, Blocks.mob_spawner, DUNGEONRADIUS);

		if (blockpos != null) {

			EntityDungeonEye entityendereye = new EntityDungeonEye(world, player);

			doThrow(world, player, hand, entityendereye);

			entityendereye.moveTowards(blockpos);
		}
		else {
			// not found, so play sounds to alert player

			// also drop it on ground to signal a failed throw
			BlockPos pos = player.getPosition();
			
			if (player.capabilities.isCreativeMode == false) {
				player.inventory.decrStackSize(player.inventory.currentItem, 1);
				
				UtilEntity.dropItemStackInWorld(world, pos, new ItemStack(ItemRegistry.ModItems.ender_dungeon));
		
			}
			//fizz sound
			UtilSound.playSound(player,pos,SoundEvents.block_fire_extinguish);
		
		}
	}

}
