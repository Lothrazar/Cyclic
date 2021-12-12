package com.lothrazar.cyclic.block.solidifier;

import com.google.gson.JsonObject;
import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.base.TileEntityBase;
import com.lothrazar.cyclic.recipe.CyclicRecipe;
import com.lothrazar.cyclic.recipe.CyclicRecipeType;
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
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistryEntry;

public class RecipeSolidifier<T> extends CyclicRecipe {

  private final ItemStack result;
  public final FluidTagIngredient fluidIngredient;
  private final NonNullList<Ingredient> ingredients;

  public RecipeSolidifier(ResourceLocation id,
      Ingredient in, Ingredient inSecond, Ingredient inThird, FluidTagIngredient fluid,
      ItemStack result) {
    super(id);
    this.result = result;
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
  public boolean matches(TileEntityBase inv, World worldIn) {
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
    return ing.getMatchingStacks();
  }

  @Override
  public ItemStack getRecipeOutput() {
    return result.copy();
  }

  @Override
  public IRecipeType<?> getType() {
    return CyclicRecipeType.SOLID;
  }

  @Override
  public IRecipeSerializer<?> getSerializer() {
    return SERIALIZER;
  }

  public static final SerializeSolidifier SERIALIZER = new SerializeSolidifier();

  @SuppressWarnings("rawtypes")
  public static class SerializeSolidifier extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<RecipeSolidifier<? extends TileEntityBase>> {

    SerializeSolidifier() {
      // This registry name is what people will specify in their json files.
      this.setRegistryName(new ResourceLocation(ModCyclic.MODID, "solidifier"));
    }

    @SuppressWarnings("unchecked")
    @Override
    public RecipeSolidifier<? extends TileEntityBase> read(ResourceLocation recipeId, JsonObject json) {
      RecipeSolidifier r = null;
      try {
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
        //valid recipe created
        r = new RecipeSolidifier(recipeId, inputTop, inputMiddle, inputBottom, fs, resultStack);
      }
      catch (Exception e) {
        ModCyclic.LOGGER.error("Error loading recipe" + recipeId, e);
      }
      ModCyclic.LOGGER.info("Recipe loaded " + recipeId);
      return r;
    }

    @Override
    public RecipeSolidifier<? extends TileEntityBase> read(final ResourceLocation recipeId, final PacketBuffer buffer) {
      return new RecipeSolidifier<>(recipeId,
          Ingredient.read(buffer), Ingredient.read(buffer), Ingredient.read(buffer), FluidTagIngredient.readFromPacket(buffer),
          buffer.readItemStack());
    }

    @Override
    public void write(final PacketBuffer buffer, final RecipeSolidifier recipe) {
      Ingredient zero = (Ingredient) recipe.ingredients.get(0);
      Ingredient one = (Ingredient) recipe.ingredients.get(1);
      Ingredient two = (Ingredient) recipe.ingredients.get(2);
      zero.write(buffer);
      one.write(buffer);
      two.write(buffer);
      recipe.fluidIngredient.writeToPacket(buffer);
      buffer.writeItemStack(recipe.getRecipeOutput());
    }
  }
}
