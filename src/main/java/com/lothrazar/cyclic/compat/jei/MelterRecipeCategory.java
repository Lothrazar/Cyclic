package com.lothrazar.cyclic.compat.jei;

import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.block.melter.RecipeMelter;
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
public class MelterRecipeCategory implements IRecipeCategory<RecipeMelter> {

  static ResourceLocation id = new ResourceLocation(CyclicRecipeType.MELTER.toString());
  private IDrawable gui;
  private IDrawable icon;

  public MelterRecipeCategory(IGuiHelper helper) {
    gui = helper.drawableBuilder(new ResourceLocation(ModCyclic.MODID, "textures/jei/melter_recipe.png"), 0, 0, 169, 69).setTextureSize(169, 69).build();
    icon = helper.drawableBuilder(new ResourceLocation(ModCyclic.MODID, "textures/blocks/melter.png"), 0, 0, 16, 16).setTextureSize(16, 16).build();
  }

  @Override
  public String getTitle() {
    return UtilChat.lang(BlockRegistry.melter.getTranslationKey());
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
  public Class<? extends RecipeMelter> getRecipeClass() {
    return RecipeMelter.class;
  }

  @Override
  public ResourceLocation getUid() {
    return id;
  }

  @Override
  public void setIngredients(RecipeMelter recipe, IIngredients ingredients) {
    List<List<ItemStack>> in = new ArrayList<>();
    List<ItemStack> stuff = new ArrayList<>();
    Collections.addAll(stuff, recipe.ingredientAt(0));
    in.add(stuff);
    stuff = new ArrayList<>();
    Collections.addAll(stuff, recipe.ingredientAt(1));
    in.add(stuff);
    ingredients.setInputLists(VanillaTypes.ITEM, in);
    ingredients.setOutput(VanillaTypes.FLUID, recipe.getRecipeFluid());
  }

  @Override
  public void setRecipe(IRecipeLayout recipeLayout, RecipeMelter recipe, IIngredients ingredients) {
    IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();
    guiItemStacks.init(0, true, 3, Const.SQ);
    guiItemStacks.init(1, true, 21, Const.SQ);
    guiItemStacks.init(2, true, 41, Const.SQ);
    guiItemStacks.init(3, true, 3, 120);
    List<List<ItemStack>> inputs = ingredients.getInputs(VanillaTypes.ITEM);
    List<ItemStack> input = inputs.get(0);
    if (input != null && input.isEmpty() == false) {
      guiItemStacks.set(0, input);
    }
    input = inputs.get(1);
    if (input != null && input.isEmpty() == false) {
      guiItemStacks.set(1, input);
    }
    //    ingredients.setOutput(VanillaTypes.FLUID, recipe.getRecipeFluid());
    ingredients.setOutput(VanillaTypes.FLUID, recipe.getRecipeFluid());
    //getname is the same   
    recipeLayout.getFluidStacks().init(0, true, 140, Const.SQ + 1, Const.SQ - 2, Const.SQ - 2,
        FluidAttributes.BUCKET_VOLUME, false,
        null);
    recipeLayout.getFluidStacks().set(0, recipe.getRecipeFluid());
  }
}