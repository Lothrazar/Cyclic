package com.lothrazar.cyclicmagic.registry;

import com.lothrazar.cyclicmagic.util.Const;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;


public class RecipeRegistry{

	public static void register(){
		//todo:
		// IHasRecipe
		//loop on blocks/items, if they implement this then call
		
		
		GameRegistry.addShapelessRecipe(new ItemStack(Items.skull,4,Const.skull_player),
				new ItemStack(Items.skull,1,Const.skull_wither),
				new ItemStack(Items.skull,1,Const.skull_skeleton),
				new ItemStack(Items.skull,1,Const.skull_zombie),
				new ItemStack(Items.skull,1,Const.skull_creeper)
				);

		
		
		//top logs recipe

		//smoothstone block
		
	}
}
