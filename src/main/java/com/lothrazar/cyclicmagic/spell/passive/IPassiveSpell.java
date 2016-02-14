package com.lothrazar.cyclicmagic.spell.passive;

import net.minecraft.entity.player.EntityPlayer;

public interface IPassiveSpell{
	
	int getID();

	boolean canTrigger(EntityPlayer entity);

	void trigger(EntityPlayer entity);

	String info();
}
