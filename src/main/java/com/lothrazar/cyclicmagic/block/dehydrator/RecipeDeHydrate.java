/*******************************************************************************
 * The MIT License (MIT)
 * 
 * Copyright (C) 2014-2018 Sam Bassett (aka Lothrazar)
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 ******************************************************************************/
package com.lothrazar.cyclicmagic.block.dehydrator;

import java.util.ArrayList;
import java.util.UUID;
import com.lothrazar.cyclicmagic.util.Const;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.registries.IForgeRegistryEntry;

public class RecipeDeHydrate extends IForgeRegistryEntry.Impl<IRecipe> implements IRecipe {

  public static ArrayList<RecipeDeHydrate> recipes = new ArrayList<RecipeDeHydrate>();
  private ItemStack recipeInput = ItemStack.EMPTY;
  private ItemStack resultItem = ItemStack.EMPTY;
  private int time;
  private int fluid;

  public RecipeDeHydrate(ItemStack in, ItemStack out, int time, int fluid) {
    recipeInput = in;
    resultItem = out;
    this.setFluid(fluid);
    this.time = time;
    this.setRegistryName(new ResourceLocation(Const.MODID, "dehydrate" + UUID.randomUUID().toString() + out.getTranslationKey()));
  }

  public FluidStack getOutputFluid() {
    return new FluidStack(FluidRegistry.WATER, this.fluid);
  }
  @Override
  public boolean matches(InventoryCrafting inv, World worldIn) {
    return recipeSlotMatches(inv.getStackInSlot(0), recipeInput);
  }

  public static boolean recipeSlotMatches(ItemStack sInvo, ItemStack sRecipe) {
    if (sInvo.isEmpty() != sRecipe.isEmpty()) {
      return false;//empty matching empty
    }
    //if item matches, then we are fine. check ore dict as well after that if item doesnt match 
    return sInvo.getCount() >= sRecipe.getCount() &&
        (sInvo.isItemEqual(sRecipe)
            || OreDictionary.itemMatches(sInvo, sRecipe, false));
  }

  public boolean tryPayCost(IInventory invoSource) {
    if (invoSource.getStackInSlot(0).getCount() < recipeInput.getCount()) {
      return false;//at least one of the stacks cannot pay
    }
    //now actually pay, since they all can
    invoSource.decrStackSize(0, recipeInput.getCount());
    return true;
  }

  @Override
  public ItemStack getCraftingResult(InventoryCrafting inv) {
    return resultItem.copy();
  }

  @Override
  public boolean canFit(int width, int height) {
    return (width <= 1 && height <= 1);
  }

  @Override
  public ItemStack getRecipeOutput() {
    return resultItem.copy();
  }

  public ItemStack getRecipeInput() {
    return recipeInput;
  }

  public int getTime() {
    return time;
  }

  public void setTime(int time) {
    this.time = time;
  }

  public static void initAllRecipes() {
    int defaultTime = 10;
    int defaultFluid = 100;
    RecipeDeHydrate.addRecipe(new RecipeDeHydrate(new ItemStack(Blocks.CLAY),
        new ItemStack(Blocks.HARDENED_CLAY), defaultTime, defaultFluid));
    RecipeDeHydrate.addRecipe(new RecipeDeHydrate(new ItemStack(Blocks.DIRT),
        new ItemStack(Blocks.GRAVEL), defaultTime, defaultFluid));
    RecipeDeHydrate.addRecipe(new RecipeDeHydrate(new ItemStack(Blocks.TALLGRASS, 1, 2),
        new ItemStack(Blocks.DEADBUSH), defaultTime, defaultFluid));
    RecipeDeHydrate.addRecipe(new RecipeDeHydrate(new ItemStack(Blocks.GRASS),
        new ItemStack(Blocks.GRASS_PATH), defaultTime, defaultFluid));
    RecipeDeHydrate.addRecipe(new RecipeDeHydrate(new ItemStack(Blocks.DIRT, 1, 1),
        new ItemStack(Blocks.DIRT, 1, 2), defaultTime, defaultFluid));
    RecipeDeHydrate.addRecipe(new RecipeDeHydrate(new ItemStack(Items.ROTTEN_FLESH, 12),
        new ItemStack(Items.LEATHER), defaultTime, defaultFluid));
    RecipeDeHydrate.addRecipe(new RecipeDeHydrate(new ItemStack(Blocks.SAPLING, 1, OreDictionary.WILDCARD_VALUE),
        new ItemStack(Items.STICK), defaultTime, defaultFluid));
    for (int i = 0; i < 6; i++) {
      RecipeDeHydrate.addRecipe(new RecipeDeHydrate(new ItemStack(Blocks.SAPLING, 1, i),
          new ItemStack(Items.STICK), defaultTime, defaultFluid));
    }
    //dry out concrete back to powder
    for (EnumDyeColor col : EnumDyeColor.values()) {
      addRecipe(new RecipeDeHydrate(new ItemStack(Blocks.CONCRETE, 1, col.getMetadata()),
          new ItemStack(Blocks.CONCRETE_POWDER, 1, col.getMetadata()), defaultTime, defaultFluid));
    }
  }

  public static void addRecipe(RecipeDeHydrate rec) {
    if (rec.recipeInput.getMetadata() == 32767) {
      ItemStack snew = new ItemStack(rec.recipeInput.getItem(), rec.recipeInput.getCount(), 0);
      snew.setTagCompound(rec.recipeInput.getTagCompound());
      rec.recipeInput = snew;
    }
    recipes.add(rec);
  }

  public int getFluid() {
    return fluid;
  }

  public void setFluid(int fluid) {
    this.fluid = fluid;
  }

}
