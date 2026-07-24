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
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.neoforge.NeoForgeTypes;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.neoforged.neoforge.fluids.FluidStack;

public class SolidifierRecipeCategory implements IRecipeCategory<RecipeHolder<RecipeSolidifier>> {

  private static final int FONT = 0xFFFFFFFF;
  private static final Identifier ID = Identifier.fromNamespaceAndPath(ModCyclic.MODID, "solidifier");
  static final RecipeType<RecipeHolder<RecipeSolidifier>> TYPE = new RecipeType<>(ID, (Class)RecipeHolder.class);
  private IDrawable gui;
  private IDrawable icon;
  private Font font;
  private EnergyBar bar;
  private TexturedProgress progress;

  public SolidifierRecipeCategory(IGuiHelper helper) {
    font = Minecraft.getInstance().font;
    gui = helper.drawableBuilder(Identifier.fromNamespaceAndPath(ModCyclic.MODID, "textures/jei/solidifier_recipe.png"), 0, 0, 169, 69).setTextureSize(169, 69).build();
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
  public int getWidth() {
    return gui.getWidth();
  }

  @Override
  public int getHeight() {
    return gui.getHeight();
  }

  @Override
  public RecipeType<RecipeHolder<RecipeSolidifier>> getRecipeType() {
    return TYPE;
  }

  @Override
  public void draw(RecipeHolder<RecipeSolidifier> recipeHolder, IRecipeSlotsView recipeSlotsView, GuiGraphics ms, double mouseX, double mouseY) {
    gui.draw(ms, 0, 0);
    var recipe = recipeHolder.value();
    ms.drawString(font, recipe.getEnergy().getRfPertick() + " RF/t", 55, 10, FONT);
    bar.draw(ms, recipe.getEnergy().getEnergyTotal());
    progress.draw(ms, 0);
    bar.renderHoveredToolTip(ms, (int) mouseX, (int) mouseY, recipe.getEnergy().getEnergyTotal());
    progress.renderHoveredToolTip(ms, (int) mouseX, (int) mouseY, recipe.getEnergy().getTicks());
  }

  @Override
  public void setRecipe(IRecipeLayoutBuilder builder, RecipeHolder<RecipeSolidifier> recipeHolder, IFocusGroup focuses) {
    RecipeSolidifier recipe = recipeHolder.value();
    builder.addSlot(RecipeIngredientRole.INPUT, 34, 7).addIngredients(recipe.at(0));
    builder.addSlot(RecipeIngredientRole.INPUT, 34, 25).addIngredients(recipe.at(1));
    builder.addSlot(RecipeIngredientRole.INPUT, 34, 43).addIngredients(recipe.at(2));
    builder.addSlot(RecipeIngredientRole.OUTPUT, 104, 25).addItemStack(recipe.result);
    List<FluidStack> matchingFluids = recipe.getMatchingFluids();
    builder.addSlot(RecipeIngredientRole.INPUT, 4, 25).addIngredients(NeoForgeTypes.FLUID_STACK, matchingFluids).setFluidRenderer(4000, false, 16, 16);
  }
}
