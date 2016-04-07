package com.lothrazar.cyclicmagic.registry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.lothrazar.cyclicmagic.block.*;
import com.lothrazar.cyclicmagic.itemblock.ItemBlockBucket;
import com.lothrazar.cyclicmagic.util.Const;
import com.lothrazar.cyclicmagic.util.UtilUncraft;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class BlockRegistry{

	public static ArrayList<Block> blocks = new ArrayList<Block>();
	private static Map<String,Boolean> configToggle = new HashMap<String,Boolean>();
	public static BlockFragile block_fragile;
	public static BlockNetherGold nether_gold_ore;

	public static BlockBucketStorage block_storelava;
	public static BlockBucketStorage block_storewater;
	public static BlockBucketStorage block_storemilk;
	public static BlockBucketStorage block_storeempty;

	
	public static void registerBucketBlock(Block s, String name){

		s.setUnlocalizedName(name);

		GameRegistry.registerBlock(s, ItemBlockBucket.class, name);

		blocks.add(s);
	}
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
		
		
		BlockUncrafting uncrafting_block = new BlockUncrafting();
		uncrafting_block.setUnlocalizedName("uncrafting_block");
		GameRegistry.registerBlock(uncrafting_block, "uncrafting_block");

		GameRegistry.addRecipe(new ItemStack(uncrafting_block), 
				" r ", 
				"fdf", 
				" o ", 'o', Blocks.obsidian, 'f', Blocks.furnace, 'r', Blocks.dropper, 'd', Blocks.diamond_block);

		
		

		if(configToggle.get(BlockFragile.name)){

			block_fragile = new BlockFragile();
			BlockRegistry.registerBlock(block_fragile, BlockFragile.name);
		}
		
		nether_gold_ore = new BlockNetherGold();
		BlockRegistry.registerBlock(nether_gold_ore, BlockNetherGold.name);
		
		
		
		

		BlockRegistry.block_storewater = new BlockBucketStorage(Items.water_bucket);
		registerBucketBlock(BlockRegistry.block_storewater, "block_storewater");

		BlockRegistry.block_storemilk = new BlockBucketStorage(Items.milk_bucket);
		registerBucketBlock(BlockRegistry.block_storemilk, "block_storemilk");

		BlockRegistry.block_storelava = new BlockBucketStorage(Items.lava_bucket);
		registerBucketBlock(BlockRegistry.block_storelava, "block_storelava");

		GameRegistry.registerTileEntity(TileEntityBucketStorage.class, Const.MODID);

		BlockRegistry.block_storeempty = new BlockBucketStorage(null); 
		BlockRegistry.block_storeempty.setCreativeTab(CreativeTabs.tabMisc);
		registerBucketBlock(BlockRegistry.block_storeempty, "block_storeempty");

		BlockRegistry.block_storeempty.addRecipe();
		
	}

	public static void syncConfig(Configuration config){
		String category = Const.MODCONF+"blocks";
		
		config.setCategoryComment(category, "Blocks added to the game");
		
		configToggle.put(BlockFragile.name,config.getBoolean(BlockFragile.name, category, true, "Enable the scaffolding block that breaks by itself"));

		
		syncConfigUncrafting(config);
	}
	



	public static void syncConfigUncrafting(Configuration config) {
		String category = Configuration.CATEGORY_GENERAL;
		UtilUncraft.TIMER_FULL = config.getInt("speed_uncraft", category, 75, 10, 99999, "How fast this can uncraft items and blocks.  Lower numbers are faster");

		// blockIfCannotDoit = config.getBoolean("auto_block_slots", category,
		// true,
		// "Automatically block items from entering the slots if we cannot find a way to uncraft them.  If this is set to false. then items like flowers and such will be allowed in, but then spat right back out again.");

		UtilUncraft.dictionaryFreedom = config.getBoolean("pick_first_metadata", category, true, "If you change this to true, then the uncrafting will just take the first of many options in any recipe that takes multiple input types.  For example, false means chests cannot be uncrafted, but true means chests will ALWAYS give oak wooden planks.");

		category = "blacklist_items";
		config.addCustomCategoryComment(category, "Here you can blacklist any thing, vanilla or modded.  Mostly for creating modpacks.  Input means you cannot uncraft it at all.  Output means it will not come out of a recipe.");

		// so when uncrafting cake, you do not get milk buckets back
		String def = "";
		String csv = config.getString("blacklist_input", category, def, "Items that cannot be uncrafted; not allowed in the slots.  EXAMPLE : 'item.stick,tile.hayBlock,tile.chest'  ");
		// [item.stick, tile.cloth]
		UtilUncraft.blacklistInput = (List<String>) Arrays.asList(csv.split(","));
		if (UtilUncraft.blacklistInput == null)
			UtilUncraft.blacklistInput = new ArrayList<String>();

		def = "item.milk";
		csv = config.getString("blacklist_output", category, def, "Comma seperated items that cannot come out of crafting recipes.  For example, if milk is in here, then cake is uncrafted you get all items except the milk buckets.  ");

		UtilUncraft.blacklistOutput = (List<String>) Arrays.asList(csv.split(","));
		if (UtilUncraft.blacklistOutput == null)
			UtilUncraft.blacklistOutput = new ArrayList<String>();

	}
}
