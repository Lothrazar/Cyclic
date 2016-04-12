package com.lothrazar.cyclicmagic.event;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EventLadderClimb {
	
	@SubscribeEvent
	public void onPlayerTick(LivingUpdateEvent event) {
		
		if(event.getEntityLiving() instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) event.getEntityLiving();
			//move up or down without 'w'
			if(player.isOnLadder() && !player.isSneaking() && player.moveForward == 0){
				System.out.println("rotationPitch="+player.rotationPitch);

				if( player.rotationPitch > 75){
					player.moveEntity(0, -0.15, 0);
				}
				if( player.rotationPitch < -75){
					//even bigger to counter gravity
					player.moveEntity(0, +0.4, 0);
				}
			}
		}
	}
}
