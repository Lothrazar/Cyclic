package com.lothrazar.cyclicmagic.event;

import com.lothrazar.cyclicmagic.IHasConfig;
import com.lothrazar.cyclicmagic.util.Const;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.entity.item.ItemExpireEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EventSaplingPlantDespawn  implements IHasConfig{

	public static boolean enabled = true;

	public EventSaplingPlantDespawn() {
		//from one of my mods https://github.com/LothrazarMinecraftMods/SaplingGrowthControl/blob/master/src/main/java/com/lothrazar/samssaplings/ModConfig.java
		
	}
	
	@SubscribeEvent
	public void onItemExpireEvent(ItemExpireEvent event) {
		
		if(!enabled){
			return;
		}
		
		EntityItem entityItem = event.getEntityItem();
		Entity entity = event.getEntity();
		ItemStack is = entityItem.getEntityItem();
		if (is == null) {
			return;
		}// has not happened in the wild, yet

		Block blockhere = entity.worldObj.getBlockState(entityItem.getPosition()).getBlock();
		Block blockdown = entity.worldObj.getBlockState(entityItem.getPosition().down()).getBlock();

		if (blockhere == Blocks.AIR && blockdown == Blocks.DIRT || blockdown == Blocks.GRASS) {
			// plant the sapling, replacing the air and on top of dirt/plantable

			//BlockSapling.TYPE
			
			if (Block.getBlockFromItem(is.getItem()) == Blocks.SAPLING)
				entity.worldObj.setBlockState(entityItem.getPosition(), Blocks.SAPLING.getStateFromMeta(is.getItemDamage()));
			else if (Block.getBlockFromItem(is.getItem()) == Blocks.RED_MUSHROOM)
				entity.worldObj.setBlockState(entityItem.getPosition(), Blocks.RED_MUSHROOM.getDefaultState());
			else if (Block.getBlockFromItem(is.getItem()) == Blocks.BROWN_MUSHROOM)
				entity.worldObj.setBlockState(entityItem.getPosition(), Blocks.BROWN_MUSHROOM.getDefaultState());

		}
	}

	public  void syncConfig(Configuration config) {
		String category = Const.ConfigCategory.environment;
		
		enabled = config.getBoolean("PlantDespawningSaplings", category, true, "Plant saplings (and mushrooms) if they despawn on grass/dirt");
		
	}
}