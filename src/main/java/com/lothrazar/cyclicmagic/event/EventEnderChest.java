package com.lothrazar.cyclicmagic.event;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EventEnderChest {

	@SubscribeEvent
	public void onHit(PlayerInteractEvent.RightClickBlock event) {

		EntityPlayer entityPlayer = event.getEntityPlayer();
		ItemStack held = entityPlayer.getHeldItem(event.getHand());
		
		if(held != null && held.getItem() == Item.getItemFromBlock(Blocks.ender_chest)){

			entityPlayer.displayGUIChest(entityPlayer.getInventoryEnderChest());
		}
	}
}
