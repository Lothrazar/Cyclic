package com.lothrazar.cyclic.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.lothrazar.cyclic.recipe.ingredient.FluidTagIngredient;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;

public class RecipeUtil {

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
      if (FluidHelpers.matches(tileFluid.getFluid(), ft)) {
        return true;
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

  public static NonNullList<Ingredient> getIngredientsArray(JsonObject obj) {
    JsonArray array = GsonHelper.getAsJsonArray(obj, "ingredients");
    NonNullList<Ingredient> nonnulllist = NonNullList.create();
    for (int i = 0; i < array.size(); ++i) {
      Ingredient ingredient = Ingredient.fromJson(array.get(i));
      if (!ingredient.isEmpty()) {
        nonnulllist.add(ingredient);
      }
    }
    return nonnulllist;
  }

  public static FluidStack getFluid(JsonObject fluidJson) {
    if (fluidJson.has("fluidTag")) {
      //      String fluidTag = fluidJson.get("fluidTag").getAsString();
    }
    String fluidId = GsonHelper.getAsString(fluidJson, "fluid");
    ResourceLocation resourceLocation = new ResourceLocation(fluidId);
    Fluid fluid = ForgeRegistries.FLUIDS.getValue(resourceLocation);
    int count = fluidJson.get("count").getAsInt();
    if (count < 1) {
      count = 1;
    }
    return new FluidStack(fluid, count);
  }
}
