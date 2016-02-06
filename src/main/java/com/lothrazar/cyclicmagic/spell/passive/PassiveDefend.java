package com.lothrazar.cyclicmagic.spell.passive;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.StatCollector;
import com.lothrazar.cyclicmagic.Const;
import com.lothrazar.cyclicmagic.PotionRegistry;

public class PassiveDefend implements IPassiveSpell{
	private static final String info = "passive.defend";
	private final static float HEALTHLIMIT = 10;//1 heart = 2 health
	private final static int SECONDS = 30;
	 
	
	public void trigger(EntityPlayer entity){
		
		PotionRegistry.addOrMergePotionEffect(entity, new PotionEffect(Potion.absorption.id,SECONDS * Const.TICKS_PER_SEC, PotionRegistry.V));
	
		PotionRegistry.addOrMergePotionEffect(entity, new PotionEffect(Potion.resistance.id,SECONDS * Const.TICKS_PER_SEC, PotionRegistry.I));
	}
	
	@Override
	public boolean canTrigger(EntityPlayer entity) {
		return (entity.getHealth() <= HEALTHLIMIT) && (entity.isPotionActive(Potion.absorption.id) == false);
	}
	
	@Override
	public String info() {
		return StatCollector.translateToLocal(info);
	}
}
