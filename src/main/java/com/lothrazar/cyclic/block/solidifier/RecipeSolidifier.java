package com.lothrazar.cyclic.block.solidifier;

import java.util.ArrayList;
import java.util.List;
import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.fluid.FluidBiomassHolder;
import com.lothrazar.cyclic.fluid.FluidHoneyHolder;
import com.lothrazar.cyclic.fluid.FluidMagmaHolder;
import com.lothrazar.cyclic.recipe.CyclicRecipe;
import com.lothrazar.cyclic.recipe.CyclicRecipeType;
import com.lothrazar.cyclic.registry.BlockRegistry;
import com.lothrazar.cyclic.registry.ItemRegistry;
import net.minecraft.block.Blocks;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tags.ItemTags;
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
      FluidStack fluid,
      ItemStack result) {
    super(id);
    this.result = result;
    this.fluidInput = fluid;
  }

  private RecipeSolidifier(ResourceLocation id,
      ItemStack in, ItemStack inSecond, ItemStack inThird, FluidStack fluid,
      ItemStack result) {
    this(id, fluid, result);
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

  @SuppressWarnings("unchecked")
  public static void initAllRecipes() {
    RecipeSolidifier.addRecipe("concrete",
        new ItemStack(Blocks.BLACK_CONCRETE_POWDER),
        new ItemStack(Blocks.BLACK_CONCRETE_POWDER),
        new ItemStack(Blocks.BLACK_CONCRETE_POWDER),
        new FluidStack(Fluids.WATER, FluidAttributes.BUCKET_VOLUME / 10),
        new ItemStack(Blocks.BLACK_CONCRETE, 3));
    RecipeSolidifier.addRecipe("freezeice",
        new ItemStack(Blocks.SNOW_BLOCK),
        new ItemStack(Blocks.SNOW_BLOCK),
        new ItemStack(Blocks.SNOW_BLOCK),
        new FluidStack(Fluids.WATER, FluidAttributes.BUCKET_VOLUME),
        new ItemStack(Blocks.ICE));
    RecipeSolidifier.addRecipe("obsidian",
        new ItemStack(Blocks.COBBLESTONE),
        new ItemStack(Blocks.COBBLESTONE),
        new ItemStack(Blocks.COBBLESTONE),
        new FluidStack(Fluids.LAVA, FluidAttributes.BUCKET_VOLUME),
        new ItemStack(Blocks.OBSIDIAN));
    //
    RecipeSolidifier.addRecipe("bio",
        new ItemStack(ItemRegistry.biomass),
        new ItemStack(Blocks.DIRT),
        new ItemStack(Items.CHARCOAL),
        new FluidStack(FluidBiomassHolder.STILL.get(), FluidAttributes.BUCKET_VOLUME / 2),
        new ItemStack(BlockRegistry.peat_unbaked));
    RecipeSolidifier rec = new RecipeSolidifier(
        new ResourceLocation(ModCyclic.MODID, "solid_leafbiomass"),
        new FluidStack(Fluids.WATER, FluidAttributes.BUCKET_VOLUME / 4),
        new ItemStack(ItemRegistry.biomass));
    rec.ingredients.add(Ingredient.fromTag(ItemTags.LEAVES));
    rec.ingredients.add(Ingredient.fromTag(ItemTags.LEAVES));
    rec.ingredients.add(Ingredient.fromTag(ItemTags.LEAVES));
    RECIPES.add(rec);
    rec = new RecipeSolidifier(
        new ResourceLocation(ModCyclic.MODID, "solid_biomass"),
        new FluidStack(Fluids.WATER, FluidAttributes.BUCKET_VOLUME / 10),
        new ItemStack(ItemRegistry.biomass));
    rec.ingredients.add(Ingredient.fromTag(ItemTags.SMALL_FLOWERS));
    rec.ingredients.add(Ingredient.fromTag(ItemTags.SMALL_FLOWERS));
    rec.ingredients.add(Ingredient.fromTag(ItemTags.SMALL_FLOWERS));
    RECIPES.add(rec);
    //
    RecipeSolidifier.addRecipe("honeyamber",
        new ItemStack(Items.DIAMOND),
        new ItemStack(Items.REDSTONE),
        new ItemStack(Blocks.MAGMA_BLOCK),
        new FluidStack(FluidHoneyHolder.STILL.get(), FluidAttributes.BUCKET_VOLUME / 2),
        new ItemStack(ItemRegistry.gem_amber));
    RecipeSolidifier.addRecipe("honeyfireamber",
        new ItemStack(Items.FIRE_CHARGE),
        new ItemStack(Blocks.REDSTONE_BLOCK),
        new ItemStack(Items.HONEY_BOTTLE),
        new FluidStack(FluidMagmaHolder.STILL.get(), FluidAttributes.BUCKET_VOLUME / 2),
        new ItemStack(ItemRegistry.gem_amber));
    //    RecipeSolidifier.addRecipe("honeyemamber",
    //        new ItemStack(Items.FIRE_CHARGE),
    //        new ItemStack(Blocks.REDSTONE_BLOCK),
    //        new ItemStack(Items.IRON_INGOT),
    //        new FluidStack(FluidMagmaHolder.STILL.get(), FluidAttributes.BUCKET_VOLUME / 2),
    //        new ItemStack(ItemRegistry.gem_amber));
    //
    RecipeSolidifier.addRecipe("purpemerald",
        new ItemStack(Blocks.OBSIDIAN),
        new ItemStack(ItemRegistry.gem_amber),
        new ItemStack(Items.EMERALD),
        new FluidStack(FluidMagmaHolder.STILL.get(), FluidAttributes.BUCKET_VOLUME / 4),
        new ItemStack(ItemRegistry.gem_obsidian));
    RecipeSolidifier.addRecipe("purp",
        new ItemStack(Blocks.OBSIDIAN),
        new ItemStack(ItemRegistry.gem_amber),
        new ItemStack(Items.DIAMOND),
        new FluidStack(FluidMagmaHolder.STILL.get(), FluidAttributes.BUCKET_VOLUME),
        new ItemStack(ItemRegistry.gem_obsidian));
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
