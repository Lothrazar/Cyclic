package com.lothrazar.cyclicmagic.event;

import com.lothrazar.cyclicmagic.util.Const;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EventLadderClimb  implements IFeatureEvent{

	private boolean fastLadderClimb;
	
	@SubscribeEvent
	public void onPlayerTick(LivingUpdateEvent event) {
		if(!fastLadderClimb){
			return;
		}
		
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

	@Override
	public void syncConfig(Configuration config) {

		String category = Const.MODCONF + "Player";
		fastLadderClimb = config.getBoolean("Faster Ladders", category, true,
				"Allows you to quickly climb ladders by looking up instead of moving forward");

		
	}
}
