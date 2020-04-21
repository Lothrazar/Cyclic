package com.lothrazar.cyclic.recipe;

import java.util.ArrayList;
import java.util.List;
import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.block.melter.TileMelter;
import com.lothrazar.cyclic.util.UtilItemStack;
import net.minecraft.block.Blocks;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidStack;

@SuppressWarnings("rawtypes")
public class RecipeMelter<TileEntityBase> extends CyclicRecipe {

  public static List<RecipeMelter> RECIPES = new ArrayList<>();
  NonNullList<ItemStack> ing = NonNullList.withSize(1, ItemStack.EMPTY);
  NonNullList<Ingredient> ingredients = NonNullList.create();
  private FluidStack outFluid;
  private ItemStack outOptional;

  protected RecipeMelter(ResourceLocation id, ItemStack in, FluidStack out, ItemStack outOptional) {
    super(id);
    ing.set(0, in);
    ingredients.add(Ingredient.fromStacks(in));
    this.outFluid = out;
    this.outOptional = outOptional;
  }

  @Override
  public boolean matches(com.lothrazar.cyclic.base.TileEntityBase inv, World worldIn) {
    try {
      TileMelter tile = (TileMelter) inv;
      ItemStack current = tile.getStackInputSlot();
      if (UtilItemStack.matches(current, getRecipeInput())
          && current.getCount() >= getRecipeInput().getCount()) {
        return true;
      }
      return false;
    }
    catch (ClassCastException e) {
      return false;
    }
  }

  @Override
  public NonNullList<Ingredient> getIngredients() {
    return ingredients;
  }

  public ItemStack getRecipeInput() {
    return ing.get(0);
  }

  @Override
  public ItemStack getRecipeOutput() {
    return outOptional.copy();
  }

  @Override
  public FluidStack getRecipeFluidOutput() {
    return outFluid.copy();
  }

  @Override
  public IRecipeType<?> getType() {
    return CyclicRecipeType.MELTER;
  }

  public static void initAllRecipes() {
    RecipeMelter.addRecipe("snowwater", new ItemStack(Blocks.SNOW_BLOCK),
        new FluidStack(Fluids.WATER, FluidAttributes.BUCKET_VOLUME / 10), ItemStack.EMPTY);
    RecipeMelter.addRecipe("icetowater", new ItemStack(Blocks.ICE),
        new FluidStack(Fluids.WATER, FluidAttributes.BUCKET_VOLUME), ItemStack.EMPTY);
    RecipeMelter.addRecipe("obsidianlava", new ItemStack(Blocks.OBSIDIAN),
        new FluidStack(Fluids.LAVA, FluidAttributes.BUCKET_VOLUME), ItemStack.EMPTY);
  }

  private static void addRecipe(String name, ItemStack itemStack, FluidStack fluidStack,
      ItemStack empty) {
    RECIPES.add(new RecipeMelter(
        new ResourceLocation(ModCyclic.MODID, name),
        itemStack,
        fluidStack,
        empty));
  }
}
