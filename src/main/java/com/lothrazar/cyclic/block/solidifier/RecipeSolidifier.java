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
  NonNullList<Ingredient> ing = NonNullList.create();
  private FluidStack fluid;

  protected RecipeSolidifier(ResourceLocation id, ItemStack item, FluidStack fluid) {
    super(id);
    this.result = item;
    this.fluid = fluid;
  }

  @Override
  public boolean matches(com.lothrazar.cyclic.base.TileEntityBase inv, World worldIn) {
    try {
      TileSolidifier tile = (TileSolidifier) inv;
      if (tile.getFluid() != null && tile.getFluid().getFluid() == this.fluid.getFluid()
          && this.fluid.getAmount() >= tile.getFluid().getAmount()) {
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
    return ing;//TODO: cant figure how to get fluid in here if it matters
  }

  @Override
  public ItemStack getRecipeOutput() {
    return result.copy();
  }

  @Override
  public FluidStack getRecipeFluid() {
    return fluid.copy();
  }

  @Override
  public IRecipeType<?> getType() {
    return CyclicRecipeType.MELTER;
  }

  public static void initAllRecipes() {
    RecipeSolidifier.addRecipe("freezeice", new ItemStack(Blocks.ICE),
        new FluidStack(Fluids.WATER, FluidAttributes.BUCKET_VOLUME));
    //    RecipeSolidifier.addRecipe("picetowater", new ItemStack(Blocks.PACKED_ICE),
    //        new FluidStack(Fluids.WATER, FluidAttributes.BUCKET_VOLUME * 9));
    //    RecipeSolidifier.addRecipe("bicetowater", new ItemStack(Blocks.BLUE_ICE),
    //        new FluidStack(Fluids.WATER, FluidAttributes.BUCKET_VOLUME * 9 * 9));
    //    //
    //    RecipeSolidifier.addRecipe("fbio", new ItemStack(ItemRegistry.biomass),
    //        new FluidStack(FluidBiomassHolder.STILL.get(), FluidAttributes.BUCKET_VOLUME));
    //    //
    //    RecipeSolidifier.addRecipe("obsidianlava", new ItemStack(Blocks.OBSIDIAN),
    //        new FluidStack(Fluids.LAVA, FluidAttributes.BUCKET_VOLUME));
    //    RecipeSolidifier.addRecipe("fgem_amber", new ItemStack(ItemRegistry.gem_amber),
    //        new FluidStack(FluidAmberHolder.STILL.get(), FluidAttributes.BUCKET_VOLUME));
    //    RecipeSolidifier.addRecipe("fgem_obsidian", new ItemStack(ItemRegistry.gem_obsidian),
    //        new FluidStack(FluidCrystalHolder.STILL.get(), FluidAttributes.BUCKET_VOLUME));
    //    //
    //    RecipeSolidifier.addRecipe("fexperience_food", new ItemStack(ItemRegistry.experience_food),
    //        new FluidStack(FluidXpJuiceHolder.STILL.get(), FluidAttributes.BUCKET_VOLUME));
  }

  private static void addRecipe(String name, ItemStack itemStack, FluidStack fluidStack) {
    RECIPES.add(new RecipeSolidifier(
        new ResourceLocation(ModCyclic.MODID, "solid_" + name),
        itemStack,
        fluidStack));
  }
}
