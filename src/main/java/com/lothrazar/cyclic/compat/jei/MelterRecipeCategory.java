package com.lothrazar.cyclic.compat.jei;

import java.util.List;
import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.block.melter.RecipeMelter;
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

public class MelterRecipeCategory implements IRecipeCategory<RecipeMelter> {

  private static final int FONT = 4210752;
  private static final ResourceLocation ID = new ResourceLocation(ModCyclic.MODID, "melter");
  static final RecipeType<RecipeMelter> TYPE = new RecipeType<>(ID, RecipeMelter.class);
  private IDrawable gui;
  private IDrawable icon;
  private Font font;
  private EnergyBar bar;
  private TexturedProgress progress;

  public MelterRecipeCategory(IGuiHelper helper) {
	font = Minecraft.getInstance().font;
    gui = helper.drawableBuilder(new ResourceLocation(ModCyclic.MODID, "textures/jei/melter_recipe.png"), 0, 0, 169, 69).setTextureSize(169, 69).build();
    icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(BlockRegistry.MELTER.get()));
    bar = new EnergyBar(font, TileSolidifier.MAX);
    progress = new TexturedProgress(font, 80, 19, 24, 17, TextureRegistry.ARROW);
    bar.guiTop = -4;
    bar.guiLeft = -2;
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
  public RecipeType<RecipeMelter> getRecipeType() {
    return TYPE;
  }

  @Override
  public void draw(RecipeMelter recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics ms, double mouseX, double mouseY) {
    ms.drawString(font, recipe.getEnergy().getRfPertick() + " RF/t", 75, 9, FONT);
    bar.draw(ms, recipe.getEnergy().getEnergyTotal());
    progress.draw(ms, 0);
    bar.renderHoveredToolTip(ms, (int) mouseX, (int) mouseY, recipe.getEnergy().getEnergyTotal());
    progress.renderHoveredToolTip(ms, (int) mouseX, (int) mouseY, recipe.getEnergy().getTicks());
  }

  @Override
  public void setRecipe(IRecipeLayoutBuilder builder, RecipeMelter recipe, IFocusGroup focuses) {
    builder.addSlot(RecipeIngredientRole.INPUT, 4, 19).addIngredients(recipe.at(0));
    builder.addSlot(RecipeIngredientRole.INPUT, 22, 19).addIngredients(recipe.at(1));
    List<FluidStack> matchingFluids = List.of(recipe.getRecipeFluid());
    builder.addSlot(RecipeIngredientRole.OUTPUT, 132, 19).addIngredients(ForgeTypes.FLUID_STACK, matchingFluids).setFluidRenderer(4000, false, 16, 16);
  }
}
