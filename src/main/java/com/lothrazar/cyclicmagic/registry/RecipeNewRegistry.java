package com.lothrazar.cyclicmagic.registry;

import com.lothrazar.cyclicmagic.util.Const;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class RecipeNewRegistry {

	public static boolean enabled = true;

	public static void syncConfig(Configuration config) {

		// TODO: could config each one by one i guess but this is fine for now
		String category = Const.ConfigCategory.recipes;	
		config.setCategoryComment(category, "New and altered recipes");
		

		enabled = config.get(category, "New Recipes Enabled", true,"New recipes added for existing blocks and items.  Bonemeal to undye wool; repeater and dispenser tweaks;  making player skulls out of the four mob heads; create mushroom blocks from items...").getBoolean();

	}

	// static int EXP = 0;
	public static void register() {
		if(enabled == false){
			return;
		}

		RecipeNewRegistry.playerSkull();

		RecipeNewRegistry.mushroomBlocks();

		RecipeNewRegistry.bonemealWool();

		RecipeNewRegistry.simpleDispenser();

		RecipeNewRegistry.woolDyeSavings();

		RecipeNewRegistry.repeaterSimple();

		RecipeNewRegistry.minecartsSimple();

		RecipeNewRegistry.notchApple();
		
		RecipeNewRegistry.dirtToGravel();

		// https://github.com/PrinceOfAmber/SamsPowerups/blob/master/Recipes/src/main/java/com/lothrazar/samsrecipes/RecipeRegistry.java

	}

	private static void dirtToGravel() {
		GameRegistry.addShapelessRecipe(new ItemStack(Blocks.GRAVEL,2), 
				new ItemStack(Blocks.DIRT), 
				new ItemStack(Blocks.SAND));
		GameRegistry.addShapelessRecipe(new ItemStack(Items.FLINT), 
				new ItemStack(Blocks.GRAVEL), 
				new ItemStack(Blocks.GRAVEL));
	}

	private static void notchApple() {
		// https://www.reddit.com/r/minecraftsuggestions/comments/4d20g5/bring_back_the_notch_apple_crafting_recipe/
		GameRegistry.addRecipe(new ItemStack(Items.GOLDEN_APPLE, 1, 1), "ggg", "gag", "ggg", 'g', new ItemStack(Blocks.GOLD_BLOCK), 'a', new ItemStack(Items.APPLE));

	}

	private static void playerSkull() {

		GameRegistry.addShapelessRecipe(new ItemStack(Items.SKULL, 4, Const.skull_player), 
				new ItemStack(Items.SKULL, 1, Const.skull_wither), 
				new ItemStack(Items.SKULL, 1, Const.skull_skeleton), 
				new ItemStack(Items.SKULL, 1, Const.skull_zombie), 
				new ItemStack(Items.SKULL, 1, Const.skull_creeper));
	}

	private static void mushroomBlocks() {

		GameRegistry.addRecipe(new ItemStack(Blocks.RED_MUSHROOM_BLOCK), "mm", "mm", 'm', Blocks.RED_MUSHROOM);
		GameRegistry.addRecipe(new ItemStack(Blocks.BROWN_MUSHROOM_BLOCK), "mm", "mm", 'm', Blocks.BROWN_MUSHROOM);
	}

	private static void bonemealWool() {

		// use bonemeal to bleach colored wool back to white
		// its easy since we knwo white is 15, and other colours are all from zero
		// up to that
		// http://minecraft.gamepedia.com/Dye#Data_values
		// but wool is inverse: wool has zero for white
		for (int i = 0; i < Const.dye_bonemeal; i++)
			GameRegistry.addShapelessRecipe(new ItemStack(Blocks.WOOL, 1, Const.wool_white), new ItemStack(Blocks.WOOL, 1, i), new ItemStack(Items.DYE, 1, Const.dye_bonemeal));
	}

	private static void repeaterSimple() {

		GameRegistry.addRecipe(new ItemStack(Items.REPEATER), "r r", "srs", "ttt", 't', new ItemStack(Blocks.STONE), 's', new ItemStack(Items.STICK), 'r', new ItemStack(Items.REDSTONE));
	}

	private static void minecartsSimple() {

		// normally you would need the minecart created in a different step. this is
		// faster

		GameRegistry.addRecipe(new ItemStack(Items.CHEST_MINECART), "   ", "ici", "iii", 'i', Items.IRON_INGOT, 'c', Blocks.CHEST);

		GameRegistry.addRecipe(new ItemStack(Items.TNT_MINECART), "   ", "ici", "iii", 'i', Items.IRON_INGOT, 'c', Blocks.TNT);

		GameRegistry.addRecipe(new ItemStack(Items.HOPPER_MINECART), "   ", "ici", "iii", 'i', Items.IRON_INGOT, 'c', Blocks.HOPPER);

		GameRegistry.addRecipe(new ItemStack(Items.FURNACE_MINECART), "   ", "ici", "iii", 'i', Items.IRON_INGOT, 'c', Blocks.FURNACE);
	}

	private static void woolDyeSavings() {

		// so any color that is not white, add the new recipe with all 8 blocks
		for (int dye = 0; dye < 15; dye++)// only since we know that the dyes are
		                                  // these numbers
		{
			if (dye != Const.dye_bonemeal) {
				// removeRecipe(new ItemStack(Blocks.wool,1,dye));

				GameRegistry.addRecipe(new ItemStack(Blocks.WOOL, 8, dye), "www", "wdw", "www", 'w', new ItemStack(Blocks.WOOL, 1, Const.dye_bonemeal), 'd', new ItemStack(Items.DYE, 1, dye));
			}
		}
	}

	private static void simpleDispenser() {

		GameRegistry.addRecipe(new ItemStack(Blocks.DISPENSER), "ccc", "csc", "crc", 'c', Blocks.COBBLESTONE, 's', Items.STRING, 'r', Items.REDSTONE);
	}
}
