package com.lothrazar.cyclicmagic.event;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import com.lothrazar.cyclicmagic.ModMain;
import com.lothrazar.cyclicmagic.registry.EventRegistry;
import com.lothrazar.cyclicmagic.registry.PotionRegistry;

public class EventPotions {

	@SubscribeEvent
	public void onEntityUpdate(LivingUpdateEvent event) {

		EntityLivingBase entityLiving = event.getEntityLiving();
		if (entityLiving == null) { return; }


		PotionRegistry.handle((EntityLivingBase)event.getEntity());
	
		
		if(entityLiving instanceof EntityPlayer && entityLiving.worldObj.isRemote)
			ModMain.proxy.renderPotions();
	}
	
	
	

	@SubscribeEvent
	public void onPotionShiftEvent(GuiScreenEvent.PotionShiftEvent event) {
		event.setCanceled(EventRegistry.cancelPotionInventoryShift);
	}
	
	
}
