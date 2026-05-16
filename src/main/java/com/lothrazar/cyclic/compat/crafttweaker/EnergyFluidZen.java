package com.lothrazar.cyclic.compat.crafttweaker;

import java.util.Arrays;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openzen.zencode.java.ZenCodeType;
import com.blamejared.crafttweaker.api.CraftTweakerAPI;
import com.blamejared.crafttweaker.api.action.recipe.ActionAddRecipe;
import com.blamejared.crafttweaker.api.annotation.ZenRegister;
import com.blamejared.crafttweaker.api.fluid.IFluidStack;
import com.blamejared.crafttweaker.api.recipe.manager.base.IRecipeManager;
import com.lothrazar.cyclic.block.generatorfluid.RecipeGeneratorFluid;
import com.lothrazar.cyclic.registry.CyclicRecipeType;
import com.lothrazar.library.recipe.ingredient.EnergyIngredient;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;

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
    ResourceLocation id = fixRecipeId(name);
    RecipeGeneratorFluid m = new RecipeGeneratorFluid(
        SizedFluidIngredient.of(fluid.getFluid(), (int) fluid.getAmount()),
        new EnergyIngredient(rfPertick, ticks));
    RecipeHolder<RecipeGeneratorFluid> holder = createHolder(id, m);
    CraftTweakerAPI.apply(new ActionAddRecipe<RecipeGeneratorFluid>(this, holder, ""));
    logger.debug("Recipe loaded " + id.toString());
  }

  @ZenCodeType.Method
  public void addRecipe(String name, String fluidTag, int amount, int rfPertick, int ticks) {
    ResourceLocation id = fixRecipeId(name);
    fluidTag = fluidTag.replace("<", "").replace(">", "").replace("fluid:", "");
    TagKey<Fluid> tag = TagKey.create(Registries.FLUID, ResourceLocation.parse(fluidTag));
    RecipeGeneratorFluid m = new RecipeGeneratorFluid(
        SizedFluidIngredient.of(tag, amount),
        new EnergyIngredient(rfPertick, ticks));
    RecipeHolder<RecipeGeneratorFluid> holder = createHolder(id, m);
    CraftTweakerAPI.apply(new ActionAddRecipe<RecipeGeneratorFluid>(this, holder, ""));
    logger.debug("Recipe  (tag %s) loaded " + id.toString(), fluidTag);
  }

  @ZenCodeType.Method
  public void removeRecipe(String... names) {
    removeByName(names);
    logger.debug("Recipe removed " + Arrays.toString(names));
  }
}
