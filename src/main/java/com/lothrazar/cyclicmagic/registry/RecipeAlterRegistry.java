package com.lothrazar.cyclicmagic.registry;

import com.lothrazar.cyclicmagic.util.Const;
import com.lothrazar.cyclicmagic.util.UtilItem;
import com.lothrazar.cyclicmagic.util.UtilRecipe;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.registry.GameRegistry;


public class RecipeAlterRegistry{
	//does not handle recipes for items added new in the mod
	
	
	public static boolean enabled = true;

	public static void register(){

   		smoothstoneRequired();
   		
   		furnaceNeedsCoal();  
		
	}
	public static void syncConfig(Configuration config){

		String category = Const.MODCONF+ "new_recipes";
		

		config.setCategoryComment(category, "Tons of new recipes for existing blocks and items.  Bonemeal to undye wool; repeater and dispenser tweaks;  making player skulls out of the four mob heads...");

		enabled  = config.get(category, "enabled", true).getBoolean();
		
		
	}  


	private static void furnaceNeedsCoal(){ 
		
		UtilRecipe.removeRecipe(Blocks.furnace);

		GameRegistry.addRecipe(new ItemStack(Blocks.furnace), 
				"bbb",
				"bcb", 
				"bbb", 
				'b', Blocks.cobblestone,  
				'c', Items.coal );
	}
	 

	private static void smoothstoneRequired(){ 
	
		UtilRecipe.removeRecipe(Items.stone_pickaxe); 
		
		GameRegistry.addRecipe(new ItemStack(Items.stone_pickaxe,1,UtilItem.getMaxDmgFraction(Items.stone_pickaxe,4)), 
				"sss",
				" t ", 
				" t ", 
				's', Blocks.cobblestone,  
				't', Items.stick ); 
		GameRegistry.addRecipe(new ItemStack(Items.stone_pickaxe), 
				"sss",
				" t ", 
				" t ", 
				's', Blocks.stone,  
				't', Items.stick );

		UtilRecipe.removeRecipe(Items.stone_sword);

		GameRegistry.addRecipe(new ItemStack(Items.stone_sword,1,UtilItem.getMaxDmgFraction(Items.stone_sword,4)), 
				" s ",
				" s ", 
				" t ", 
				's', Blocks.cobblestone,  
				't', Items.stick );
		GameRegistry.addRecipe(new ItemStack(Items.stone_sword), 
				" s ",
				" s ", 
				" t ", 
				's', Blocks.stone,  
				't', Items.stick );
 
		UtilRecipe.removeRecipe(Items.stone_axe);

		GameRegistry.addRecipe(new ItemStack(Items.stone_axe,1,UtilItem.getMaxDmgFraction(Items.stone_axe,4)), 
				"ss ",
				"st ", 
				" t ", 
				's', Blocks.cobblestone,  
				't', Items.stick );
		GameRegistry.addRecipe(new ItemStack(Items.stone_axe,1,UtilItem.getMaxDmgFraction(Items.stone_axe,4)), 
				" ss", 
				" ts", 
				" t ", 
				's', Blocks.cobblestone,  
				't', Items.stick );
		GameRegistry.addRecipe(new ItemStack(Items.stone_axe), 
				"ss ",
				"st ", 
				" t ", 
				's', Blocks.stone,  
				't', Items.stick );
		GameRegistry.addRecipe(new ItemStack(Items.stone_axe), 
				" ss", 
				" ts", 
				" t ", 
				's', Blocks.stone,  
				't', Items.stick );

		UtilRecipe.removeRecipe(Items.stone_hoe);

		GameRegistry.addRecipe(new ItemStack(Items.stone_hoe,1,UtilItem.getMaxDmgFraction(Items.stone_hoe,4)), 
				"ss ",
				" t ", 
				" t ", 
				's', Blocks.cobblestone,  
				't', Items.stick ); 
		GameRegistry.addRecipe(new ItemStack(Items.stone_hoe,1,UtilItem.getMaxDmgFraction(Items.stone_hoe,4)), 
				" ss", 
				" t ", 
				" t ", 
				's', Blocks.cobblestone,  
				't', Items.stick );
		GameRegistry.addRecipe(new ItemStack(Items.stone_hoe), 
				"ss ",
				" t ", 
				" t ", 
				's', Blocks.stone,  
				't', Items.stick ); 
		GameRegistry.addRecipe(new ItemStack(Items.stone_hoe), 
				" ss", 
				" t ", 
				" t ", 
				's', Blocks.stone,  
				't', Items.stick );

		UtilRecipe.removeRecipe(Items.stone_shovel);

		GameRegistry.addRecipe(new ItemStack(Items.stone_shovel,1,
				UtilItem.getMaxDmgFraction(Items.stone_shovel,4)), 
				" s ",
				" t ", 
				" t ", 
				's', Blocks.cobblestone,  
				't', Items.stick );
		GameRegistry.addRecipe(new ItemStack(Items.stone_shovel), 
				" s ",
				" t ", 
				" t ", 
				's', Blocks.stone,  
				't', Items.stick );
	} 
	
}
