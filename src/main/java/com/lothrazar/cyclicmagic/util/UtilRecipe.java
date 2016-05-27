package com.lothrazar.cyclicmagic.util;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.oredict.OreDictionary;

public class UtilRecipe {

	public static void removeRecipe(Item resultItem) {
		removeRecipe(new ItemStack(resultItem,1,OreDictionary.WILDCARD_VALUE));
	}

	public static void removeRecipe(Block resultItem) {
		removeRecipe(new ItemStack(resultItem,1,OreDictionary.WILDCARD_VALUE));
	}

	public static void removeRecipe(ItemStack resultItem) {
		// REFERENCES
		// http://www.minecraftforge.net/forum/index.php/topic,7146.0.html
		// http://stackoverflow.com/questions/27459815/minecraft-forge-1-7-10-removing-recipes-from-id

		int countRemoved = 0;
		List<IRecipe> recipes = CraftingManager.getInstance().getRecipeList();
		IRecipe tmpRecipe;
		ItemStack recipeResult;
		for (int i = 0; i < recipes.size(); i++) {
			tmpRecipe = recipes.get(i);

			recipeResult = tmpRecipe.getRecipeOutput(); 
//??
			if (recipeResult != null && ItemStack.areItemStacksEqual(resultItem, 
					new ItemStack(recipeResult.getItem(),1,OreDictionary.WILDCARD_VALUE))) {
				recipes.remove(i--);
				countRemoved++;
			}
		}
		
		/*]: REMOVED :0 FOR ITEM item.pickaxeStone
[22:58:23] [Client thread/INFO] [STDOUT]: [com.lothrazar.cyclicmagic.util.UtilRecipe:removeRecipe:42]: REMOVED :0 FOR ITEM item.swordStone
[22:58:23] [Client thread/INFO] [STDOUT]: [com.lothrazar.cyclicmagic.util.UtilRecipe:removeRecipe:42]: REMOVED :0 FOR ITEM item.hatchetStone
[22:58:23] [Client thread/INFO] [STDOUT]: [com.lothrazar.cyclicmagic.util.UtilRecipe:removeRecipe:42]: REMOVED :0 FOR ITEM item.hoeStone
[22:58:23] [Client thread/INFO] [STDOUT]: [com.lothrazar.cyclicmagic.util.UtilRecipe:removeRecipe:42]: REMOVED :0 FOR ITEM item.shovelStone
[22:58:23] [Client thread/INFO] [STDOUT]: [com.lothrazar.cyclicmagic.util.UtilRecipe:removeRecipe:42]: REMOVED :0 FOR ITEM tile.furnace*/
		System.out.println("REMOVED :"+countRemoved+" FOR ITEM "+resultItem.getItem().getUnlocalizedName());
		
	}
}
