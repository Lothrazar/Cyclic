package com.lothrazar.cyclic.compat.jei;

import java.util.List;
import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.block.generatorfluid.RecipeGeneratorFluid;
import com.lothrazar.cyclic.registry.BlockRegistry;
import com.lothrazar.library.util.ChatUtil;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.neoforge.NeoForgeTypes;
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
import net.minecraft.world.item.crafting.RecipeHolder;
import net.neoforged.neoforge.fluids.FluidStack;

public class GenfluidRecipeCategory implements IRecipeCategory<RecipeHolder<RecipeGeneratorFluid>> {

  private static final int FONT = 0xFFFFFFFF;
  static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(ModCyclic.MODID, "generator_fluid");
  static final RecipeType<RecipeHolder<RecipeGeneratorFluid>> TYPE = new RecipeType<>(ID, (Class)RecipeHolder.class);
  private IDrawable gui;
  private IDrawable icon;

  public GenfluidRecipeCategory(IGuiHelper helper) {
    gui = helper.drawableBuilder(ResourceLocation.fromNamespaceAndPath(ModCyclic.MODID, "textures/jei/generator_fluid.png"), 0, 0, 118, 32).setTextureSize(118, 32).build();
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
  public RecipeType<RecipeHolder<RecipeGeneratorFluid>> getRecipeType() {
    return TYPE;
  }

  @Override
  public void draw(RecipeHolder<RecipeGeneratorFluid> recipeHolder, IRecipeSlotsView recipeSlotsView, GuiGraphics ms, double mouseX, double mouseY) {
    var recipe = recipeHolder.value();
    var font = Minecraft.getInstance().font;
    ms.drawString(font, recipe.getTicks() + " t", 60, 0, FONT);
    ms.drawString(font, recipe.getRfpertick() + " RF/t", 60, 10, FONT);
    ms.drawString(font, recipe.getRfTotal() + " RF", 60, 20, FONT);
    //ms.drawString(font, recipe.fluidIng.getAmount() + " mB", 60, 30, FONT);
  }

  @Override
  public void setRecipe(IRecipeLayoutBuilder builder, RecipeHolder<RecipeGeneratorFluid> recipeHolder, IFocusGroup focuses) {
    RecipeGeneratorFluid recipe = recipeHolder.value();
    List<FluidStack> matchingFluids = recipe.fluidIng.getMatchingFluids();
    builder.addSlot(RecipeIngredientRole.INPUT, 6, 7).addIngredients(NeoForgeTypes.FLUID_STACK, matchingFluids).setFluidRenderer(4000, false, 16, 16);
  }
}
