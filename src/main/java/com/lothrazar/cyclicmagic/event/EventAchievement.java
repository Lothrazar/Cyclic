package com.lothrazar.cyclicmagic.event;

import com.lothrazar.cyclicmagic.IHasConfig;
import com.lothrazar.cyclicmagic.util.Const;
import com.lothrazar.cyclicmagic.util.UtilExperience;
import com.lothrazar.cyclicmagic.util.UtilSound;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.entity.player.AchievementEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EventAchievement implements IHasConfig {

	private boolean isEnabled;
	
	@SubscribeEvent
	public void onAchievement(AchievementEvent event){
		if(!isEnabled){
			return;
		}
		EntityPlayer p = event.getEntityPlayer();
	
		if(event.getAchievement().isAchievement() && p.hasAchievement(event.getAchievement()) == false){
		
			int mult = (event.getAchievement().getSpecial()) ? 100 : 1;
			
			int amt = mult * (p.worldObj.rand.nextInt(30) + 20);// between 20,30
			
			UtilExperience.setXp(p, (int)UtilExperience.getExpTotal(p) + amt );
			
			UtilSound.playSound(p, SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP);
		}
	}

	@Override
	public void syncConfig(Configuration config) {
		isEnabled = config.getBoolean("Exp Achievements", Const.ConfigCategory.player, true, "Get experience from achievements (random 20-30 exp, multiplied by 100 if it is flagged as special)");
	}
}
