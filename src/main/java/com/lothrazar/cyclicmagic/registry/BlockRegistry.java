package com.lothrazar.cyclicmagic.registry;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Map;
import com.lothrazar.cyclicmagic.block.*;
import com.lothrazar.cyclicmagic.util.Const;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class BlockRegistry{

	public static ArrayList<Block> blocks = new ArrayList<Block>();
	private static Map<String,Boolean> configToggle = new HashMap<String,Boolean>();
	public static BlockFragile block_fragile;

	private static void registerBlock(Block block, String name){

		block.setUnlocalizedName(name);

		//the new 'correct' undeprecated methods give 
		/*[16:59:41] [Client thread/WARN] [FML]: * Dangerous alternative prefix cyclicmagic: for name block_fragile, invalid registry invocation/invalid name?
[16:59:41] [Client thread/WARN] [FML]: *  at net.minecraftforge.fml.common.registry.IForgeRegistryEntry$Impl.setRegistryName(IForgeRegistryEntry.java:63)
[16:59:41] [Client thread/WARN] [FML]: *  at net.minecraftforge.fml.common.registry.IForgeRegistryEntry$Impl.setRegistryName(IForgeRegistryEntry.java:72)
[16:59:41] [Client thread/WARN] [FML]: *  at com.lothrazar.cyclicmagic.registry.BlockRegistry.registerBlock(BlockRegistry.java:24)
[16:59:41] [Client thread/WARN] [FML]: *  at com.lothrazar.cyclicmagic.registry.BlockRegistry.register(BlockRegistry.java:40)
[16:59:41] [Client thread/WARN] [FML]: *  at com.lothrazar.cyclicmagic.ModMain.onInit(ModMain.java:70)
[16:59:41] [Client thread/WARN] [FML]: *  at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)...

*about:blank
*
*/
		 
		//block.setRegistryName(Const.MODID ,name);
		//GameRegistry.register(block);
		//GameRegistry.register(new ItemBlock(block), new ResourceLocation(Const.MODID ,name));

		
		//if it worked like items, this would be donezo
		//GameRegistry.register(block, new ResourceLocation(Const.MODID,name));
		
		GameRegistry.registerBlock(block,ItemBlock.class,name);


		block.setCreativeTab(ItemRegistry.tab);
		blocks.add(block);
	}
	
	public static void register(){

		if(configToggle.get(BlockFragile.name)){

			block_fragile = new BlockFragile();
			BlockRegistry.registerBlock(block_fragile, BlockFragile.name);
		}
	}

	public static void syncConfig(Configuration config){
		String category = "blocks";
		
		config.setCategoryComment(category, "Blocks added to the game");
		
		configToggle.put(BlockFragile.name,config.getBoolean(BlockFragile.name, category, true, "Enable the scaffolding block that breaks by itself"));

	}
}
