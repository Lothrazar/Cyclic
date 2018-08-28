/*******************************************************************************
 * The MIT License (MIT)
 * 
 * Copyright (C) 2014-2018 Sam Bassett (aka Lothrazar)
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 ******************************************************************************/
package com.lothrazar.cyclicmagic.compat.jei;

import com.lothrazar.cyclicmagic.block.crafter.ContainerCrafter;
import com.lothrazar.cyclicmagic.block.dehydrator.ContainerDeHydrator;
import com.lothrazar.cyclicmagic.block.dehydrator.GuiDeHydrator;
import com.lothrazar.cyclicmagic.block.dehydrator.RecipeDeHydrate;
import com.lothrazar.cyclicmagic.block.hydrator.ContainerHydrator;
import com.lothrazar.cyclicmagic.block.hydrator.GuiHydrator;
import com.lothrazar.cyclicmagic.block.hydrator.RecipeHydrate;
import com.lothrazar.cyclicmagic.block.packager.ContainerPackager;
import com.lothrazar.cyclicmagic.block.packager.GuiPackager;
import com.lothrazar.cyclicmagic.block.packager.RecipePackage;
import com.lothrazar.cyclicmagic.block.workbench.ContainerWorkBench;
import com.lothrazar.cyclicmagic.playerupgrade.crafting.ContainerPlayerExtWorkbench;
import com.lothrazar.cyclicmagic.registry.ItemRegistry;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import mezz.jei.api.recipe.VanillaRecipeCategoryUid;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

@mezz.jei.api.JEIPlugin
public class JEIPlugin implements IModPlugin { // extends mezz.jei.api.BlankModPlugin {

  static final String RECIPE_CATEGORY_HYDRATOR = "hydrator";
  static final String RECIPE_CATEGORY_DEHYDRATOR = "dehydrator";
  static final String RECIPE_CATEGORY_PACKAGER = "packager";

  @SuppressWarnings("deprecation")
  @Override
  public void register(IModRegistry registry) {
    ////////////////first register all crafting GUI's
    // thanks to http://www.minecraftforum.net/forums/mapping-and-modding/mapping-and-modding-tutorials/1571434-tutorial-modding-with-apis
    //and of course readme on https://github.com/mezz/JustEnoughItems
    // setup [+] feature
    registry.getRecipeTransferRegistry().addRecipeTransferHandler(ContainerPlayerExtWorkbench.class, VanillaRecipeCategoryUid.CRAFTING,
        6, // @param recipeSlotStart    the first slot for recipe inputs // skip over the 1 output and the 5 armor slots
        9, // @param recipeSlotCount    the number of slots for recipe inputs //3x3
        15, //@param inventorySlotStart the first slot of the available inventory (usually player inventory) =9+6
        36);//@param inventorySlotCount the number of slots of the available inventory //top right including hotbar =4*9
    registry.getRecipeTransferRegistry().addRecipeTransferHandler(ContainerCrafter.class, VanillaRecipeCategoryUid.CRAFTING,
        10, // @param recipeSlotStart    the first slot for recipe inputs // skip over the 10 INPUT
        9, // @param recipeSlotCount    the number of slots for recipe inputs //3x3
        29, //@param inventorySlotStart the first slot of the available inventory (usually player inventory) =9+6
        4 * 9);//@param inventorySlotCount the number of slots of the available inventory //top right including hotbar =4*9
    registry.getRecipeTransferRegistry().addRecipeTransferHandler(ContainerWorkBench.class, VanillaRecipeCategoryUid.CRAFTING,
        1, // @param recipeSlotStart    the first slot for recipe inputs // skip over the 1 output and the 5 armor slots
        9, // @param recipeSlotCount    the number of slots for recipe inputs //3x3
        10, //@param inventorySlotStart the first slot of the available inventory (usually player inventory) =9
        4 * 9);//@param inventorySlotCount the number of slots of the available inventory //top right including hotbar =4*9
    registry.getRecipeTransferRegistry().addRecipeTransferHandler(ContainerHydrator.class, RECIPE_CATEGORY_HYDRATOR,
        0, // @param recipeSlotStart    the first slot for recipe inputs // skip over the 1 output and the 5 armor slots
        4, // @param recipeSlotCount    the number of slots for recipe inputs //2x2
        8, //@param inventorySlotStart the first slot of the available inventory (usually player inventory) =9
        4 * 9);//@param inventorySlotCount the number of slots of the available inventory //top right including hotbar =4*9
    registry.getRecipeTransferRegistry().addRecipeTransferHandler(ContainerPackager.class, RECIPE_CATEGORY_PACKAGER,
        0, // @param recipeSlotStart    the first slot for recipe inputs // skip over the 1 output and the 5 armor slots
        6, // @param recipeSlotCount    the number of slots for recipe inputs //2x2
        8, //@param inventorySlotStart the first slot of the available inventory (usually player inventory) =9
        4 * 9);//@param inventorySlotCount the number of slots of the available inventory //top right RECIPE_CATEGORY_DEHYDRATOR hotbar =4*9
    registry.getRecipeTransferRegistry().addRecipeTransferHandler(ContainerDeHydrator.class, RECIPE_CATEGORY_PACKAGER,
        0, // @param recipeSlotStart    the first slot for recipe inputs // skip over the 1 output and the 5 armor slots
        1, // @param recipeSlotCount    the number of slots for recipe inputs 
        8, //@param inventorySlotStart the first slot of the available inventory (usually player inventory) =9
        4 * 9);//@param inventorySlotCount the number of slots of the available inventory //top right including hotbar =4*9
    // Start Custom recipe type: Hydrator
    registry.addRecipeClickArea(GuiHydrator.class, 75, 0, 40, 26, RECIPE_CATEGORY_HYDRATOR);
    registry.handleRecipes(RecipeHydrate.class, new HydratorFactory(), RECIPE_CATEGORY_HYDRATOR);
    registry.addRecipes(RecipeHydrate.recipes, RECIPE_CATEGORY_HYDRATOR);
    registry.addRecipeCategoryCraftingItem(new ItemStack(Block.getBlockFromName("cyclicmagic:block_hydrator")), RECIPE_CATEGORY_HYDRATOR);
    // End Custom recipe type: Hydrator
    // Packager
    registry.addRecipeClickArea(GuiPackager.class, 75, 0, 40, 26, RECIPE_CATEGORY_PACKAGER);
    registry.handleRecipes(RecipePackage.class, new PackagerFactory(), RECIPE_CATEGORY_PACKAGER);
    registry.addRecipes(RecipePackage.recipes, RECIPE_CATEGORY_PACKAGER);
    registry.addRecipeCategoryCraftingItem(new ItemStack(Block.getBlockFromName("cyclicmagic:auto_packager")), RECIPE_CATEGORY_PACKAGER);
    //DEHydrator
    registry.addRecipeClickArea(GuiDeHydrator.class, 75, 0, 40, 26, RECIPE_CATEGORY_DEHYDRATOR);
    registry.handleRecipes(RecipeDeHydrate.class, new DehydratorFactory(), RECIPE_CATEGORY_DEHYDRATOR);
    registry.addRecipes(RecipeDeHydrate.recipes, RECIPE_CATEGORY_DEHYDRATOR);
    registry.addRecipeCategoryCraftingItem(new ItemStack(Block.getBlockFromName("cyclicmagic:dehydrator")), RECIPE_CATEGORY_DEHYDRATOR);
    //Start of the Info tab
    for (Item item : ItemRegistry.itemList) {
      //YES its deprecated. but new method is NOT in wiki. at all. 
      // i found something similar... and didnt work when i tried
      //https://github.com/mezz/JustEnoughItems/wiki/Recipes-Overview
      registry.addDescription(new ItemStack(item), item.getUnlocalizedName() + ".guide");
    }
    //end of Info tab
  }

  @Override
  public void registerCategories(IRecipeCategoryRegistration registry) {
    registry.addRecipeCategories(new HydratorRecipeCategory(registry.getJeiHelpers().getGuiHelper()));
    registry.addRecipeCategories(new PackagerRecipeCategory(registry.getJeiHelpers().getGuiHelper()));
    registry.addRecipeCategories(new DehydratorRecipeCategory(registry.getJeiHelpers().getGuiHelper()));
  }
}
