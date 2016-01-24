package com.lothrazar.cyclicmagic.spell.passive;

import java.util.ArrayList;
import java.util.Arrays;
import com.lothrazar.cyclicmagic.Const;
import com.lothrazar.cyclicmagic.PotionRegistry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

public class PassiveBurn  implements IPassiveSpell{
	private final static int SECONDS = 30;
	
	private static final ArrayList<String> info = new ArrayList<String>(Arrays.asList("passive.burn"));
	@Override
	public boolean canTrigger(EntityPlayer entity) {
		return (entity.isBurning() && entity.isPotionActive(Potion.fireResistance.id) == false);
	}

	@Override
	public void trigger(EntityPlayer entity) {
		PotionRegistry.addOrMergePotionEffect(entity, new PotionEffect(Potion.fireResistance.id,SECONDS * Const.TICKS_PER_SEC, PotionRegistry.V));	
	
	
		//TODO: fire prot on your mount?
	}

	@Override
	public ArrayList<String> info() {
		return info;
	}
}
