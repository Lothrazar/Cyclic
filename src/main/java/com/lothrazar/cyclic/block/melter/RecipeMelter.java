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

  @Override
  public IRecipeSerializer<?> getSerializer() {
    return SERIALMELTER;
  }

  public static final SerializeMelter SERIALMELTER = new SerializeMelter();

  /**
   * SHOUTOUT https://github.com/Minecraft-Forge-Tutorials/Custom-Json-Recipes
   */
  public static class SerializeMelter extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<RecipeMelter> {

    SerializeMelter() {
      // This registry name is what people will specify in their json files.
      this.setRegistryName(new ResourceLocation(ModCyclic.MODID, "melter"));
    }

    /**
     * The fluid stuff i was helped out a ton by looking at this https://github.com/mekanism/Mekanism/blob/921d10be54f97518c1f0cb5a6fc64bf47d5e6773/src/api/java/mekanism/api/SerializerHelper.java#L129
     */
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
      }
      ModCyclic.LOGGER.info("Recipe loaded " + recipeId);
      return r;
    }

    @Override
    public RecipeMelter read(ResourceLocation recipeId, PacketBuffer buffer) {
      return new RecipeMelter(recipeId,
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

  private static Set<String> hashes = new HashSet<>();

  private static void addRecipe(RecipeMelter r) {
    ResourceLocation id = r.getId();
    if (hashes.contains(id.toString())) {
      ModCyclic.LOGGER.error("Error: Duplicate melter recipe id " + id.toString());
    }
    else {
      RECIPES.add(r);
      hashes.add(id.toString());
    }
  }
}
