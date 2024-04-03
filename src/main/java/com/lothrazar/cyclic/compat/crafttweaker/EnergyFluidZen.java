package com.lothrazar.cyclic.compat.crafttweaker;

import com.blamejared.crafttweaker.api.CraftTweakerAPI;
import com.blamejared.crafttweaker.api.annotations.ZenRegister;
import com.blamejared.crafttweaker.api.fluid.IFluidStack;
import com.blamejared.crafttweaker.api.managers.IRecipeManager;
import com.blamejared.crafttweaker.impl.actions.recipes.ActionAddRecipe;
import com.lothrazar.cyclic.block.generatorfluid.RecipeGeneratorFluid;
import com.lothrazar.cyclic.compat.CompatConstants;
import com.lothrazar.cyclic.recipe.CyclicRecipeType;
import com.lothrazar.cyclic.recipe.FluidTagIngredient;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openzen.zencode.java.ZenCodeType;

@ZenRegister
@ZenCodeType.Name("mods.cyclic.generator_fluid")
public class EnergyFluidZen implements IRecipeManager {

  private final Logger logger = LogManager.getLogger();

  @Override
  public IRecipeType<?> getRecipeType() {
    return CyclicRecipeType.GENERATOR_FLUID;
  }

  @SuppressWarnings("rawtypes")
  @ZenCodeType.Method
  public void addRecipe(String name, IFluidStack fluid, int rfPertick, int ticks) {
    name = fixRecipeName(name);
    RecipeGeneratorFluid m = new RecipeGeneratorFluid(new ResourceLocation(CompatConstants.CRAFTTWEAKER, name),
        new FluidTagIngredient(new FluidStack(fluid.getFluid(), 1), "", fluid.getAmount()),
        ticks, rfPertick);
    CraftTweakerAPI.apply(new ActionAddRecipe(this, m, ""));
    logger.debug("Recipe loaded " + m.getId().toString());
  }

  @SuppressWarnings("rawtypes")
  @ZenCodeType.Method
  public void addRecipe(String name, String fluidTag, int amount, int rfPertick, int ticks) {
    name = fixRecipeName(name);
    RecipeGeneratorFluid m = new RecipeGeneratorFluid(new ResourceLocation(CompatConstants.CRAFTTWEAKER, name),
        new FluidTagIngredient(null, fluidTag, amount),
        ticks, rfPertick);
    CraftTweakerAPI.apply(new ActionAddRecipe(this, m, ""));
    logger.debug("Recipe  (tag %s) loaded " + m.getId().toString(), fluidTag);
  }

  @ZenCodeType.Method
  public void removeRecipe(String names) {
    removeByName(names);
    logger.debug("Recipe removed " + names);
  }
}
