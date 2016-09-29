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
      registry.getRecipeTransferRegistry().addRecipeTransferHandler(ContainerPlayerExtWorkbench.class, VanillaRecipeCategoryUid.CRAFTING, 1, 9, 10, 36);

  }
}
