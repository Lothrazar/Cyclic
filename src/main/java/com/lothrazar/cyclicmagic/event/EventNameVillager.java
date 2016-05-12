package com.lothrazar.cyclicmagic.event;

import com.lothrazar.cyclicmagic.IHasConfig;
import com.lothrazar.cyclicmagic.util.Const;

import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.EntityInteract;
//import net.minecraftforge.event.entity.player.EntityInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EventNameVillager  implements IHasConfig{

	private boolean nameVillagerTag;
	
	@SubscribeEvent
	public void onEntityInteractEvent(EntityInteract event) {

		if(!nameVillagerTag){return;}
		
		EntityPlayer entityPlayer = event.getEntityPlayer();
		ItemStack held = entityPlayer.getHeldItem(event.getHand());
		Entity target = event.getTarget();
		

		if (held != null && held.getItem() == Items.name_tag && held.hasDisplayName() && target instanceof EntityVillager) {

			EntityVillager v = (EntityVillager) target;

			v.setCustomNameTag(held.getDisplayName());

			if (entityPlayer.capabilities.isCreativeMode == false) {
				entityPlayer.inventory.decrStackSize(entityPlayer.inventory.currentItem, 1);
			}

			event.setCanceled(true);// stop the GUI inventory opening
		}
	}

	@Override
	public void syncConfig(Configuration config) {
		String category = Const.ConfigCategory.player;

		nameVillagerTag = config.getBoolean("Villager Nametag", category, true,
				"Let players name villagers with nametags");
		
		
	}
}
