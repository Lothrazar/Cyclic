package com.lothrazar.cyclicmagic.block.packager;

import java.util.ArrayList;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;
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
    //  ModCyclic.logger.info("???? ! " + inputItem);
    return inv.getStackInSlot(0).isItemEqual(inputItem)
        || OreDictionary.itemMatches(inv.getStackInSlot(0), inputItem, false);
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
    addRecipe(new RecipePackage(new ItemStack(Items.IRON_NUGGET, 9), new ItemStack(Items.IRON_INGOT)));
    addRecipe(new RecipePackage(new ItemStack(Items.GOLD_INGOT, 9), new ItemStack(Blocks.GOLD_BLOCK)));
    addRecipe(new RecipePackage(new ItemStack(Items.GOLD_NUGGET, 9), new ItemStack(Items.GOLD_INGOT)));
    addRecipe(new RecipePackage(new ItemStack(Items.MELON, 9), new ItemStack(Blocks.MELON_BLOCK)));
    addRecipe(new RecipePackage(new ItemStack(Items.COAL, 9), new ItemStack(Blocks.COAL_BLOCK)));
    addRecipe(new RecipePackage(new ItemStack(Items.REDSTONE, 9), new ItemStack(Blocks.REDSTONE_BLOCK)));
    addRecipe(new RecipePackage(new ItemStack(Items.GLOWSTONE_DUST, 4), new ItemStack(Blocks.GLOWSTONE)));
    addRecipe(new RecipePackage(new ItemStack(Items.QUARTZ, 4), new ItemStack(Blocks.QUARTZ_BLOCK)));
    addRecipe(new RecipePackage(new ItemStack(Items.WHEAT, 9), new ItemStack(Blocks.HAY_BLOCK)));
    addRecipe(new RecipePackage(new ItemStack(Items.NETHER_WART, 9), new ItemStack(Blocks.NETHER_WART_BLOCK)));
    addRecipe(new RecipePackage(new ItemStack(Items.DYE, 9, EnumDyeColor.BLUE.getDyeDamage()), new ItemStack(Blocks.LAPIS_BLOCK)));
  }

  public static void addRecipe(RecipePackage rec) {
    recipes.add(rec);
  }
}
