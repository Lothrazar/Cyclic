package com.lothrazar.cyclic.block.solidifier;

import java.util.ArrayList;
import java.util.List;
import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.block.expcollect.ExpItemGain;
import com.lothrazar.cyclic.fluid.FluidBiomassHolder;
import com.lothrazar.cyclic.fluid.FluidHoneyHolder;
import com.lothrazar.cyclic.fluid.FluidMagmaHolder;
import com.lothrazar.cyclic.fluid.FluidSlimeHolder;
import com.lothrazar.cyclic.fluid.FluidXpJuiceHolder;
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
    //water
    RecipeSolidifier.addRecipe("btubecoral",
        new ItemStack(Blocks.DEAD_TUBE_CORAL_BLOCK),
        new ItemStack(Blocks.TUBE_CORAL_BLOCK),
        new ItemStack(Blocks.DEAD_TUBE_CORAL_BLOCK),
        new FluidStack(Fluids.WATER, 100),
        new ItemStack(Blocks.TUBE_CORAL_BLOCK, 3));
    RecipeSolidifier.addRecipe("bbraincoral",
        new ItemStack(Blocks.DEAD_BRAIN_CORAL_BLOCK),
        new ItemStack(Blocks.BRAIN_CORAL_BLOCK),
        new ItemStack(Blocks.DEAD_BRAIN_CORAL_BLOCK),
        new FluidStack(Fluids.WATER, 100),
        new ItemStack(Blocks.BRAIN_CORAL_BLOCK, 3));
    RecipeSolidifier.addRecipe("bbubblecoral",
        new ItemStack(Blocks.DEAD_BUBBLE_CORAL_BLOCK),
        new ItemStack(Blocks.BUBBLE_CORAL_BLOCK),
        new ItemStack(Blocks.DEAD_BUBBLE_CORAL_BLOCK),
        new FluidStack(Fluids.WATER, 100),
        new ItemStack(Blocks.BUBBLE_CORAL_BLOCK, 3));
    RecipeSolidifier.addRecipe("bfirecoral",
        new ItemStack(Blocks.DEAD_FIRE_CORAL_BLOCK),
        new ItemStack(Blocks.FIRE_CORAL_BLOCK),
        new ItemStack(Blocks.DEAD_FIRE_CORAL_BLOCK),
        new FluidStack(Fluids.WATER, 100),
        new ItemStack(Blocks.FIRE_CORAL_BLOCK, 3));
    RecipeSolidifier.addRecipe("bfirecoral",
        new ItemStack(Blocks.DEAD_HORN_CORAL_BLOCK),
        new ItemStack(Blocks.HORN_CORAL_BLOCK),
        new ItemStack(Blocks.DEAD_HORN_CORAL_BLOCK),
        new FluidStack(Fluids.WATER, 100),
        new ItemStack(Blocks.HORN_CORAL_BLOCK, 3));
    //
    //
    RecipeSolidifier.addRecipe("braincoraltube",
        new ItemStack(Blocks.DEAD_TUBE_CORAL),
        new ItemStack(Blocks.TUBE_CORAL),
        new ItemStack(Blocks.DEAD_TUBE_CORAL),
        new FluidStack(Fluids.WATER, 100),
        new ItemStack(Blocks.TUBE_CORAL_BLOCK, 3));
    RecipeSolidifier.addRecipe("braincoral",
        new ItemStack(Blocks.DEAD_BRAIN_CORAL),
        new ItemStack(Blocks.BRAIN_CORAL),
        new ItemStack(Blocks.DEAD_BRAIN_CORAL),
        new FluidStack(Fluids.WATER, 100),
        new ItemStack(Blocks.BRAIN_CORAL_BLOCK, 3));
    RecipeSolidifier.addRecipe("bubblecoral",
        new ItemStack(Blocks.DEAD_BUBBLE_CORAL),
        new ItemStack(Blocks.BUBBLE_CORAL),
        new ItemStack(Blocks.DEAD_BUBBLE_CORAL),
        new FluidStack(Fluids.WATER, 100),
        new ItemStack(Blocks.BUBBLE_CORAL, 3));
    RecipeSolidifier.addRecipe("firecoral",
        new ItemStack(Blocks.DEAD_FIRE_CORAL),
        new ItemStack(Blocks.FIRE_CORAL),
        new ItemStack(Blocks.DEAD_FIRE_CORAL),
        new FluidStack(Fluids.WATER, 100),
        new ItemStack(Blocks.FIRE_CORAL, 3));
    RecipeSolidifier.addRecipe("hornecoral",
        new ItemStack(Blocks.DEAD_HORN_CORAL),
        new ItemStack(Blocks.HORN_CORAL),
        new ItemStack(Blocks.DEAD_HORN_CORAL),
        new FluidStack(Fluids.WATER, 100),
        new ItemStack(Blocks.HORN_CORAL, 3));
    //fans
    RecipeSolidifier.addRecipe("brainffaltube",
        new ItemStack(Blocks.DEAD_TUBE_CORAL_FAN),
        new ItemStack(Blocks.TUBE_CORAL_FAN),
        new ItemStack(Blocks.DEAD_TUBE_CORAL_FAN),
        new FluidStack(Fluids.WATER, 100),
        new ItemStack(Blocks.TUBE_CORAL_FAN, 3));
    RecipeSolidifier.addRecipe("braincoralfa",
        new ItemStack(Blocks.DEAD_BRAIN_CORAL_FAN),
        new ItemStack(Blocks.BRAIN_CORAL_FAN),
        new ItemStack(Blocks.DEAD_BRAIN_CORAL_FAN),
        new FluidStack(Fluids.WATER, 100),
        new ItemStack(Blocks.BRAIN_CORAL_FAN, 3));
    RecipeSolidifier.addRecipe("bubblecoralfan",
        new ItemStack(Blocks.DEAD_BUBBLE_CORAL_FAN),
        new ItemStack(Blocks.BUBBLE_CORAL_FAN),
        new ItemStack(Blocks.DEAD_BUBBLE_CORAL_FAN),
        new FluidStack(Fluids.WATER, 100),
        new ItemStack(Blocks.BUBBLE_CORAL_FAN, 3));
    RecipeSolidifier.addRecipe("firecoralfan",
        new ItemStack(Blocks.DEAD_FIRE_CORAL_FAN),
        new ItemStack(Blocks.FIRE_CORAL_FAN),
        new ItemStack(Blocks.DEAD_FIRE_CORAL_FAN),
        new FluidStack(Fluids.WATER, 100),
        new ItemStack(Blocks.FIRE_CORAL_FAN, 3));
    RecipeSolidifier.addRecipe("firecoralfann",
        new ItemStack(Blocks.DEAD_HORN_CORAL_FAN),
        new ItemStack(Blocks.HORN_CORAL_FAN),
        new ItemStack(Blocks.DEAD_HORN_CORAL_FAN),
        new FluidStack(Fluids.WATER, 100),
        new ItemStack(Blocks.HORN_CORAL_FAN, 3));
    //
    //
    RecipeSolidifier.addRecipe("bbbconcrete",
        new ItemStack(Blocks.BLACK_CONCRETE_POWDER),
        new ItemStack(Blocks.BLACK_CONCRETE_POWDER),
        new ItemStack(Blocks.BLACK_CONCRETE_POWDER),
        new FluidStack(Fluids.WATER, 10),
        new ItemStack(Blocks.BLACK_CONCRETE, 3));
    RecipeSolidifier.addRecipe("wconcrete",
        new ItemStack(Blocks.WHITE_CONCRETE_POWDER),
        new ItemStack(Blocks.WHITE_CONCRETE_POWDER),
        new ItemStack(Blocks.WHITE_CONCRETE_POWDER),
        new FluidStack(Fluids.WATER, 10),
        new ItemStack(Blocks.WHITE_CONCRETE, 3));
    RecipeSolidifier.addRecipe("oconcrete",
        new ItemStack(Blocks.ORANGE_CONCRETE_POWDER),
        new ItemStack(Blocks.ORANGE_CONCRETE_POWDER),
        new ItemStack(Blocks.ORANGE_CONCRETE_POWDER),
        new FluidStack(Fluids.WATER, 10),
        new ItemStack(Blocks.ORANGE_CONCRETE, 3));
    RecipeSolidifier.addRecipe("mconcrete",
        new ItemStack(Blocks.MAGENTA_CONCRETE_POWDER),
        new ItemStack(Blocks.MAGENTA_CONCRETE_POWDER),
        new ItemStack(Blocks.MAGENTA_CONCRETE_POWDER),
        new FluidStack(Fluids.WATER, 10),
        new ItemStack(Blocks.MAGENTA_CONCRETE, 3));
    RecipeSolidifier.addRecipe("lconcrete",
        new ItemStack(Blocks.LIGHT_BLUE_CONCRETE_POWDER),
        new ItemStack(Blocks.LIGHT_BLUE_CONCRETE_POWDER),
        new ItemStack(Blocks.LIGHT_BLUE_CONCRETE_POWDER),
        new FluidStack(Fluids.WATER, 10),
        new ItemStack(Blocks.LIGHT_BLUE_CONCRETE, 3));
    RecipeSolidifier.addRecipe("yconcrete",
        new ItemStack(Blocks.YELLOW_CONCRETE_POWDER),
        new ItemStack(Blocks.YELLOW_CONCRETE_POWDER),
        new ItemStack(Blocks.YELLOW_CONCRETE_POWDER),
        new FluidStack(Fluids.WATER, 10),
        new ItemStack(Blocks.YELLOW_CONCRETE, 3));
    RecipeSolidifier.addRecipe("concrete",
        new ItemStack(Blocks.LIME_CONCRETE_POWDER),
        new ItemStack(Blocks.LIME_CONCRETE_POWDER),
        new ItemStack(Blocks.LIME_CONCRETE_POWDER),
        new FluidStack(Fluids.WATER, 10),
        new ItemStack(Blocks.LIME_CONCRETE, 3));
    RecipeSolidifier.addRecipe("concrepinkte",
        new ItemStack(Blocks.PINK_CONCRETE_POWDER),
        new ItemStack(Blocks.PINK_CONCRETE_POWDER),
        new ItemStack(Blocks.PINK_CONCRETE_POWDER),
        new FluidStack(Fluids.WATER, 10),
        new ItemStack(Blocks.PINK_CONCRETE, 3));
    RecipeSolidifier.addRecipe("gggrayconcrete",
        new ItemStack(Blocks.GRAY_CONCRETE_POWDER),
        new ItemStack(Blocks.GRAY_CONCRETE_POWDER),
        new ItemStack(Blocks.GRAY_CONCRETE_POWDER),
        new FluidStack(Fluids.WATER, 10),
        new ItemStack(Blocks.GRAY_CONCRETE, 3));
    RecipeSolidifier.addRecipe("ggrconcrete",
        new ItemStack(Blocks.LIGHT_GRAY_CONCRETE_POWDER),
        new ItemStack(Blocks.LIGHT_GRAY_CONCRETE_POWDER),
        new ItemStack(Blocks.LIGHT_GRAY_CONCRETE_POWDER),
        new FluidStack(Fluids.WATER, 10),
        new ItemStack(Blocks.LIGHT_GRAY_CONCRETE, 3));
    RecipeSolidifier.addRecipe("lgconcrete",
        new ItemStack(Blocks.CYAN_CONCRETE_POWDER),
        new ItemStack(Blocks.CYAN_CONCRETE_POWDER),
        new ItemStack(Blocks.CYAN_CONCRETE_POWDER),
        new FluidStack(Fluids.WATER, 10),
        new ItemStack(Blocks.CYAN_CONCRETE, 3));
    RecipeSolidifier.addRecipe("purplecyconcrete",
        new ItemStack(Blocks.PURPLE_CONCRETE_POWDER),
        new ItemStack(Blocks.PURPLE_CONCRETE_POWDER),
        new ItemStack(Blocks.PURPLE_CONCRETE_POWDER),
        new FluidStack(Fluids.WATER, 10),
        new ItemStack(Blocks.PURPLE_CONCRETE, 3));
    RecipeSolidifier.addRecipe("bluconcrete",
        new ItemStack(Blocks.BLUE_CONCRETE_POWDER),
        new ItemStack(Blocks.BLUE_CONCRETE_POWDER),
        new ItemStack(Blocks.BLUE_CONCRETE_POWDER),
        new FluidStack(Fluids.WATER, 10),
        new ItemStack(Blocks.BLUE_CONCRETE, 3));
    RecipeSolidifier.addRecipe("brownconcrete",
        new ItemStack(Blocks.BROWN_CONCRETE_POWDER),
        new ItemStack(Blocks.BROWN_CONCRETE_POWDER),
        new ItemStack(Blocks.BROWN_CONCRETE_POWDER),
        new FluidStack(Fluids.WATER, 10),
        new ItemStack(Blocks.BROWN_CONCRETE, 3));
    RecipeSolidifier.addRecipe("greeeeconcrete",
        new ItemStack(Blocks.GREEN_CONCRETE_POWDER),
        new ItemStack(Blocks.GREEN_CONCRETE_POWDER),
        new ItemStack(Blocks.GREEN_CONCRETE_POWDER),
        new FluidStack(Fluids.WATER, 10),
        new ItemStack(Blocks.GREEN_CONCRETE, 3));
    RecipeSolidifier.addRecipe("redconcrete",
        new ItemStack(Blocks.RED_CONCRETE_POWDER),
        new ItemStack(Blocks.RED_CONCRETE_POWDER),
        new ItemStack(Blocks.RED_CONCRETE_POWDER),
        new FluidStack(Fluids.WATER, 10),
        new ItemStack(Blocks.RED_CONCRETE, 3));
    //
    //
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
    RecipeSolidifier.addRecipe("obsidian",
        new ItemStack(Blocks.COBBLESTONE),
        new ItemStack(Items.BLAZE_POWDER),
        new ItemStack(Blocks.COBBLESTONE),
        new FluidStack(FluidMagmaHolder.STILL.get(), FluidAttributes.BUCKET_VOLUME),
        new ItemStack(Blocks.OBSIDIAN));
    //
    RecipeSolidifier.addRecipe("biograss",
        new ItemStack(Blocks.GRASS),
        new ItemStack(Blocks.COARSE_DIRT),
        new ItemStack(Blocks.DIRT),
        new FluidStack(FluidBiomassHolder.STILL.get(), FluidAttributes.BUCKET_VOLUME / 2),
        new ItemStack(Blocks.GRASS_BLOCK, 2));
    RecipeSolidifier.addRecipe("biofarm",
        new ItemStack(Blocks.DIRT),
        new ItemStack(Blocks.COARSE_DIRT),
        new ItemStack(Blocks.DIRT),
        new FluidStack(FluidBiomassHolder.STILL.get(), FluidAttributes.BUCKET_VOLUME / 2),
        new ItemStack(Blocks.FARMLAND, 2));
    RecipeSolidifier.addRecipe("biofarmpodzol",
        new ItemStack(Blocks.GRASS_BLOCK),
        new ItemStack(Blocks.COARSE_DIRT),
        new ItemStack(Blocks.GRASS_BLOCK),
        new FluidStack(FluidBiomassHolder.STILL.get(), FluidAttributes.BUCKET_VOLUME),
        new ItemStack(Blocks.PODZOL, 2));
    RecipeSolidifier.addRecipe("biochar",
        new ItemStack(ItemRegistry.biomass),
        new ItemStack(Blocks.DIRT),
        new ItemStack(Items.CHARCOAL),
        new FluidStack(FluidBiomassHolder.STILL.get(), FluidAttributes.BUCKET_VOLUME / 2),
        new ItemStack(BlockRegistry.peat_unbaked));
    RecipeSolidifier.addRecipe("biocharrrr",
        new ItemStack(Items.STICK),
        new ItemStack(ItemRegistry.peat_fuel),
        new ItemStack(Items.STICK),
        new FluidStack(FluidBiomassHolder.STILL.get(), 100),
        new ItemStack(Items.CHARCOAL, 4));
    //
    //
    RecipeSolidifier.addRecipe("honeymelon",
        new ItemStack(Items.MELON_SLICE),
        new ItemStack(Items.GOLD_NUGGET),
        new ItemStack(Items.MELON_SLICE),
        new FluidStack(FluidHoneyHolder.STILL.get(), 50),
        new ItemStack(Items.GLISTERING_MELON_SLICE, 2));
    RecipeSolidifier.addRecipe("honeymelon",
        new ItemStack(Items.CARROT),
        new ItemStack(Items.GOLD_NUGGET),
        new ItemStack(Items.CARROT),
        new FluidStack(FluidHoneyHolder.STILL.get(), 50),
        new ItemStack(Items.GOLDEN_CARROT, 2));
    RecipeSolidifier.addRecipe("honeycake",
        new ItemStack(Items.EGG),
        new ItemStack(Items.WHEAT),
        new ItemStack(Items.WHEAT),
        new FluidStack(FluidHoneyHolder.STILL.get(), FluidAttributes.BUCKET_VOLUME / 2),
        new ItemStack(Items.CAKE));
    RecipeSolidifier.addRecipe("honeycookie",
        new ItemStack(Items.WHEAT),
        new ItemStack(Items.COCOA_BEANS),
        new ItemStack(Items.WHEAT),
        new FluidStack(FluidHoneyHolder.STILL.get(), 50),
        new ItemStack(Items.COOKIE, 16));
    RecipeSolidifier.addRecipe("honeyspidereye",
        new ItemStack(Items.SPIDER_EYE),
        new ItemStack(Items.BROWN_MUSHROOM),
        new ItemStack(Items.RED_MUSHROOM),
        new FluidStack(FluidHoneyHolder.STILL.get(), 10),
        new ItemStack(Items.FERMENTED_SPIDER_EYE, 2));
    RecipeSolidifier.addRecipe("honeypie",
        new ItemStack(Items.PUMPKIN),
        new ItemStack(Items.EGG),
        new ItemStack(Items.PUMPKIN),
        new FluidStack(FluidHoneyHolder.STILL.get(), 50),
        new ItemStack(Items.PUMPKIN_PIE, 3));
    RecipeSolidifier.addRecipe("honeyhive",
        new ItemStack(Items.CHEST),
        new ItemStack(Items.HONEYCOMB),
        new ItemStack(Items.STICK),
        new FluidStack(FluidHoneyHolder.STILL.get(), 100),
        new ItemStack(Blocks.BEEHIVE));
    RecipeSolidifier.addRecipe("honeynest",
        new ItemStack(Blocks.HONEYCOMB_BLOCK),
        new ItemStack(Blocks.HONEYCOMB_BLOCK),
        new ItemStack(Blocks.HONEYCOMB_BLOCK),
        new FluidStack(FluidHoneyHolder.STILL.get(), FluidAttributes.BUCKET_VOLUME * 8),
        new ItemStack(Blocks.BEE_NEST));
    //
    RecipeSolidifier.addRecipe("slimehoney",
        new ItemStack(Items.SLIME_BALL),
        new ItemStack(Items.SLIME_BALL),
        new ItemStack(Items.SLIME_BALL),
        new FluidStack(FluidHoneyHolder.STILL.get(), 600),
        new ItemStack(Blocks.SLIME_BLOCK));
    RecipeSolidifier.addRecipe("slimefill",
        new ItemStack(Items.SLIME_BALL),
        new ItemStack(Items.SLIME_BALL),
        new ItemStack(Items.SLIME_BALL),
        new FluidStack(FluidSlimeHolder.STILL.get(), 600),
        new ItemStack(Items.SLIME_BALL, 9));
    //
    //
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
        new ItemStack(Items.FIRE_CHARGE),
        new ItemStack(Items.REDSTONE),
        new ItemStack(Blocks.MAGMA_BLOCK),
        new FluidStack(FluidHoneyHolder.STILL.get(), FluidAttributes.BUCKET_VOLUME / 2),
        new ItemStack(ItemRegistry.gem_amber));
    RecipeSolidifier.addRecipe("honeyfireamber",
        new ItemStack(Items.FIRE_CHARGE),
        new ItemStack(Blocks.REDSTONE_BLOCK),
        new ItemStack(Items.GOLD_INGOT),
        new FluidStack(FluidMagmaHolder.STILL.get(), FluidAttributes.BUCKET_VOLUME),
        new ItemStack(ItemRegistry.gem_amber));
    RecipeSolidifier.addRecipe("purp",
        new ItemStack(Blocks.OBSIDIAN),
        new ItemStack(ItemRegistry.gem_amber),
        new ItemStack(Items.CHORUS_FRUIT),
        new FluidStack(FluidSlimeHolder.STILL.get(), FluidAttributes.BUCKET_VOLUME),
        new ItemStack(ItemRegistry.gem_obsidian));
    //xp
    // 
    RecipeSolidifier.addRecipe("expglass",
        new ItemStack(Items.GLASS_BOTTLE),
        new ItemStack(Items.GLASS_BOTTLE),
        new ItemStack(Items.GLASS_BOTTLE),
        new FluidStack(FluidXpJuiceHolder.STILL.get(), ExpItemGain.FLUID_PER_EXP * 30),
        new ItemStack(Items.EXPERIENCE_BOTTLE, 3));
    RecipeSolidifier.addRecipe("expglassfood",
        new ItemStack(Items.SUGAR),
        new ItemStack(Items.SUGAR),
        new ItemStack(Items.SUGAR),
        new FluidStack(FluidXpJuiceHolder.STILL.get(), ExpItemGain.FLUID_PER_EXP * ExpItemGain.EXP_PER_FOOD),
        new ItemStack(ItemRegistry.experience_food));
  }

  private static void addRecipe(String name, ItemStack a, ItemStack b, ItemStack c, FluidStack fluidStack,
      ItemStack res) {
    RECIPES.add(new RecipeSolidifier(
        new ResourceLocation(ModCyclic.MODID, "solid_" + name),
        a, b, c,
        fluidStack, res));
  }
}
