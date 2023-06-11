package com.lothrazar.cyclic.compat.jei;

import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.block.crusher.RecipeCrusher;
import com.lothrazar.cyclic.registry.BlockRegistry;
import com.lothrazar.cyclic.util.ChatUtil;
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
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class CrusherRecipeCategory implements IRecipeCategory<RecipeCrusher> {

  private static final int FONT = 4210752;
  private static final ResourceLocation ID = new ResourceLocation(ModCyclic.MODID, "crusher");
  static final RecipeType<RecipeCrusher> TYPE = new RecipeType<>(ID, RecipeCrusher.class);
  private IDrawable gui;
  private IDrawable icon;

  public CrusherRecipeCategory(IGuiHelper helper) {
    gui = helper.drawableBuilder(new ResourceLocation(ModCyclic.MODID, "textures/jei/crusher.png"), 0, 0, 155, 49).setTextureSize(155, 49).build();
    icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(BlockRegistry.CRUSHER.get()));
  }

  @Override
  public Component getTitle() {
    return ChatUtil.ilang(BlockRegistry.CRUSHER.get().getDescriptionId());
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
  public RecipeType<RecipeCrusher> getRecipeType() {
    return TYPE;
  }

  @Override
  public void draw(RecipeCrusher recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics ms, double mouseX, double mouseY) {
    int x = 78;
    var font=Minecraft.getInstance().font;
    if (recipe.energy.getTicks() < 40) {
      ms.drawString(font, recipe.energy.getTicks() + " t", x, 6, FONT);
    }
    else {
      int sec = recipe.energy.getTicks() / 20;
      ms.drawString(font, sec + " s", x, 6, FONT);
    }
    ms.drawString(font, recipe.energy.getRfPertick() + " RF/t", x, 16, FONT);
    ms.drawString(font, recipe.energy.getEnergyTotal() + " RF", x, 26, FONT);
    if (!recipe.randOutput.bonus.isEmpty() && recipe.randOutput.percent > 0) {
      ms.drawString(font, recipe.randOutput.percent + "%", 56, 36, FONT);
    }
  }

  @Override
  public void setRecipe(IRecipeLayoutBuilder builder, RecipeCrusher recipe, IFocusGroup focuses) {
    builder.addSlot(RecipeIngredientRole.INPUT, 3, 14).addIngredients(recipe.at(0));
    builder.addSlot(RecipeIngredientRole.OUTPUT, 35, 6).addItemStack(recipe.result);
    if (!recipe.randOutput.bonus.isEmpty() && recipe.randOutput.percent > 0) {
      builder.addSlot(RecipeIngredientRole.OUTPUT, 34, 31).addItemStack(recipe.randOutput.bonus);
    }
  }
  //keep old code for reference
  //  @Override
  //  public void setIngredients(RecipeCrusher recipe, IIngredients ingredients) {
  //    List<List<ItemStack>> in = new ArrayList<>();
  //    List<ItemStack> stuff = new ArrayList<>();
  //    Collections.addAll(stuff, recipe.ingredientAt(0));
  //    in.add(stuff);
  //    ingredients.setInputLists(VanillaTypes.ITEM, in);
  //    if (recipe.randOutput.bonus.isEmpty()) {
  //      ingredients.setOutput(VanillaTypes.ITEM, recipe.getResultItem());
  //    }
  //    else if (recipe.getResultItem().isEmpty()) {
  //      ingredients.setOutput(VanillaTypes.ITEM, recipe.randOutput.bonus);
  //    }
  //    else {
  //      ingredients.setOutputs(VanillaTypes.ITEM, Arrays.asList(recipe.getResultItem(), recipe.randOutput.bonus));
  //    }
  //  }
  //  @Override
  //  public void setRecipe(IRecipeLayout recipeLayout, RecipeCrusher recipe, IIngredients ingredients) {
  //    IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();
  //    guiItemStacks.init(0, true, 2, 13);
  //    List<List<ItemStack>> inputs = ingredients.getInputs(VanillaTypes.ITEM);
  //    guiItemStacks.set(0, inputs.get(0));
  //    guiItemStacks.init(1, false, 34, 5);
  //    guiItemStacks.set(1, recipe.getResultItem());
  //    if (!recipe.randOutput.bonus.isEmpty() && recipe.randOutput.percent > 0) {
  //      guiItemStacks.init(2, false, 33, 30);
  //      guiItemStacks.set(2, recipe.randOutput.bonus);
  //    }
  //  }
}
