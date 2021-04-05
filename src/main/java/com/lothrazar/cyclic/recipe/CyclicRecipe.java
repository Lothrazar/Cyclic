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
package com.lothrazar.cyclic.recipe;

import com.lothrazar.cyclic.base.TileEntityBase;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;

public abstract class CyclicRecipe implements IRecipe<TileEntityBase> {

  private final ResourceLocation id;

  protected CyclicRecipe(ResourceLocation id) {
    this.id = id;
  }

  @Override
  public ItemStack getCraftingResult(TileEntityBase inv) {
    return ItemStack.EMPTY;
  }

  @Override
  public boolean canFit(int width, int height) {
    return true;
  }

  @Override
  public ItemStack getRecipeOutput() {
    return ItemStack.EMPTY;
  }

  public FluidStack getRecipeFluid() {
    return FluidStack.EMPTY;
  }

  @Override
  public ResourceLocation getId() {
    return id;
  }

  @Override
  public IRecipeSerializer<?> getSerializer() {
    return null;
  }
}
