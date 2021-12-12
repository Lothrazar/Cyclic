package com.lothrazar.cyclic.block.melter;

import com.google.gson.JsonObject;
import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.base.TileEntityBase;
import com.lothrazar.cyclic.recipe.CyclicRecipe;
import com.lothrazar.cyclic.recipe.CyclicRecipeType;
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
public class RecipeMelter<T> extends CyclicRecipe {

  private final NonNullList<Ingredient> ingredients = NonNullList.create();
  private final FluidStack outFluid;

  public RecipeMelter(ResourceLocation id, Ingredient in, Ingredient inSecond, FluidStack out) {
    super(id);
    ingredients.add(in);
    if (inSecond == null) {
      inSecond = Ingredient.EMPTY;
    }
    ingredients.add(inSecond);
    this.outFluid = out;
  }

  @Override
  public boolean matches(final TileEntityBase inv, final World worldIn) {
    try {
      TileMelter tile = (TileMelter) inv;
      //if first one matches check second
      //if first does not match, fail
      boolean matchLeft = matches(tile.getStackInputSlot(0), ingredients.get(0));
      boolean matchRight = matches(tile.getStackInputSlot(1), ingredients.get(1));
      return matchLeft && matchRight;
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
  public FluidStack getRecipeFluid() {
    return outFluid.copy();
  }

  @Override
  public IRecipeType<?> getType() {
    return CyclicRecipeType.MELTER;
  }

  @Override
  public IRecipeSerializer<?> getSerializer() {
    return SERIALMELTER;
  }

  public static final SerializeMelter SERIALMELTER = new SerializeMelter();

  public static class SerializeMelter extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<RecipeMelter<? extends com.lothrazar.cyclic.base.TileEntityBase>> {

    SerializeMelter() {
      // This registry name is what people will specify in their json files.
      this.setRegistryName(new ResourceLocation(ModCyclic.MODID, "melter"));
    }

    /**
     * The fluid stuff i was helped out a ton by looking at this https://github.com/mekanism/Mekanism/blob/921d10be54f97518c1f0cb5a6fc64bf47d5e6773/src/api/java/mekanism/api/SerializerHelper.java#L129
     */
    @SuppressWarnings("unchecked")
    @Override
    public RecipeMelter<? extends TileEntityBase> read(final ResourceLocation recipeId, final JsonObject json) {
      RecipeMelter r = null;
      try {
        Ingredient inputFirst = Ingredient.deserialize(JSONUtils.getJsonObject(json, "inputFirst"));
        Ingredient inputSecond = Ingredient.EMPTY;
        if (JSONUtils.hasField(json, "inputSecond")) {
          inputSecond = Ingredient.deserialize(JSONUtils.getJsonObject(json, "inputSecond"));
        }
        JsonObject result = json.get("result").getAsJsonObject();
        int count = result.get("count").getAsInt();
        //TODO: shared get fluid
        String fluidId = JSONUtils.getString(result, "fluid");
        ResourceLocation resourceLocation = new ResourceLocation(fluidId);
        Fluid fluid = ForgeRegistries.FLUIDS.getValue(resourceLocation);
        if (fluid != null) {
          r = new RecipeMelter(recipeId, inputFirst, inputSecond, new FluidStack(fluid, count));
        }
      }
      catch (Exception e) {
        ModCyclic.LOGGER.error("Error loading recipe" + recipeId, e);
      }
      ModCyclic.LOGGER.info("Recipe loaded " + recipeId);
      return r;
    }

    @Override
    public RecipeMelter<? extends TileEntityBase> read(final ResourceLocation recipeId, final PacketBuffer buffer) {
      //server reading recipe from client or vice/versa
      return new RecipeMelter<>(recipeId,
        Ingredient.read(buffer), Ingredient.read(buffer), FluidStack.readFromPacket(buffer));
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
}
