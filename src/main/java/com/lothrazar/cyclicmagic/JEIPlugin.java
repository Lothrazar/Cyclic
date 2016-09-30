package com.lothrazar.cyclicmagic;

import com.lothrazar.cyclicmagic.gui.playerworkbench.ContainerPlayerExtWorkbench;
import mezz.jei.api.BlankModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.recipe.VanillaRecipeCategoryUid;

@mezz.jei.api.JEIPlugin
public class JEIPlugin extends BlankModPlugin {
  @Override
  public void register(IModRegistry registry) {

// thanks to http://www.minecraftforum.net/forums/mapping-and-modding/mapping-and-modding-tutorials/1571434-tutorial-modding-with-apis
    //and of course readme on https://github.com/mezz/JustEnoughItems

      // setup [+] feature
    //copied from source:

    /**
     * Basic method for adding a recipe transfer handler.
     *
     * @param containerClass     the class of the container that this recipe transfer handler is for
     * @param recipeCategoryUid  the recipe categories that this container can use 
     * @param recipeSlotStart    the first slot for recipe inputs // skip over the 1 output and the 4 armor slots
     * @param recipeSlotCount    the number of slots for recipe inputs //3x3
     * @param inventorySlotStart the first slot of the available inventory (usually player inventory) //top left
     * @param inventorySlotCount the number of slots of the available inventory //top right including hotbar
     */
      registry.getRecipeTransferRegistry().addRecipeTransferHandler(ContainerPlayerExtWorkbench.class, VanillaRecipeCategoryUid.CRAFTING, 5, 9, 14, 36);

  }
}
