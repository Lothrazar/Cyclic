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
import com.lothrazar.cyclic.block.TileBlockEntityCyclic;
import com.lothrazar.cyclic.recipe.ingredient.FluidTagIngredient;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;

public abstract class CyclicRecipe implements Recipe<TileBlockEntityCyclic> {

  private final ResourceLocation id;

  protected CyclicRecipe(ResourceLocation id) {
    this.id = id;
  }

  @Override
  public boolean isSpecial() {
    return true;
  }

  @Override
  public ItemStack assemble(TileBlockEntityCyclic inv) {
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

  public static boolean matchFluid(FluidStack tileFluid, FluidTagIngredient ing) {
    if (tileFluid == null || tileFluid.isEmpty()) {
      return false;
    }
    if (ing.hasFluid() && tileFluid.getFluid() == ing.getFluidStack().getFluid()) {
      return true;
    }
    //either recipe has no fluid or didnt match, try for tag
    if (ing.hasTag()) {
      //see /data/<id>/tags/fluids/
      TagKey<Fluid> ft = FluidTags.create(new ResourceLocation(ing.getTag()));
      if (ft != null && tileFluid.getFluid().is(ft)) {
        return true; // yes is matching the tag
      }
    }
    return false;
  }

  public static FluidTagIngredient parseFluid(JsonObject json, String key) {
    JsonObject mix = json.get(key).getAsJsonObject();
    int count = mix.get("count").getAsInt();
    if (count < 1) {
      count = 1;
    }
    FluidStack fluidstack = FluidStack.EMPTY;
    if (mix.has("fluid")) {
      String fluidId = mix.get("fluid").getAsString(); // JSONUtils.getString(mix, "fluid");
      ResourceLocation resourceLocation = new ResourceLocation(fluidId);
      Fluid fluid = ForgeRegistries.FLUIDS.getValue(resourceLocation);
      fluidstack = (fluid == null) ? FluidStack.EMPTY : new FluidStack(fluid, count);
    }
    String ftag = mix.has("tag") ? mix.get("tag").getAsString() : "";
    return new FluidTagIngredient(fluidstack, ftag, count);
  }
}
