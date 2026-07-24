package com.lothrazar.cyclic.compat.jei;

import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.block.crusher.RecipeCrusher;
import com.lothrazar.cyclic.block.solidifier.TileSolidifier;
import com.lothrazar.cyclic.registry.BlockRegistry;
import com.lothrazar.library.gui.EnergyBar;
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
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;

public class CrusherRecipeCategory implements IRecipeCategory<RecipeHolder<RecipeCrusher>> {

  private static final int FONT = 0xFFFFFFFF;
  private static final Identifier ID = Identifier.fromNamespaceAndPath(ModCyclic.MODID, "crusher");
  static final RecipeType<RecipeHolder<RecipeCrusher>> TYPE = new RecipeType<>(ID, (Class)RecipeHolder.class);
  private IDrawable gui;
  private IDrawable icon;
  private Font font;
  private EnergyBar bar;

  public CrusherRecipeCategory(IGuiHelper helper) {
    font = Minecraft.getInstance().font;
    gui = helper.drawableBuilder(Identifier.fromNamespaceAndPath(ModCyclic.MODID, "textures/jei/crusher.png"), 0, 0, 155, 49).setTextureSize(155, 49).build();
    icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(BlockRegistry.CRUSHER.get()));
    bar = new EnergyBar(font, TileSolidifier.MAX);
    bar.setHeight(48);
    bar.guiLeft = -16;
    bar.guiTop = -8;
  }

  @Override
  public Component getTitle() {
    return ChatUtil.ilang(BlockRegistry.CRUSHER.get().getDescriptionId());
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
  public RecipeType<RecipeHolder<RecipeCrusher>> getRecipeType() {
    return TYPE;
  }

  @Override
  public void draw(RecipeHolder<RecipeCrusher> recipeHolder, IRecipeSlotsView recipeSlotsView, GuiGraphics ms, double mouseX, double mouseY) {
    gui.draw(ms, 0, 0);
    var recipe = recipeHolder.value();
    int x = 78;
    if (recipe.energy.getTicks() < 40) {
      ms.drawString(font, recipe.energy.getTicks() + " t", x, 6, FONT);
    }
    else {
      int sec = recipe.energy.getTicks() / 20;
      ms.drawString(font, sec + " s", x, 6, FONT);
    }
    ms.drawString(font, recipe.energy.getRfPertick() + " RF/t", x, 16, FONT);
    if (!recipe.randOutput.bonus.isEmpty() && recipe.randOutput.percent > 0) {
      ms.drawString(font, recipe.randOutput.percent + "%", 56, 36, FONT);
    }
    bar.draw(ms, recipe.energy.getEnergyTotal());
    bar.renderHoveredToolTip(ms, (int) mouseX, (int) mouseY, recipe.energy.getEnergyTotal());
  }

  @Override
  public void setRecipe(IRecipeLayoutBuilder builder, RecipeHolder<RecipeCrusher> recipeHolder, IFocusGroup focuses) {
    RecipeCrusher recipe = recipeHolder.value();
    builder.addSlot(RecipeIngredientRole.INPUT, 3, 14).addIngredients(recipe.at(0));
    builder.addSlot(RecipeIngredientRole.OUTPUT, 35, 6).addItemStack(recipe.result);
    if (!recipe.randOutput.bonus.isEmpty() && recipe.randOutput.percent > 0) {
      builder.addSlot(RecipeIngredientRole.OUTPUT, 34, 31).addItemStack(recipe.randOutput.bonus);
    }
  }
}
