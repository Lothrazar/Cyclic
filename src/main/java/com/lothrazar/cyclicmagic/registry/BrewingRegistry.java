package com.lothrazar.cyclicmagic.registry;

import com.lothrazar.cyclicmagic.util.UtilNBT;

import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;

public class BrewingRegistry {

	public static void register(){
		//thanks for the demos
		// https://github.com/MinecraftForge/MinecraftForge/blob/1.9/src/test/java/net/minecraftforge/test/BrewingRecipeRegistryTest.java
		
		NBTTagCompound tag = UtilNBT.buildPotionTag(MobEffects.LUCK,PotionRegistry.II,900,"minecraft:luck");
		ItemStack luck = new ItemStack(Items.POTIONITEM);
		luck.setTagCompound(tag);
		
		//INPUT, TOPINGREDIENT, OUTPUT
		 BrewingRecipeRegistry.addRecipe(new ItemStack(Items.DIAMOND_HOE), 
				 new ItemStack(Items.ROTTEN_FLESH), 
				 luck);
//		 ItemStack output0 = BrewingRecipeRegistry.getOutput(new ItemStack(Items.DIAMOND_SWORD), new ItemStack(Items.ROTTEN_FLESH));
//	        if(output0.getItem() == Items.DIAMOND_HOE)
//	            System.out.println("Recipe succefully registered and working. Diamond Hoe obtained.");
	}
}
