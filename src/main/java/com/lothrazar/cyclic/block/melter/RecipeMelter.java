package com.lothrazar.cyclic.block.melter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.block.expcollect.ExpItemGain;
import com.lothrazar.cyclic.fluid.FluidBiomassHolder;
import com.lothrazar.cyclic.fluid.FluidHoneyHolder;
import com.lothrazar.cyclic.fluid.FluidMagmaHolder;
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
      return matches(tile, 0) && matches(tile, 1);
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
    // WATER
    RecipeMelter.addRecipe("snowwater",
        new ItemStack(Blocks.SNOW_BLOCK),
        new ItemStack(Blocks.SNOW_BLOCK),
        new FluidStack(Fluids.WATER, FluidAttributes.BUCKET_VOLUME / 5));
    RecipeMelter.addRecipe("icetowater",
        new ItemStack(Blocks.ICE),
        new ItemStack(Blocks.ICE),
        new FluidStack(Fluids.WATER, FluidAttributes.BUCKET_VOLUME * 2));
    RecipeMelter.addRecipe("fbio",
        new ItemStack(ItemRegistry.biomass),
        new ItemStack(ItemRegistry.biomass),
        new FluidStack(FluidBiomassHolder.STILL.get(), FluidAttributes.BUCKET_VOLUME));
    RecipeMelter.addRecipe("bambooskelp",
        new ItemStack(ItemRegistry.biomass),
        new ItemStack(Items.BAMBOO),
        new FluidStack(FluidBiomassHolder.STILL.get(), FluidAttributes.BUCKET_VOLUME));
    RecipeMelter.addRecipe("wheatkelp",
        new ItemStack(ItemRegistry.biomass),
        new ItemStack(Items.WHEAT_SEEDS),
        new FluidStack(FluidBiomassHolder.STILL.get(), FluidAttributes.BUCKET_VOLUME));
    RecipeMelter.addRecipe("beetrootkelp",
        new ItemStack(ItemRegistry.biomass),
        new ItemStack(Items.BEETROOT_SEEDS),
        new FluidStack(FluidBiomassHolder.STILL.get(), FluidAttributes.BUCKET_VOLUME));
    RecipeMelter.addRecipe("melonkelp",
        new ItemStack(ItemRegistry.biomass),
        new ItemStack(Items.MELON_SEEDS),
        new FluidStack(FluidBiomassHolder.STILL.get(), FluidAttributes.BUCKET_VOLUME));
    RecipeMelter.addRecipe("pumpskelp",
        new ItemStack(ItemRegistry.biomass),
        new ItemStack(Items.PUMPKIN_SEEDS),
        new FluidStack(FluidBiomassHolder.STILL.get(), FluidAttributes.BUCKET_VOLUME));
    RecipeMelter.addRecipe("pumpsasdfkelp",
        new ItemStack(ItemRegistry.biomass),
        new ItemStack(Items.KELP),
        new FluidStack(FluidBiomassHolder.STILL.get(), FluidAttributes.BUCKET_VOLUME));
    // 
    RecipeMelter.addRecipe("obsidiansmstonelava",
        new ItemStack(Blocks.OBSIDIAN),
        new ItemStack(Blocks.SMOOTH_STONE),
        new FluidStack(Fluids.LAVA, FluidAttributes.BUCKET_VOLUME));
    RecipeMelter.addRecipe("obsidiancobblelava",
        new ItemStack(Blocks.OBSIDIAN),
        new ItemStack(Blocks.COBBLESTONE),
        new FluidStack(Fluids.LAVA, FluidAttributes.BUCKET_VOLUME));
    RecipeMelter.addRecipe("obsidiangravellava",
        new ItemStack(Blocks.OBSIDIAN),
        new ItemStack(Blocks.GRAVEL),
        new FluidStack(Fluids.LAVA, FluidAttributes.BUCKET_VOLUME));
    RecipeMelter.addRecipe("slimemagmaball",
        new ItemStack(Items.MAGMA_CREAM),
        new ItemStack(Items.MAGMA_CREAM),
        new FluidStack(FluidSlimeHolder.STILL.get(), 200));
    RecipeMelter.addRecipe("slimeball",
        new ItemStack(Items.SLIME_BALL),
        new ItemStack(Items.SLIME_BALL),
        new FluidStack(FluidSlimeHolder.STILL.get(), 200));
    RecipeMelter.addRecipe("slimeblock",
        new ItemStack(Blocks.SLIME_BLOCK),
        new ItemStack(Blocks.SLIME_BLOCK),
        new FluidStack(FluidSlimeHolder.STILL.get(), 1800));
    //    
    RecipeMelter.addRecipe("doubleexperience_food",
        new ItemStack(ItemRegistry.experience_food),
        new ItemStack(ItemRegistry.experience_food),
        new FluidStack(FluidXpJuiceHolder.STILL.get(), 2 * ExpItemGain.FLUID_PER_EXP * ExpItemGain.EXP_PER_FOOD));
    RecipeMelter.addRecipe("fexperience_food",
        new ItemStack(ItemRegistry.experience_food),
        new ItemStack(Items.GHAST_TEAR),
        new FluidStack(FluidXpJuiceHolder.STILL.get(), 200 + ExpItemGain.FLUID_PER_EXP * ExpItemGain.EXP_PER_FOOD));
    RecipeMelter.addRecipe("blazeexperience_food",
        new ItemStack(ItemRegistry.experience_food),
        new ItemStack(Items.BLAZE_ROD),
        new FluidStack(FluidXpJuiceHolder.STILL.get(), 100 + ExpItemGain.FLUID_PER_EXP * ExpItemGain.EXP_PER_FOOD));
    RecipeMelter.addRecipe("witherroseexperience_food",
        new ItemStack(ItemRegistry.experience_food),
        new ItemStack(Items.WITHER_ROSE),
        new FluidStack(FluidXpJuiceHolder.STILL.get(), 150 + ExpItemGain.FLUID_PER_EXP * ExpItemGain.EXP_PER_FOOD));
    RecipeMelter.addRecipe("fleshexperience_food",
        new ItemStack(ItemRegistry.experience_food),
        new ItemStack(Items.ROTTEN_FLESH),
        new FluidStack(FluidXpJuiceHolder.STILL.get(), 50 + ExpItemGain.FLUID_PER_EXP * ExpItemGain.EXP_PER_FOOD));
    RecipeMelter.addRecipe("boneexperience_food",
        new ItemStack(ItemRegistry.experience_food),
        new ItemStack(Items.BONE),
        new FluidStack(FluidXpJuiceHolder.STILL.get(), 20 + ExpItemGain.FLUID_PER_EXP * ExpItemGain.EXP_PER_FOOD));
    RecipeMelter.addRecipe("honey",
        new ItemStack(Items.HONEY_BLOCK),
        new ItemStack(Items.HONEY_BLOCK),
        new FluidStack(FluidHoneyHolder.STILL.get(), 2000));
    RecipeMelter.addRecipe("honeyb",
        new ItemStack(Items.HONEY_BOTTLE),
        new ItemStack(Items.HONEY_BOTTLE),
        new FluidStack(FluidHoneyHolder.STILL.get(), 500));
    RecipeMelter.addRecipe("honeyc",
        new ItemStack(Items.HONEYCOMB_BLOCK),
        new ItemStack(Items.HONEY_BLOCK),
        new FluidStack(FluidHoneyHolder.STILL.get(), 5000));
    //
    RecipeMelter.addRecipe("magma",
        new ItemStack(Blocks.MAGMA_BLOCK),
        new ItemStack(Blocks.MAGMA_BLOCK),
        new FluidStack(FluidMagmaHolder.STILL.get(), FluidAttributes.BUCKET_VOLUME));
    RecipeMelter.addRecipe("magmar",
        new ItemStack(Blocks.MAGMA_BLOCK),
        new ItemStack(Blocks.NETHER_BRICKS),
        new FluidStack(FluidMagmaHolder.STILL.get(), FluidAttributes.BUCKET_VOLUME));
    RecipeMelter.addRecipe("magmamelt",
        new ItemStack(Blocks.MAGMA_BLOCK),
        new ItemStack(Blocks.NETHERRACK),
        new FluidStack(FluidMagmaHolder.STILL.get(), 600));
    ModCyclic.LOGGER.info("Melter Recipes added " + RECIPES.size());
  }

  private static Set<String> hashes = new HashSet<>();

  private static void addRecipe(String name, ItemStack itemStack, ItemStack secnd, FluidStack fluidStack) {
    ResourceLocation id = new ResourceLocation(ModCyclic.MODID, "melter_" + name);
    if (hashes.contains(id.toString())) {
      ModCyclic.LOGGER.error("Duplicate melter recipe id " + id.toString());
    }
    RECIPES.add(new RecipeMelter(
        new ResourceLocation(ModCyclic.MODID, name),
        itemStack,
        secnd,
        fluidStack));
    hashes.add(id.toString());
  }
}
