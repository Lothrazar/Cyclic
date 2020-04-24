package com.lothrazar.cyclic.jei;

import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.block.solidifier.RecipeSolidifier;
import com.lothrazar.cyclic.data.Const;
import com.lothrazar.cyclic.registry.BlockRegistry;
import com.lothrazar.cyclic.util.UtilChat;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidAttributes;

@SuppressWarnings("rawtypes")
public class SolidifierRecipeCategory implements IRecipeCategory<RecipeSolidifier> {

  static ResourceLocation id = new ResourceLocation(ModCyclic.MODID, "solid_jei");
  private IDrawable gui;
  private IDrawable icon;

  public SolidifierRecipeCategory(IGuiHelper helper) {
    gui = helper.drawableBuilder(new ResourceLocation(ModCyclic.MODID, "textures/jei/solidifier_recipe.png"), 0, 0, 169, 69).setTextureSize(169, 69).build();
    icon = helper.drawableBuilder(new ResourceLocation(ModCyclic.MODID, "textures/blocks/solidifier.png"), 0, 0, 16, 16).setTextureSize(16, 16).build();
  }

  @Override
  public String getTitle() {
    return UtilChat.lang(BlockRegistry.solidifier.getTranslationKey());
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
    return id;
  }

  @Override
  public void setIngredients(RecipeSolidifier recipe, IIngredients ingredients) {
    ingredients.setInput(VanillaTypes.FLUID, recipe.getRecipeFluid());
    ingredients.setOutput(VanillaTypes.ITEM, recipe.getRecipeOutput());
  }

  @Override
  public void setRecipe(IRecipeLayout recipeLayout, RecipeSolidifier recipe, IIngredients ingredients) {
    IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();
    guiItemStacks.init(0, true, 139, Const.SQ);
    guiItemStacks.set(0, recipe.getRecipeOutput());
    ingredients.setInput(VanillaTypes.FLUID, recipe.getRecipeFluid());
    //
    //    ingredients.setOutput(VanillaTypes.FLUID, recipe.getRecipeFluidOutput());
    //getname is the same   
    recipeLayout.getFluidStacks().init(0, true, 4, Const.SQ + 1, Const.SQ - 2, Const.SQ - 2,
        FluidAttributes.BUCKET_VOLUME, false,
        null);
    recipeLayout.getFluidStacks().set(0, recipe.getRecipeFluid());
  }
}