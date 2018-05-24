package com.lothrazar.cyclicmagic.block.imbue;

import com.lothrazar.cyclicmagic.block.imbue.BlockImbue.ImbueFlavor;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.oredict.OreDictionary;

public class RecipeImbue {

  private NonNullList<ItemStack> recipeInput = NonNullList.withSize(2, ItemStack.EMPTY);
  ImbueFlavor flavor;

  public RecipeImbue(ItemStack[] in, ImbueFlavor action) {
    if (in.length != 1) {//more sizes later
      throw new IllegalArgumentException("Input array must be length 1");
    }
    recipeInput.set(0, in[0]);
    //  recipeInput.set(1, in[1]);
    this.flavor = action;
  }

  public static void initAllRecipes() {
    BlockImbue.addRecipe(new RecipeImbue(new ItemStack[] {
        new ItemStack(Items.SPIDER_EYE) },
        ImbueFlavor.FIRE));
    //
    BlockImbue.addRecipe(new RecipeImbue(new ItemStack[] {
        new ItemStack(Items.FEATHER) },
        ImbueFlavor.LEVITATE));
  }

  public boolean matches(ItemStack stackInSlot) {
    return OreDictionary.itemMatches(stackInSlot, recipeInput.get(0), false);
  }
}
