package com.lothrazar.cyclic.compat.crafttweaker;

import com.blamejared.crafttweaker.api.CraftTweakerAPI;
import com.blamejared.crafttweaker.api.annotations.ZenRegister;
import com.blamejared.crafttweaker.api.item.IIngredient;
import com.blamejared.crafttweaker.api.managers.IRecipeManager;
import com.blamejared.crafttweaker.impl.actions.recipes.ActionAddRecipe;
import com.lothrazar.cyclic.block.generatoritem.RecipeGeneratorItem;
import com.lothrazar.cyclic.compat.CompatConstants;
import com.lothrazar.cyclic.recipe.CyclicRecipeType;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openzen.zencode.java.ZenCodeType;

@ZenRegister
@ZenCodeType.Name("mods.cyclic.generator_item")
public class EnergyItemZen implements IRecipeManager {

  private final Logger logger = LogManager.getLogger();

  @Override
  public IRecipeType<?> getRecipeType() {
    return CyclicRecipeType.GENERATOR_ITEM;
  }

  @SuppressWarnings("rawtypes")
  @ZenCodeType.Method
  public void addRecipe(String name, IIngredient input, int rfPertick, int ticks) {
    name = fixRecipeName(name);
    RecipeGeneratorItem m = new RecipeGeneratorItem(new ResourceLocation(CompatConstants.CRAFTTWEAKER, name), input.asVanillaIngredient(), ticks, rfPertick);
    CraftTweakerAPI.apply(new ActionAddRecipe(this, m, ""));
    logger.debug("Recipe loaded " + m.getId().toString());
  }

  @ZenCodeType.Method
  public void removeRecipe(String names) {
    removeByName(names);
    logger.debug("Recipe removed " + names);
  }
}
