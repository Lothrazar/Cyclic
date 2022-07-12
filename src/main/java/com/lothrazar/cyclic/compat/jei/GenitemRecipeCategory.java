package com.lothrazar.cyclic.compat.jei;

import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.block.generatoritem.RecipeGeneratorItem;
import com.lothrazar.cyclic.registry.BlockRegistry;
import com.lothrazar.cyclic.util.UtilChat;
import com.mojang.blaze3d.vertex.PoseStack;
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

@SuppressWarnings("rawtypes")
public class GenitemRecipeCategory implements IRecipeCategory<RecipeGeneratorItem> {

  private static final int FONT = 4210752;
  private static final ResourceLocation ID = new ResourceLocation(ModCyclic.MODID, "generator_item");
  static final RecipeType<RecipeGeneratorItem> TYPE = new RecipeType<>(ID, RecipeGeneratorItem.class);
  private IDrawable gui;
  private IDrawable icon;

  public GenitemRecipeCategory(IGuiHelper helper) {
    gui = helper.drawableBuilder(new ResourceLocation(ModCyclic.MODID, "textures/jei/generator_item.png"), 0, 0, 118, 32).setTextureSize(118, 32).build();
    icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(BlockRegistry.GENERATOR_ITEM.get()));
  }

  @Override
  public Component getTitle() {
    return UtilChat.ilang(BlockRegistry.GENERATOR_ITEM.get().getDescriptionId());
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
  public Class<? extends RecipeGeneratorItem> getRecipeClass() {
    return RecipeGeneratorItem.class;
  }

  @Override
  public ResourceLocation getUid() {
    return ID;
  }

  @Override
  public RecipeType<RecipeGeneratorItem> getRecipeType() {
    return TYPE;
  }

  @Override
  public void draw(RecipeGeneratorItem recipe, PoseStack ms, double mouseX, double mouseY) {
    Minecraft.getInstance().font.draw(ms, recipe.getTicks() + " t", 60, 0, FONT);
    Minecraft.getInstance().font.draw(ms, recipe.getRfPertick() + " RF/t", 60, 10, FONT);
    Minecraft.getInstance().font.draw(ms, recipe.getEnergyTotal() + " RF", 60, 20, FONT);
  }

  @Override
  public void setRecipe(IRecipeLayoutBuilder builder, RecipeGeneratorItem recipe, IFocusGroup focuses) {
    builder.addSlot(RecipeIngredientRole.INPUT, 6, 7).addIngredients(recipe.at(0));
  }
  //  @Override
  //  public void setIngredients(RecipeGeneratorItem recipe, IIngredients ingredients) { 
  //    List<List<ItemStack>> in = new ArrayList<>();
  //    List<ItemStack> stuff = new ArrayList<>();
  //    Collections.addAll(stuff, recipe.ingredientAt(0));
  //    in.add(stuff);
  //    ingredients.setInputLists(VanillaTypes.ITEM, in);
  //  }
  //
  //  @Override
  //  public void setRecipe(IRecipeLayout recipeLayout, RecipeGeneratorItem recipe, IIngredients ingredients) {
  //    IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();
  //    guiItemStacks.init(0, true, 5, 6);
  //    List<List<ItemStack>> inputs = ingredients.getInputs(VanillaTypes.ITEM);
  //    guiItemStacks.set(0, inputs.get(0));
  //  }
}
