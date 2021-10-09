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

import com.google.gson.JsonObject;
import com.lothrazar.cyclic.base.TileEntityBase;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.Tag;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;

public abstract class CyclicRecipe implements Recipe<TileEntityBase> {

  private final ResourceLocation id;

  protected CyclicRecipe(ResourceLocation id) {
    this.id = id;
  }

  @Override
  public ItemStack assemble(TileEntityBase inv) {
    return ItemStack.EMPTY;
  }

  @Override
  public boolean canCraftInDimensions(int width, int height) {
    return true;
  }

  @Override
  public ItemStack getResultItem() {
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
  public RecipeSerializer<?> getSerializer() {
    return null;
  }

  public boolean matchFluid(FluidStack tileFluid) {
    if (tileFluid == null || tileFluid.isEmpty()) {
      return false;
    }
    if (tileFluid.getFluid() == this.getRecipeFluid().getFluid()) {
      return true;
    }
    //if the fluids are not identical, they might have a matching tag
    //see /data/forge/tags/fluids/
    for (Tag<Fluid> fluidTag : FluidTags.getAllTags().getAllTags().values()) { // FluidTags.getStaticTags()) {
      if (getRecipeFluid().getFluid().is(fluidTag) && tileFluid.getFluid().is(fluidTag)) {
        return true;
      }
    }
    return false;
  }

  public static FluidStack getFluid(JsonObject json, String key) {
    JsonObject mix = json.get(key).getAsJsonObject();
    int count = mix.get("count").getAsInt();
    if (count < 1) {
      count = 1;
    }
    FluidStack fs = null;
    String fluidId = GsonHelper.getAsString(mix, "fluid");
    ResourceLocation resourceLocation = new ResourceLocation(fluidId);
    Fluid fluid = ForgeRegistries.FLUIDS.getValue(resourceLocation);
    if (fluid == FluidStack.EMPTY.getFluid()) {
      throw new IllegalArgumentException("Invalid fluid specified " + fluidId);
    }
    fs = new FluidStack(fluid, count);
    return fs;
  }
}
