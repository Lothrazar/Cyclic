package com.lothrazar.cyclicmagic.compat.jei;

import java.util.ArrayList;
import java.util.List;
import com.lothrazar.cyclicmagic.block.hydrator.RecipeHydrate;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.item.ItemStack;

public class HydratorWrapper implements IRecipeWrapper {

  private RecipeHydrate src;

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
    ingredients.setInputs(ItemStack.class, ing);
    ingredients.setOutput(ItemStack.class, src.getRecipeOutput());
  }

  public int size() {
    int size = 0;
    for (ItemStack s : src.getRecipeInput()) {
      if (s.isEmpty() == false) {
        size++;
      }
    }
    return size;
  }
}