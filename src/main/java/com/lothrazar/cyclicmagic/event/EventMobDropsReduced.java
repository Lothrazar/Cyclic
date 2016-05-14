package com.lothrazar.cyclicmagic.event;

import java.util.List;

import com.lothrazar.cyclicmagic.IHasConfig;
import com.lothrazar.cyclicmagic.util.Const;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EventMobDropsReduced  implements IHasConfig{
 
	private static boolean zombieDropsNerfed;
 
	@SubscribeEvent
	public void onLivingDropsEvent(LivingDropsEvent event) {
		if(!zombieDropsNerfed){return;}

		Entity entity = event.getEntity();

		List<EntityItem> drops = event.getDrops();

		if (entity instanceof EntityZombie){

			Item item; 
			for (int i = 0; i < drops.size(); i++) {
				//EntityItem item = ;

				item = drops.get(i).getEntityItem().getItem();
				
				if (item == Items.carrot || item == Items.potato || item == Items.iron_ingot) {
					drops.remove(i);
				}
			}
		}
	}

	@Override
	public void syncConfig(Configuration config) {
		String category = Const.ConfigCategory.mobs;

		zombieDropsNerfed = config.getBoolean("ZombieDropsNerfed", category, true,
				"Zombies no longer drops carrots, potatoes, or iron ingots");
	 
		
	}
}
