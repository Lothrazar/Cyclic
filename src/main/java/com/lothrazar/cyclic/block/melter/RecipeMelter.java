package com.lothrazar.cyclic.block.melter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import com.google.gson.JsonObject;
import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.recipe.CyclicRecipe;
import com.lothrazar.cyclic.recipe.CyclicRecipeType;
import com.lothrazar.cyclic.util.UtilItemStack;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ForgeRegistryEntry;

@SuppressWarnings("rawtypes")
public class RecipeMelter<TileEntityBase> extends CyclicRecipe {

  public static List<RecipeMelter> RECIPES = new ArrayList<>();
  private NonNullList<Ingredient> ingredients = NonNullList.create();
  private FluidStack outFluid;

  @Deprecated
  protected RecipeMelter(ResourceLocation id, ItemStack in, ItemStack inSecond, FluidStack out) {
    this(id, Ingredient.fromStacks(in), Ingredient.fromStacks(inSecond), out);
  }

  protected RecipeMelter(ResourceLocation id, Ingredient in, Ingredient inSecond, FluidStack out) {
    super(id);
    ingredients.add(in);
    ingredients.add(inSecond);
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

  public static final Serializer SERIALIZER = new Serializer();

  @Override
  public IRecipeSerializer<?> getSerializer() {
    return SERIALIZER;
  }

  /**
   * SHOUTOUT https://github.com/Minecraft-Forge-Tutorials/Custom-Json-Recipes
   */
  public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<RecipeMelter> {

    Serializer() {
      // This registry name is what people will specify in their json files.
      this.setRegistryName(new ResourceLocation(ModCyclic.MODID, "melter"));
    }

    @Override
    public RecipeMelter read(ResourceLocation recipeId, JsonObject json) {
      RecipeMelter r = null;
      try {
        Ingredient inputFirst = Ingredient.deserialize(JSONUtils.getJsonObject(json, "inputFirst"));
        Ingredient inputSecond = Ingredient.deserialize(JSONUtils.getJsonObject(json, "inputSecond"));
        JsonObject result = json.get("result").getAsJsonObject();
        int count = result.get("count").getAsInt();
        String fluidId = JSONUtils.getString(result, "fluid");
        ResourceLocation resourceLocation = new ResourceLocation(fluidId);
        Fluid fluid = ForgeRegistries.FLUIDS.getValue(resourceLocation);
        r = new RecipeMelter(recipeId, inputFirst, inputSecond, new FluidStack(fluid, count));
        addRecipe(r);
      }
      catch (Exception e) {
        ModCyclic.LOGGER.error("Error loading recipe" + recipeId, e);
        return null;
      }
      ModCyclic.LOGGER.info("Recipe loaded " + recipeId);
      return r;
    }

    @Override
    public RecipeMelter read(ResourceLocation recipeId, PacketBuffer buffer) {
      return new RecipeMelter(recipeId, Ingredient.read(buffer), Ingredient.read(buffer), FluidStack.readFromPacket(buffer));
    }

    @Override
    public void write(PacketBuffer buffer, RecipeMelter recipe) {
      Ingredient zero = (Ingredient) recipe.ingredients.get(0);
      Ingredient one = (Ingredient) recipe.ingredients.get(1);
      zero.write(buffer);
      one.write(buffer);
      recipe.outFluid.writeToPacket(buffer);
    }
  }

  public static void initAllRecipes() {
    hashes = new HashSet<>();
    //    RecipeMelter.addRecipe("wheatkelp",
    //        new ItemStack(ItemRegistry.biomass),
    //            new ItemStack(Items.WHEAT_SEEDS),
    //        new FluidStack(FluidBiomassHolder.STILL.get(), FluidAttributes.BUCKET_VOLUME));
    //    RecipeMelter.addRecipe("beetrootkelp",
    //        new ItemStack(ItemRegistry.biomass),
    //        new ItemStack(Items.BEETROOT_SEEDS),
    //        new FluidStack(FluidBiomassHolder.STILL.get(), FluidAttributes.BUCKET_VOLUME));
    //    RecipeMelter.addRecipe("melonkelp",
    //        new ItemStack(ItemRegistry.biomass),
    //        new ItemStack(Items.MELON_SEEDS),
    //        new FluidStack(FluidBiomassHolder.STILL.get(), FluidAttributes.BUCKET_VOLUME));
    //    RecipeMelter.addRecipe("pumpskelp",
    //        new ItemStack(ItemRegistry.biomass),
    //        new ItemStack(Items.PUMPKIN_SEEDS),
    //        new FluidStack(FluidBiomassHolder.STILL.get(), FluidAttributes.BUCKET_VOLUME));
    //    RecipeMelter.addRecipe("pumpsasdfkelp",
    //        new ItemStack(ItemRegistry.biomass),
    //        new ItemStack(Items.KELP),
    //        new FluidStack(FluidBiomassHolder.STILL.get(), FluidAttributes.BUCKET_VOLUME));
    //    // 
    //    RecipeMelter.addRecipe("obsidiansmstonelava",
    //        new ItemStack(Blocks.OBSIDIAN),
    //        new ItemStack(Blocks.SMOOTH_STONE),
    //        new FluidStack(Fluids.LAVA, FluidAttributes.BUCKET_VOLUME));
    //    RecipeMelter.addRecipe("obsidiancobblelava",
    //        new ItemStack(Blocks.OBSIDIAN),
    //        new ItemStack(Blocks.COBBLESTONE),
    //        new FluidStack(Fluids.LAVA, FluidAttributes.BUCKET_VOLUME));
    //    RecipeMelter.addRecipe("obsidiangravellava",
    //        new ItemStack(Blocks.OBSIDIAN),
    //        new ItemStack(Blocks.GRAVEL),
    //        new FluidStack(Fluids.LAVA, FluidAttributes.BUCKET_VOLUME));
    //    RecipeMelter.addRecipe("slimemagmaball",
    //        new ItemStack(Items.MAGMA_CREAM),
    //        new ItemStack(Items.MAGMA_CREAM),
    //        new FluidStack(FluidSlimeHolder.STILL.get(), 200));
    //    RecipeMelter.addRecipe("slimeball",
    //        new ItemStack(Items.SLIME_BALL),
    //        new ItemStack(Items.SLIME_BALL),
    //        new FluidStack(FluidSlimeHolder.STILL.get(), 200));
    //    RecipeMelter.addRecipe("slimeblock",
    //        new ItemStack(Blocks.SLIME_BLOCK),
    //        new ItemStack(Blocks.SLIME_BLOCK),
    //        new FluidStack(FluidSlimeHolder.STILL.get(), 1800));
    //    //    
    //    RecipeMelter.addRecipe("doubleexperience_food",
    //        new ItemStack(ItemRegistry.experience_food),
    //        new ItemStack(ItemRegistry.experience_food),
    //        new FluidStack(FluidXpJuiceHolder.STILL.get(), 2 * ExpItemGain.FLUID_PER_EXP * ExpItemGain.EXP_PER_FOOD));
    //    RecipeMelter.addRecipe("fexperience_food",
    //        new ItemStack(ItemRegistry.experience_food),
    //        new ItemStack(Items.GHAST_TEAR),
    //        new FluidStack(FluidXpJuiceHolder.STILL.get(), 200 + ExpItemGain.FLUID_PER_EXP * ExpItemGain.EXP_PER_FOOD));
    //    RecipeMelter.addRecipe("blazeexperience_food",
    //        new ItemStack(ItemRegistry.experience_food),
    //        new ItemStack(Items.BLAZE_ROD),
    //        new FluidStack(FluidXpJuiceHolder.STILL.get(), 100 + ExpItemGain.FLUID_PER_EXP * ExpItemGain.EXP_PER_FOOD));
    //    RecipeMelter.addRecipe("witherroseexperience_food",
    //        new ItemStack(ItemRegistry.experience_food),
    //        new ItemStack(Items.WITHER_ROSE),
    //        new FluidStack(FluidXpJuiceHolder.STILL.get(), 150 + ExpItemGain.FLUID_PER_EXP * ExpItemGain.EXP_PER_FOOD));
    //    RecipeMelter.addRecipe("fleshexperience_food",
    //        new ItemStack(ItemRegistry.experience_food),
    //        new ItemStack(Items.ROTTEN_FLESH),
    //        new FluidStack(FluidXpJuiceHolder.STILL.get(), 50 + ExpItemGain.FLUID_PER_EXP * ExpItemGain.EXP_PER_FOOD));
    //    RecipeMelter.addRecipe("boneexperience_food",
    //        new ItemStack(ItemRegistry.experience_food),
    //        new ItemStack(Items.BONE),
    //        new FluidStack(FluidXpJuiceHolder.STILL.get(), 20 + ExpItemGain.FLUID_PER_EXP * ExpItemGain.EXP_PER_FOOD));
    //    RecipeMelter.addRecipe("honey",
    //        new ItemStack(Items.HONEY_BLOCK),
    //        new ItemStack(Items.HONEY_BLOCK),
    //        new FluidStack(FluidHoneyHolder.STILL.get(), 2000));
    //    RecipeMelter.addRecipe("honeyb",
    //        new ItemStack(Items.HONEY_BOTTLE),
    //        new ItemStack(Items.HONEY_BOTTLE),
    //        new FluidStack(FluidHoneyHolder.STILL.get(), 500));
    //    RecipeMelter.addRecipe("honeyc",
    //        new ItemStack(Items.HONEYCOMB_BLOCK),
    //        new ItemStack(Items.HONEY_BLOCK),
    //        new FluidStack(FluidHoneyHolder.STILL.get(), 5000));
    //    //
    //    RecipeMelter.addRecipe("magma",
    //        new ItemStack(Blocks.MAGMA_BLOCK),
    //        new ItemStack(Blocks.MAGMA_BLOCK),
    //        new FluidStack(FluidMagmaHolder.STILL.get(), FluidAttributes.BUCKET_VOLUME));
    //    RecipeMelter.addRecipe("magmar",
    //        new ItemStack(Blocks.MAGMA_BLOCK),
    //        new ItemStack(Blocks.NETHER_BRICKS),
    //        new FluidStack(FluidMagmaHolder.STILL.get(), FluidAttributes.BUCKET_VOLUME));
    //    RecipeMelter.addRecipe("magmamelt",
    //        new ItemStack(Blocks.MAGMA_BLOCK),
    //        new ItemStack(Blocks.NETHERRACK),
    //        new FluidStack(FluidMagmaHolder.STILL.get(), 600));
    ModCyclic.LOGGER.info("Melter Recipes added " + RECIPES.size());
  }

  private static Set<String> hashes = new HashSet<>();

  private static void addRecipe(RecipeMelter r) {
    ResourceLocation id = r.getId();
    if (hashes.contains(id.toString())) {
      ModCyclic.LOGGER.error("Duplicate melter recipe id " + id.toString());
    }
    RECIPES.add(r);
    hashes.add(id.toString());
  }
  //  private static void addRecipe(String name, ItemStack itemStack, ItemStack secnd, FluidStack fluidStack) {
  //    ResourceLocation id = new ResourceLocation(ModCyclic.MODID, "melter_" + name);
  //    if (hashes.contains(id.toString())) {
  //      ModCyclic.LOGGER.error("Duplicate melter recipe id " + id.toString());
  //    }
  //    RECIPES.add(new RecipeMelter(
  //        new ResourceLocation(ModCyclic.MODID, name),
  //        itemStack,
  //        secnd,
  //        fluidStack));
  //    hashes.add(id.toString());
  //  }
}
