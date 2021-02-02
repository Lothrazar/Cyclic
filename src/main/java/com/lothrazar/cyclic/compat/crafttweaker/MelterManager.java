package com.lothrazar.cyclic.compat.crafttweaker;

import com.blamejared.crafttweaker.api.annotations.ZenRegister;
import com.blamejared.crafttweaker.api.fluid.IFluidStack;
import com.blamejared.crafttweaker.api.item.IIngredient;
import com.blamejared.crafttweaker.api.managers.IRecipeManager;
import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.block.melter.RecipeMelter;
import com.lothrazar.cyclic.compat.CompatConstants;
import com.lothrazar.cyclic.recipe.CyclicRecipeType;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;
import org.openzen.zencode.java.ZenCodeType;

@SuppressWarnings("rawtypes")
@ZenRegister
@ZenCodeType.Name("mods.cyclic.melter")
public class MelterManager implements IRecipeManager {

  @Override
  public IRecipeType<?> getRecipeType() {
    return CyclicRecipeType.MELTER;
  }

  @ZenCodeType.Method
  public void addRecipe(String name, IIngredient inputFirst, IIngredient inputSecond, IFluidStack f) {
    if (f == null || name == null || f.getFluid() == null) {
      return;
    }
    ModCyclic.LOGGER.info("recipe   " + name + f.getFluid());
    RecipeMelter<?> m = new RecipeMelter(new ResourceLocation(CompatConstants.CT_ID, name),
        inputFirst.asVanillaIngredient(),
        inputSecond.asVanillaIngredient(),
        new FluidStack(f.getFluid(), f.getAmount()));
    if (RecipeMelter.addRecipe(m)) {
      ModCyclic.LOGGER.error(String.format("melter: addRecipe success %s | %d ", name, RecipeMelter.RECIPES.size()));
    }
    else {
      ModCyclic.LOGGER.error(String.format("melter: addRecipe error %s  " + m, name));
    }
  }

  @ZenCodeType.Method
  public void removeRecipe(String name) {
    if (name == null) {
      return;
    }
    ModCyclic.LOGGER.info("CT: removeRecipe attempt %s %d ", name,
        RecipeMelter.RECIPES.size());
    this.removeByName(name);
    // go
    RecipeMelter<?> found = null;
    for (RecipeMelter<?> m : RecipeMelter.RECIPES) {
      if (m != null && m.getId() != null &&
          name.equalsIgnoreCase(m.getId().toString())) {
        found = m;
        break;
      }
    }
    if (found != null) {
      int before = RecipeMelter.RECIPES.size();
      RecipeMelter.RECIPES.remove(found);
      ModCyclic.LOGGER.error(String.format("CT: removeRecipe found %s ||  %d -> %d "
          + found.getRecipeFluid().getFluid().getRegistryName(), name, before, RecipeMelter.RECIPES.size()));
    }
    else {
      ModCyclic.LOGGER.error(String.format("CT: removeRecipe NOT found %s  ", name));
    }
  }
}
