package com.lothrazar.cyclic.compat.jei;

import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.block.packager.UtilPackager;
import com.lothrazar.cyclic.registry.BlockRegistry;
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
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;

import java.util.ArrayList;
import java.util.List;

public class PackagerRecipeCategory implements IRecipeCategory<RecipeHolder<CraftingRecipe>> {

  private static final Identifier ID = Identifier.fromNamespaceAndPath(ModCyclic.MODID, "packager");
  static final RecipeType<RecipeHolder<CraftingRecipe>> TYPE = new RecipeType<>(ID, (Class) RecipeHolder.class);
  Minecraft instance; // since we call on this so often for recipe validatoin, cache one copy of it for the duration
  private IDrawable gui;
  private IDrawable icon;

  public PackagerRecipeCategory(IGuiHelper helper) {
    gui = helper.drawableBuilder(Identifier.fromNamespaceAndPath(ModCyclic.MODID, "textures/jei/packager.png"), 0, 0, 118, 32).setTextureSize(118, 32).build();
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
  public int getWidth() {
    return gui.getWidth();
  }

  @Override
  public int getHeight() {
    return gui.getHeight();
  }

  @Override
  public RecipeType<RecipeHolder<CraftingRecipe>> getRecipeType() {
    return TYPE;
  }

  @Override
  public void draw(RecipeHolder<CraftingRecipe> recipeHolder, IRecipeSlotsView recipeSlotsView, GuiGraphicsExtractor ms, double mouseX, double mouseY) {
    gui.draw(ms, 0, 0);
  }

  @Override
  public boolean isHandled(RecipeHolder<CraftingRecipe> recipeHolder) {
    return UtilPackager.isRecipeValid(recipeHolder.value(), instance.level.registryAccess());
  }

  @Override
  public void setRecipe(IRecipeLayoutBuilder builder, RecipeHolder<CraftingRecipe> recipeHolder, IFocusGroup focuses) {
    CraftingRecipe recipe = recipeHolder.value();
    // 26.1: Recipe#getIngredients()/getResultItem(HolderLookup.Provider) removed from the interface -
    // getIngredients() -> placementInfo().ingredients(); getResultItem() has no universal replacement,
    // but vanilla CraftingRecipe#assemble(CraftingInput) ignores the actual input for standard recipes,
    // so assemble(CraftingInput.EMPTY) is a safe stand-in for "give me a representative result" here.
    List<Ingredient> ingredients = recipe.placementInfo().ingredients();
    if (ingredients.isEmpty()) {
      return;
    }
    int sz = 0; // recipe.getIngredients().size();
    for (Ingredient wtf : ingredients) {
      if (!wtf.isEmpty()) {
        sz++;
      }
    }
    List<ItemStack> haxor = new ArrayList<>();
    Ingredient ingredientKey = ingredients.get(0);
    for (ItemStack st : ingredientKey.items().map(ItemStack::new).toList()) {
      if (st.isEmpty()) {
        continue;
      }
      ItemStack cpy = st.copy();
      cpy.setCount(sz);
      haxor.add(cpy);
    }
    builder.addSlot(RecipeIngredientRole.INPUT, 6, 7).addIngredients(VanillaTypes.ITEM_STACK, haxor);
    builder.addSlot(RecipeIngredientRole.OUTPUT, 69, 8).addItemStack(recipe.assemble(CraftingInput.EMPTY));
  }
}
