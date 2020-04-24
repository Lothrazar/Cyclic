package com.lothrazar.cyclic.block.solidifier;

import java.util.ArrayList;
import java.util.List;
import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.recipe.CyclicRecipe;
import com.lothrazar.cyclic.recipe.CyclicRecipeType;
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
public class RecipeSolidifier<TileEntityBase> extends CyclicRecipe {

  public static List<RecipeSolidifier> RECIPES = new ArrayList<>();
  private ItemStack result = ItemStack.EMPTY;
  private NonNullList<Ingredient> ingredients = NonNullList.create();
  private FluidStack fluidInput;

  private RecipeSolidifier(ResourceLocation id,
      ItemStack in, ItemStack inSecond, ItemStack inThird, FluidStack fluid,
      ItemStack result) {
    super(id);
    this.result = result;
    this.fluidInput = fluid;
    ingredients.add(Ingredient.fromStacks(in));
    ingredients.add(Ingredient.fromStacks(inSecond));
    ingredients.add(Ingredient.fromStacks(inThird));
  }

  @Override
  public boolean matches(com.lothrazar.cyclic.base.TileEntityBase inv, World worldIn) {
    try {
      TileSolidifier tile = (TileSolidifier) inv;
      if (tile.getFluid() != null && tile.getFluid().getFluid() == this.fluidInput.getFluid()
          && this.fluidInput.getAmount() >= tile.getFluid().getAmount()) {
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

  public ItemStack[] ingredientAt(int slot) {
    Ingredient ing = ingredients.get(slot);
    return ing.getMatchingStacks();
  }

  @Override
  public ItemStack getRecipeOutput() {
    return result.copy();
  }

  @Override
  public FluidStack getRecipeFluid() {
    return fluidInput.copy();
  }

  @Override
  public IRecipeType<?> getType() {
    return CyclicRecipeType.SOLID;
  }

  public static void initAllRecipes() {
    RecipeSolidifier.addRecipe("freezeice",
        new ItemStack(Blocks.SNOW_BLOCK),
        new ItemStack(Blocks.SNOW_BLOCK),
        new ItemStack(Blocks.SNOW_BLOCK),
        new FluidStack(Fluids.WATER, FluidAttributes.BUCKET_VOLUME),
        new ItemStack(Blocks.ICE));
    RecipeSolidifier.addRecipe("freezeice",
        new ItemStack(Blocks.COBBLESTONE),
        new ItemStack(Blocks.COBBLESTONE),
        new ItemStack(Blocks.COBBLESTONE),
        new FluidStack(Fluids.LAVA, FluidAttributes.BUCKET_VOLUME),
        new ItemStack(Blocks.OBSIDIAN));
    //    RecipeSolidifier.addRecipe("fbio", new ItemStack(ItemRegistry.biomass),
    //        new FluidStack(FluidBiomassHolder.STILL.get(), FluidAttributes.BUCKET_VOLUME));
    //    RecipeSolidifier.addRecipe("obsidianlava", new ItemStack(Blocks.OBSIDIAN),
    //        new FluidStack(Fluids.LAVA, FluidAttributes.BUCKET_VOLUME));
    //    RecipeSolidifier.addRecipe("fgem_amber", new ItemStack(ItemRegistry.gem_amber),
    //        new FluidStack(FluidSlimeHolder.STILL.get(), FluidAttributes.BUCKET_VOLUME));
    //    //
    //    RecipeSolidifier.addRecipe("fexperience_food", new ItemStack(ItemRegistry.experience_food),
    //        new FluidStack(FluidXpJuiceHolder.STILL.get(), FluidAttributes.BUCKET_VOLUME));
  }

  private static void addRecipe(String name, ItemStack a, ItemStack b, ItemStack c, FluidStack fluidStack,
      ItemStack res) {
    RECIPES.add(new RecipeSolidifier(
        new ResourceLocation(ModCyclic.MODID, "solid_" + name),
        a, b, c,
        fluidStack, res));
  }
}
