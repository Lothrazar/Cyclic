package com.lothrazar.cyclicmagic.event;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraftforge.client.GuiIngameForge;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntitySafeMount {

	@SubscribeEvent
	public void onLivingHurtEvent(LivingHurtEvent event){
		
		DamageSource source = event.getSource();
		if(source.getSourceOfDamage() == null){
			return;
		}
		Entity sourceOfDamage = source.getEntity();
		
		EntityLivingBase entity = event.getEntityLiving();
		if(entity == null ){
			return;
		}
		List<Entity> getPassengers = entity.getPassengers();
		
		for(Entity p : getPassengers){
			if(p != null && p.getUniqueID() == sourceOfDamage.getUniqueID() && sourceOfDamage instanceof EntityPlayer){
			
				//with arrows/sword/etc
				//System.out.println("Cannot hurt your own horse");
				
				event.setCanceled(true);
			}
		}
	}
	
	
	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onRenderOverlay(RenderGameOverlayEvent event) {
		// https://github.com/LothrazarMinecraftMods/OverpoweredInventory/blob/8a7459161837b930c5417f774676504bce970e66/src/main/java/com/lothrazar/powerinventory/EventHandler.java
		// force it to always show food - otherwise its hidden when riding a
		// horse

		//TODO: CONFIG:
		//if (ModConfig.alwaysShowHungerbar) {
			GuiIngameForge.renderFood = true;
			
			//TODO: if space bar is down, then hide jump bar and show this
			GuiIngameForge.renderExperiance = true;
		//}
	}
	
}
