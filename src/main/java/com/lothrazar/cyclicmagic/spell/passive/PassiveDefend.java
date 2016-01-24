package com.lothrazar.cyclicmagic.spell.passive;

import java.util.ArrayList;
import java.util.Arrays;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import com.lothrazar.cyclicmagic.Const;
import com.lothrazar.cyclicmagic.PotionRegistry;

public class PassiveDefend implements IPassiveSpell{
	private final static float HEALTHLIMIT = 10;//1 heart = 2 health
	private final static int SECONDS = 30;
	
	private static final ArrayList<String> info = new ArrayList<String>(Arrays.asList("passive.defend"));
	
	public void trigger(EntityPlayer entity){
		
		PotionRegistry.addOrMergePotionEffect(entity, new PotionEffect(Potion.absorption.id,SECONDS * Const.TICKS_PER_SEC, PotionRegistry.V));
	
		PotionRegistry.addOrMergePotionEffect(entity, new PotionEffect(Potion.resistance.id,SECONDS * Const.TICKS_PER_SEC, PotionRegistry.I));
	}
	
	@Override
	public boolean canTrigger(EntityPlayer entity) {
		return (entity.getHealth() <= HEALTHLIMIT) && (entity.isPotionActive(Potion.absorption.id) == false);
	}
	
	@Override
	public ArrayList<String> info() {
		return info;
	}
}
