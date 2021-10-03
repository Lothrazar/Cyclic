package com.lothrazar.cyclic.compat.jei;

import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.registry.BlockRegistry;
import com.lothrazar.cyclic.util.UtilChat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.ICraftingRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;

public class PackagerRecipeCategory implements IRecipeCategory<ICraftingRecipe> {

  static final ResourceLocation ID = new ResourceLocation("cyclic:packager");
  private IDrawable gui;
  private IDrawable icon;

  public PackagerRecipeCategory(IGuiHelper helper) {
    gui = helper.drawableBuilder(new ResourceLocation(ModCyclic.MODID, "textures/jei/packager.png"), 0, 0, 118, 32).setTextureSize(118, 32).build();
    icon = helper.createDrawableIngredient(new ItemStack(BlockRegistry.PACKAGER.get())); // helper.drawableBuilder(new ResourceLocation(ModCyclic.MODID, "textures/blocks/machine/packager.png"), 0, 0, 16, 16).setTextureSize(16, 16).build();
  }

  @Override
  public String getTitle() {
    return UtilChat.lang(BlockRegistry.PACKAGER.get().getTranslationKey());
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
  public Class<? extends ICraftingRecipe> getRecipeClass() {
    return ICraftingRecipe.class;
  }

  @Override
  public ResourceLocation getUid() {
    return ID;
  }

  @Override
  public boolean isHandled(ICraftingRecipe recipe) {
    boolean testingBreakpoints = false;
    testingBreakpoints = true;
    if (testingBreakpoints) {
      throw new IllegalArgumentException("returning false does not work is really bad what is happening");
    }
    System.out.println(recipe.getId() + " ISHANDLED!!");
    return isRecipeValid(recipe);
  }

  private boolean isRecipeValid(ICraftingRecipe recipe) {
    int total = 0, matched = 0;
    Ingredient first = null;
    for (Ingredient ingr : recipe.getIngredients()) {
      if (ingr == Ingredient.EMPTY || ingr.getMatchingStacks().length == 0) {
        continue;
      }
      total++;
      if (first == null) {
        first = ingr;
        matched = 1;
        continue;
      }
      if (first.test(ingr.getMatchingStacks()[0])) {
        matched++;
      }
    }
    System.out.println(recipe.getId() + " total=" + total + " matched=" + matched);
    //
    if (total > 0 && total == matched &&
    //        stack.getCount() >= total &&
        (total == 4 || total == 9) &&
        (recipe.getRecipeOutput().getCount() == 1 || recipe.getRecipeOutput().getCount() == total)) {
      System.out.println(recipe.getId() + " IS VALID=");
      return true;
    }
    System.out.println(recipe.getId() + " IS NOT");
    return false;
  }

  @Override
  public void setIngredients(ICraftingRecipe recipe, IIngredients ingredients) {
    if (!isRecipeValid(recipe)) {
      return;
    }
    //    ingredients.setInput(VanillaTypes.FLUID, recipe.getRecipeFluid());
    List<List<ItemStack>> in = new ArrayList<>();
    List<ItemStack> stuff = new ArrayList<>();
    Ingredient ingr = recipe.getIngredients().get(0);
    Collections.addAll(stuff, ingr.getMatchingStacks());
    in.add(stuff);
    ingredients.setInputLists(VanillaTypes.ITEM, in);
  }
  //  @Override
  //  public void draw(ICraftingRecipe recipe, MatrixStack ms, double mouseX, double mouseY) {
  //    Minecraft.getInstance().fontRenderer.drawString(ms, recipe.getTicks() + " t", 60, 0, FONT);
  //    Minecraft.getInstance().fontRenderer.drawString(ms, recipe.getRfpertick() + " RF/t", 60, 10, FONT);
  //    Minecraft.getInstance().fontRenderer.drawString(ms, recipe.getRfTotal() + " RF", 60, 20, FONT);
  //  }

  @Override
  public void setRecipe(IRecipeLayout recipeLayout, ICraftingRecipe recipe, IIngredients ingredients) {
    if (!isRecipeValid(recipe)) {
      return;
    }
    List<List<ItemStack>> inputs = ingredients.getInputs(VanillaTypes.ITEM);
    IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();
    guiItemStacks.init(0, true, 5, 6);
    int sz = recipe.getIngredients().size();
    for (Ingredient wtf : recipe.getIngredients()) {
      //      System.out.println(recipe.getId() + " WTF " + wtf);
    }
    List<ItemStack> haxor = new ArrayList<>();
    for (ItemStack st : inputs.get(0)) {
      ItemStack cpy = st.copy();
      cpy.setCount(sz);
      haxor.add(cpy);
    }
    guiItemStacks.set(0, haxor);
    guiItemStacks.init(1, false, 67, 8);
    guiItemStacks.set(1, recipe.getRecipeOutput());
  }
}
