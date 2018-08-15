package com.lothrazar.cyclicmagic.compat.jei;

import java.util.ArrayList;
import java.util.List;
import com.lothrazar.cyclicmagic.block.dehydrator.RecipeDeHydrate;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.item.ItemStack;

public class DehydratorWrapper implements IRecipeWrapper {

  private RecipeDeHydrate src;

  public DehydratorWrapper(RecipeDeHydrate source) {
    this.src = source;
  }

  public ItemStack getOut() {
    return src.getRecipeOutput();
  }

  @Override
  public void getIngredients(IIngredients ingredients) {
    List<ItemStack> ing = new ArrayList<ItemStack>();
    //    for (ItemStack wtf : src.getInput()) {
    ing.add(src.getRecipeInput().copy());
    //    } 
    ingredients.setInputs(ItemStack.class, ing);
    ingredients.setOutput(ItemStack.class, src.getRecipeOutput());
  }
  //  public int size() {
  //    int size = 0;
  //    for (ItemStack s : src.getInput()) {
  //      if (s.isEmpty() == false) {
  //        size++;
  //      }
  //    }
  //    return size;
  //  }
}