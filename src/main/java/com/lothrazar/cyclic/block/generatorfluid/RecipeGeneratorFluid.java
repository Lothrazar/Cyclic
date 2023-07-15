package com.lothrazar.cyclic.block.generatorfluid;

import com.google.gson.JsonObject;
import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.registry.CyclicRecipeType;
import com.lothrazar.library.recipe.ingredient.EnergyIngredient;
import com.lothrazar.library.recipe.ingredient.FluidTagIngredient;
import com.lothrazar.library.util.RecipeUtil;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraftforge.fluids.FluidStack;

public class RecipeGeneratorFluid implements Recipe<TileGeneratorFluid> {

  private final ResourceLocation id;
  private NonNullList<Ingredient> ingredients = NonNullList.create();
  public final FluidTagIngredient fluidIng;
  private final EnergyIngredient energy;

  public RecipeGeneratorFluid(ResourceLocation id, FluidTagIngredient in, EnergyIngredient energy) {
    this.id = id;
    this.fluidIng = in;
    this.energy = energy;
  }

  @Override
  public boolean isSpecial() {
    return true;
  }

  @Override
  public ItemStack assemble(TileGeneratorFluid inv, RegistryAccess ra) {
    return ItemStack.EMPTY;
  }

  @Override
  public boolean canCraftInDimensions(int width, int height) {
    return true;
  }

  @Override
  public ItemStack getResultItem(RegistryAccess ra) {
    return ItemStack.EMPTY;
  }

  @Override
  public ResourceLocation getId() {
    return id;
  }

  //  @Override
  public FluidStack getRecipeFluid() {
    return fluidIng.getFluidStack();
  }

  @Override
  public boolean matches(TileGeneratorFluid inv, Level worldIn) {
    try {
      TileGeneratorFluid tile = inv;
      return RecipeUtil.matchFluid(tile.getFluid(), this.fluidIng);
    }
    catch (ClassCastException e) {
      return false;
    }
  }

  public boolean matches(ItemStack current, Ingredient ing) {
    if (ing == Ingredient.EMPTY) {
      //it must be empty
      return current.isEmpty();
    }
    if (current.isEmpty()) {
      return ing == Ingredient.EMPTY;
    }
    return ing.test(current);
  }

  public ItemStack[] ingredientAt(int slot) {
    Ingredient ing = ingredients.get(slot);
    return ing.getItems();
  }

  @Override
  public NonNullList<Ingredient> getIngredients() {
    return ingredients;
  }

  @Override
  public RecipeType<?> getType() {
    return CyclicRecipeType.GENERATOR_FLUID.get();
  }

  @Override
  public RecipeSerializer<?> getSerializer() {
    return CyclicRecipeType.GENERATOR_FLUID_S.get();
  }

  public int getTicks() {
    return energy.getTicks();
  }

  public int getRfpertick() {
    return energy.getRfPertick();
  }

  public int getRfTotal() {
    return this.getRfpertick() * this.getTicks();
  }

  public static class SerializeGenerateFluid implements RecipeSerializer<RecipeGeneratorFluid> {

    public SerializeGenerateFluid() {}

    /**
     * The fluid stuff i was helped out a ton by looking at this https://github.com/mekanism/Mekanism/blob/921d10be54f97518c1f0cb5a6fc64bf47d5e6773/src/api/java/mekanism/api/SerializerHelper.java#L129
     */
    @Override
    public RecipeGeneratorFluid fromJson(ResourceLocation recipeId, JsonObject json) {
      RecipeGeneratorFluid r = null;
      try {
        //        Ingredient inputFirst = Ingredient.deserialize(JSONUtils.getJsonObject(json, "fuel"));
        FluidTagIngredient fs = RecipeUtil.parseFluid(json, "fuel");
        r = new RecipeGeneratorFluid(recipeId, fs, new EnergyIngredient(json));
      }
      catch (Exception e) {
        ModCyclic.LOGGER.error("Error loading recipe " + recipeId, e);
      }
      return r;
    }

    @Override
    public RecipeGeneratorFluid fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
      return new RecipeGeneratorFluid(recipeId,
          FluidTagIngredient.readFromPacket(buffer),
          new EnergyIngredient(buffer.readInt(), buffer.readInt()));
    }

    @Override
    public void toNetwork(FriendlyByteBuf buffer, RecipeGeneratorFluid recipe) {
      recipe.fluidIng.writeToPacket(buffer);
      buffer.writeInt(recipe.energy.getRfPertick());
      buffer.writeInt(recipe.energy.getTicks());
    }
  }
}
