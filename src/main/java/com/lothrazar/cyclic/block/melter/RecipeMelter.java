package com.lothrazar.cyclic.block.melter;

import com.google.gson.JsonObject;
import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.recipe.CyclicRecipe;
import com.lothrazar.cyclic.recipe.ingredient.EnergyIngredient;
import com.lothrazar.cyclic.registry.CyclicRecipeType;
import com.lothrazar.cyclic.util.RecipeUtil;
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
  private final EnergyIngredient energy;

  public RecipeMelter(ResourceLocation id, NonNullList<Ingredient> ingredientsIn, FluidStack out, EnergyIngredient energy) {
    super(id);
    this.energy = energy;
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
  public boolean matches(com.lothrazar.cyclic.block.TileBlockEntityCyclic inv, Level worldIn) {
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
    Ingredient ing = at(slot);
    return ing.getItems();
  }

  public Ingredient at(int slot) {
    return ingredients.get(slot);
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
    return CyclicRecipeType.MELTER.get();
  }

  @Override
  public RecipeSerializer<?> getSerializer() {
    return CyclicRecipeType.MELTER_S.get();
  }

  @SuppressWarnings("unchecked")
  public static class SerializeMelter extends ForgeRegistryEntry<RecipeSerializer<?>> implements RecipeSerializer<RecipeMelter<? extends com.lothrazar.cyclic.block.TileBlockEntityCyclic>> {

    public SerializeMelter() {}

    /**
     * The fluid stuff i was helped out a ton by looking at this https://github.com/mekanism/Mekanism/blob/921d10be54f97518c1f0cb5a6fc64bf47d5e6773/src/api/java/mekanism/api/SerializerHelper.java#L129
     */
    @Override
    public RecipeMelter<? extends com.lothrazar.cyclic.block.TileBlockEntityCyclic> fromJson(ResourceLocation recipeId, JsonObject json) {
      RecipeMelter r = null;
      try {
        NonNullList<Ingredient> list = RecipeUtil.getIngredientsArray(json);
        JsonObject result = json.get("result").getAsJsonObject();
        FluidStack fluid = RecipeUtil.getFluid(result);
        r = new RecipeMelter(recipeId, list, fluid, new EnergyIngredient(json));
      }
      catch (Exception e) {
        ModCyclic.LOGGER.error("Error loading recipe " + recipeId, e);
      }
      return r;
    }

    @Override
    public RecipeMelter fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buf) {
      NonNullList<Ingredient> ins = NonNullList.create();
      // ing, ing, fluid, (int,int)
      ins.add(Ingredient.fromNetwork(buf));
      ins.add(Ingredient.fromNetwork(buf));
      return new RecipeMelter(recipeId, ins, FluidStack.readFromPacket(buf),
          new EnergyIngredient(buf.readInt(), buf.readInt()));
    }

    @Override
    public void toNetwork(FriendlyByteBuf buf, RecipeMelter recipe) {
      //ing, ing, fluid, (int,int)
      Ingredient zero = (Ingredient) recipe.ingredients.get(0);
      Ingredient one = (Ingredient) recipe.ingredients.get(1);
      zero.toNetwork(buf);
      one.toNetwork(buf);
      recipe.outFluid.writeToPacket(buf);
      buf.writeInt(recipe.energy.getRfPertick());
      buf.writeInt(recipe.energy.getTicks());
    }
  }

  public int getEnergyCost() {
    return this.energy.getEnergyTotal();
  }

  public EnergyIngredient getEnergy() {
    return energy;
  }
}
