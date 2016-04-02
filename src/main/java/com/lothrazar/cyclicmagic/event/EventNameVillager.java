package com.lothrazar.cyclicmagic.event;

import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.EntityInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EventNameVillager{

	@SubscribeEvent
	public void onEntityInteractEvent(EntityInteractEvent event){

		EntityPlayer entityPlayer = event.getEntityPlayer();
		ItemStack held = entityPlayer.getHeldItem(event.getHand());
		Entity target = event.getTarget();

		if(held != null && held.getItem() == Items.name_tag && held.hasDisplayName() && target instanceof EntityVillager){

			EntityVillager v = (EntityVillager) target;

			v.setCustomNameTag(held.getDisplayName());

			if(entityPlayer.capabilities.isCreativeMode == false){
				entityPlayer.inventory.decrStackSize(entityPlayer.inventory.currentItem, 1);
			}

			event.setCanceled(true);// stop the GUI inventory opening
		}
	}
}
