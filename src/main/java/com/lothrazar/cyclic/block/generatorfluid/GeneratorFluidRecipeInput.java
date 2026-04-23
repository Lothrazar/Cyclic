package com.lothrazar.cyclic.block.generatorfluid;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;
import net.neoforged.neoforge.fluids.FluidStack;

public class GeneratorFluidRecipeInput implements RecipeInput {

  private final FluidStack fluid;

  public GeneratorFluidRecipeInput(FluidStack fluid) {
    this.fluid = fluid != null ? fluid : FluidStack.EMPTY;
  }

  public FluidStack getFluid() {
    return fluid;
  }

  @Override
  public ItemStack getItem(int index) {
    return ItemStack.EMPTY;
  }

  @Override
  public int size() {
    return 0;
  }
}
