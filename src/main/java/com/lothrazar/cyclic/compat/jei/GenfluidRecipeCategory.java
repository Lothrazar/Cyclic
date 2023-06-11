package com.lothrazar.cyclic.compat.jei;

import java.util.List;
import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.block.generatorfluid.RecipeGeneratorFluid;
import com.lothrazar.cyclic.registry.BlockRegistry;
import com.lothrazar.cyclic.util.ChatUtil;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.forge.ForgeTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class GenfluidRecipeCategory implements IRecipeCategory<RecipeGeneratorFluid> {

  private static final int FONT = 4210752;
  static final ResourceLocation ID = new ResourceLocation(ModCyclic.MODID, "generator_fluid");
  static final RecipeType<RecipeGeneratorFluid> TYPE = new RecipeType<>(ID, RecipeGeneratorFluid.class);
  private IDrawable gui;
  private IDrawable icon;

  public GenfluidRecipeCategory(IGuiHelper helper) {
    gui = helper.drawableBuilder(new ResourceLocation(ModCyclic.MODID, "textures/jei/generator_fluid.png"), 0, 0, 118, 32).setTextureSize(118, 32).build();
    icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(BlockRegistry.GENERATOR_FLUID.get()));
  }

  @Override
  public Component getTitle() {
    return ChatUtil.ilang(BlockRegistry.GENERATOR_FLUID.get().getDescriptionId());
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
  public RecipeType<RecipeGeneratorFluid> getRecipeType() {
    return TYPE;
  }

  @Override
  public void draw(RecipeGeneratorFluid recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics ms, double mouseX, double mouseY) {
    var font = Minecraft.getInstance().font;
    ms.drawString(font, recipe.getTicks() + " t", 60, 0, FONT);
    ms.drawString(font, recipe.getRfpertick() + " RF/t", 60, 10, FONT);
    ms.drawString(font, recipe.getRfTotal() + " RF", 60, 20, FONT);
  }

  @Override
  public void setRecipe(IRecipeLayoutBuilder builder, RecipeGeneratorFluid recipe, IFocusGroup focuses) {
    List<FluidStack> matchingFluids = recipe.fluidIng.getMatchingFluids();
    builder.addSlot(RecipeIngredientRole.INPUT, 6, 7).addIngredients(ForgeTypes.FLUID_STACK, matchingFluids);
  }
  //keep old code for reference
  //
  //  @Override
  //  public void setIngredients(RecipeGeneratorFluid recipe, IIngredients ingredients) {
  //    if (recipe.getRecipeFluid().isEmpty()) {
  //      List<FluidStack> matchingFluids = recipe.fluidIng.getMatchingFluids();
  //      if (matchingFluids != null) {
  //        ingredients.setInputs(VanillaTypes.FLUID, recipe.fluidIng.getMatchingFluids());
  //      }
  //    }
  //    else {
  //      ingredients.setInput(VanillaTypes.FLUID, recipe.getRecipeFluid());
  //    }
  //  }
  //
  //  @Override
  //  public void setRecipe(IRecipeLayout recipeLayout, RecipeGeneratorFluid recipe, IIngredients ingredients) {
  //    //    ingredients.setOutput(VanillaTypes.FLUID, recipe.getRecipeFluid());
  //    recipeLayout.getFluidStacks().init(0, true, 6, 7, Const.SQ - 2, Const.SQ - 2,
  //        FluidAttributes.BUCKET_VOLUME, false, null);
  //    //tag or stack?
  //    if (recipe.fluidIng.hasTag()) {
  //      List<FluidStack> matchingFluids = recipe.fluidIng.getMatchingFluids();
  //      if (matchingFluids != null) {
  //        recipeLayout.getFluidStacks().set(0, matchingFluids);
  //      }
  //    }
  //    else if (!recipe.getRecipeFluid().isEmpty()) {
  //      recipeLayout.getFluidStacks().set(0, recipe.getRecipeFluid());
  //    }
  //  }
}
