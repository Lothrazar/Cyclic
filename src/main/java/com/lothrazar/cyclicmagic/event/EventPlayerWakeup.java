package com.lothrazar.cyclicmagic.event;

import com.lothrazar.cyclicmagic.registry.PotionRegistry;
import com.lothrazar.cyclicmagic.util.Const;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.entity.player.PlayerWakeUpEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EventPlayerWakeup  implements IFeatureEvent{

	int	levelBoost							= PotionRegistry.I;	// 1 means Hunger II. int = 2
	                                                // means Hunger III, etc.
	int	sleeping_hunger_seconds	= 30;

	// TODO: this should be in standalone , but what goes with it?
	@SubscribeEvent
	public void onPlayerWakeUpEvent(PlayerWakeUpEvent event) {

		EntityPlayer entityPlayer = event.getEntityPlayer();
		boolean didSleepAllNight = !event.updateWorld();

		if (entityPlayer.worldObj.isRemote == false && didSleepAllNight) {

			entityPlayer.addPotionEffect(new PotionEffect(MobEffects.hunger, sleeping_hunger_seconds * Const.TICKS_PER_SEC, levelBoost));
			entityPlayer.addPotionEffect(new PotionEffect(MobEffects.digSlowdown, sleeping_hunger_seconds * Const.TICKS_PER_SEC, levelBoost));
			entityPlayer.addPotionEffect(new PotionEffect(MobEffects.weakness, sleeping_hunger_seconds * Const.TICKS_PER_SEC, levelBoost));
		}
	}

	@Override
	public void syncConfig(Configuration config) {
		// TODO Auto-generated method stub
		
	}
}
