package com.lothrazar.cyclic.block.tank;

import java.util.function.Predicate;
import javax.annotation.Nonnull;
import com.lothrazar.cyclic.CyclicRegistry;
import com.lothrazar.cyclic.base.TileEntityBase;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;

public class TileTank extends TileEntityBase {

  private static final int CAPACITY = 64 * FluidAttributes.BUCKET_VOLUME;
  private FluidTank tank;

  public TileTank() {
    super(CyclicRegistry.Tiles.tank);
    tank = new FluidTank(CAPACITY, isFluidValid());
  }

  public Predicate<FluidStack> isFluidValid() {
    return p -> true;
  }

  @Override
  public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, Direction side) {
    if (cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
      return LazyOptional.of(() -> tank).cast();
    }
    return super.getCapability(cap, side);
  }

  @Override
  public void setField(int field, int value) {}

  public FluidStack getFluid() {
    return tank.getFluid();
  }
}
