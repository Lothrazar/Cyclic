package com.lothrazar.cyclicmagic.guide;

import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.common.brewing.BrewingRecipe;

public class GuidePage {

  public String text = null;
  public IRecipe recipe = null;
  public BrewingRecipe brewRecipe = null;

  public GuidePage(String t) {
    text = t;
  }

  public GuidePage(IRecipe t) {
    recipe = t;
  }

  public GuidePage(BrewingRecipe t) {
    brewRecipe = t;
  }
}