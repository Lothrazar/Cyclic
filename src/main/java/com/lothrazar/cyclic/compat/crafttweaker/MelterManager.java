//package com.lothrazar.cyclic.compat.crafttweaker;
//
//import com.blamejared.crafttweaker.api.CraftTweakerAPI;
//import com.blamejared.crafttweaker.api.annotation.ZenRegister;
//import com.blamejared.crafttweaker.api.fluid.IFluidStack;
//import com.blamejared.crafttweaker.api.ingredient.IIngredient;
//import com.blamejared.crafttweaker.api.recipe.manager.base.IRecipeManager;
//import com.lothrazar.cyclic.ModCyclic;
//import com.lothrazar.cyclic.block.melter.RecipeMelter;
//import com.lothrazar.cyclic.compat.CompatConstants;
//import com.lothrazar.cyclic.registry.CyclicRecipeType;
//import net.minecraft.world.item.crafting.RecipeType;
//import net.minecraft.resources.ResourceLocation;
//import net.minecraftforge.fluids.FluidStack;
//import org.openzen.zencode.java.ZenCodeType;
//@ZenRegister
//@ZenCodeType.Name("mods.cyclic.melter")
//public class MelterManager implements IRecipeManager {
//
//  @Override
//  public RecipeType<?> getRecipeType() {
//    return CyclicRecipeType.MELTER.get();
//  }
//
//  @ZenCodeType.Method
//  public void addRecipe(String name, IIngredient[] input, IFluidStack fluidStack) {
//    name = fixRecipeName(name);
//    RecipeMelter<?> m = new RecipeMelter(new ResourceLocation(CompatConstants.CRAFTTWEAKER, name),
//        inputFirst.asVanillaIngredient(),
//        inputSecond.asVanillaIngredient(),
//        new FluidStack(fluidStack.getFluid(), fluidStack.getAmount()));
//    CraftTweakerAPI.apply(new ActionAddRecipe(this, m, ""));
//    ModCyclic.LOGGER.info("CRAFT TWEAKER: Recipe loaded " + m.getId().toString());
//  }
//
//  @ZenCodeType.Method
//  public void removeRecipe(String... names) {
//    removeByName(names);
//    ModCyclic.LOGGER.info("CRAFT TWEAKER: Recipe removed " + names);
//  }
//}
