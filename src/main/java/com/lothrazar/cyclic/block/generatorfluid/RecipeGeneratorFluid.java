package com.lothrazar.cyclic.block.generatorfluid;

import com.google.gson.JsonObject;
import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.base.TileEntityBase;
import com.lothrazar.cyclic.recipe.CyclicRecipe;
import com.lothrazar.cyclic.recipe.CyclicRecipeType;
import com.lothrazar.cyclic.recipe.FluidTagIngredient;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistryEntry;

@SuppressWarnings("rawtypes")
public class RecipeGeneratorFluid<T extends TileEntityBase> extends CyclicRecipe {

  private NonNullList<Ingredient> ingredients = NonNullList.create();
  private final int ticks;
  private final int rfpertick;
  public final FluidTagIngredient fluidIng;

  public RecipeGeneratorFluid(ResourceLocation id, FluidTagIngredient in, int ticks, int rfpertick) {
    super(id);
    this.fluidIng = in;
    this.ticks = (Math.max(1, ticks));
    this.rfpertick = Math.max(1, rfpertick);
  }

  @Override
  public FluidStack getRecipeFluid() {
    return fluidIng.getFluidStack();
  }

  @Override
  public boolean matches(com.lothrazar.cyclic.base.TileEntityBase inv, World worldIn) {
    try {
      TileGeneratorFluid tile = (TileGeneratorFluid) inv;
      return CyclicRecipe.matchFluid(tile.getFluid(), this.fluidIng);
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
  public IRecipeType<?> getType() {
    return CyclicRecipeType.GENERATOR_FLUID;
  }

  @Override
  public IRecipeSerializer<?> getSerializer() {
    return SERIALGENERATORF;
  }

  public int getTicks() {
    return ticks;
  }

  public int getRfpertick() {
    return rfpertick;
  }

  public static final SerializeGenerateFluid SERIALGENERATORF = new SerializeGenerateFluid();

  public static class SerializeGenerateFluid extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<RecipeGeneratorFluid<? extends com.lothrazar.cyclic.base.TileEntityBase>> {

    SerializeGenerateFluid() {
      // This registry name is what people will specify in their json files.
      this.setRegistryName(new ResourceLocation(ModCyclic.MODID, "generator_fluid"));
    }

    /**
     * The fluid stuff i was helped out a ton by looking at this https://github.com/mekanism/Mekanism/blob/921d10be54f97518c1f0cb5a6fc64bf47d5e6773/src/api/java/mekanism/api/SerializerHelper.java#L129
     */
    @SuppressWarnings("unchecked")
    @Override
    public RecipeGeneratorFluid<? extends TileEntityBase> read(ResourceLocation recipeId, JsonObject json) {
      RecipeGeneratorFluid r = null;
      try {
        //        Ingredient inputFirst = Ingredient.deserialize(JSONUtils.getJsonObject(json, "fuel"));
        FluidTagIngredient fs = parseFluid(json, "fuel");
        JsonObject result = json.get("energy").getAsJsonObject();
        int ticks = result.get("ticks").getAsInt();
        int rfpertick = result.get("rfpertick").getAsInt();
        r = new RecipeGeneratorFluid(recipeId, fs, ticks, rfpertick);
      }
      catch (Exception e) {
        ModCyclic.LOGGER.error("Error loading recipe" + recipeId, e);
      }
      ModCyclic.LOGGER.info("Recipe loaded " + r);
      return r;
    }

    @Override
    public RecipeGeneratorFluid<TileEntityBase> read(ResourceLocation recipeId, PacketBuffer buffer) {
      //server reading recipe from client or vice/versa
      return new RecipeGeneratorFluid<TileEntityBase>(recipeId,
          FluidTagIngredient.readFromPacket(buffer),
          buffer.readInt(), buffer.readInt());
    }

    @Override
    public void write(PacketBuffer buffer, RecipeGeneratorFluid recipe) {
      recipe.fluidIng.writeToPacket(buffer);
      buffer.writeInt(recipe.getTicks());
      buffer.writeInt(recipe.rfpertick);
    }
  }

  public int getRfTotal() {
    return this.getRfpertick() * this.getTicks();
  }
}
