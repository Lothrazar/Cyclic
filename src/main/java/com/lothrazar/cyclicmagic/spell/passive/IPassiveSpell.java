package com.lothrazar.cyclicmagic.spell.passive;

import java.util.ArrayList;
import net.minecraft.entity.player.EntityPlayer;

public interface IPassiveSpell {

	boolean canTrigger(EntityPlayer entity);
	void trigger(EntityPlayer entity);
	ArrayList<String> info();
}
