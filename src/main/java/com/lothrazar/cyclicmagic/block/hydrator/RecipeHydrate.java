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
package com.lothrazar.cyclicmagic.block.hydrator;

import java.util.List;
import java.util.UUID;
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.core.util.Const;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.oredict.OreDictionary;

public class RecipeHydrate extends net.minecraftforge.registries.IForgeRegistryEntry.Impl<IRecipe> implements IRecipe {

  private NonNullList<ItemStack> recipeInput = NonNullList.withSize(4, ItemStack.EMPTY);// new ItemStack[4];
  private ItemStack resultItem = ItemStack.EMPTY;
  private int fluidCost = 25;

  public RecipeHydrate(ItemStack in, ItemStack out) {
    this(new ItemStack[] { in }, out, 25);
  }

  public RecipeHydrate(ItemStack[] in, ItemStack out) {
    this(in, out, 25);
  }

  public RecipeHydrate(ItemStack[] in, ItemStack out, int w) {
    if (in.length > 4 || in.length == 0) {
      throw new IllegalArgumentException("Input array must be length 4 or less");
    }
    ModCyclic.logger.info("Hydrator recipe for " + out.getDisplayName() + " is size? " + in.length);
    for (int i = 0; i < in.length; i++) {
      if (in[i] != null && in[i].isEmpty() == false)
        recipeInput.set(i, in[i]);
    }
    this.fluidCost = w;
    this.resultItem = out;
    this.setRegistryName(new ResourceLocation(Const.MODID, "hydrator_" + UUID.randomUUID().toString() + out.getUnlocalizedName()));
  }

  @Override
  public boolean matches(InventoryCrafting inv, World worldIn) {
    return recipeSlotMatches(inv.getStackInSlot(0), recipeInput.get(0)) &&
        recipeSlotMatches(inv.getStackInSlot(1), recipeInput.get(1)) &&
        recipeSlotMatches(inv.getStackInSlot(2), recipeInput.get(2)) &&
        recipeSlotMatches(inv.getStackInSlot(3), recipeInput.get(3));
  }

  private boolean recipeSlotMatches(ItemStack sInvo, ItemStack sRecipe) {
    if (sInvo.isEmpty() != sRecipe.isEmpty()) {
      return false;//empty matching empty
    }
    return OreDictionary.itemMatches(sInvo, sRecipe, false)
        && sInvo.getCount() >= sRecipe.getCount();
  }

  public boolean tryPayCost(IInventory invoSource, FluidTank tank, boolean keepOneMinimum) {
    if (tank.getFluidAmount() < this.getFluidCost()) {
      return false;//not enough fluid, so stop now
    }
    //if minimum is 2, then the recipe slots always stay locked with at least 1 in each spot
    //otherwise its allowed to drain empty
    int minimum = (keepOneMinimum) ? 2 : 1;
    //TODO: merge/codeshare between shaped and shapeless!
    int inputStackToPay = -1, inputCountToPay = -1;
    //    if (this.isShapeless()) {
    //      //find the one input slot has the thing, and decrement THAT ONE only 
    //      final ItemStack theRecipe = recipeInput[0];
    //      for (int i = 0; i < recipeInput.length; i++) {
    //      }
    //      for (int i = 0; i < recipeInput.length; i++) {
    //        //its shapeless so only one thing will have the input
    //        if (invoSource.getStackInSlot(i).getCount() >= minimum
    //            && invoSource.getStackInSlot(i).getCount() >= theRecipe.getCount() + (minimum - 1)) {
    //          inputStackToPay = i;
    //          //VALIDATE
    //          inputCountToPay = theRecipe.getCount();
    //          break;
    //        }
    //      }
    //      if (inputStackToPay < 0 || inputCountToPay <= 0) {
    //        return false;
    //      }
    //      invoSource.decrStackSize(inputStackToPay, inputCountToPay);
    //    }
    //    else {
    //first test before we try to pay
    for (int i = 0; i < recipeInput.size(); i++) {
      if (recipeInput.get(i).isEmpty()) {
        continue; //pay zero
      }
      if (invoSource.getStackInSlot(i).getCount() < recipeInput.get(i).getCount() + (minimum - 1)) {
        return false;//at least one of the stacks cannot pay
      }
    }
    //now actually pay, since they all can
    for (int i = 0; i < recipeInput.size(); i++) {
      if (recipeInput.get(i).isEmpty()) {
        continue; //pay zero
      }
      invoSource.decrStackSize(i, recipeInput.get(i).getCount());
    }
    //    }
    //pay fluid last. same for shaped and shapeless
    tank.drain(this.getFluidCost(), true);
    return true;
  }

  @Override
  public ItemStack getCraftingResult(InventoryCrafting inv) {
    return resultItem.copy();
  }

  @Override
  public boolean canFit(int width, int height) {
    return (width <= 2 && height <= 2);
  }

  @Override
  public ItemStack getRecipeOutput() {
    return resultItem.copy();
  }

  public List<ItemStack> getRecipeInput() {
    return recipeInput;
  }

  public int getFluidCost() {
    return fluidCost;
  }

  public void setFluidCost(int fluidCost) {
    this.fluidCost = fluidCost;
  }
}
