package com.lothrazar.cyclic.compat.crafttweaker;

import com.blamejared.crafttweaker.api.annotations.ZenRegister;
import com.blamejared.crafttweaker.api.fluid.IFluidStack;
import com.blamejared.crafttweaker.api.item.IIngredient;
import com.blamejared.crafttweaker.api.item.IItemStack;
import com.blamejared.crafttweaker.api.managers.IRecipeManager;
import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.block.melter.RecipeMelter;
import com.lothrazar.cyclic.block.solidifier.RecipeSolidifier;
import com.lothrazar.cyclic.compat.CompatConstants;
import com.lothrazar.cyclic.recipe.CyclicRecipeType;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;
import org.openzen.zencode.java.ZenCodeType;

@SuppressWarnings("rawtypes")
@ZenRegister
@ZenCodeType.Name("mods.cyclic.solidifier")
public class SolidManager implements IRecipeManager {

  @Override
  public IRecipeType<?> getRecipeType() {
    return CyclicRecipeType.SOLID;
  }

  @ZenCodeType.Method
  public void addRecipe(String name, IIngredient inputFirst, IIngredient inputSecond, IIngredient inputThird,
      IFluidStack f, IItemStack output) {
    if (f == null || name == null || f.getFluid() == null || output.getInternal().isEmpty()) {
      return;
    }
    ModCyclic.LOGGER.info("solidifier:addRecipe " + name + f.getFluid() + "  " + output.getInternal());
    RecipeSolidifier<?> m = new RecipeSolidifier(new ResourceLocation(CompatConstants.CT_ID, name),
        inputFirst.asVanillaIngredient(),
        inputSecond.asVanillaIngredient(),
        inputThird.asVanillaIngredient(),
        new FluidStack(f.getFluid(), f.getAmount()),
        output.getInternal());
    if (RecipeSolidifier.addRecipe(m)) {
      ModCyclic.LOGGER.error(String.format("RecipeSolidifier: addRecipe success %s | %d ", name, RecipeMelter.RECIPES.size()));
    }
    else {
      ModCyclic.LOGGER.error(String.format("RecipeSolidifier: addRecipe error %s  " + m, name));
    }
  }

  @ZenCodeType.Method
  public void removeRecipe(String name) {
    if (name == null) {
      return;
    }
    ModCyclic.LOGGER.info("CT: removeRecipe attempt %s %d ", name, RecipeMelter.RECIPES.size());
    this.removeByName(name);
    // go
    RecipeSolidifier<?> found = null;
    for (RecipeSolidifier<?> m : RecipeSolidifier.RECIPES) {
      if (m != null && m.getId() != null &&
          name.equalsIgnoreCase(m.getId().toString())) {
        found = m;
        break;
      }
    }
    if (found != null) {
      int before = RecipeSolidifier.RECIPES.size();
      RecipeSolidifier.RECIPES.remove(found);
      ModCyclic.LOGGER.error(String.format("RecipeSolidifier: removeRecipe found %s ||  %d -> %d "
          + found.getRecipeFluid().getFluid().getRegistryName(), name, before, RecipeMelter.RECIPES.size()));
    }
    else {
      ModCyclic.LOGGER.error(String.format("RecipeSolidifier: removeRecipe NOT found %s  ", name));
    }
  }
}
