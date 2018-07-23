package com.lothrazar.cyclicmagic.block.packager;

import java.util.ArrayList;
import com.lothrazar.cyclicmagic.block.hydrator.RecipeHydrate;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;
import net.minecraftforge.registries.IForgeRegistryEntry;

public class RecipePackage extends IForgeRegistryEntry.Impl<IRecipe> implements IRecipe {

  public static ArrayList<RecipePackage> recipes = new ArrayList<>();
  private ItemStack inputItem = ItemStack.EMPTY;
  private ItemStack resultItem = ItemStack.EMPTY;

  public RecipePackage(ItemStack in, ItemStack out) {
    inputItem = in;
    resultItem = out;
  }
  @Override
  public boolean matches(InventoryCrafting inv, World worldIn) {
    //item matches AND size is enough
    return RecipeHydrate.recipeSlotMatches(inv.getStackInSlot(0), inputItem)
        && inputItem.getCount() <= inv.getStackInSlot(0).getCount();
  }

  public int getIngredientCount() {
    return inputItem.getCount();
  }

  @Override
  public ItemStack getCraftingResult(InventoryCrafting inv) {

    return resultItem.copy();
  }

  @Override
  public boolean canFit(int width, int height) {
    return width == 1 && height == 1;
  }

  @Override
  public ItemStack getRecipeOutput() {
    return resultItem.copy();
  }

  public static void initAllRecipes() {
    addRecipe(new RecipePackage(new ItemStack(Items.IRON_INGOT, 9), new ItemStack(Blocks.IRON_BLOCK)));
  }

  public static void addRecipe(RecipePackage rec) {
    recipes.add(rec);
  }
}
