package com.lothrazar.cyclic.compat.jei;

import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.block.generatoritem.RecipeGeneratorItem;
import com.lothrazar.cyclic.recipe.CyclicRecipeType;
import com.lothrazar.cyclic.registry.BlockRegistry;
import com.lothrazar.cyclic.util.UtilChat;
import com.mojang.blaze3d.matrix.MatrixStack;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;

@SuppressWarnings("rawtypes")
public class GenitemRecipeCategory implements IRecipeCategory<RecipeGeneratorItem> {

  static final ResourceLocation ID = new ResourceLocation(CyclicRecipeType.GENERATOR_ITEM.toString());
  private IDrawable gui;
  private IDrawable icon;
  private IDrawable drawString;

  public GenitemRecipeCategory(IGuiHelper helper) {
    gui = helper.drawableBuilder(new ResourceLocation(ModCyclic.MODID, "textures/jei/solidifier_recipe.png"), 0, 0, 169, 69).setTextureSize(169, 69).build();
    icon = helper.drawableBuilder(new ResourceLocation(ModCyclic.MODID, "textures/blocks/generator_item.png"), 0, 0, 16, 16).setTextureSize(16, 16).build();
    drawString = helper.createBlankDrawable(20, 40);
  }

  @Override
  public String getTitle() {
    return UtilChat.lang(BlockRegistry.GENERATOR_ITEM.get().getTranslationKey());
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
  public Class<? extends RecipeGeneratorItem> getRecipeClass() {
    return RecipeGeneratorItem.class;
  }

  @Override
  public ResourceLocation getUid() {
    return ID;
  }

  @Override
  public void setIngredients(RecipeGeneratorItem recipe, IIngredients ingredients) {
    //    ingredients.setInput(VanillaTypes.FLUID, recipe.getRecipeFluid());
    List<List<ItemStack>> in = new ArrayList<>();
    List<ItemStack> stuff = new ArrayList<>();
    Collections.addAll(stuff, recipe.ingredientAt(0));
    in.add(stuff);
    //    stuff = new ArrayList<>();
    //    Collections.addAll(stuff, recipe.ingredientAt(1));
    //    in.add(stuff);
    //    stuff = new ArrayList<>();
    //    Collections.addAll(stuff, recipe.ingredientAt(2));
    //    in.add(stuff);
    ingredients.setInputLists(VanillaTypes.ITEM, in);
    //    ingredients.setOutput(VanillaTypes.ITEM, recipe.getRecipeOutput());
  }

  @Override
  public void draw(RecipeGeneratorItem recipe, MatrixStack ms, double mouseX, double mouseY) {
    //
    Minecraft.getInstance().fontRenderer.drawString(ms, recipe.getTicks() + " t", 60, 5, 4210752);
    Minecraft.getInstance().fontRenderer.drawString(ms, recipe.getRfpertick() + " RF/t", 60, 20, 4210752);
  }

  @Override
  public void setRecipe(IRecipeLayout recipeLayout, RecipeGeneratorItem recipe, IIngredients ingredients) {
    IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();
    guiItemStacks.init(0, true, 33, 6);
    //    guiItemStacks.init(1, true, 33, 6 + Const.SQ);
    //    guiItemStacks.init(2, true, 33, 6 + 2 * Const.SQ);
    //    guiItemStacks.init(3, false, 104, 6 + Const.SQ);
    //    guiItemStacks.set(3, recipe.getRecipeOutput());
    List<List<ItemStack>> inputs = ingredients.getInputs(VanillaTypes.ITEM);
    guiItemStacks.set(0, inputs.get(0));
    //    recipeLayout.wr
    //    recipeLayout.getFluidStacks().init(0, true, 4, 25, Const.SQ - 2, Const.SQ - 2,
    //        FluidAttributes.BUCKET_VOLUME, false,
    //        null);
    //    recipeLayout.getFluidStacks().set(0, recipe.getRecipeFluid());
  }
}
