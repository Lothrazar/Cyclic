package com.lothrazar.cyclic.block.solidifier;

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
import net.minecraft.item.crafting.ShapedRecipe;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ForgeRegistryEntry;

public class RecipeSolidifier<TileEntityBase> extends CyclicRecipe {

  public static List<RecipeSolidifier<?>> RECIPES = new ArrayList<>();
  private ItemStack result = ItemStack.EMPTY;
  private NonNullList<Ingredient> ingredients = NonNullList.create();
  private FluidStack fluidInput;

  private RecipeSolidifier(ResourceLocation id,
      Ingredient in, Ingredient inSecond, Ingredient inThird, FluidStack fluid,
      ItemStack result) {
    super(id);
    this.result = result;
    this.fluidInput = fluid;
    ingredients.add(in);
    ingredients.add(inSecond);
    ingredients.add(inThird);
  }

  @Override
  public boolean matches(com.lothrazar.cyclic.base.TileEntityBase inv, World worldIn) {
    try {
      TileSolidifier tile = (TileSolidifier) inv;
      if (tile.getFluid() != null && tile.getFluid().getFluid() == this.fluidInput.getFluid()
      //          && this.fluidInput.getAmount() >= tile.getFluid().getAmount()
      ) {
        return matches(tile, 0) && matches(tile, 1) && matches(tile, 2);
      }
      else
        return false;
    }
    catch (ClassCastException e) {
      return false;
    }
  }

  public boolean matches(TileSolidifier tile, int slot) {
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

  @Override
  public IRecipeSerializer<?> getSerializer() {
    return SERIALIZER;
  }

  public static final SerializeSolidifier SERIALIZER = new SerializeSolidifier();

  @SuppressWarnings("rawtypes")
  public static class SerializeSolidifier extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<RecipeSolidifier<? extends com.lothrazar.cyclic.base.TileEntityBase>> {

    SerializeSolidifier() {
      // This registry name is what people will specify in their json files.
      this.setRegistryName(new ResourceLocation(ModCyclic.MODID, "solidifier"));
    }

    @Override
    public RecipeSolidifier<? extends com.lothrazar.cyclic.base.TileEntityBase> read(ResourceLocation recipeId, JsonObject json) {
      RecipeSolidifier r = null;
      try {
        Ingredient inputFirst = Ingredient.deserialize(JSONUtils.getJsonObject(json, "inputTop"));
        Ingredient inputSecond = Ingredient.deserialize(JSONUtils.getJsonObject(json, "inputMiddle"));
        Ingredient inputThird = Ingredient.deserialize(JSONUtils.getJsonObject(json, "inputBottom"));
        ItemStack resultStack = ShapedRecipe.deserializeItem(JSONUtils.getJsonObject(json, "result"));
        JsonObject mix = json.get("mix").getAsJsonObject();
        int count = mix.get("count").getAsInt();
        String fluidId = JSONUtils.getString(mix, "fluid");
        ResourceLocation resourceLocation = new ResourceLocation(fluidId);
        Fluid fluid = ForgeRegistries.FLUIDS.getValue(resourceLocation);
        //is it air? which means not found
        if (fluid == FluidStack.EMPTY.getFluid()) {
          throw new IllegalArgumentException("Invalid fluid specified " + fluidId);
        }
        r = new RecipeSolidifier(recipeId, inputFirst, inputSecond,
            inputThird, new FluidStack(fluid, count), resultStack);
        addRecipe(r);
      }
      catch (Exception e) {
        ModCyclic.LOGGER.error("Error loading recipe" + recipeId, e);
      }
      return r;
    }

    @Override
    public RecipeSolidifier read(ResourceLocation recipeId, PacketBuffer buffer) {
      RecipeSolidifier r = new RecipeSolidifier(recipeId,
          Ingredient.read(buffer), Ingredient.read(buffer), Ingredient.read(buffer), FluidStack.readFromPacket(buffer),
          buffer.readItemStack());
      //server reading recipe from client or vice/versa 
      addRecipe(r);
      return r;
    }

    @Override
    public void write(PacketBuffer buffer, RecipeSolidifier recipe) {
      Ingredient zero = (Ingredient) recipe.ingredients.get(0);
      Ingredient one = (Ingredient) recipe.ingredients.get(1);
      Ingredient two = (Ingredient) recipe.ingredients.get(2);
      zero.write(buffer);
      one.write(buffer);
      two.write(buffer);
      recipe.fluidInput.writeToPacket(buffer);
      buffer.writeItemStack(recipe.getRecipeOutput());
    }
  }

  private static Set<String> hashes = new HashSet<>();

  private static void addRecipe(RecipeSolidifier r) {
    ResourceLocation id = r.getId();
    if (hashes.contains(id.toString())) {
      ModCyclic.LOGGER.info("Warn: Duplicate solidifier recipe id " + id.toString());
    }
    else {
      RECIPES.add(r);
      hashes.add(id.toString());
      ModCyclic.LOGGER.info("Recipe loaded " + id.toString());
    }
  }
}
