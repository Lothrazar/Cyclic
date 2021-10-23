package com.lothrazar.cyclic.block.melter;

import com.google.gson.JsonObject;
import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.recipe.CyclicRecipe;
import com.lothrazar.cyclic.recipe.CyclicRecipeType;
import com.lothrazar.cyclic.util.UtilRecipe;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistryEntry;

@SuppressWarnings("rawtypes")
public class RecipeMelter<TileEntityBase> extends CyclicRecipe {

  private NonNullList<Ingredient> ingredients = NonNullList.create();
  private FluidStack outFluid;

  public RecipeMelter(ResourceLocation id, NonNullList<Ingredient> ingredientsIn, FluidStack out) {
    super(id);
    ingredients = ingredientsIn;
    if (ingredients.size() == 1) {
      ingredients.add(Ingredient.EMPTY);
    }
    if (ingredients.size() != 2) {
      throw new IllegalArgumentException("Melter recipe must have at most two ingredients");
    }
    this.outFluid = out;
  }

  @Override
  public boolean matches(com.lothrazar.cyclic.base.TileEntityBase inv, Level worldIn) {
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
    return ing.getItems();
  }

  @Override
  public NonNullList<Ingredient> getIngredients() {
    return ingredients;
  }

  @Override
  public ItemStack getResultItem() {
    return ItemStack.EMPTY;
  }

  @Override
  public FluidStack getRecipeFluid() {
    return outFluid.copy();
  }

  @Override
  public RecipeType<?> getType() {
    return CyclicRecipeType.MELTER;
  }

  @Override
  public RecipeSerializer<?> getSerializer() {
    return SERIALMELTER;
  }

  public static final SerializeMelter SERIALMELTER = new SerializeMelter();

  @SuppressWarnings("unchecked")
  public static class SerializeMelter extends ForgeRegistryEntry<RecipeSerializer<?>> implements RecipeSerializer<RecipeMelter<? extends com.lothrazar.cyclic.base.TileEntityBase>> {

    SerializeMelter() {
      // This registry name is what people will specify in their json files.
      this.setRegistryName(new ResourceLocation(ModCyclic.MODID, "melter"));
    }

    /**
     * The fluid stuff i was helped out a ton by looking at this https://github.com/mekanism/Mekanism/blob/921d10be54f97518c1f0cb5a6fc64bf47d5e6773/src/api/java/mekanism/api/SerializerHelper.java#L129
     */
    @Override
    public RecipeMelter<? extends com.lothrazar.cyclic.base.TileEntityBase> fromJson(ResourceLocation recipeId, JsonObject json) {
      RecipeMelter r = null;
      try {
        NonNullList<Ingredient> list = UtilRecipe.getIngredientsArray(json);
        JsonObject result = json.get("result").getAsJsonObject();
        FluidStack fluid = UtilRecipe.getFluid(result);
        if (json.has("energy")) {
          // TODO: rf per tick and burn time
          //see RecipeGeneratorFluid for energy settings
          //          JsonObject result = json.get("energy").getAsJsonObject();
          //          int ticks = result.get("ticks").getAsInt();
          //          int rfpertick = result.get("rfpertick").getAsInt();
        }
        r = new RecipeMelter(recipeId, list, fluid);
      }
      catch (Exception e) {
        ModCyclic.LOGGER.error("Error loading recipe" + recipeId, e);
      }
      ModCyclic.LOGGER.info("Recipe loaded " + recipeId);
      return r;
    }

    @Override
    public RecipeMelter fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
      NonNullList<Ingredient> ins = NonNullList.create();
      ins.add(Ingredient.fromNetwork(buffer));
      ins.add(Ingredient.fromNetwork(buffer));
      return new RecipeMelter(recipeId, ins, FluidStack.readFromPacket(buffer));
    }

    @Override
    public void toNetwork(FriendlyByteBuf buffer, RecipeMelter recipe) {
      Ingredient zero = (Ingredient) recipe.ingredients.get(0);
      Ingredient one = (Ingredient) recipe.ingredients.get(1);
      zero.toNetwork(buffer);
      one.toNetwork(buffer);
      recipe.outFluid.writeToPacket(buffer);
    }
  }
}
