package com.lothrazar.cyclicmagic.registry;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFishFood;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;

public class BrewingRegistry {

	public static void register(){
		//thanks for the demos
		// https://github.com/MinecraftForge/MinecraftForge/blob/1.9/src/test/java/net/minecraftforge/test/BrewingRecipeRegistryTest.java

		//i guess PotionUtils doesnt work the way I thought
		//ItemStack mundane = PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM), PotionTypes.MUNDANE), 
//		ItemStack thick = BrewingRecipeRegistry.getOutput(new ItemStack(Items.POTIONITEM), new ItemStack(Items.GLOWSTONE_DUST));
//		ItemStack mundane = BrewingRecipeRegistry.getOutput(new ItemStack(Items.POTIONITEM), new ItemStack(Items.REDSTONE));
//		;
//
//		System.out.println("POTIONITEM="+(new ItemStack(Items.POTIONITEM)).getDisplayName());
//		System.out.println(" thick="+thick.getDisplayName());
//		System.out.println(" awkward="+awkward.getDisplayName());
//		System.out.println(" mundane="+mundane.getDisplayName());

		//the names imply these are correct. HOWEVER!!
		// there is a BUG: if i add a recipe and specify the input as exactly 'thick' 
		//then what actually happens is that EVERY SINGLE TYPE OF POTION works as input
		//so awkward, mundane, even regular potions. so thats messed, like its ignoring NBT values
		//thats probably becuase items dont use DAMAGE anymre, all are zero so brew reg cant tell

		//CANT USE input as  APPLES:
		// Inputs must have a max size of 1 just like water bottles. Brewing Stands override the input with the output when the brewing is done, items that stack would end up getting lost.
		
		//todo;:??ItemSplashPotion
		
		ItemStack awkward = BrewingRecipeRegistry.getOutput(new ItemStack(Items.POTIONITEM), new ItemStack(Items.NETHER_WART));
//		
		BrewingRecipeRegistry.addRecipe(
				awkward,
				new ItemStack(Items.DYE,1,EnumDyeColor.BLUE.getDyeDamage()),
				new ItemStack(ItemRegistry.itemMap.get("potion_viscous")));
		
		BrewingRecipeRegistry.addRecipe(
				new ItemStack(ItemRegistry.potion_viscous),
				new ItemStack(Items.FEATHER),
				new ItemStack(ItemRegistry.potion_levitation));

		BrewingRegistry.addRecipe(
				ItemRegistry.potion_viscous,
				Items.FEATHER,
				ItemRegistry.potion_levitation);
		
		BrewingRecipeRegistry.addRecipe(
				new ItemStack(ItemRegistry.potion_viscous),
				new ItemStack(Items.FISH,1,ItemFishFood.FishType.CLOWNFISH.getMetadata()),
				new ItemStack(ItemRegistry.potion_luck));

		BrewingRegistry.addRecipe(
				ItemRegistry.potion_viscous,
				Items.ENDER_EYE,
				ItemRegistry.potion_ender);

		BrewingRegistry.addRecipe(
				ItemRegistry.potion_ender,
				Items.REDSTONE,
				ItemRegistry.potion_ender_long);

		BrewingRegistry.addRecipe(
				ItemRegistry.potion_viscous,
				Items.EMERALD,
				ItemRegistry.potion_haste);
		
		BrewingRegistry.addRecipe(
				ItemRegistry.potion_haste,
				Items.REDSTONE,
				ItemRegistry.potion_haste_long);
		
		BrewingRegistry.addRecipe(
				ItemRegistry.potion_haste,
				Items.GLOWSTONE_DUST,
				ItemRegistry.potion_haste_strong);

		BrewingRegistry.addRecipe(
				ItemRegistry.potion_viscous,
				Items.PRISMARINE_CRYSTALS,
				ItemRegistry.potion_waterwalk);

		BrewingRegistry.addRecipe(
				ItemRegistry.potion_waterwalk,
				Items.REDSTONE,
				ItemRegistry.potion_waterwalk_long);

		BrewingRegistry.addRecipe(
				ItemRegistry.potion_viscous,
				Items.GOLDEN_APPLE,
				ItemRegistry.potion_boost);

		BrewingRegistry.addRecipe(
				ItemRegistry.potion_boost,
				Items.REDSTONE,
				ItemRegistry.potion_boost_long);
		
		BrewingRegistry.addRecipe(
				ItemRegistry.potion_viscous,
				Items.DIAMOND,
				ItemRegistry.potion_resistance);
		
		BrewingRegistry.addRecipe(
				ItemRegistry.potion_resistance,
				Items.REDSTONE,
				ItemRegistry.potion_resistance_long);
		
		BrewingRegistry.addRecipe(
				ItemRegistry.potion_resistance,
				Items.GLOWSTONE_DUST,
				ItemRegistry.potion_resistance_strong);
		
		BrewingRegistry.addRecipe(
				ItemRegistry.potion_viscous,
				Items.IRON_INGOT,
				ItemRegistry.potion_magnet);
		
		BrewingRegistry.addRecipe(
				ItemRegistry.potion_magnet,
				Items.REDSTONE,
				ItemRegistry.potion_magnet_long);
		
		BrewingRecipeRegistry.addRecipe(
				new ItemStack(ItemRegistry.potion_viscous),
				new ItemStack(Blocks.REDSTONE_LAMP),
				new ItemStack(ItemRegistry.potion_glowing));
		
		BrewingRecipeRegistry.addRecipe(
				new ItemStack(ItemRegistry.potion_glowing),
				new ItemStack(Items.REDSTONE),
				new ItemStack(ItemRegistry.potion_glowing_long));
		
		BrewingRecipeRegistry.addRecipe(
				new ItemStack(ItemRegistry.potion_viscous),
				new ItemStack(ItemRegistry.corrupted_chorus),
				new ItemStack(ItemRegistry.potion_slowfall));
		
		BrewingRecipeRegistry.addRecipe(
				new ItemStack(ItemRegistry.potion_slowfall),
				new ItemStack(Items.REDSTONE),
				new ItemStack(ItemRegistry.potion_slowfall_long));
	}

	private static void addRecipe(Item input, Item ingredient, Item output){
		 BrewingRecipeRegistry.addRecipe(
				 new ItemStack(input), 
				 new ItemStack(ingredient), 
				 new ItemStack(output));
	}
}
