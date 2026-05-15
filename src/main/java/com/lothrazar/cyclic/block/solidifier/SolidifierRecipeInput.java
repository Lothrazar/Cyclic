package com.lothrazar.cyclic.block.solidifier;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;
import net.neoforged.neoforge.fluids.FluidStack;

public class SolidifierRecipeInput implements RecipeInput {

  private final ItemStack slot0;
  private final ItemStack slot1;
  private final ItemStack slot2;
  private final FluidStack fluid;

  public SolidifierRecipeInput(ItemStack slot0, ItemStack slot1, ItemStack slot2, FluidStack fluid) {
    this.slot0 = slot0 != null ? slot0 : ItemStack.EMPTY;
    this.slot1 = slot1 != null ? slot1 : ItemStack.EMPTY;
    this.slot2 = slot2 != null ? slot2 : ItemStack.EMPTY;
    this.fluid = fluid != null ? fluid : FluidStack.EMPTY;
  }

  @Override
  public ItemStack getItem(int index) {
    return switch (index) {
      case 0 -> slot0;
      case 1 -> slot1;
      case 2 -> slot2;
      default -> ItemStack.EMPTY;
    };
  }

  public FluidStack getFluid() {
    return fluid;
  }

  @Override
  public int size() {
    return 3;
  }
}
