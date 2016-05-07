package com.lothrazar.cyclicmagic.event;

import java.util.List;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.init.Items;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EventMobDropsReduced  implements IFeatureEvent{

	public boolean removeZombieCarrotPotato = true;

	@SubscribeEvent
	public void onLivingDropsEvent(LivingDropsEvent event) {

		Entity entity = event.getEntity();
		// World worldObj = entity.getEntityWorld();
		List<EntityItem> drops = event.getDrops();

		// BlockPos pos = entity.getPosition();

		if (entity instanceof EntityZombie) // how to get this all into its own
		                                    // class
		{
			// EntityZombie z = (EntityZombie) entity;

			for (int i = 0; i < drops.size(); i++) {
				EntityItem item = drops.get(i);

				if (item.getEntityItem().getItem() == Items.carrot || item.getEntityItem().getItem() == Items.potato) {
					drops.remove(i);
				}
			}
		}
	}

	@Override
	public void syncConfig(Configuration config) {
		// TODO Auto-generated method stub
		
	}
}
