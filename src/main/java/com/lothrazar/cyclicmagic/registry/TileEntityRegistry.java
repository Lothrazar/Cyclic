package com.lothrazar.cyclicmagic.registry;

import com.lothrazar.cyclicmagic.block.tileentity.TileEntityBucketStorage;
import com.lothrazar.cyclicmagic.block.tileentity.TileEntityBuilder;
import com.lothrazar.cyclicmagic.block.tileentity.TileEntityUncrafting;

import net.minecraftforge.fml.common.registry.GameRegistry;

public class TileEntityRegistry {

	public static void register() {

		GameRegistry.registerTileEntity(TileEntityUncrafting.class, "uncrafting_block_te");
		GameRegistry.registerTileEntity(TileEntityBuilder.class, "builder_te");
		GameRegistry.registerTileEntity(TileEntityBucketStorage.class, "bucketstorage");
	}
}
