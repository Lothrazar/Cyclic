package com.lothrazar.cyclic.compat.crafttweaker;

import java.util.Arrays;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openzen.zencode.java.ZenCodeType;
import com.blamejared.crafttweaker.api.CraftTweakerAPI;
import com.blamejared.crafttweaker.api.action.recipe.ActionAddRecipe;
import com.blamejared.crafttweaker.api.annotation.ZenRegister;
import com.blamejared.crafttweaker.api.ingredient.IIngredient;
import com.blamejared.crafttweaker.api.item.IItemStack;
import com.blamejared.crafttweaker.api.recipe.manager.base.IRecipeManager;
import com.lothrazar.cyclic.block.crusher.RecipeCrusher;
import com.lothrazar.cyclic.compat.CompatConstants;
import com.lothrazar.cyclic.registry.CyclicRecipeType;
import com.lothrazar.library.recipe.ingredient.EnergyIngredient;
import com.lothrazar.library.recipe.ingredient.RandomizedOutputIngredient;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeType;

@ZenRegister
@ZenCodeType.Name("mods.cyclic.crusher")
public class CrusherZen implements IRecipeManager<RecipeCrusher> {

  private final Logger logger = LogManager.getLogger();

  @Override
  public RecipeType<RecipeCrusher> getRecipeType() {
    return CyclicRecipeType.CRUSHER.get();
  }

  @ZenCodeType.Method
  public void addRecipePlain(String name, IIngredient input, IItemStack output, int rfPertick, int ticks) {
    addRecipe(name, input, output, rfPertick, ticks, IItemStack.empty(), 0);
  }

  @ZenCodeType.Method
  public void addRecipe(String name, IIngredient input, IItemStack output, int rfPertick, int ticks, IItemStack bonus, int percentage) {
    name = fixRecipeName(name);
    RecipeCrusher m = new RecipeCrusher(new ResourceLocation(CompatConstants.CRAFTTWEAKER, name), input.asVanillaIngredient(), new EnergyIngredient(rfPertick, ticks), output.getInternal(),
        new RandomizedOutputIngredient(percentage, bonus.getInternal()));
    CraftTweakerAPI.apply(new ActionAddRecipe<RecipeCrusher>(this, m, ""));
    logger.debug("Recipe loaded " + m.getId().toString());
  }

  @ZenCodeType.Method
  public void removeRecipe(String... names) {
    removeByName(names);
    logger.debug("Recipe removed " + Arrays.toString(names));
  }
}
