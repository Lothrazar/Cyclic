package com.lothrazar.cyclic.compat.jei;

import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.data.Const;
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
    icon = helper.drawableBuilder(new ResourceLocation(ModCyclic.MODID, "textures/blocks/machine/packager.png"), 0, 0, 16, 16).setTextureSize(16, 16).build();
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
    System.out.println(recipe.getId() + " ISHANDLED!!");
    return false;
    //    int total = 0, matched = 0;
    //    Ingredient first = Ingredient.EMPTY;
    //    for (Ingredient ingr : recipe.getIngredients()) {
    //      if (ingr == Ingredient.EMPTY || ingr.hasNoMatchingItems()) {
    //        continue;
    //      }
    //      if (first == Ingredient.EMPTY) {
    //        first = ingr;
    //        total++;
    //        continue;
    //      }
    //      //check first against loop var
    //      total++;
    //      if (first.test(ingr.getMatchingStacks()[0])) {
    //        matched++;
    //      }
    //    }
    //    System.out.println(recipe.getId() + " total=" + total + " matched=" + matched);
    //    //
    //    if (total == matched &&
    //    //        stack.getCount() >= total &&
    //        (total == 4 || total == 9) &&
    //        (recipe.getRecipeOutput().getCount() == 1 || recipe.getRecipeOutput().getCount() == total)) {
    //      System.out.println(recipe.getId() + " WINNER=");
    //      return true;
    //    }
    //    return false;
  }

  @Override
  public void setIngredients(ICraftingRecipe recipe, IIngredients ingredients) {
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
    List<List<ItemStack>> inputs = ingredients.getInputs(VanillaTypes.ITEM);
    IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();
    guiItemStacks.init(0, true, 5, 6);
    guiItemStacks.set(0, inputs.get(0));
    guiItemStacks.init(1, false, 64, 6 + Const.SQ);
    guiItemStacks.set(1, recipe.getRecipeOutput());
  }
}
