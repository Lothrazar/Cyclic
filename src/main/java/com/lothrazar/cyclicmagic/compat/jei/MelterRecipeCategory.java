package com.lothrazar.cyclicmagic.compat.jei;

import java.util.List;
import com.lothrazar.cyclicmagic.CyclicContent;
import com.lothrazar.cyclicmagic.util.Const;
import com.lothrazar.cyclicmagic.util.UtilChat;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IDrawableStatic;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeCategory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class MelterRecipeCategory implements IRecipeCategory<MelterWrapper> {

  private IDrawable gui;
  private IDrawable icon;
  private IDrawableStatic fluid;

  public MelterRecipeCategory(IGuiHelper helper) {
    gui = helper.drawableBuilder(new ResourceLocation(Const.MODID, "textures/gui/melter_recipe.png"), 0, 0, 169, 69).setTextureSize(169, 69).build();
    icon = helper.drawableBuilder(new ResourceLocation(Const.MODID, "textures/blocks/melter.png"), 0, 0, 16, 16).setTextureSize(16, 16).build();
    fluid = helper.drawableBuilder(new ResourceLocation(Const.MODID, "textures/gui/fluid.png"), 0, 0, 16, 16).setTextureSize(16, 16).build();
  }

  @Override
  public String getUid() {
    return CyclicContent.melter.getContentName();
  }

  @Override
  public String getTitle() {
    return UtilChat.lang("tile.melter.name");
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
  public void setRecipe(IRecipeLayout recipeLayout, MelterWrapper recipeWrapper, IIngredients ingredients) {
    IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();
    int x = 0, y = 18;
    int s = Const.SQ + 3;
    guiItemStacks.init(0, true, x, y);
    guiItemStacks.init(1, true, x + s, y);
    //next row
    y += s;
    guiItemStacks.init(2, true, x + s, y);
    guiItemStacks.init(3, true, x, y);
    List<List<ItemStack>> inputs = ingredients.getInputs(VanillaTypes.ITEM);
    for (int i = 0; i < inputs.size(); i++) {
      List<ItemStack> input = inputs.get(i);
      if (input != null && input.isEmpty() == false)
        guiItemStacks.set(i, input.get(0));
    }
    //List<List<FluidStack>> liq = ingredients.getOutputs(VanillaTypes.FLUID);
    //    FluidStack f = liq.get(0).get(0);
    //    recipeLayout.getFluidStacks().init(0, false, x, y, 20, 60, 64000, true,
    //        fluid);
  }
}