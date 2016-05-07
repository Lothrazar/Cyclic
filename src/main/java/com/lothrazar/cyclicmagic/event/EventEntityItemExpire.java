package com.lothrazar.cyclicmagic.event;

import com.lothrazar.cyclicmagic.util.Const;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.entity.item.ItemExpireEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EventEntityItemExpire {

	public static boolean enabled = true;

	public EventEntityItemExpire() {
		//from one of my mods https://github.com/LothrazarMinecraftMods/SaplingGrowthControl/blob/master/src/main/java/com/lothrazar/samssaplings/ModConfig.java
		
	}
	
	@SubscribeEvent
	public void onItemExpireEvent(ItemExpireEvent event) {
		EntityItem entityItem = event.getEntityItem();
		Entity entity = event.getEntity();
		ItemStack is = entityItem.getEntityItem();
		if (is == null) {
			return;
		}// has not happened in the wild, yet

		Block blockhere = entity.worldObj.getBlockState(entityItem.getPosition()).getBlock();
		Block blockdown = entity.worldObj.getBlockState(entityItem.getPosition().down()).getBlock();

		if (blockhere == Blocks.air && blockdown == Blocks.dirt || // includes
																	// podzol
																	// and such
		blockdown == Blocks.grass) {
			// plant the sapling, replacing the air and on top of dirt/plantable

			if (Block.getBlockFromItem(is.getItem()) == Blocks.sapling)
				entity.worldObj.setBlockState(entityItem.getPosition(), Blocks.sapling.getStateFromMeta(is.getItemDamage()));
			else if (Block.getBlockFromItem(is.getItem()) == Blocks.red_mushroom)
				entity.worldObj.setBlockState(entityItem.getPosition(), Blocks.red_mushroom.getDefaultState());
			else if (Block.getBlockFromItem(is.getItem()) == Blocks.brown_mushroom)
				entity.worldObj.setBlockState(entityItem.getPosition(), Blocks.brown_mushroom.getDefaultState());

		}
	}

	public static void syncConfig(Configuration config) {
		String category = Const.MODCONF + "misc";
		
		enabled = config.getBoolean("plantDespawningSaplings", category, true, "Plant saplings if they despawn on grass/dirt");
		
	}
}