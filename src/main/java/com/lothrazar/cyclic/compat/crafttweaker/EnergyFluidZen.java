package com.lothrazar.cyclic.compat.crafttweaker;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openzen.zencode.java.ZenCodeType;
import com.blamejared.crafttweaker.api.CraftTweakerAPI;
import com.blamejared.crafttweaker.api.action.recipe.ActionAddRecipe;
import com.blamejared.crafttweaker.api.annotation.ZenRegister;
import com.blamejared.crafttweaker.api.fluid.IFluidStack;
import com.blamejared.crafttweaker.api.recipe.manager.base.IRecipeManager;
import com.lothrazar.cyclic.block.generatorfluid.RecipeGeneratorFluid;
import com.lothrazar.cyclic.compat.CompatConstants;
import com.lothrazar.cyclic.recipe.ingredient.EnergyIngredient;
import com.lothrazar.cyclic.recipe.ingredient.FluidTagIngredient;
import com.lothrazar.cyclic.registry.CyclicRecipeType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.fluids.FluidStack;

@ZenRegister
@ZenCodeType.Name("mods.cyclic.generator_fluid")
public class EnergyFluidZen implements IRecipeManager<RecipeGeneratorFluid> {

  private final Logger logger = LogManager.getLogger();

  @Override
  public RecipeType<RecipeGeneratorFluid> getRecipeType() {
    return CyclicRecipeType.GENERATOR_FLUID.get();
  }

  @ZenCodeType.Method
  public void addRecipe(String name, IFluidStack fluid, int rfPertick, int ticks) {
    name = fixRecipeName(name);
    RecipeGeneratorFluid m = new RecipeGeneratorFluid(new ResourceLocation(CompatConstants.CRAFTTWEAKER, name),
        new FluidTagIngredient(new FluidStack(fluid.getFluid(), 1), "", fluid.getAmount()),
        new EnergyIngredient(rfPertick, ticks));
    CraftTweakerAPI.apply(new ActionAddRecipe<RecipeGeneratorFluid>(this, m, ""));
    logger.debug("Recipe loaded " + m.getId().toString());
  }

  @ZenCodeType.Method
  public void addRecipe(String name, String fluidTag, int amount, int rfPertick, int ticks) {
    name = fixRecipeName(name);
    RecipeGeneratorFluid m = new RecipeGeneratorFluid(new ResourceLocation(CompatConstants.CRAFTTWEAKER, name),
        new FluidTagIngredient(null, fluidTag, amount),
        new EnergyIngredient(rfPertick, ticks));
    CraftTweakerAPI.apply(new ActionAddRecipe<RecipeGeneratorFluid>(this, m, ""));
    logger.debug("Recipe  (tag %s) loaded " + m.getId().toString(), fluidTag);
  }

  @ZenCodeType.Method
  public void removeRecipe(String... names) {
    removeByName(names);
    logger.debug("Recipe removed " + names);
  }
}
