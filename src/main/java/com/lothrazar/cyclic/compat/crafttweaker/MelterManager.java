package com.lothrazar.cyclic.compat.crafttweaker;
import com.blamejared.crafttweaker.api.CraftTweakerAPI;
import com.blamejared.crafttweaker.api.action.recipe.ActionAddRecipe;
import com.blamejared.crafttweaker.api.annotation.ZenRegister;
import com.blamejared.crafttweaker.api.fluid.IFluidStack;
import com.blamejared.crafttweaker.api.ingredient.IIngredient;
import com.blamejared.crafttweaker.api.recipe.manager.base.IRecipeManager;
import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.block.melter.RecipeMelter;
import com.lothrazar.cyclic.compat.CompatConstants;
import com.lothrazar.cyclic.registry.CyclicRecipeType;
import com.lothrazar.library.recipe.ingredient.EnergyIngredient;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openzen.zencode.java.ZenCodeType;

@ZenRegister
@ZenCodeType.Name("mods.cyclic.melter")
public class MelterManager implements IRecipeManager {

  private final Logger logger = LogManager.getLogger();

  @Override
  public RecipeType<?> getRecipeType() {
    return CyclicRecipeType.MELTER.get();
  }

  @ZenCodeType.Method
  public void addRecipe(String name, IIngredient[] input, IFluidStack fluidStack, int rfPertick, int ticks) {
    name = fixRecipeName(name);
    NonNullList list = NonNullList.withSize(input.length, Ingredient.EMPTY);
    for (int i = 0; i < input.length; i++) {
      list.set(i, input[i].asVanillaIngredient());
    }
    RecipeMelter m = new RecipeMelter(new ResourceLocation(CompatConstants.CRAFTTWEAKER, name),
        list,
        new FluidStack(fluidStack.getFluid(), (int) fluidStack.getAmount()), new EnergyIngredient(rfPertick, ticks));
    //
    CraftTweakerAPI.apply(new ActionAddRecipe(this, m, ""));
    logger.debug("Recipe loaded " + m.getId().toString());
  }

  @ZenCodeType.Method
  public void removeRecipe(String... names) {
    removeByName(names);
    logger.debug("Recipe removed " + names);
  }
}
