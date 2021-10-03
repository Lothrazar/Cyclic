package com.lothrazar.cyclic.compat.jei;

import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.block.solidifier.RecipeSolidifier;
import com.lothrazar.cyclic.data.Const;
import com.lothrazar.cyclic.recipe.CyclicRecipeType;
import com.lothrazar.cyclic.registry.BlockRegistry;
import com.lothrazar.cyclic.util.UtilChat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidAttributes;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;

@SuppressWarnings("rawtypes")
public class SolidifierRecipeCategory implements IRecipeCategory<RecipeSolidifier> {

  static final ResourceLocation ID = new ResourceLocation(CyclicRecipeType.SOLID.toString());
  private IDrawable gui;
  private IDrawable icon;

  public SolidifierRecipeCategory(IGuiHelper helper) {
    gui = helper.drawableBuilder(new ResourceLocation(ModCyclic.MODID, "textures/jei/solidifier_recipe.png"), 0, 0, 169, 69).setTextureSize(169, 69).build();
    icon = helper.createDrawableIngredient(new ItemStack(BlockRegistry.SOLIDIFIER));
  }

  @Override
  public String getTitle() {
    return UtilChat.lang(BlockRegistry.SOLIDIFIER.getTranslationKey());
  }

  @Override
  public IDrawable getIcon() {
    return icon;
  }

  @Override
  public IDrawable getBackground() {
    return gui;
  }

  @Override
  public Class<? extends RecipeSolidifier> getRecipeClass() {
    return RecipeSolidifier.class;
  }

  @Override
  public ResourceLocation getUid() {
    return ID;
  }

  @Override
  public void setIngredients(RecipeSolidifier recipe, IIngredients ingredients) {
    ingredients.setInput(VanillaTypes.FLUID, recipe.getRecipeFluid());
    List<List<ItemStack>> in = new ArrayList<>();
    List<ItemStack> stuff = new ArrayList<>();
    //    for (int i = 0; i <= 2; i++) {
    //test without loop just in case?
    Collections.addAll(stuff, recipe.ingredientAt(0));
    in.add(stuff);
    stuff = new ArrayList<>();
    Collections.addAll(stuff, recipe.ingredientAt(1));
    in.add(stuff);
    stuff = new ArrayList<>();
    Collections.addAll(stuff, recipe.ingredientAt(2));
    in.add(stuff);
    ingredients.setInputLists(VanillaTypes.ITEM, in);
    ingredients.setOutput(VanillaTypes.ITEM, recipe.getRecipeOutput());
  }

  @Override
  public void setRecipe(IRecipeLayout recipeLayout, RecipeSolidifier recipe, IIngredients ingredients) {
    IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();
    guiItemStacks.init(0, true, 33, 6);
    guiItemStacks.init(1, true, 33, 6 + Const.SQ);
    guiItemStacks.init(2, true, 33, 6 + 2 * Const.SQ);
    guiItemStacks.init(3, false, 104, 6 + Const.SQ);
    guiItemStacks.set(3, recipe.getRecipeOutput());
    List<List<ItemStack>> inputs = ingredients.getInputs(VanillaTypes.ITEM);
    List<ItemStack> input = null;
    for (int i = 0; i <= 2; i++) {
      input = inputs.get(i);
      if (input != null && input.isEmpty() == false) {
        guiItemStacks.set(i, input);
      }
    } //getname is the same   
    recipeLayout.getFluidStacks().init(0, true, 4, 25, Const.SQ - 2, Const.SQ - 2,
        FluidAttributes.BUCKET_VOLUME, false,
        null);
    recipeLayout.getFluidStacks().set(0, recipe.getRecipeFluid());
  }
}
