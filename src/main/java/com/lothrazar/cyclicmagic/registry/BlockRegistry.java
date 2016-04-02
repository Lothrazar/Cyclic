package com.lothrazar.cyclicmagic.registry;

import java.util.ArrayList;
import com.lothrazar.cyclicmagic.block.*;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class BlockRegistry{

	public static ArrayList<Block> blocks = new ArrayList<Block>();

	public static BlockFragile block_fragile;

	private static void registerBlock(Block s, String name){

		s.setUnlocalizedName(name);
		GameRegistry.registerBlock(s, name);
		s.setCreativeTab(ItemRegistry.tab);
		blocks.add(s);
	}

	public static void register(){

		block_fragile = new BlockFragile();
		BlockRegistry.registerBlock(block_fragile, "block_fragile");
		block_fragile.setCreativeTab(CreativeTabs.tabMisc);
		
	}
}
