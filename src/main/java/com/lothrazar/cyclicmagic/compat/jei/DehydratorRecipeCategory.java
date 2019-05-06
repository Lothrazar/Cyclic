package com.lothrazar.cyclicmagic.compat.jei;

import java.util.List;
import com.lothrazar.cyclicmagic.block.dehydrator.RecipeDeHydrate;
import com.lothrazar.cyclicmagic.util.Const;
import com.lothrazar.cyclicmagic.util.UtilChat;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeCategory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;

public class DehydratorRecipeCategory implements IRecipeCategory<DehydratorWrapper> {

  private IDrawable gui;
  private IDrawable icon;

  public DehydratorRecipeCategory(IGuiHelper helper) {
    gui = helper.drawableBuilder(new ResourceLocation(Const.MODID, "textures/gui/dehydrate_recipe.png"), 0, 0, 169, 69).setTextureSize(169, 69).build();
    icon = helper.drawableBuilder(new ResourceLocation(Const.MODID, "textures/blocks/dehydrator_bush.png"), 0, 0, 16, 16).setTextureSize(16, 16).build();
  }

  @Override
  public String getUid() {
    return JEIPlugin.RECIPE_CATEGORY_DEHYDRATOR;
  }

  @Override
  public String getTitle() {
    return UtilChat.lang("tile.dehydrator.name");
  }

  @Override
  public String getModName() {
    return Const.MODID;
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
  public void setRecipe(IRecipeLayout recipeLayout, DehydratorWrapper recipeWrapper, IIngredients ingredients) {
    IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();
    int x = 56, y = 26;
    guiItemStacks.init(0, true, x, y);
    y += Const.SQ;
    List<List<ItemStack>> inputs = ingredients.getInputs(VanillaTypes.ITEM);
    for (int i = 0; i < inputs.size(); i++) {
      List<ItemStack> input = inputs.get(i);
      if (input != null && input.isEmpty() == false)
        guiItemStacks.set(i, input.get(0));
    }
    guiItemStacks.init(1, false, 129, 18);
    guiItemStacks.set(1, recipeWrapper.getOut());
    x = 96;
    y = 28;
    try {
      RecipeDeHydrate recipe = recipeWrapper.getRecipe();
      ingredients.setOutput(VanillaTypes.FLUID, recipe.getOutputFluid());
      //getname is the same   
      recipeLayout.getFluidStacks().init(0, true, x, y, Const.SQ - 2, Const.SQ - 2, Fluid.BUCKET_VOLUME, false,
          null);
      recipeLayout.getFluidStacks().set(0, recipe.getOutputFluid());
    }
    catch (Exception e) {
      //
    }
  }
}