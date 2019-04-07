package com.lothrazar.cyclicmagic.compat.jei;

import java.util.ArrayList;
import java.util.List;
import com.lothrazar.cyclicmagic.block.solidifier.RecipeSolidifier;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.item.ItemStack;

public class SolidifierWrapper implements IRecipeWrapper {

  private RecipeSolidifier src;

  public SolidifierWrapper(RecipeSolidifier source) {
    this.src = source;
  }

  public ItemStack getOut() {
    return src.getRecipeOutput();
  }

  public RecipeSolidifier getRecipe() {
    return src;
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

}