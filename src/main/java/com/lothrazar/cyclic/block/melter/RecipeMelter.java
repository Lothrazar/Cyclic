package com.lothrazar.cyclic.block.melter;

import java.util.ArrayList;
import java.util.List;
import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.fluid.FluidBiomassHolder;
import com.lothrazar.cyclic.fluid.FluidHoneyHolder;
import com.lothrazar.cyclic.fluid.FluidSlimeHolder;
import com.lothrazar.cyclic.fluid.FluidXpJuiceHolder;
import com.lothrazar.cyclic.recipe.CyclicRecipe;
import com.lothrazar.cyclic.recipe.CyclicRecipeType;
import com.lothrazar.cyclic.registry.ItemRegistry;
import com.lothrazar.cyclic.util.UtilItemStack;
import net.minecraft.block.Blocks;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
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
  private NonNullList<Ingredient> ingredients = NonNullList.create();
  private FluidStack outFluid;

  protected RecipeMelter(ResourceLocation id, ItemStack in, ItemStack inSecond, FluidStack out) {
    super(id);
    ingredients.add(Ingredient.fromStacks(in));
    ingredients.add(Ingredient.fromStacks(inSecond));
    this.outFluid = out;
  }

  @Override
  public boolean matches(com.lothrazar.cyclic.base.TileEntityBase inv, World worldIn) {
    try {
      TileMelter tile = (TileMelter) inv;
      if (matches(tile, 0) && matches(tile, 1)) {
        return true;
      }
      return false;
    }
    catch (ClassCastException e) {
      return false;
    }
  }

  public boolean matches(TileMelter tile, int slot) {
    ItemStack current = tile.getStackInputSlot(slot);//get slot thing
    Ingredient ing = ingredients.get(slot);
    for (ItemStack test : ing.getMatchingStacks()) {
      if (UtilItemStack.matches(current, test)) {
        return true;
      }
    }
    return false;
    //  ingredients.get(0).getMatchingStacks()
  }

  public ItemStack[] ingredientAt(int slot) {
    Ingredient ing = ingredients.get(slot);
    return ing.getMatchingStacks();
  }

  @Override
  public NonNullList<Ingredient> getIngredients() {
    return ingredients;
  }

  @Override
  public ItemStack getRecipeOutput() {
    return ItemStack.EMPTY;
  }

  @Override
  public FluidStack getRecipeFluid() {
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
    RecipeMelter.addRecipe("snowwater",
        new ItemStack(Blocks.SNOW_BLOCK),
        new ItemStack(Blocks.SNOW_BLOCK),
        new FluidStack(Fluids.WATER, FluidAttributes.BUCKET_VOLUME / 5));
    RecipeMelter.addRecipe("icetowater",
        new ItemStack(Blocks.ICE),
        new ItemStack(Blocks.ICE),
        new FluidStack(Fluids.WATER, FluidAttributes.BUCKET_VOLUME * 2));
    //    RecipeMelter.addRecipe("picetowater", 
    //        new ItemStack(Blocks.PACKED_ICE),
    //        new FluidStack(Fluids.WATER, FluidAttributes.BUCKET_VOLUME * 9));
    //    RecipeMelter.addRecipe("bicetowater", new ItemStack(Blocks.BLUE_ICE),
    //        new FluidStack(Fluids.WATER, FluidAttributes.BUCKET_VOLUME * 9 * 9));
    //
    RecipeMelter.addRecipe("fbio",
        new ItemStack(ItemRegistry.biomass),
        new ItemStack(ItemRegistry.biomass),
        new FluidStack(FluidBiomassHolder.STILL.get(), FluidAttributes.BUCKET_VOLUME));
    //
    RecipeMelter.addRecipe("obsidianlava",
        new ItemStack(Blocks.OBSIDIAN),
        new ItemStack(Blocks.COBBLESTONE),
        new FluidStack(Fluids.LAVA, FluidAttributes.BUCKET_VOLUME));
    RecipeMelter.addRecipe("slimeball",
        new ItemStack(Items.SLIME_BALL),
        new ItemStack(Items.SLIME_BALL),
        new FluidStack(FluidSlimeHolder.STILL.get(), 200));
    RecipeMelter.addRecipe("slimeblock",
        new ItemStack(Blocks.SLIME_BLOCK),
        new ItemStack(Blocks.SLIME_BLOCK),
        new FluidStack(FluidSlimeHolder.STILL.get(), 1800));
    RecipeMelter.addRecipe("fexperience_food",
        new ItemStack(ItemRegistry.experience_food),
        new ItemStack(ItemRegistry.experience_food),
        new FluidStack(FluidXpJuiceHolder.STILL.get(), FluidAttributes.BUCKET_VOLUME));
    RecipeMelter.addRecipe("honey",
        new ItemStack(Items.HONEY_BLOCK),
        new ItemStack(Items.HONEY_BLOCK),
        new FluidStack(FluidHoneyHolder.STILL.get(), 2000));
    RecipeMelter.addRecipe("honeyb",
        new ItemStack(Items.HONEY_BOTTLE),
        new ItemStack(Items.HONEY_BOTTLE),
        new FluidStack(FluidHoneyHolder.STILL.get(), 500));
  }

  private static void addRecipe(String name, ItemStack itemStack, ItemStack secnd, FluidStack fluidStack) {
    RECIPES.add(new RecipeMelter(
        new ResourceLocation(ModCyclic.MODID, name),
        itemStack,
        secnd,
        fluidStack));
  }
}
