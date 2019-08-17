package com.lothrazar.cyclic.setup;

import net.minecraft.world.World;

public class ServerProxy implements IProxy{
	@Override
	public World getClientWorld() {
		 throw new IllegalStateException("Client side only code on the server");
	}
}
