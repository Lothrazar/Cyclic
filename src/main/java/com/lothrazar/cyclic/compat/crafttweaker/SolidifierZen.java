package com.lothrazar.cyclic.compat.crafttweaker;

import com.blamejared.crafttweaker.api.CraftTweakerAPI;
import com.blamejared.crafttweaker.api.action.recipe.ActionAddRecipe;
import com.blamejared.crafttweaker.api.annotation.ZenRegister;
import com.blamejared.crafttweaker.api.fluid.IFluidStack;
import com.blamejared.crafttweaker.api.ingredient.IIngredient;
import com.blamejared.crafttweaker.api.item.IItemStack;
import com.blamejared.crafttweaker.api.recipe.manager.base.IRecipeManager;
import com.lothrazar.cyclic.block.solidifier.RecipeSolidifier;
import com.lothrazar.cyclic.registry.CyclicRecipeType;
import com.lothrazar.library.recipe.ingredient.EnergyIngredient;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openzen.zencode.java.ZenCodeType;

import java.util.Arrays;

@ZenRegister
@ZenCodeType.Name("mods.cyclic.solidifier")
public class SolidifierZen implements IRecipeManager<RecipeSolidifier> {

  private final Logger logger = LogManager.getLogger();

  @Override
  public RecipeType<RecipeSolidifier> getRecipeType() {
    return CyclicRecipeType.SOLID.get();
  }

  @ZenCodeType.Method
  public void addRecipe(String name, IIngredient[] input, IFluidStack fluid, IItemStack output, int rfPertick, int ticks) {
    Identifier id = fixRecipeId(name);
    if (output.isEmpty()) {
      throw new IllegalArgumentException("Output cannot be empty!");
    }
    NonNullList<Ingredient> list = NonNullList.withSize(input.length, Ingredient.EMPTY);
    for (int i = 0; i < input.length; i++) {
      list.set(i, input[i].asVanillaIngredient());
    }
    RecipeSolidifier recipe = new RecipeSolidifier(
        list,
        SizedFluidIngredient.of(fluid.getFluid(), (int) fluid.getAmount()),
        output.getInternal(),
        new EnergyIngredient(rfPertick, ticks));
    RecipeHolder<RecipeSolidifier> holder = createHolder(id, recipe);
    CraftTweakerAPI.apply(new ActionAddRecipe<RecipeSolidifier>(this, holder, ""));
    logger.debug("Recipe loaded " + id.toString());
  }

  @ZenCodeType.Method
  public void addRecipe(String name, IIngredient[] input, String fluidTag, int fluidQuantity, IItemStack output, int rfPertick, int ticks) {
    Identifier id = fixRecipeId(name);
    if (output.isEmpty()) {
      throw new IllegalArgumentException("Output cannot be empty!");
    }
    NonNullList<Ingredient> list = NonNullList.withSize(input.length, Ingredient.EMPTY);
    for (int i = 0; i < input.length; i++) {
      list.set(i, input[i].asVanillaIngredient());
    }
    //because CT doesnt have a fluid tag ingredient type really and it could come in foramt <fluid:minecraft:water>
    //parse it out into a TagKey
    fluidTag = fluidTag.replace("<", "").replace(">", "").replace("fluid:", "");
    TagKey<Fluid> tag = TagKey.create(Registries.FLUID, Identifier.parse(fluidTag));
    RecipeSolidifier recipe = new RecipeSolidifier(
        list,
        SizedFluidIngredient.of(tag, fluidQuantity),
        output.getInternal(),
        new EnergyIngredient(rfPertick, ticks));
    RecipeHolder<RecipeSolidifier> holder = createHolder(id, recipe);
    CraftTweakerAPI.apply(new ActionAddRecipe<RecipeSolidifier>(this, holder, ""));
    logger.debug("Recipe (tag %s) loaded " + id.toString(), fluidTag);
  }

  @ZenCodeType.Method
  public void removeRecipe(String... names) {
    removeByName(names);
    logger.debug("Recipe removed " + Arrays.toString(names));
  }
}
