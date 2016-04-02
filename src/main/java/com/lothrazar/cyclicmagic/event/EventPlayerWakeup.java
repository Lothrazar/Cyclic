package com.lothrazar.cyclicmagic.event;

import com.lothrazar.cyclicmagic.registry.PotionRegistry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.event.entity.player.PlayerWakeUpEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EventPlayerWakeup{

	int levelBoost = PotionRegistry.I;// 1 means Hunger II. int = 2 means Hunger III, etc.
	int sleeping_hunger_seconds = 10;

	// TODO: this should be in standalone , but what goes with it?
	@SubscribeEvent
	public void onPlayerWakeUpEvent(PlayerWakeUpEvent event){

		EntityPlayer entityPlayer = event.getEntityPlayer();

		if(event.getEntityPlayer().worldObj.isRemote == false){
			boolean didSleepAllNight = !event.updateWorld();

			if(didSleepAllNight){
				entityPlayer.addPotionEffect(new PotionEffect(MobEffects.hunger, sleeping_hunger_seconds * 20, levelBoost));
			}
		}
	}
}
