package com.lothrazar.cyclicmagic.event;

import com.lothrazar.cyclicmagic.util.Const;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EventEnderChest  implements IFeatureEvent{

	private static boolean easyEnderChest;


	@SubscribeEvent
	public void onHit(PlayerInteractEvent.RightClickBlock event) {
		if(!easyEnderChest){
			return;
		}

		EntityPlayer entityPlayer = event.getEntityPlayer();
		ItemStack held = entityPlayer.getHeldItem(event.getHand());
		
		if(held != null && held.getItem() == Item.getItemFromBlock(Blocks.ender_chest)){

			entityPlayer.displayGUIChest(entityPlayer.getInventoryEnderChest());
		}
	}

	@Override
	public void syncConfig(Configuration config) {

		String category = Const.MODCONF + "Player";
		easyEnderChest = config.getBoolean("Easy Enderchest", category, true,
				"Open ender chest without placing it down, just attack with it");

		
	}
}
