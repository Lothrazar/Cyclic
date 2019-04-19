package com.lothrazar.cyclicmagic.compat.jei;

import java.util.ArrayList;
import java.util.List;
import com.lothrazar.cyclicmagic.block.melter.RecipeMelter;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.item.ItemStack;

public class MelterWrapper implements IRecipeWrapper {

  private RecipeMelter src;

  public MelterWrapper(RecipeMelter source) {
    this.src = source;
  }

  @Override
  public void getIngredients(IIngredients ingredients) {
    List<ItemStack> ing = new ArrayList<ItemStack>();
    for (ItemStack wtf : src.getRecipeInput()) {
      ing.add(wtf.copy());
    }
    ingredients.setInputs(VanillaTypes.ITEM, ing);
    ingredients.setOutput(VanillaTypes.ITEM, src.getRecipeOutput());
  }

  public RecipeMelter getRecipe() {
    return src;
  }
}