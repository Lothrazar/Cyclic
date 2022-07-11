package com.lothrazar.cyclic.block.generatoritem;

import com.google.gson.JsonObject;
import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.recipe.CyclicRecipe;
import com.lothrazar.cyclic.recipe.CyclicRecipeType;
import com.lothrazar.cyclic.recipe.ingredient.EnergyIngredient;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistryEntry;

@SuppressWarnings("rawtypes")
public class RecipeGeneratorItem<TileEntityBase> extends CyclicRecipe {

  private NonNullList<Ingredient> ingredients = NonNullList.create();
  private final EnergyIngredient energy;

  public RecipeGeneratorItem(ResourceLocation id, Ingredient in, EnergyIngredient energy) {
    super(id);
    ingredients.add(in);
    this.energy = energy;
  }

  @Override
  public boolean matches(com.lothrazar.cyclic.block.TileBlockEntityCyclic inv, Level worldIn) {
    try {
      TileGeneratorDrops tile = (TileGeneratorDrops) inv;
      return matches(tile.inputSlots.getStackInSlot(0), ingredients.get(0));
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
  public RecipeType<?> getType() {
    return CyclicRecipeType.GENERATOR_ITEM;
  }

  @Override
  public RecipeSerializer<?> getSerializer() {
    return SERIALGENERATOR;
  }

  public int getTicks() {
    return energy.getTicks();
  }

  public int getRfPertick() {
    return energy.getRfPertick();
  }

  public int getEnergyTotal() {
    return this.getRfPertick() * this.getTicks();
  }

  public static final SerializeGenerateItem SERIALGENERATOR = new SerializeGenerateItem();

  public static class SerializeGenerateItem extends ForgeRegistryEntry<RecipeSerializer<?>> implements RecipeSerializer<RecipeGeneratorItem<? extends com.lothrazar.cyclic.block.TileBlockEntityCyclic>> {

    SerializeGenerateItem() {
      // This registry name is what people will specify in their json files.
      this.setRegistryName(new ResourceLocation(ModCyclic.MODID, "generator_item"));
    }

    /**
     * The fluid stuff i was helped out a ton by looking at this https://github.com/mekanism/Mekanism/blob/921d10be54f97518c1f0cb5a6fc64bf47d5e6773/src/api/java/mekanism/api/SerializerHelper.java#L129
     */
    @SuppressWarnings("unchecked")
    @Override
    public RecipeGeneratorItem<? extends com.lothrazar.cyclic.block.TileBlockEntityCyclic> fromJson(ResourceLocation recipeId, JsonObject json) {
      RecipeGeneratorItem r = null;
      try {
        Ingredient inputFirst = Ingredient.fromJson(GsonHelper.getAsJsonObject(json, "fuel"));
        r = new RecipeGeneratorItem(recipeId, inputFirst, new EnergyIngredient(json));
      }
      catch (Exception e) {
        ModCyclic.LOGGER.error("Error loading recipe " + recipeId, e);
      }
      return r;
    }

    @Override
    public RecipeGeneratorItem fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
      RecipeGeneratorItem r = new RecipeGeneratorItem(recipeId, Ingredient.fromNetwork(buffer),
          new EnergyIngredient(buffer.readInt(), buffer.readInt()));
      //server reading recipe from client or vice/versa 
      return r;
    }

    @Override
    public void toNetwork(FriendlyByteBuf buffer, RecipeGeneratorItem recipe) {
      Ingredient zero = (Ingredient) recipe.ingredients.get(0);
      zero.toNetwork(buffer);
      buffer.writeInt(recipe.energy.getTicks());
      buffer.writeInt(recipe.energy.getRfPertick());
    }
  }
}
