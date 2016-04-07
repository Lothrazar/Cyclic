package com.lothrazar.cyclicmagic.registry;

import com.lothrazar.cyclicmagic.block.TileEntityUncrafting;

import net.minecraftforge.fml.common.registry.GameRegistry;

public class TileEntityRegistry {

	public static void register(){

		GameRegistry.registerTileEntity(TileEntityUncrafting.class, "uncrafting_block_te");
	}
}
