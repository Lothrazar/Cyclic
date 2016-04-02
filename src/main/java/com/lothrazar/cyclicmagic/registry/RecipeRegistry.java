package com.lothrazar.cyclicmagic.registry;

import com.lothrazar.cyclicmagic.util.Const;
import com.lothrazar.cyclicmagic.util.UtilItem;
import com.lothrazar.cyclicmagic.util.UtilRecipe;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraft.init.Blocks;


public class RecipeRegistry{

	static int EXP = 0;
	public static void register(){

		RecipeRegistry.playerSkull();
		
		RecipeRegistry.mushroomBlocks(); 
		
		RecipeRegistry.bonemealWool();
		
		RecipeRegistry.simpleDispenser();
		
   		RecipeRegistry.smoothstoneRequired();
   		
   		RecipeRegistry.furnaceNeedsCoal();  
   		 
   		RecipeRegistry.woolDyeSavings();
		  
   		RecipeRegistry.repeaterSimple();
		
   		RecipeRegistry.minecartsSimple();


		//top logs recipe

		//smoothstone block
	//https://github.com/PrinceOfAmber/SamsPowerups/blob/master/Recipes/src/main/java/com/lothrazar/samsrecipes/RecipeRegistry.java	
 
	}

	private static void playerSkull(){

		GameRegistry.addShapelessRecipe(new ItemStack(Items.skull,4,Const.skull_player),
				new ItemStack(Items.skull,1,Const.skull_wither),
				new ItemStack(Items.skull,1,Const.skull_skeleton),
				new ItemStack(Items.skull,1,Const.skull_zombie),
				new ItemStack(Items.skull,1,Const.skull_creeper)
				);
	}
   		
	private static void mushroomBlocks(){

		GameRegistry.addRecipe(new ItemStack(Blocks.red_mushroom_block),
				"mm",
				"mm",
				'm', Blocks.red_mushroom);
		GameRegistry.addRecipe(new ItemStack(Blocks.brown_mushroom_block),
				"mm",
				"mm",
				'm', Blocks.brown_mushroom);
	} 
	
	private static void bonemealWool(){

		//use bonemeal to bleach colored wool back to white
		//its easy since we knwo white is 15, and other colours are all from zero up to that
		//  http://minecraft.gamepedia.com/Dye#Data_values
		//but wool is inverse: wool has zero for white
		for(int i = 0; i < Const.dye_bonemeal; i++)
		GameRegistry.addShapelessRecipe(new ItemStack(Blocks.wool, 1, Const.wool_white),
				new ItemStack(Blocks.wool, 1, i), new ItemStack(Items.dye, 1,Const.dye_bonemeal)); 
	}

	private static void repeaterSimple(){ 
	
		GameRegistry.addRecipe(new ItemStack(Items.repeater), "r r", "srs","ttt"
				, 't', new ItemStack(Blocks.stone)
				, 's', new ItemStack(Items.stick)
				, 'r', new ItemStack(Items.redstone) );
	}

	private static void minecartsSimple(){
	
		//normally you would need the minecart created in a different step. this is faster
 
		GameRegistry.addRecipe(new ItemStack(Items.chest_minecart), 
				"   ","ici", "iii", 
				'i', Items.iron_ingot,
				'c', Blocks.chest);
		 
		GameRegistry.addRecipe(new ItemStack(Items.tnt_minecart), 
				"   ","ici", "iii", 
				'i', Items.iron_ingot,
				'c', Blocks.tnt);

		GameRegistry.addRecipe(new ItemStack(Items.hopper_minecart), 
				"   ","ici", "iii", 
				'i', Items.iron_ingot,
				'c', Blocks.hopper);

		GameRegistry.addRecipe(new ItemStack(Items.furnace_minecart), 
				"   ","ici", "iii", 
				'i', Items.iron_ingot,
				'c', Blocks.furnace);
	}

	private static void woolDyeSavings(){

		//so any color that is not white, add the new recipe with all 8 blocks
		for(int dye = 0; dye < 15; dye++)//only since we know that the dyes are these numbers
		{ 
			if(dye != Const.dye_bonemeal)
			{
				//removeRecipe(new ItemStack(Blocks.wool,1,dye));	 	
			
				GameRegistry.addRecipe(new ItemStack(Blocks.wool,8,dye), 
						"www","wdw", "www", 
						'w', new ItemStack(Blocks.wool,1,Const.dye_bonemeal),
						'd', new ItemStack(Items.dye,1, dye));
			}
		} 
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
 
	private static void simpleDispenser() {
	
		GameRegistry.addRecipe(new ItemStack(Blocks.dispenser), 
				"ccc",
				"csc", 
				"crc", 
				'c', Blocks.cobblestone,  
				's', Items.string,
				'r', Items.redstone );  
	}
}
