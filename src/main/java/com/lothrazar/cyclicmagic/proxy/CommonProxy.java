package com.lothrazar.cyclicmagic.proxy;

import net.minecraft.util.BlockPos;


public class CommonProxy {
	public void register() {
	}

	public void displayGuiSpellbook()  {
		
		//server does nothing
	}

	public BlockPos getBlockMouseover(int max) {
		///server side
		return null;
	}
}
