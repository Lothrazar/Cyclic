package com.lothrazar.cyclicmagic.registry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import com.lothrazar.cyclicmagic.ModMain;
import com.lothrazar.cyclicmagic.block.*;
import com.lothrazar.cyclicmagic.itemblock.ItemBlockBucket;
import com.lothrazar.cyclicmagic.util.Const;
import com.lothrazar.cyclicmagic.util.UtilUncraft;
import net.minecraft.block.Block;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class BlockRegistry {

	public static ArrayList<Block>		blocks							= new ArrayList<Block>();

	public static BlockFragile				block_fragile;
	public static BlockBucketStorage	block_storelava;
	public static BlockBucketStorage	block_storewater;
	public static BlockBucketStorage	block_storemilk;
	public static BlockBucketStorage	block_storeempty;

	public static BlockNetherOre			nether_gold_ore;
	public static BlockNetherOre			nether_coal_ore;
	public static BlockNetherOre			nether_lapis_ore;
	public static BlockNetherOre			nether_emerald_ore;

	public static BlockNetherOre			end_redstone_ore;
	public static BlockNetherOre			end_coal_ore;
	public static BlockNetherOre			end_lapis_ore;
	public static BlockNetherOre			end_emerald_ore;

	private static boolean						enabledBucketBlocks	= true;

	private static boolean						enableBlockFragile;

	private static boolean enableBlockUncrafting;

	@SuppressWarnings("rawtypes")
	public static void registerBlock(Block b, Class c, String name, boolean isHidden) {

		b.setUnlocalizedName(name);
		GameRegistry.registerBlock(b, c, name);

		if (isHidden == false) {
			b.setCreativeTab(ModMain.TAB);
		}

		// the new 'correct' undeprecated methods give
		/*
		 * [16:59:41] [Client thread/WARN] [FML]: * Dangerous alternative prefix
		 * cyclicmagic: for name block_fragile, invalid registry invocation/invalid
		 * name?
		 * [16:59:41] [Client thread/WARN] [FML]: * at
		 * net.minecraftforge.fml.common.registry.IForgeRegistryEntry$Impl.
		 * setRegistryName(IForgeRegistryEntry.java:63)
		 * [16:59:41] [Client thread/WARN] [FML]: * at
		 * net.minecraftforge.fml.common.registry.IForgeRegistryEntry$Impl.
		 * setRegistryName(IForgeRegistryEntry.java:72)
		 * [16:59:41] [Client thread/WARN] [FML]: * at
		 * com.lothrazar.cyclicmagic.registry.BlockRegistry.registerBlock(
		 * BlockRegistry.java:24)
		 * [16:59:41] [Client thread/WARN] [FML]: * at
		 * com.lothrazar.cyclicmagic.registry.BlockRegistry.register(BlockRegistry.
		 * java:40)
		 * [16:59:41] [Client thread/WARN] [FML]: * at
		 * com.lothrazar.cyclicmagic.ModMain.onInit(ModMain.java:70)
		 * [16:59:41] [Client thread/WARN] [FML]: * at
		 * sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)...
		 * 
		 * about:blank
		 *
		 */

		// block.setRegistryName(Const.MODID ,name);
		// GameRegistry.register(block);
		// GameRegistry.register(new ItemBlock(block), new
		// ResourceLocation(Const.MODID ,name));

		// if it worked like items, this would be donezo
		// GameRegistry.register(block, new ResourceLocation(Const.MODID,name));

		blocks.add(b);
	}

	private static void registerBlock(Block block, String name) {
		registerBlock(block, ItemBlock.class, name, false);
	}

	public static void register() {

		if(enableBlockUncrafting){
			BlockUncrafting uncrafting_block = new BlockUncrafting();
			registerBlock(uncrafting_block, "uncrafting_block");
		}

		if (enableBlockFragile) {

			block_fragile = new BlockFragile();
			registerBlock(block_fragile, BlockFragile.name);
		}

		if (WorldGenRegistry.netherOreEnabled) {

			nether_gold_ore = new BlockNetherOre(Items.gold_nugget, 0, 4);
			registerBlock(nether_gold_ore, "nether_gold_ore");

			nether_coal_ore = new BlockNetherOre(Items.coal);
			registerBlock(nether_coal_ore, "nether_coal_ore");

			nether_lapis_ore = new BlockNetherOre(Items.dye, EnumDyeColor.BLUE.getDyeDamage(), 3);
			registerBlock(nether_lapis_ore, "nether_lapis_ore");

			nether_emerald_ore = new BlockNetherOre(Items.emerald);
			registerBlock(nether_emerald_ore, "nether_emerald_ore");
		}

		if (WorldGenRegistry.endOreEnabled) {

			end_redstone_ore = new BlockNetherOre(Items.redstone);
			registerBlock(end_redstone_ore, "end_redstone_ore");

			end_coal_ore = new BlockNetherOre(Items.coal);
			registerBlock(end_coal_ore, "end_coal_ore");

			end_lapis_ore = new BlockNetherOre(Items.dye, EnumDyeColor.BLUE.getDyeDamage(), 3);
			registerBlock(end_lapis_ore, "end_lapis_ore");

			end_emerald_ore = new BlockNetherOre(Items.emerald);
			registerBlock(end_emerald_ore, "end_emerald_ore");
		}

		if (enabledBucketBlocks) {
			block_storewater = new BlockBucketStorage(Items.water_bucket);
			registerBlock(block_storewater, ItemBlockBucket.class, "block_storewater", true);

			block_storemilk = new BlockBucketStorage(Items.milk_bucket);
			registerBlock(block_storemilk, ItemBlockBucket.class, "block_storemilk", true);

			block_storelava = new BlockBucketStorage(Items.lava_bucket);
			registerBlock(block_storelava, ItemBlockBucket.class, "block_storelava", true);

			block_storeempty = new BlockBucketStorage(null);
			registerBlock(block_storeempty, ItemBlockBucket.class, "block_storeempty", false);

			// not irecipe so just like this is fine i guess
			block_storeempty.addRecipe();
		}
	}

	public static void syncConfig(Configuration config) {
		String category = Const.MODCONF + "Blocks";

		// TODO : requires restart
		config.setCategoryComment(category, "Blocks added to the game");

		enableBlockFragile = config.getBoolean(BlockFragile.name, category, true, "Enable the scaffolding block that breaks by itself");

		enabledBucketBlocks = config.getBoolean("Bucket Blocks", category, true, "Enable Bucket Storage Blocks");


		category = Const.MODCONF +  "Blocks.Uncrafting";
		
		enableBlockUncrafting = config.getBoolean("enabled", category, true, "Enable uncrafting");

		
		UtilUncraft.TIMER_FULL = config.getInt("speed_uncraft", category, 75, 10, 99999, "How fast this can uncraft items and blocks.  Lower numbers are faster");

		// blockIfCannotDoit = config.getBoolean("auto_block_slots", category,
		// true,
		// "Automatically block items from entering the slots if we cannot find a
		// way to uncraft them. If this is set to false. then items like flowers and
		// such will be allowed in, but then spat right back out again.");

		UtilUncraft.dictionaryFreedom = config.getBoolean("pick_first_metadata", category, true, "If you change this to true, then the uncrafting will just take the first of many options in any recipe that takes multiple input types.  For example, false means chests cannot be uncrafted, but true means chests will ALWAYS give oak wooden planks.");

		config.addCustomCategoryComment(category, "Here you can blacklist any thing, vanilla or modded.  Mostly for creating modpacks.  Input means you cannot uncraft it at all.  Output means it will not come out of a recipe.");

		// so when uncrafting cake, you do not get milk buckets back
		String def = "";
		String csv = config.getString("blacklist_input", category, def, "Items that cannot be uncrafted; not allowed in the slots.  EXAMPLE : 'item.stick,tile.hayBlock,tile.chest'  ");
		// [item.stick, tile.cloth]
		UtilUncraft.blacklistInput = (List<String>) Arrays.asList(csv.split(","));
		if (UtilUncraft.blacklistInput == null){
			UtilUncraft.blacklistInput = new ArrayList<String>();
		}
		def = "item.milk";
		csv = config.getString("blacklist_output", category, def, "Comma seperated items that cannot come out of crafting recipes.  For example, if milk is in here, then cake is uncrafted you get all items except the milk buckets.  ");

		UtilUncraft.blacklistOutput = (List<String>) Arrays.asList(csv.split(","));
		if (UtilUncraft.blacklistOutput == null) {
			UtilUncraft.blacklistOutput = new ArrayList<String>();
		}
	}
}
