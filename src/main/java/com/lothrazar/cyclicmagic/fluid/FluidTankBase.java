package com.lothrazar.cyclicmagic.fluid;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;

public class FluidTankBase extends FluidTank {
  Fluid fluidAllowed;
  public FluidTankBase(int capacity) {
    super(capacity);
  }
  public void setFluidAllowed(Fluid f) {
    this.fluidAllowed = f;
  }
  @Override
  public boolean canFillFluidType(FluidStack fluid) {
    if (this.fluidAllowed != null && this.fluidAllowed != fluid.getFluid()) {
      return false;
    }
    //else either allowed is null, or they match
    return super.canFillFluidType(fluid);
  }
}
