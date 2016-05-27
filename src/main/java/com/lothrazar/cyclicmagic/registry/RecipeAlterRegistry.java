package com.lothrazar.cyclicmagic.registry;

import com.lothrazar.cyclicmagic.util.Const;
import com.lothrazar.cyclicmagic.util.UtilItem;
import com.lothrazar.cyclicmagic.util.UtilRecipe;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;

public class RecipeAlterRegistry {
	// does not handle recipes for items added new in the mod

	public static boolean enabled = true;

	public static void register() {
		if(enabled == false){
			return;
		}

		smoothstoneRequired();

		furnaceNeedsCoal();

	}

	public static void syncConfig(Configuration config) {

		String category = Const.ConfigCategory.recipes;

		enabled = config.get(category, "Altered Recipes Enabled", true,"Furnace requires coal in the middle, stone tools require smoothstone to be fully repaired").getBoolean();

	}

	private static void furnaceNeedsCoal() {

		UtilRecipe.removeRecipe(Blocks.FURNACE);

		GameRegistry.addRecipe(new ItemStack(Blocks.FURNACE), "bbb", "bcb", "bbb", 'b', Blocks.COBBLESTONE, 'c', Items.COAL);
	}

	private static void smoothstoneRequired() {

		UtilRecipe.removeRecipe(Items.STONE_PICKAXE);

//		GameRegistry.addRecipe(
//				new ItemStack(Items.STONE_PICKAXE, 1, UtilItem.getMaxDmgFraction(Items.STONE_PICKAXE, 4)),
//				"sss", " t ", " t ", 's', Blocks.COBBLESTONE, 't', Items.STICK);
		 
//		GameRegistry.addRecipe(new ItemStack(Items.STONE_PICKAXE, 1, UtilItem.getMaxDmgFraction(Items.STONE_PICKAXE, 4)), "sss", " t ", " t ", 's', Blocks.COBBLESTONE, 't', Items.STICK);
		GameRegistry.addRecipe(new ItemStack(Items.STONE_PICKAXE), 
				"sss", " t ", " t ", 's', Blocks.STONE, 't', Items.STICK);

		UtilRecipe.removeRecipe(Items.STONE_SWORD);

//		GameRegistry.addRecipe(new ItemStack(Items.STONE_SWORD, 1, UtilItem.getMaxDmgFraction(Items.STONE_SWORD, 4)), " s ", " s ", " t ", 's', Blocks.COBBLESTONE, 't', Items.STICK);
		GameRegistry.addRecipe(new ItemStack(Items.STONE_SWORD), " s ", " s ", " t ", 's', Blocks.STONE, 't', Items.STICK);

		UtilRecipe.removeRecipe(Items.STONE_AXE);

//		GameRegistry.addRecipe(new ItemStack(Items.STONE_AXE, 1, UtilItem.getMaxDmgFraction(Items.STONE_AXE, 4)), "ss ", "st ", " t ", 's', Blocks.COBBLESTONE, 't', Items.STICK);
//		GameRegistry.addRecipe(new ItemStack(Items.STONE_AXE, 1, UtilItem.getMaxDmgFraction(Items.STONE_AXE, 4)), " ss", " ts", " t ", 's', Blocks.COBBLESTONE, 't', Items.STICK);
		GameRegistry.addRecipe(new ItemStack(Items.STONE_AXE), "ss ", "st ", " t ", 's', Blocks.STONE, 't', Items.STICK);
		GameRegistry.addRecipe(new ItemStack(Items.STONE_AXE), " ss", " ts", " t ", 's', Blocks.STONE, 't', Items.STICK);

		UtilRecipe.removeRecipe(Items.STONE_HOE);

//		GameRegistry.addRecipe(new ItemStack(Items.STONE_HOE, 1, UtilItem.getMaxDmgFraction(Items.STONE_HOE, 4)), "ss ", " t ", " t ", 's', Blocks.COBBLESTONE, 't', Items.STICK);
//		GameRegistry.addRecipe(new ItemStack(Items.STONE_HOE, 1, UtilItem.getMaxDmgFraction(Items.STONE_HOE, 4)), " ss", " t ", " t ", 's', Blocks.COBBLESTONE, 't', Items.STICK);
		GameRegistry.addRecipe(new ItemStack(Items.STONE_HOE), "ss ", " t ", " t ", 's', Blocks.STONE, 't', Items.STICK);
		GameRegistry.addRecipe(new ItemStack(Items.STONE_HOE), " ss", " t ", " t ", 's', Blocks.STONE, 't', Items.STICK);

		UtilRecipe.removeRecipe(Items.STONE_SHOVEL);

//		GameRegistry.addRecipe(new ItemStack(Items.STONE_SHOVEL, 1, UtilItem.getMaxDmgFraction(Items.STONE_SHOVEL, 4)), " s ", " t ", " t ", 's', Blocks.COBBLESTONE, 't', Items.STICK);
		GameRegistry.addRecipe(new ItemStack(Items.STONE_SHOVEL), " s ", " t ", " t ", 's', Blocks.STONE, 't', Items.STICK);
	
	
		GameRegistry.addRecipe(new ItemStack(Items.IRON_CHESTPLATE, 1, UtilItem.getMaxDmgFraction(Items.IRON_CHESTPLATE, 2)), 
				"i i", "iii", "iii", 'i',Items.IRON_INGOT);

		GameRegistry.addShapelessRecipe(new ItemStack(Items.IRON_CHESTPLATE),
				new ItemStack(Items.IRON_CHESTPLATE, 1, OreDictionary.WILDCARD_VALUE),
				new ItemStack(Items.LEATHER_CHESTPLATE, 1, OreDictionary.WILDCARD_VALUE)
				);
	
	}

}
