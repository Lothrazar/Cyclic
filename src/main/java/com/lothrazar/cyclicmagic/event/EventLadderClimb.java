package com.lothrazar.cyclicmagic.event;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EventLadderClimb {
	
	@SubscribeEvent
	public void onPlayerTick(LivingUpdateEvent event) {
		
		if(event.getEntityLiving() instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) event.getEntityLiving();
			if(player.isOnLadder() && !player.isSneaking() && player.moveForward == 0){
				//move up faster without 'w'
		
				if( player.rotationPitch < -78){
					//even bigger to counter gravity
					player.moveEntity(0, +0.4, 0);
				}
			}
		}
	}
}
