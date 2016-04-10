package com.lothrazar.cyclicmagic.registry;

import com.lothrazar.cyclicmagic.util.Const;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraft.init.Blocks;

public class RecipeNewRegistry {

	public static boolean enabled = true;

	public static void syncConfig(Configuration config) {

		// TODO: could config each one by one i guess but this is fine for now
		String category = Const.MODCONF + "Recipes";	
		config.setCategoryComment(category, "New and altered recipes");
		

		enabled = config.get(category, "New Recipes Enabled", true,"New recipes added for existing blocks and items.  Bonemeal to undye wool; repeater and dispenser tweaks;  making player skulls out of the four mob heads; create mushroom blocks from items...").getBoolean();

	}

	// static int EXP = 0;
	public static void register() {

		RecipeNewRegistry.playerSkull();

		RecipeNewRegistry.mushroomBlocks();

		RecipeNewRegistry.bonemealWool();

		RecipeNewRegistry.simpleDispenser();

		RecipeNewRegistry.woolDyeSavings();

		RecipeNewRegistry.repeaterSimple();

		RecipeNewRegistry.minecartsSimple();

		RecipeNewRegistry.notchApple();

		// https://github.com/PrinceOfAmber/SamsPowerups/blob/master/Recipes/src/main/java/com/lothrazar/samsrecipes/RecipeRegistry.java

	}

	private static void notchApple() {
		// https://www.reddit.com/r/minecraftsuggestions/comments/4d20g5/bring_back_the_notch_apple_crafting_recipe/
		GameRegistry.addRecipe(new ItemStack(Items.golden_apple, 1, 1), "ggg", "gag", "ggg", 'g', new ItemStack(Blocks.gold_block), 'a', new ItemStack(Items.apple));

	}

	private static void playerSkull() {

		GameRegistry.addShapelessRecipe(new ItemStack(Items.skull, 4, Const.skull_player), new ItemStack(Items.skull, 1, Const.skull_wither), new ItemStack(Items.skull, 1, Const.skull_skeleton), new ItemStack(Items.skull, 1, Const.skull_zombie), new ItemStack(Items.skull, 1, Const.skull_creeper));
	}

	private static void mushroomBlocks() {

		GameRegistry.addRecipe(new ItemStack(Blocks.red_mushroom_block), "mm", "mm", 'm', Blocks.red_mushroom);
		GameRegistry.addRecipe(new ItemStack(Blocks.brown_mushroom_block), "mm", "mm", 'm', Blocks.brown_mushroom);
	}

	private static void bonemealWool() {

		// use bonemeal to bleach colored wool back to white
		// its easy since we knwo white is 15, and other colours are all from zero
		// up to that
		// http://minecraft.gamepedia.com/Dye#Data_values
		// but wool is inverse: wool has zero for white
		for (int i = 0; i < Const.dye_bonemeal; i++)
			GameRegistry.addShapelessRecipe(new ItemStack(Blocks.wool, 1, Const.wool_white), new ItemStack(Blocks.wool, 1, i), new ItemStack(Items.dye, 1, Const.dye_bonemeal));
	}

	private static void repeaterSimple() {

		GameRegistry.addRecipe(new ItemStack(Items.repeater), "r r", "srs", "ttt", 't', new ItemStack(Blocks.stone), 's', new ItemStack(Items.stick), 'r', new ItemStack(Items.redstone));
	}

	private static void minecartsSimple() {

		// normally you would need the minecart created in a different step. this is
		// faster

		GameRegistry.addRecipe(new ItemStack(Items.chest_minecart), "   ", "ici", "iii", 'i', Items.iron_ingot, 'c', Blocks.chest);

		GameRegistry.addRecipe(new ItemStack(Items.tnt_minecart), "   ", "ici", "iii", 'i', Items.iron_ingot, 'c', Blocks.tnt);

		GameRegistry.addRecipe(new ItemStack(Items.hopper_minecart), "   ", "ici", "iii", 'i', Items.iron_ingot, 'c', Blocks.hopper);

		GameRegistry.addRecipe(new ItemStack(Items.furnace_minecart), "   ", "ici", "iii", 'i', Items.iron_ingot, 'c', Blocks.furnace);
	}

	private static void woolDyeSavings() {

		// so any color that is not white, add the new recipe with all 8 blocks
		for (int dye = 0; dye < 15; dye++)// only since we know that the dyes are
		                                  // these numbers
		{
			if (dye != Const.dye_bonemeal) {
				// removeRecipe(new ItemStack(Blocks.wool,1,dye));

				GameRegistry.addRecipe(new ItemStack(Blocks.wool, 8, dye), "www", "wdw", "www", 'w', new ItemStack(Blocks.wool, 1, Const.dye_bonemeal), 'd', new ItemStack(Items.dye, 1, dye));
			}
		}
	}

	private static void simpleDispenser() {

		GameRegistry.addRecipe(new ItemStack(Blocks.dispenser), "ccc", "csc", "crc", 'c', Blocks.cobblestone, 's', Items.string, 'r', Items.redstone);
	}
}
