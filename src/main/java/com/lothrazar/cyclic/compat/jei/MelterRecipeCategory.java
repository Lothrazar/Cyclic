package com.lothrazar.cyclic.compat.jei;

import java.util.List;
import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.block.melter.RecipeMelter;
import com.lothrazar.cyclic.registry.BlockRegistry;
import com.lothrazar.cyclic.util.ChatUtil;
import com.mojang.blaze3d.vertex.PoseStack;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.forge.ForgeTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class MelterRecipeCategory implements IRecipeCategory<RecipeMelter> {

  private static final int FONT = 4210752;
  private static final ResourceLocation ID = new ResourceLocation(ModCyclic.MODID, "melter");
  static final RecipeType<RecipeMelter> TYPE = new RecipeType<>(ID, RecipeMelter.class);
  private IDrawable gui;
  private IDrawable icon;

  public MelterRecipeCategory(IGuiHelper helper) {
    gui = helper.drawableBuilder(new ResourceLocation(ModCyclic.MODID, "textures/jei/melter_recipe.png"), 0, 0, 169, 69).setTextureSize(169, 69).build();
    icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(BlockRegistry.MELTER.get()));
  }

  @Override
  public Component getTitle() {
    return ChatUtil.ilang(BlockRegistry.MELTER.get().getDescriptionId());
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
    return ID;
  }

  @Override
  public RecipeType<RecipeMelter> getRecipeType() {
    return TYPE;
  }

  @Override
  public void draw(RecipeMelter recipe, PoseStack ms, double mouseX, double mouseY) {
    Minecraft.getInstance().font.draw(ms, recipe.getEnergy().getTicks() + " t", 60, 0, FONT);
    Minecraft.getInstance().font.draw(ms, recipe.getEnergy().getRfPertick() + " RF/t", 60, 10, FONT);
    Minecraft.getInstance().font.draw(ms, recipe.getEnergy().getEnergyTotal() + " RF", 60, 20, FONT);
  }

  @Override
  public void setRecipe(IRecipeLayoutBuilder builder, RecipeMelter recipe, IFocusGroup focuses) {
    builder.addSlot(RecipeIngredientRole.INPUT, 4, 19).addIngredients(recipe.at(0));
    builder.addSlot(RecipeIngredientRole.INPUT, 22, 19).addIngredients(recipe.at(1));
    List<FluidStack> matchingFluids = List.of(recipe.getRecipeFluid());
    builder.addSlot(RecipeIngredientRole.OUTPUT, 140, 19).addIngredients(ForgeTypes.FLUID_STACK, matchingFluids);
  }
  //  @Override
  //  public void setIngredients(RecipeMelter recipe, IIngredients ingredients) {
  //    List<List<ItemStack>> in = new ArrayList<>();
  //    List<ItemStack> stuff = new ArrayList<>();
  //    Collections.addAll(stuff, recipe.ingredientAt(0));
  //    in.add(stuff);
  //    stuff = new ArrayList<>();
  //    Collections.addAll(stuff, recipe.ingredientAt(1));
  //    in.add(stuff);
  //    ingredients.setInputLists(VanillaTypes.ITEM, in);
  //    ingredients.setOutput(VanillaTypes.FLUID, recipe.getRecipeFluid());
  //  }
  //
  //  @Override
  //  public void setRecipe(IRecipeLayout recipeLayout, RecipeMelter recipe, IIngredients ingredients) {
  //    IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();
  //    guiItemStacks.init(0, true, 3, Const.SQ);
  //    guiItemStacks.init(1, true, 21, Const.SQ);
  //    guiItemStacks.init(2, true, 41, Const.SQ);
  //    guiItemStacks.init(3, true, 3, 120);
  //    List<List<ItemStack>> inputs = ingredients.getInputs(VanillaTypes.ITEM);
  //    List<ItemStack> input = inputs.get(0);
  //    if (input != null && input.isEmpty() == false) {
  //      guiItemStacks.set(0, input);
  //    }
  //    input = inputs.get(1);
  //    if (input != null && input.isEmpty() == false) {
  //      guiItemStacks.set(1, input);
  //    }
  //    ingredients.setOutput(VanillaTypes.FLUID, recipe.getRecipeFluid());
  //    //getname is the same
  //    recipeLayout.getFluidStacks().init(0, true, 140, Const.SQ + 1, Const.SQ - 2, Const.SQ - 2,
  //        FluidAttributes.BUCKET_VOLUME, false,
  //        null);
  //    recipeLayout.getFluidStacks().set(0, recipe.getRecipeFluid());
  //  }
}
