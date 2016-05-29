package com.lothrazar.cyclicmagic.registry;

import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
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
		ItemStack awkward = BrewingRecipeRegistry.getOutput(new ItemStack(Items.POTIONITEM), new ItemStack(Items.NETHER_WART));
//		
		BrewingRecipeRegistry.addRecipe(
				awkward,
				new ItemStack(Items.DYE,1,EnumDyeColor.BLUE.getDyeDamage()),
				new ItemStack(ItemRegistry.itemMap.get("potion_viscous")));
		
		BrewingRecipeRegistry.addRecipe(
				new ItemStack(ItemRegistry.itemMap.get("potion_viscous")),
				new ItemStack(Items.FEATHER),
				new ItemStack(ItemRegistry.itemMap.get("potion_levitation")));

		BrewingRegistry.addRecipe(
				ItemRegistry.itemMap.get("potion_viscous"),
				Items.FEATHER,
				ItemRegistry.itemMap.get("potion_levitation"));
	 
	}

	private static void addRecipe(Item input, Item ingredient, Item output){

		 BrewingRecipeRegistry.addRecipe(
				 new ItemStack(input), 
				 new ItemStack(ingredient), 
				 new ItemStack(output));
	}
}
