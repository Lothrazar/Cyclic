package com.lothrazar.cyclicmagic.compat.jei;

import java.util.ArrayList;
import java.util.List;
import com.lothrazar.cyclicmagic.block.hydrator.RecipeHydrate;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.item.ItemStack;

public class HydratorWrapper implements IRecipeWrapper {

  RecipeHydrate src;

  public HydratorWrapper(RecipeHydrate source) {
    this.src = source;
  }

  public ItemStack getOut() {
    return src.getRecipeOutput();
  }

  @Override
  public void getIngredients(IIngredients ingredients) {
    List<ItemStack> ing = new ArrayList<ItemStack>();
    for (ItemStack wtf : src.getRecipeInput()) {
      ing.add(wtf.copy());
    }
    ingredients.setInputs(VanillaTypes.ITEM, ing);
    ingredients.setOutput(VanillaTypes.ITEM, src.getRecipeOutput());
    ingredients.setInput(VanillaTypes.FLUID, src.getFluidIngredient());
  }

}