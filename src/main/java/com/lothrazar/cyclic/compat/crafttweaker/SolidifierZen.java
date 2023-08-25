package com.lothrazar.cyclic.compat.crafttweaker;

import java.util.Arrays;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openzen.zencode.java.ZenCodeType;
import com.blamejared.crafttweaker.api.CraftTweakerAPI;
import com.blamejared.crafttweaker.api.action.recipe.ActionAddRecipe;
import com.blamejared.crafttweaker.api.annotation.ZenRegister;
import com.blamejared.crafttweaker.api.fluid.IFluidStack;
import com.blamejared.crafttweaker.api.ingredient.IIngredient;
import com.blamejared.crafttweaker.api.item.IItemStack;
import com.blamejared.crafttweaker.api.recipe.manager.base.IRecipeManager;
import com.lothrazar.cyclic.block.solidifier.RecipeSolidifier;
import com.lothrazar.cyclic.compat.CompatConstants;
import com.lothrazar.cyclic.registry.CyclicRecipeType;
import com.lothrazar.library.recipe.ingredient.EnergyIngredient;
import com.lothrazar.library.recipe.ingredient.FluidTagIngredient;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.fluids.FluidStack;

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
    name = fixRecipeName(name);
    if (output.isEmpty()) {
      throw new IllegalArgumentException("Output cannot be empty!");
    }
    NonNullList<Ingredient> list = NonNullList.withSize(input.length, Ingredient.EMPTY);
    for (int i = 0; i < input.length; i++) {
      list.set(i, input[i].asVanillaIngredient());
    }
    RecipeSolidifier recipe = new RecipeSolidifier(new ResourceLocation(CompatConstants.CRAFTTWEAKER, name),
        list,
        new FluidTagIngredient(new FluidStack(fluid.getFluid(), 1), "", (int) fluid.getAmount()),
        output.getInternal(),
        new EnergyIngredient(rfPertick, ticks));
    CraftTweakerAPI.apply(new ActionAddRecipe<RecipeSolidifier>(this, recipe, ""));
    logger.debug("Recipe loaded " + recipe.getId().toString());
  }

  @ZenCodeType.Method
  public void addRecipe(String name, IIngredient[] input, String fluidTag, int fluidQuantity, IItemStack output, int rfPertick, int ticks) {
    name = fixRecipeName(name);
    if (output.isEmpty()) {
      throw new IllegalArgumentException("Output cannot be empty!");
    }
    NonNullList<Ingredient> list = NonNullList.withSize(input.length, Ingredient.EMPTY);
    for (int i = 0; i < input.length; i++) {
      list.set(i, input[i].asVanillaIngredient());
    }
    //because CT doesnt have a fluid tag ingredient type really and it could come in foramt <fluid:minecraft:water>
    //but the FluidTagIngredient just uses teh raw string, parse it out
    fluidTag = fluidTag.replace("<", "").replace(">", "").replace("fluid:", "");
    RecipeSolidifier recipe = new RecipeSolidifier(new ResourceLocation(CompatConstants.CRAFTTWEAKER, name),
        list,
        new FluidTagIngredient(null, fluidTag, fluidQuantity), // TAG of fluid instead of actual fluid. 
        output.getInternal(),
        new EnergyIngredient(rfPertick, ticks));
    CraftTweakerAPI.apply(new ActionAddRecipe<RecipeSolidifier>(this, recipe, ""));
    logger.debug("Recipe (tag %s) loaded " + recipe.getId().toString(), fluidTag);
  }

  @ZenCodeType.Method
  public void removeRecipe(String... names) {
    removeByName(names);
    logger.debug("Recipe removed " + Arrays.toString(names));
  }
}