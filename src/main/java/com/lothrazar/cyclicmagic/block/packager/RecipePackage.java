package com.lothrazar.cyclicmagic.block.packager;

import java.util.ArrayList;
import java.util.List;
import com.google.common.collect.Lists;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.registries.IForgeRegistryEntry;

public class RecipePackage extends IForgeRegistryEntry.Impl<IRecipe> implements IRecipe {

  public static ArrayList<RecipePackage> recipes = new ArrayList<>();
  private NonNullList<ItemStack> input;
  private ItemStack output = ItemStack.EMPTY;

  public RecipePackage(ItemStack out, ItemStack in) {
    this(out, new ItemStack[] { in });
  }

  public RecipePackage(ItemStack out, ItemStack[] in) {
    if (in.length > 6 || in.length == 0) {
      throw new IllegalArgumentException("Input array must be length 6 or less");
    }
    input = NonNullList.withSize(in.length, ItemStack.EMPTY);
    for (int i = 0; i < in.length; i++) {
      if (in[i] != null && in[i].isEmpty() == false)
        input.set(i, in[i]);
    }
    output = out;
  }

  public NonNullList<ItemStack> getInput() {
    return input;
  }

  @Override
  public boolean matches(InventoryCrafting inv, World worldIn) {
    //    ShapelessRecipes matches has been copied to here
    List<ItemStack> inventory = Lists.newArrayList();
    for (int i = 0; i < inv.getHeight(); ++i) {
      for (int j = 0; j < inv.getWidth(); ++j) {
        ItemStack itemstack = inv.getStackInRowAndColumn(j, i);
        if (!itemstack.isEmpty()) {
          inventory.add(itemstack);
        }
      }
    }
    if (inventory.size() != this.input.size()) {
      return false;
    }
    int matches = 0;
    for (ItemStack invo : inventory) {
      for (ItemStack recipe : this.input) {
        if (recipe.isItemEqual(invo)) {
          matches++;
          break;
        }
      }
    }
    return matches == input.size();
  }

  @Override
  public ItemStack getCraftingResult(InventoryCrafting inv) {
    return output.copy();
  }

  @Override
  public boolean canFit(int width, int height) {
    return width == 3 && height == 2;
  }

  @Override
  public ItemStack getRecipeOutput() {
    return output.copy();
  }

  public static void initAllRecipes() {
    addRecipe(new RecipePackage(new ItemStack(Blocks.MYCELIUM),
        new ItemStack[] { new ItemStack(Blocks.DIRT, 2, 1), new ItemStack(Blocks.BROWN_MUSHROOM), new ItemStack(Blocks.RED_MUSHROOM) }));
    addRecipe(new RecipePackage(new ItemStack(Blocks.IRON_BLOCK), new ItemStack(Items.IRON_INGOT, 9)));
    addRecipe(new RecipePackage(new ItemStack(Items.IRON_INGOT), new ItemStack(Items.IRON_NUGGET, 9)));
    addRecipe(new RecipePackage(new ItemStack(Blocks.GOLD_BLOCK), new ItemStack(Items.GOLD_INGOT, 9)));
    addRecipe(new RecipePackage(new ItemStack(Items.GOLD_INGOT), new ItemStack(Items.GOLD_NUGGET, 9)));
    addRecipe(new RecipePackage(new ItemStack(Blocks.MELON_BLOCK), new ItemStack(Items.MELON, 9)));
    addRecipe(new RecipePackage(new ItemStack(Blocks.COAL_BLOCK), new ItemStack(Items.COAL, 9)));
    addRecipe(new RecipePackage(new ItemStack(Blocks.REDSTONE_BLOCK), new ItemStack(Items.REDSTONE, 9)));
    addRecipe(new RecipePackage(new ItemStack(Blocks.GLOWSTONE), new ItemStack(Items.GLOWSTONE_DUST, 4)));
    addRecipe(new RecipePackage(new ItemStack(Blocks.QUARTZ_BLOCK), new ItemStack(Items.QUARTZ, 4)));
    addRecipe(new RecipePackage(new ItemStack(Blocks.HAY_BLOCK), new ItemStack(Items.WHEAT, 9)));
    addRecipe(new RecipePackage(new ItemStack(Blocks.NETHER_WART_BLOCK), new ItemStack(Items.NETHER_WART, 9)));
    addRecipe(new RecipePackage(new ItemStack(Blocks.LAPIS_BLOCK), new ItemStack(Items.DYE, 9, EnumDyeColor.BLUE.getDyeDamage())));
  }

  public static void addRecipe(RecipePackage rec) {
    recipes.add(rec);
  }
}
