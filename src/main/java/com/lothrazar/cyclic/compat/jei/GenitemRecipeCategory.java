package com.lothrazar.cyclic.compat.jei;

import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.block.generatoritem.RecipeGeneratorItem;
import com.lothrazar.cyclic.registry.BlockRegistry;
import com.lothrazar.library.util.ChatUtil;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;

public class GenitemRecipeCategory implements IRecipeCategory<RecipeHolder<RecipeGeneratorItem>> {

  private static final int FONT = 0xFFFFFFFF;
  private static final Identifier ID = Identifier.fromNamespaceAndPath(ModCyclic.MODID, "generator_item");
  static final RecipeType<RecipeHolder<RecipeGeneratorItem>> TYPE = new RecipeType<>(ID, (Class) RecipeHolder.class);
  private IDrawable gui;
  private IDrawable icon;

  public GenitemRecipeCategory(IGuiHelper helper) {
    gui = helper.drawableBuilder(Identifier.fromNamespaceAndPath(ModCyclic.MODID, "textures/jei/generator_item.png"), 0, 0, 118, 32).setTextureSize(118, 32).build();
    icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(BlockRegistry.GENERATOR_ITEM.get()));
  }

  @Override
  public Component getTitle() {
    return ChatUtil.ilang(BlockRegistry.GENERATOR_ITEM.get().getDescriptionId());
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
  public RecipeType<RecipeHolder<RecipeGeneratorItem>> getRecipeType() {
    return TYPE;
  }

  @Override
  public void draw(RecipeHolder<RecipeGeneratorItem> recipeHolder, IRecipeSlotsView recipeSlotsView, GuiGraphicsExtractor ms, double mouseX, double mouseY) {
    gui.draw(ms, 0, 0);
    var recipe = recipeHolder.value();
    var font = Minecraft.getInstance().font;
    ms.text(font, recipe.getTicks() + " t", 60, 0, FONT);
    ms.text(font, recipe.getRfPertick() + " RF/t", 60, 10, FONT);
    ms.text(font, recipe.getEnergyTotal() + " RF", 60, 20, FONT);
  }

  @Override
  public void setRecipe(IRecipeLayoutBuilder builder, RecipeHolder<RecipeGeneratorItem> recipeHolder, IFocusGroup focuses) {
    RecipeGeneratorItem recipe = recipeHolder.value();
    builder.addSlot(RecipeIngredientRole.INPUT, 6, 7).addIngredients(recipe.at(0));
  }
}
