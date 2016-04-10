package com.lothrazar.cyclicmagic.event;

import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import com.lothrazar.cyclicmagic.registry.PotionRegistry;

public class EventPotions {

	@SubscribeEvent
	public void onEntityUpdate(LivingUpdateEvent event) {

		EntityLivingBase entityLiving = event.getEntityLiving();
		if (entityLiving == null) { return; }

		PotionRegistry.tickSlowfall(entityLiving);

		PotionRegistry.tickMagnet(entityLiving);
	}
}
