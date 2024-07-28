package com.lothrazar.cyclic.compat.jei;

import java.util.ArrayList;
import java.util.List;
import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.block.packager.UtilPackager;
import com.lothrazar.cyclic.registry.BlockRegistry;
import com.lothrazar.library.util.ChatUtil;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Ingredient;

public class PackagerRecipeCategory implements IRecipeCategory<CraftingRecipe> {

  private static final ResourceLocation ID = new ResourceLocation(ModCyclic.MODID, "packager");
  static final RecipeType<CraftingRecipe> TYPE = new RecipeType<>(ID, CraftingRecipe.class);
  Minecraft instance; // since we call on this so often for recipe validatoin, cache one copy of it for the duration
  private IDrawable gui;
  private IDrawable icon;

  public PackagerRecipeCategory(IGuiHelper helper) {
    gui = helper.drawableBuilder(new ResourceLocation(ModCyclic.MODID, "textures/jei/packager.png"), 0, 0, 118, 32).setTextureSize(118, 32).build();
    icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(BlockRegistry.PACKAGER.get()));
    instance = Minecraft.getInstance();
  }

  @Override
  public Component getTitle() {
    return ChatUtil.ilang(BlockRegistry.PACKAGER.get().getDescriptionId());
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
  public RecipeType<CraftingRecipe> getRecipeType() {
    return TYPE;
  }

  @Override
  public boolean isHandled(CraftingRecipe recipe) {
    return UtilPackager.isRecipeValid(recipe, instance.level.registryAccess());
  }

  @Override
  public void setRecipe(IRecipeLayoutBuilder builder, CraftingRecipe recipe, IFocusGroup focuses) {
    if (recipe.getIngredients().size() == 0) {
      return;
    }
    int sz = 0; // recipe.getIngredients().size();
    for (Ingredient wtf : recipe.getIngredients()) {
      if (wtf != Ingredient.EMPTY && wtf.getItems().length > 0) {
        sz++;
      }
    }
    List<ItemStack> haxor = new ArrayList<>();
    Ingredient ingredientKey = recipe.getIngredients().get(0);
    for (ItemStack st : ingredientKey.getItems()) {
      if (st.isEmpty()) {
        continue;
      }
      ItemStack cpy = st.copy();
      cpy.setCount(sz);
      haxor.add(cpy);
    }
    builder.addSlot(RecipeIngredientRole.INPUT, 6, 7).addIngredients(VanillaTypes.ITEM_STACK, haxor);
    builder.addSlot(RecipeIngredientRole.OUTPUT, 69, 8).addItemStack(recipe.getResultItem(instance.level.registryAccess()));
  }
}
