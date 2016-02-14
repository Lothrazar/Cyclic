package com.lothrazar.cyclicmagic.spell.passive;

import net.minecraft.entity.player.EntityPlayer;

public interface IPassiveSpell{
	
	int getID();

	public String getName();

	public String getInfo();
	
	boolean canTrigger(EntityPlayer entity);

	void trigger(EntityPlayer entity);
}
