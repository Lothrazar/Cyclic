package com.lothrazar.cyclic.compat.crafttweaker;

import java.util.Arrays;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openzen.zencode.java.ZenCodeType;
import com.blamejared.crafttweaker.api.CraftTweakerAPI;
import com.blamejared.crafttweaker.api.action.recipe.ActionAddRecipe;
import com.blamejared.crafttweaker.api.annotation.ZenRegister;
import com.blamejared.crafttweaker.api.ingredient.IIngredient;
import com.blamejared.crafttweaker.api.recipe.manager.base.IRecipeManager;
import com.lothrazar.cyclic.block.generatoritem.RecipeGeneratorItem;
import com.lothrazar.cyclic.registry.CyclicRecipeType;
import com.lothrazar.library.recipe.ingredient.EnergyIngredient;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;

@ZenRegister
@ZenCodeType.Name("mods.cyclic.generator_item")
public class EnergyItemZen implements IRecipeManager<RecipeGeneratorItem> {

  private final Logger logger = LogManager.getLogger();

  @Override
  public RecipeType<RecipeGeneratorItem> getRecipeType() {
    return CyclicRecipeType.GENERATOR_ITEM.get();
  }

  @ZenCodeType.Method
  public void addRecipe(String name, IIngredient input, int rfPertick, int ticks) {
    ResourceLocation id = fixRecipeId(name);
    RecipeGeneratorItem m = new RecipeGeneratorItem(input.asVanillaIngredient(), new EnergyIngredient(rfPertick, ticks));
    RecipeHolder<RecipeGeneratorItem> holder = createHolder(id, m);
    CraftTweakerAPI.apply(new ActionAddRecipe<RecipeGeneratorItem>(this, holder, ""));
    logger.debug("Recipe loaded " + id.toString());
  }

  @ZenCodeType.Method
  public void removeRecipe(String... names) {
    removeByName(names);
    logger.debug("Recipe removed " + Arrays.toString(names));
  }
}
