package com.lothrazar.cyclicmagic.proxy;

import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;


public class CommonProxy {
	public void register() {
	}

	public void displayGuiSpellbook()  {
		
		//server does nothing
	}

	public BlockPos getBlockMouseoverExact(int max) {
		///server side
		return null;
	}
	public BlockPos getBlockMouseoverOffset(int max) {
		///server side
		return null;
	}

	public EnumFacing getSideMouseover(int max) {
		return null;
	}
}
