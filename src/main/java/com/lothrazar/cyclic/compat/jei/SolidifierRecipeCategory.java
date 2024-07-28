package com.lothrazar.cyclic.compat.jei;

import java.util.List;
import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.block.solidifier.RecipeSolidifier;
import com.lothrazar.cyclic.block.solidifier.TileSolidifier;
import com.lothrazar.cyclic.registry.BlockRegistry;
import com.lothrazar.cyclic.registry.TextureRegistry;
import com.lothrazar.library.gui.EnergyBar;
import com.lothrazar.library.gui.TexturedProgress;
import com.lothrazar.library.util.ChatUtil;
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
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class SolidifierRecipeCategory implements IRecipeCategory<RecipeSolidifier> {

  private static final int FONT = 0xFFFFFFFF;
  private static final ResourceLocation ID = new ResourceLocation(ModCyclic.MODID, "solidifier");
  static final RecipeType<RecipeSolidifier> TYPE = new RecipeType<>(ID, RecipeSolidifier.class);
  private IDrawable gui;
  private IDrawable icon;
  private Font font;
  private EnergyBar bar;
  private TexturedProgress progress;

  public SolidifierRecipeCategory(IGuiHelper helper) {
    font = Minecraft.getInstance().font;
    gui = helper.drawableBuilder(new ResourceLocation(ModCyclic.MODID, "textures/jei/solidifier_recipe.png"), 0, 0, 169, 69).setTextureSize(169, 69).build();
    icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(BlockRegistry.SOLIDIFIER.get()));
    bar = new EnergyBar(font, TileSolidifier.MAX);
    progress = new TexturedProgress(font, 63, 25, 24, 17, TextureRegistry.ARROW);
    bar.guiTop = -4;
    bar.guiLeft = -2;
  }

  @Override
  public Component getTitle() {
    return ChatUtil.ilang(BlockRegistry.SOLIDIFIER.get().getDescriptionId());
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
  public RecipeType<RecipeSolidifier> getRecipeType() {
    return TYPE;
  }

  @Override
  public void draw(RecipeSolidifier recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics ms, double mouseX, double mouseY) {
    ms.drawString(font, recipe.getEnergy().getRfPertick() + " RF/t", 55, 10, FONT);
    bar.draw(ms, recipe.getEnergy().getEnergyTotal());
    progress.draw(ms, 0);
    bar.renderHoveredToolTip(ms, (int) mouseX, (int) mouseY, recipe.getEnergy().getEnergyTotal());
    progress.renderHoveredToolTip(ms, (int) mouseX, (int) mouseY, recipe.getEnergy().getTicks());
  }

  @Override
  public void setRecipe(IRecipeLayoutBuilder builder, RecipeSolidifier recipe, IFocusGroup focuses) {
    builder.addSlot(RecipeIngredientRole.INPUT, 34, 7).addIngredients(recipe.at(0));
    builder.addSlot(RecipeIngredientRole.INPUT, 34, 25).addIngredients(recipe.at(1));
    builder.addSlot(RecipeIngredientRole.INPUT, 34, 43).addIngredients(recipe.at(2));
    builder.addSlot(RecipeIngredientRole.OUTPUT, 104, 25).addItemStack(recipe.result);
    List<FluidStack> matchingFluids = recipe.fluidIngredient.getMatchingFluids();
    builder.addSlot(RecipeIngredientRole.INPUT, 4, 25).addIngredients(ForgeTypes.FLUID_STACK, matchingFluids).setFluidRenderer(4000, false, 16, 16);
  }
  //  @Override
  //  public void setIngredients(RecipeSolidifier recipe, IIngredients ingredients) {
  //    if (recipe.getRecipeFluid().isEmpty()) {
  //      //then we use tags
  //      ingredients.setInputs(VanillaTypes.FLUID, recipe.fluidIngredient.getMatchingFluids());
  //    }
  //    else {
  //      ingredients.setInput(VanillaTypes.FLUID, recipe.getRecipeFluid());
  //    }
  //    List<List<ItemStack>> in = new ArrayList<>();
  //    List<ItemStack> stuff = new ArrayList<>();
  //    //    for (int i = 0; i <= 2; i++) {
  //    //test without loop just in case?
  //    Collections.addAll(stuff, recipe.ingredientAt(0));
  //    in.add(stuff);
  //    stuff = new ArrayList<>();
  //    Collections.addAll(stuff, recipe.ingredientAt(1));
  //    in.add(stuff);
  //    stuff = new ArrayList<>();
  //    Collections.addAll(stuff, recipe.ingredientAt(2));
  //    in.add(stuff);
  //    ingredients.setInputLists(VanillaTypes.ITEM, in);
  //    ingredients.setOutput(VanillaTypes.ITEM, recipe.getResultItem());
  //  }
  //
  //  @Override
  //  public void setRecipe(IRecipeLayout recipeLayout, RecipeSolidifier recipe, IIngredients ingredients) {
  //    IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();
  //    guiItemStacks.init(0, true, 33, 6);
  //    guiItemStacks.init(1, true, 33, 6 + Const.SQ);
  //    guiItemStacks.init(2, true, 33, 6 + 2 * Const.SQ);
  //    guiItemStacks.init(3, false, 104, 6 + Const.SQ);
  //    guiItemStacks.set(3, recipe.getResultItem());
  //    List<List<ItemStack>> inputs = ingredients.getInputs(VanillaTypes.ITEM);
  //    List<ItemStack> input = null;
  //    for (int i = 0; i <= 2; i++) {
  //      input = inputs.get(i);
  //      if (input != null && input.isEmpty() == false) {
  //        guiItemStacks.set(i, input);
  //      }
  //    } //getname is the same
  //    recipeLayout.getFluidStacks().init(0, true, 4, 25, Const.SQ - 2, Const.SQ - 2, FluidAttributes.BUCKET_VOLUME, false, null);
  //    //tag or stack?
  //    if (recipe.fluidIngredient.hasTag()) {
  //      List<FluidStack> matchingFluids = recipe.fluidIngredient.getMatchingFluids();
  //      if (matchingFluids != null) {
  //        recipeLayout.getFluidStacks().set(0, matchingFluids);
  //      }
  //    }
  //    else if (!recipe.getRecipeFluid().isEmpty()) {
  //      recipeLayout.getFluidStacks().set(0, recipe.getRecipeFluid());
  //    }
  //  }
}
