package com.lothrazar.cyclic.compat.crafttweaker;

import com.blamejared.crafttweaker.api.CraftTweakerAPI;
import com.blamejared.crafttweaker.api.annotations.ZenRegister;
import com.blamejared.crafttweaker.api.fluid.IFluidStack;
import com.blamejared.crafttweaker.api.item.IIngredient;
import com.blamejared.crafttweaker.api.item.IItemStack;
import com.blamejared.crafttweaker.api.logger.ILogger;
import com.blamejared.crafttweaker.api.managers.IRecipeManager;
import com.blamejared.crafttweaker.impl.actions.recipes.ActionAddRecipe;
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
    name = fixRecipeName(name);
    if (output.getInternal().isEmpty()) {
      throw new IllegalArgumentException("Output cannot be empty!");
    }
    RecipeSolidifier<?> m = new RecipeSolidifier(new ResourceLocation(CompatConstants.CT_ID, name),
        inputFirst.asVanillaIngredient(),
        inputSecond.asVanillaIngredient(),
        inputThird.asVanillaIngredient(),
        new FluidStack(f.getFluid(), f.getAmount()),
        output.getInternal());
    
      CraftTweakerAPI.apply(new ActionAddRecipe(this, m, "") {
          @Override
          public void apply() {
              RecipeSolidifier.addRecipe((RecipeSolidifier) recipe);
          }
      });
  }
    
    @ZenCodeType.Method
    public void removeRecipe(String name) {
        removeByName(name);
    }
}
