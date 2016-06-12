package com.lothrazar.cyclicmagic.potion;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class PotionSlowfall extends PotionCustom{


	public static final float slowfallSpeed = 0.41F;
	
	public PotionSlowfall(String name, boolean b, int potionColor) {
		super(name, b, potionColor); 
	}

	@SubscribeEvent
	public void onEntityUpdate(LivingUpdateEvent event) {
 
		EntityLivingBase entity = event.getEntityLiving();
		if (entity == null) { return; }
 
		if (entity.isPotionActive(this)) {
			this.tick(entity);
		}
	}
	public void tick(EntityLivingBase entityLiving){

		if (entityLiving instanceof EntityPlayer) {
			EntityPlayer p = (EntityPlayer) entityLiving;
			if (p.isSneaking()) { return; }
		}

		// else: so we are either a non-sneaking player, or a non player
		// entity

		// a normal fall seems to go up to 0, -1.2, -1.4, -1.6, then
		// flattens out at -0.078
		if (entityLiving.motionY < 0) {
			entityLiving.motionY *= slowfallSpeed;

			entityLiving.fallDistance = 0f; // for no fall damage
		}
		
	}
}
