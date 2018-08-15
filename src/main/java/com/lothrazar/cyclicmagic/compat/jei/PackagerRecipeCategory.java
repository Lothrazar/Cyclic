package com.lothrazar.cyclicmagic.compat.jei;

import java.util.List;
import com.lothrazar.cyclicmagic.core.util.Const;
import com.lothrazar.cyclicmagic.core.util.UtilChat;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class PackagerRecipeCategory implements IRecipeCategory<PackagerWrapper> {

  private IDrawable gui;
  private IDrawable icon;

  public PackagerRecipeCategory(IGuiHelper helper) {
    gui = helper.createDrawable(new ResourceLocation(Const.MODID, "textures/gui/packager_recipe.png"), 0, 0, 169, 69, 169, 69);
    icon = helper.createDrawable(new ResourceLocation(Const.MODID, "textures/blocks/auto_packager.png"), 0, 0, 16, 16, 16, 16);
  }

  @Override
  public String getUid() {
    return JEIPlugin.RECIPE_CATEGORY_PACKAGER;
  }

  @Override
  public String getTitle() {
    return UtilChat.lang("tile.auto_packager.name");
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
  public void setRecipe(IRecipeLayout recipeLayout, PackagerWrapper recipeWrapper, IIngredients ingredients) {
    IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();
    int x = 3, y = 18;
    guiItemStacks.init(0, true, x, y);
    guiItemStacks.init(1, true, x + Const.SQ, y);
    guiItemStacks.init(2, true, x + 2 * Const.SQ, y);
    //next row
    y += Const.SQ;
    guiItemStacks.init(3, true, x, y);
    guiItemStacks.init(4, true, x + Const.SQ, y);
    guiItemStacks.init(5, true, x + 2 * Const.SQ, y);
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