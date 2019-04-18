package com.lothrazar.cyclicmagic.compat.jei;

import java.util.List;
import com.lothrazar.cyclicmagic.CyclicContent;
import com.lothrazar.cyclicmagic.block.solidifier.RecipeSolidifier;
import com.lothrazar.cyclicmagic.util.Const;
import com.lothrazar.cyclicmagic.util.UtilChat;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeCategory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

public class SolidifierRecipeCategory implements IRecipeCategory<SolidifierWrapper> {

  private IDrawable gui;
  private IDrawable icon;

  public SolidifierRecipeCategory(IGuiHelper helper) {
    gui = helper.drawableBuilder(new ResourceLocation(Const.MODID, "textures/gui/solidifier_recipe.png"), 0, 0, 169, 69).setTextureSize(169, 69).build();
    icon = helper.drawableBuilder(new ResourceLocation(Const.MODID, "textures/blocks/solidifier.png"), 0, 0, 16, 16).setTextureSize(16, 16).build();
  }

  @Override
  public String getUid() {
    return CyclicContent.solidifier.getContentName();
  }

  @Override
  public String getTitle() {
    return UtilChat.lang("tile.solidifier.name");
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
  public void setRecipe(IRecipeLayout recipeLayout, SolidifierWrapper recipeWrapper, IIngredients ingredients) {
    IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();
    int x = 3, y = 18;
    guiItemStacks.init(0, true, x, y);
    guiItemStacks.init(1, true, x + Const.SQ, y);
    //guiItemStacks.init(2, true, x + 2 * Const.SQ, y);
    //next row
    y += Const.SQ;
    guiItemStacks.init(2, true, x, y);
    guiItemStacks.init(3, true, x + Const.SQ, y);
    List<List<ItemStack>> inputs = ingredients.getInputs(VanillaTypes.ITEM);
    for (int i = 0; i < inputs.size(); i++) {
      List<ItemStack> input = inputs.get(i);
      if (input != null && input.isEmpty() == false)
        guiItemStacks.set(i, input.get(0));
    }
    x = 103;
    y = 28;
    guiItemStacks.init(4, false, x, y);
    guiItemStacks.set(4, recipeWrapper.getOut());
    // 
    x = 59;
    y = 27;
    try {
      RecipeSolidifier recipe = recipeWrapper.getRecipe();
      Fluid f = FluidRegistry.getFluid(recipe.getFluidString());//recipeWrapper.getRecipe().getFluidResult();
      //getname is the same  
      recipeLayout.getFluidStacks().init(0, true, x, y, Const.SQ, Const.SQ - 2, Fluid.BUCKET_VOLUME, false,
          null);
      recipeLayout.getFluidStacks().set(0, new FluidStack(f, recipe.getFluidCost()));
    }
    catch (Exception e) {
      //
    }
  }
}