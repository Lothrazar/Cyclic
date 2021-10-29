package com.lothrazar.cyclic.compat.jei;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.block.crusher.RecipeCrusher;
import com.lothrazar.cyclic.data.Const;
import com.lothrazar.cyclic.recipe.CyclicRecipeType;
import com.lothrazar.cyclic.registry.BlockRegistry;
import com.lothrazar.cyclic.util.UtilChat;
import com.mojang.blaze3d.vertex.PoseStack;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

@SuppressWarnings("rawtypes")
public class CrusherRecipeCategory implements IRecipeCategory<RecipeCrusher> {

  private static final int FONT = 4210752;
  static final ResourceLocation ID = new ResourceLocation(CyclicRecipeType.CRUSHER.toString());
  private IDrawable gui;
  private IDrawable icon;

  public CrusherRecipeCategory(IGuiHelper helper) {
    gui = helper.drawableBuilder(new ResourceLocation(ModCyclic.MODID, "textures/jei/generator_item.png"), 0, 0, 118, 32).setTextureSize(118, 32).build();
    icon = helper.createDrawableIngredient(new ItemStack(BlockRegistry.GENERATOR_ITEM.get()));
  }

  @Override
  public Component getTitle() {
    return UtilChat.ilang(BlockRegistry.CRUSHER.get().getDescriptionId());
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
  public Class<? extends RecipeCrusher> getRecipeClass() {
    return RecipeCrusher.class;
  }

  @Override
  public ResourceLocation getUid() {
    return ID;
  }

  @Override
  public void setIngredients(RecipeCrusher recipe, IIngredients ingredients) {
    List<List<ItemStack>> in = new ArrayList<>();
    List<ItemStack> stuff = new ArrayList<>();
    Collections.addAll(stuff, recipe.ingredientAt(0));
    in.add(stuff);
    ingredients.setInputLists(VanillaTypes.ITEM, in);
    if (recipe.bonus.isEmpty()) {
      ingredients.setOutput(VanillaTypes.ITEM, recipe.getResultItem());
    }
    else if (recipe.getResultItem().isEmpty()) {
      ingredients.setOutput(VanillaTypes.ITEM, recipe.bonus);
    }
    else {
      ingredients.setOutputs(VanillaTypes.ITEM, Arrays.asList(recipe.getResultItem(), recipe.bonus));
    }
  }

  @Override
  public void draw(RecipeCrusher recipe, PoseStack ms, double mouseX, double mouseY) {
    Minecraft.getInstance().font.draw(ms, recipe.getTicks() + " t", 60, 0, FONT);
    Minecraft.getInstance().font.draw(ms, recipe.getRfpertick() + " RF/t", 60, 10, FONT);
    Minecraft.getInstance().font.draw(ms, recipe.getRfTotal() + " RF", 60, 20, FONT);
  }

  @Override
  public void setRecipe(IRecipeLayout recipeLayout, RecipeCrusher recipe, IIngredients ingredients) {
    IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();
    guiItemStacks.init(0, true, 5, 6);
    List<List<ItemStack>> inputs = ingredients.getInputs(VanillaTypes.ITEM);
    guiItemStacks.set(0, inputs.get(0));
    guiItemStacks.init(1, false, 90, 6 + Const.SQ);
    guiItemStacks.set(1, recipe.getResultItem());
    if (!recipe.bonus.isEmpty()) {
      guiItemStacks.init(2, false, 104, 16 + Const.SQ);
      guiItemStacks.set(2, recipe.bonus);
    }
  }
}
