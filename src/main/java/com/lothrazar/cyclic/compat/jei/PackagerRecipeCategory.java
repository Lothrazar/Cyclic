package com.lothrazar.cyclic.compat.jei;

import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.block.packager.TilePackager;
import com.lothrazar.cyclic.registry.BlockRegistry;
import com.lothrazar.cyclic.util.UtilChat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.resources.ResourceLocation;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;

public class PackagerRecipeCategory implements IRecipeCategory<CraftingRecipe> {

  static final ResourceLocation ID = new ResourceLocation("cyclic:packager");
  private IDrawable gui;
  private IDrawable icon;

  public PackagerRecipeCategory(IGuiHelper helper) {
    gui = helper.drawableBuilder(new ResourceLocation(ModCyclic.MODID, "textures/jei/packager.png"), 0, 0, 118, 32).setTextureSize(118, 32).build();
    icon = helper.createDrawableIngredient(new ItemStack(BlockRegistry.PACKAGER.get())); // helper.drawableBuilder(new ResourceLocation(ModCyclic.MODID, "textures/blocks/machine/packager.png"), 0, 0, 16, 16).setTextureSize(16, 16).build();
  }

  @Override
  public Component getTitle() {
    return UtilChat.ilang(BlockRegistry.PACKAGER.get().getDescriptionId());
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
  public Class<? extends CraftingRecipe> getRecipeClass() {
    return CraftingRecipe.class;
  }

  @Override
  public ResourceLocation getUid() {
    return ID;
  }

  @Override
  public boolean isHandled(CraftingRecipe recipe) {
    return TilePackager.isRecipeValid(recipe);
  }

  @Override
  public void setIngredients(CraftingRecipe recipe, IIngredients ingredients) {
    //    if (!TilePackager.isRecipeValid(recipe)) {
    //      return;
    //    }
    //    ingredients.setInput(VanillaTypes.FLUID, recipe.getRecipeFluid());
    List<List<ItemStack>> in = new ArrayList<>();
    List<ItemStack> stuff = new ArrayList<>();
    Ingredient ingr = recipe.getIngredients().get(0);
    Collections.addAll(stuff, ingr.getItems());
    in.add(stuff);
    ingredients.setInputLists(VanillaTypes.ITEM, in);
  }

  @Override
  public void setRecipe(IRecipeLayout recipeLayout, CraftingRecipe recipe, IIngredients ingredients) {
    //    if (!TilePackager.isRecipeValid(recipe)) {
    //      return;
    //    }
    List<List<ItemStack>> inputs = ingredients.getInputs(VanillaTypes.ITEM);
    IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();
    guiItemStacks.init(0, true, 5, 6);
    int sz = 0; // recipe.getIngredients().size();
    for (Ingredient wtf : recipe.getIngredients()) {
      if (wtf != Ingredient.EMPTY && wtf.getItems().length > 0) {
        sz++;
      }
    }
    List<ItemStack> haxor = new ArrayList<>();
    for (ItemStack st : inputs.get(0)) {
      if (st.isEmpty()) {
        continue;
      }
      ItemStack cpy = st.copy();
      cpy.setCount(sz);
      haxor.add(cpy);
    }
    guiItemStacks.set(0, haxor);
    guiItemStacks.init(1, false, 67, 8);
    guiItemStacks.set(1, recipe.getResultItem());
  }
}
