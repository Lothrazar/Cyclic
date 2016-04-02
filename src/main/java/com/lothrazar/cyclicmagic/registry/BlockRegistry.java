package com.lothrazar.cyclicmagic.registry;

import java.util.ArrayList;
import com.lothrazar.cyclicmagic.block.*;
import com.lothrazar.cyclicmagic.util.Const;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class BlockRegistry{

	public static ArrayList<Block> blocks = new ArrayList<Block>();

	public static BlockFragile block_fragile;

	private static void registerBlock(Block s, String name){

		s.setUnlocalizedName(name);
		//s.setRegistryName(Const.MODRES , name);
		GameRegistry.registerBlock(s,ItemBlock.class,name);//
		
		//GameRegistry.register(s, new ResourceLocation(Const.MODRES + name));
		//GameRegistry.register(new ItemBlock(s), new ResourceLocation(Const.MODID , name));
		// // , new ResourceLocation(Const.MODID , name)
		s.setCreativeTab(ItemRegistry.tab);
		blocks.add(s);
	}

	public static void register(){

		block_fragile = new BlockFragile();
		BlockRegistry.registerBlock(block_fragile, "block_fragile");
		
	}
}
