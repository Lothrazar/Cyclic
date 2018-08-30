package com.lothrazar.cyclicmagic.compat.jei;

import java.util.List;
import com.lothrazar.cyclicmagic.util.Const;
import com.lothrazar.cyclicmagic.util.UtilChat;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class HydratorRecipeCategory implements IRecipeCategory<HydratorWrapper> {

  private IDrawable gui;
  private IDrawable icon;

  public HydratorRecipeCategory(IGuiHelper helper) {
    gui = helper.createDrawable(new ResourceLocation(Const.MODID, "textures/gui/hydrator_recipe.png"), 0, 0, 169, 69, 169, 69);
    icon = helper.createDrawable(new ResourceLocation(Const.MODID, "textures/blocks/hydrator.png"), 0, 0, 16, 16, 16, 16);
  }

  @Override
  public String getUid() {
    return JEIPlugin.RECIPE_CATEGORY_HYDRATOR;
  }

  @Override
  public String getTitle() {
    return UtilChat.lang("tile.block_hydrator.name");
  }

  @Override
  public String getModName() {
    return Const.MODID;
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
  public void setRecipe(IRecipeLayout recipeLayout, HydratorWrapper recipeWrapper, IIngredients ingredients) {
    IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();
    guiItemStacks.init(0, true, 3, Const.SQ);
    guiItemStacks.init(1, true, 3, 2 * Const.SQ);
    guiItemStacks.init(2, true, 3 + Const.SQ, Const.SQ);
    guiItemStacks.init(3, true, 3 + Const.SQ, 2 * Const.SQ);
    List<List<ItemStack>> inputs = ingredients.getInputs(ItemStack.class);
    for (int i = 0; i < inputs.size(); i++) {
      List<ItemStack> input = inputs.get(i);
      if (input != null && input.isEmpty() == false)
        guiItemStacks.set(i, input.get(0));
    }
    guiItemStacks.init(4, false, 129, 18);
    guiItemStacks.set(4, recipeWrapper.getOut());
  }
}