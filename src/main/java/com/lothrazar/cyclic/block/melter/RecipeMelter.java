package com.lothrazar.cyclic.block.melter;

import java.util.ArrayList;
import java.util.List;
import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.fluid.FluidAmberHolder;
import com.lothrazar.cyclic.fluid.FluidBiomassHolder;
import com.lothrazar.cyclic.fluid.FluidCrystalHolder;
import com.lothrazar.cyclic.fluid.FluidXpJuiceHolder;
import com.lothrazar.cyclic.recipe.CyclicRecipe;
import com.lothrazar.cyclic.recipe.CyclicRecipeType;
import com.lothrazar.cyclic.registry.ItemRegistry;
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
  NonNullList<Ingredient> ingredients = NonNullList.create();
  private FluidStack outFluid;

  protected RecipeMelter(ResourceLocation id, ItemStack in, FluidStack out) {
    super(id);
    ingredients.add(Ingredient.fromStacks(in));
    this.outFluid = out;
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
    ItemStack[] matches = ingredients.get(0).getMatchingStacks();
    return matches.length == 0 ? ItemStack.EMPTY : matches[0];
  }

  public ItemStack[] getRecipeInputs() {
    return ingredients.get(0).getMatchingStacks();
  }

  @Override
  public ItemStack getRecipeOutput() {
    return ItemStack.EMPTY;
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
    //    RecipeMelter.addRecipe("c", new ItemStack(Blocks.BLACK_CONCRETE),
    //        new FluidStack(Fluids.WATER, FluidAttributes.BUCKET_VOLUME / 10),
    //        new ItemStack(Blocks.BLACK_CONCRETE_POWDER));
    //
    RecipeMelter.addRecipe("snowwater", new ItemStack(Blocks.SNOW_BLOCK),
        new FluidStack(Fluids.WATER, FluidAttributes.BUCKET_VOLUME / 10));
    RecipeMelter.addRecipe("icetowater", new ItemStack(Blocks.ICE),
        new FluidStack(Fluids.WATER, FluidAttributes.BUCKET_VOLUME));
    RecipeMelter.addRecipe("picetowater", new ItemStack(Blocks.PACKED_ICE),
        new FluidStack(Fluids.WATER, FluidAttributes.BUCKET_VOLUME * 9));
    RecipeMelter.addRecipe("bicetowater", new ItemStack(Blocks.BLUE_ICE),
        new FluidStack(Fluids.WATER, FluidAttributes.BUCKET_VOLUME * 9 * 9));
    //
    RecipeMelter.addRecipe("fbio", new ItemStack(ItemRegistry.biomass),
        new FluidStack(FluidBiomassHolder.STILL.get(), FluidAttributes.BUCKET_VOLUME));
    //
    RecipeMelter.addRecipe("obsidianlava", new ItemStack(Blocks.OBSIDIAN),
        new FluidStack(Fluids.LAVA, FluidAttributes.BUCKET_VOLUME));
    RecipeMelter.addRecipe("fgem_amber", new ItemStack(ItemRegistry.gem_amber),
        new FluidStack(FluidAmberHolder.STILL.get(), FluidAttributes.BUCKET_VOLUME));
    RecipeMelter.addRecipe("fgem_obsidian", new ItemStack(ItemRegistry.gem_obsidian),
        new FluidStack(FluidCrystalHolder.STILL.get(), FluidAttributes.BUCKET_VOLUME));
    //
    RecipeMelter.addRecipe("fexperience_food", new ItemStack(ItemRegistry.experience_food),
        new FluidStack(FluidXpJuiceHolder.STILL.get(), FluidAttributes.BUCKET_VOLUME));
  }

  private static void addRecipe(String name, ItemStack itemStack, FluidStack fluidStack) {
    RECIPES.add(new RecipeMelter(
        new ResourceLocation(ModCyclic.MODID, name),
        itemStack,
        fluidStack));
  }
}
