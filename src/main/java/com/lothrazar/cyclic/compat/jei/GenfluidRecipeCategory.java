package com.lothrazar.cyclic.compat.jei;

import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.block.generatorfluid.RecipeGeneratorFluid;
import com.lothrazar.cyclic.data.Const;
import com.lothrazar.cyclic.recipe.CyclicRecipeType;
import com.lothrazar.cyclic.registry.BlockRegistry;
import com.lothrazar.cyclic.util.UtilChat;
import com.mojang.blaze3d.matrix.MatrixStack;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidStack;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;

@SuppressWarnings("rawtypes")
public class GenfluidRecipeCategory implements IRecipeCategory<RecipeGeneratorFluid> {

  private static final int FONT = 4210752;
  static final ResourceLocation ID = new ResourceLocation(CyclicRecipeType.GENERATOR_FLUID.toString());
  private IDrawable gui;
  private IDrawable icon;

  public GenfluidRecipeCategory(IGuiHelper helper) {
    gui = helper.drawableBuilder(new ResourceLocation(ModCyclic.MODID, "textures/jei/generator_fluid.png"), 0, 0, 118, 32).setTextureSize(118, 32).build();
    icon = helper.createDrawableIngredient(new ItemStack(BlockRegistry.GENERATOR_FLUID.get()));
  }

  @Override
  public String getTitle() {
    return UtilChat.lang(BlockRegistry.GENERATOR_FLUID.get().getTranslationKey());
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
  public Class<? extends RecipeGeneratorFluid> getRecipeClass() {
    return RecipeGeneratorFluid.class;
  }

  @Override
  public ResourceLocation getUid() {
    return ID;
  }

  @Override
  public void setIngredients(RecipeGeneratorFluid recipe, IIngredients ingredients) {
    if (recipe.getRecipeFluid().isEmpty()) {
      List<FluidStack> matchingFluids = recipe.fluidIng.getMatchingFluids();
      if (matchingFluids != null) {
        ingredients.setInputs(VanillaTypes.FLUID, recipe.fluidIng.getMatchingFluids());
      }
    }
    else {
      ingredients.setInput(VanillaTypes.FLUID, recipe.getRecipeFluid());
    }
  }

  @Override
  public void draw(RecipeGeneratorFluid recipe, MatrixStack ms, double mouseX, double mouseY) {
    Minecraft.getInstance().fontRenderer.drawString(ms, recipe.getTicks() + " t", 60, 0, FONT);
    Minecraft.getInstance().fontRenderer.drawString(ms, recipe.getRfpertick() + " RF/t", 60, 10, FONT);
    Minecraft.getInstance().fontRenderer.drawString(ms, recipe.getRfTotal() + " RF", 60, 20, FONT);
  }

  @Override
  public void setRecipe(IRecipeLayout recipeLayout, RecipeGeneratorFluid recipe, IIngredients ingredients) {
    //    ingredients.setOutput(VanillaTypes.FLUID, recipe.getRecipeFluid());
    recipeLayout.getFluidStacks().init(0, true, 6, 7, Const.SQ - 2, Const.SQ - 2,
        FluidAttributes.BUCKET_VOLUME, false, null);
    //tag or stack?
    if (recipe.fluidIng.hasTag()) {
      List<FluidStack> matchingFluids = recipe.fluidIng.getMatchingFluids();
      if (matchingFluids != null) {
        recipeLayout.getFluidStacks().set(0, matchingFluids);
      }
    }
    else if (!recipe.getRecipeFluid().isEmpty()) {
      recipeLayout.getFluidStacks().set(0, recipe.getRecipeFluid());
    }
  }
}
