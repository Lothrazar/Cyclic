package com.lothrazar.cyclic.block.solidifier;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.JsonObject;
import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.recipe.CyclicRecipe;
import com.lothrazar.cyclic.recipe.CyclicRecipeType;
<<<<<<< HEAD
import com.lothrazar.cyclic.util.UtilRecipe;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.level.Level;
=======
import com.lothrazar.cyclic.recipe.FluidTagIngredient;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipe;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
>>>>>>> 9f4791a4f5c1dbc36e417a790d13312fb60c6528
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistryEntry;

@SuppressWarnings("rawtypes")
public class RecipeSolidifier<TileEntityBase> extends CyclicRecipe {

<<<<<<< HEAD
  private ItemStack result = ItemStack.EMPTY;
  private NonNullList<Ingredient> ingredients = NonNullList.create();
  private FluidStack fluidInput;
  private final int energy;

  public RecipeSolidifier(ResourceLocation id,
      NonNullList<Ingredient> inList, FluidStack fluid,
      ItemStack result, int energyIn) {
=======
  private final ItemStack result;
  public final FluidTagIngredient fluidIngredient;
  private final NonNullList<Ingredient> ingredients;

  public RecipeSolidifier(ResourceLocation id,
      Ingredient in, Ingredient inSecond, Ingredient inThird, FluidTagIngredient fluid,
      ItemStack result) {
>>>>>>> 9f4791a4f5c1dbc36e417a790d13312fb60c6528
    super(id);
    ingredients = inList;
    if (ingredients.size() == 2) {
      ingredients.add(Ingredient.EMPTY);
    }
    else if (ingredients.size() == 1) {
      ingredients.add(Ingredient.EMPTY);
      ingredients.add(Ingredient.EMPTY);
    }
    if (ingredients.size() != 3) {
      throw new IllegalArgumentException("Solidifier recipe must have at most three ingredients");
    }
    this.result = result;
<<<<<<< HEAD
    this.fluidInput = fluid;
    if (energyIn < 0) {
      energyIn = 0;
    }
    this.energy = energyIn;
  }

  @Override
  public boolean matches(com.lothrazar.cyclic.base.TileEntityBase inv, Level worldIn) {
=======
    fluidIngredient = fluid;
    ingredients = NonNullList.create();
    ingredients.add(in);
    ingredients.add(inSecond);
    ingredients.add(inThird);
  }

  @Override
  public FluidStack getRecipeFluid() {
    return this.fluidIngredient.getFluidStack();
  }

  public List<FluidStack> getRecipeFluids() {
    List<Fluid> fluids = fluidIngredient.list();
    if (fluids == null) {
      return null;
    }
    List<FluidStack> me = new ArrayList<>();
    for (Fluid f : fluids) {
      me.add(new FluidStack(f, fluidIngredient.getFluidStack().getAmount()));
    }
    return me;
  }

  @Override
  public boolean matches(com.lothrazar.cyclic.base.TileEntityBase inv, World worldIn) {
>>>>>>> 9f4791a4f5c1dbc36e417a790d13312fb60c6528
    try {
      TileSolidifier tile = (TileSolidifier) inv;
      return matchItems(tile) && CyclicRecipe.matchFluid(tile.getFluid(), this.fluidIngredient);
    }
    catch (ClassCastException e) {
      return false; // i think we fixed this
    }
  }

  private int findMatchingSlot(TileSolidifier tile, Ingredient shapeless, final List<Integer> skip) {
    for (int i = 0; i < 3; i++) {
      if (skip.contains(i)) {
        continue; // we already matched this one
      }
      if (shapeless.test(tile.getStackInputSlot(i))) {
        return i;
      }
    }
    //
    return -1;
  }

  private boolean matchItems(TileSolidifier tile) {
    Ingredient top = ingredients.get(0);
    Ingredient middle = ingredients.get(1);
    Ingredient bottom = ingredients.get(2);
    //
    List<Integer> matchingSlots = new ArrayList<>();
    matchingSlots.add(findMatchingSlot(tile, top, matchingSlots));
    matchingSlots.add(findMatchingSlot(tile, middle, matchingSlots));
    matchingSlots.add(findMatchingSlot(tile, bottom, matchingSlots));
    if (matchingSlots.contains(-1)) {
      return false;
    }
    return matchingSlots.contains(0) && matchingSlots.contains(1) && matchingSlots.contains(2);
  }

  @Override
  public NonNullList<Ingredient> getIngredients() {
    return ingredients;
  }

  public ItemStack[] ingredientAt(int slot) {
    Ingredient ing = ingredients.get(slot);
    return ing.getItems();
  }

  @Override
  public ItemStack getResultItem() {
    return result.copy();
  }

  @Override
<<<<<<< HEAD
  public FluidStack getRecipeFluid() {
    return fluidInput.copy();
  }

  @Override
  public RecipeType<?> getType() {
=======
  public IRecipeType<?> getType() {
>>>>>>> 9f4791a4f5c1dbc36e417a790d13312fb60c6528
    return CyclicRecipeType.SOLID;
  }

  public int getEnergyCost() {
    return this.energy;
  }

  @Override
  public RecipeSerializer<?> getSerializer() {
    return SERIALIZER;
  }

  public static final SerializeSolidifier SERIALIZER = new SerializeSolidifier();

  @SuppressWarnings("unchecked")
  public static class SerializeSolidifier extends ForgeRegistryEntry<RecipeSerializer<?>> implements RecipeSerializer<RecipeSolidifier<? extends com.lothrazar.cyclic.base.TileEntityBase>> {

    SerializeSolidifier() {
      // This registry name is what people will specify in their json files.
      this.setRegistryName(new ResourceLocation(ModCyclic.MODID, "solidifier"));
    }

    @Override
    public RecipeSolidifier<? extends com.lothrazar.cyclic.base.TileEntityBase> fromJson(ResourceLocation recipeId, JsonObject json) {
      RecipeSolidifier r = null;
      try {
<<<<<<< HEAD
        NonNullList<Ingredient> list = UtilRecipe.getIngredientsArray(json);
        ItemStack resultStack = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "result"));
        FluidStack fs = UtilRecipe.getFluid(json.get("mix").getAsJsonObject());
=======
        //TODO: in 1.17 make array
        Ingredient inputTop = Ingredient.deserialize(JSONUtils.getJsonObject(json, "inputTop"));
        Ingredient inputMiddle = Ingredient.EMPTY;
        if (JSONUtils.hasField(json, "inputMiddle")) {
          inputMiddle = Ingredient.deserialize(JSONUtils.getJsonObject(json, "inputMiddle"));
        }
        Ingredient inputBottom = Ingredient.EMPTY;
        if (JSONUtils.hasField(json, "inputBottom")) {
          inputBottom = Ingredient.deserialize(JSONUtils.getJsonObject(json, "inputBottom"));
        }
        ItemStack resultStack = ShapedRecipe.deserializeItem(JSONUtils.getJsonObject(json, "result"));
        if (inputTop == Ingredient.EMPTY) {
          throw new IllegalArgumentException("Invalid items: inputTop required to be non-empty: " + json);
        }
        FluidTagIngredient fs = parseFluid(json, "mix");
>>>>>>> 9f4791a4f5c1dbc36e417a790d13312fb60c6528
        //valid recipe created
        int energy = 5000;
        if (json.has("energy")) {
          energy = json.get("energy").getAsInt();
        }
        r = new RecipeSolidifier(recipeId, list, fs, resultStack, energy);
      }
      catch (Exception e) {
        ModCyclic.LOGGER.error("Error loading recipe" + recipeId, e);
      }
      ModCyclic.LOGGER.info("Recipe loaded " + recipeId);
      return r;
    }

    @Override
    public RecipeSolidifier fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
      NonNullList<Ingredient> ins = NonNullList.create();
      ins.add(Ingredient.fromNetwork(buffer));
      ins.add(Ingredient.fromNetwork(buffer));
      ins.add(Ingredient.fromNetwork(buffer));
      RecipeSolidifier r = new RecipeSolidifier(recipeId,
<<<<<<< HEAD
          ins, FluidStack.readFromPacket(buffer),
          buffer.readItem(), buffer.readInt());
=======
          Ingredient.read(buffer), Ingredient.read(buffer), Ingredient.read(buffer), FluidTagIngredient.readFromPacket(buffer),
          buffer.readItemStack());
>>>>>>> 9f4791a4f5c1dbc36e417a790d13312fb60c6528
      return r;
    }

    @Override
    public void toNetwork(FriendlyByteBuf buffer, RecipeSolidifier recipe) {
      Ingredient zero = (Ingredient) recipe.ingredients.get(0);
      Ingredient one = (Ingredient) recipe.ingredients.get(1);
      Ingredient two = (Ingredient) recipe.ingredients.get(2);
<<<<<<< HEAD
      zero.toNetwork(buffer);
      one.toNetwork(buffer);
      two.toNetwork(buffer);
      recipe.fluidInput.writeToPacket(buffer);
      buffer.writeItem(recipe.getResultItem());
      buffer.writeInt(recipe.energy);
=======
      zero.write(buffer);
      one.write(buffer);
      two.write(buffer);
      recipe.fluidIngredient.writeToPacket(buffer);
      buffer.writeItemStack(recipe.getRecipeOutput());
>>>>>>> 9f4791a4f5c1dbc36e417a790d13312fb60c6528
    }
  }
}
