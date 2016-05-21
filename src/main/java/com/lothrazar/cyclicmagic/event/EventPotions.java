package com.lothrazar.cyclicmagic.event;

import com.lothrazar.cyclicmagic.IHasConfig;
import com.lothrazar.cyclicmagic.registry.PotionRegistry;
import com.lothrazar.cyclicmagic.util.Const;

import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EventPotions implements IHasConfig {

	public boolean cancelPotionInventoryShift;
	
	@SubscribeEvent
	public void onEntityUpdate(LivingUpdateEvent event) {
 
		EntityLivingBase entity = event.getEntityLiving();
		if (entity == null) { return; }
 
		if (entity.isPotionActive(PotionRegistry.slowfall)) {
			PotionRegistry.slowfall.tick(entity);
		}

		if (entity.isPotionActive(PotionRegistry.magnet)) {
			PotionRegistry.magnet.tick(entity);
		}

		if (entity.isPotionActive(PotionRegistry.waterwalk)) {
			PotionRegistry.waterwalk.tick(entity);
		}  
	}
	 
	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onPotionShiftEvent(GuiScreenEvent.PotionShiftEvent event) {
		
		event.setCanceled(cancelPotionInventoryShift);
	}
 
	@Override
	public void syncConfig(Configuration config) {

		String category = Const.ConfigCategory.inventory;

		cancelPotionInventoryShift = config.getBoolean("Potion Inventory Shift", category, true,
				"When true, this blocks the potions moving the inventory over");
	}
}
