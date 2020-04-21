package com.lothrazar.cyclic.jei;

import java.util.ArrayList;
import java.util.List;
import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.data.Const;
import com.lothrazar.cyclic.recipe.RecipeMelter;
import com.lothrazar.cyclic.registry.BlockRegistry;
import com.lothrazar.cyclic.util.UtilChat;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidAttributes;

public class MelterRecipeCategory implements IRecipeCategory<RecipeMelter> {

  static ResourceLocation id = new ResourceLocation(ModCyclic.MODID, "jei");
  private IDrawable gui;
  private IDrawable icon;

  public MelterRecipeCategory(IGuiHelper helper) {
    gui = helper.drawableBuilder(new ResourceLocation(ModCyclic.MODID, "textures/gui/melter_recipe.png"), 0, 0, 169, 69).setTextureSize(169, 69).build();
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
    stuff.add(recipe.getRecipeInput());
    in.add(stuff);
    ingredients.setInputLists(VanillaTypes.ITEM, in);
    if (!recipe.getRecipeOutput().isEmpty())
      ingredients.setOutput(VanillaTypes.ITEM, recipe.getRecipeOutput());
  }

  @Override
  public void setRecipe(IRecipeLayout recipeLayout, RecipeMelter recipe, IIngredients ingredients) {
    IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();
    int x = 3, y = 18;
    int s = Const.SQ;
    guiItemStacks.init(0, true, 3, 18);
    guiItemStacks.init(1, true, 139, 18);
    if (!recipe.getRecipeOutput().isEmpty())
      guiItemStacks.set(1, recipe.getRecipeOutput());
    ingredients.setOutput(VanillaTypes.FLUID, recipe.getRecipeFluidOutput());
    //    //getname is the same   
    List<List<ItemStack>> inputs = ingredients.getInputs(VanillaTypes.ITEM);
    for (int i = 0; i < inputs.size(); i++) {
      List<ItemStack> input = inputs.get(i);
      if (input != null && input.isEmpty() == false) {
        //ok so
        guiItemStacks.set(i, input.get(0));
      }
    }
    //
    x = 90;
    try {
      ingredients.setOutput(VanillaTypes.FLUID, recipe.getRecipeFluidOutput());
      //getname is the same   
      recipeLayout.getFluidStacks().init(0, true, x, y, Const.SQ - 2, Const.SQ - 2,
          FluidAttributes.BUCKET_VOLUME, false,
          null);
      recipeLayout.getFluidStacks().set(0, recipe.getRecipeFluidOutput());
    }
    catch (Exception e) {
      //
    }
  }
}