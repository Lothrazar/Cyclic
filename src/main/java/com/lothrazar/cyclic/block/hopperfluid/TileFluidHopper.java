package com.lothrazar.cyclic.block.hopperfluid;

import com.lothrazar.cyclic.base.FluidTankBase;
import com.lothrazar.cyclic.base.TileEntityBase;
import com.lothrazar.cyclic.registry.TileRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;

public class TileFluidHopper extends TileEntityBase implements ITickableTileEntity {

  private static final int FLOW = FluidAttributes.BUCKET_VOLUME;
  public static final int CAPACITY = FluidAttributes.BUCKET_VOLUME;
  public FluidTankBase tank = new FluidTankBase(this, CAPACITY, p -> true);
  private LazyOptional<IFluidHandler> fluidHandlerLazyOptional = LazyOptional.of(() -> tank);

  public TileFluidHopper() {
    super(TileRegistry.FLUIDHOPPER.get());
  }

  @Override
  public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
    if (cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
      return fluidHandlerLazyOptional.cast();
    }
    return super.getCapability(cap, side);
  }

  @Override
  public void invalidateCaps() {
    fluidHandlerLazyOptional.invalidate();
    super.invalidateCaps();
  }

  @Override
  public FluidStack getFluid() {
    return tank == null ? FluidStack.EMPTY : tank.getFluid();
  }

  @Override
  public void setFluid(FluidStack fluid) {
    tank.setFluid(fluid);
  }

  @Override
  public void tick() {
    if (world == null || world.isRemote) {
      return;
    }
    if (this.isPowered()) {
      return;
    }
    //first pull down from above
    getFluidsFromAdjacent(tank, Direction.UP, FLOW);
    //then pull from hopper facey side
    Direction exportToSide = this.getBlockState().get(BlockFluidHopper.FACING);
    if (exportToSide != Direction.UP) {
      moveFluidsToAdjacent(tank, exportToSide, FLOW);
    }
  }

  @Override
  public void read(BlockState bs, CompoundNBT tag) {
    tank.readFromNBT(tag.getCompound(NBTFLUID));
    super.read(bs, tag);
  }

  @Override
  public CompoundNBT write(CompoundNBT tag) {
    CompoundNBT fluid = new CompoundNBT();
    tank.writeToNBT(fluid);
    tag.put(NBTFLUID, fluid);
    return super.write(tag);
  }

  public int getFill() {
    if (tank.getFluidInTank(0).isEmpty()) {
      return 0;
    }
    return tank.getFluidInTank(0).getAmount();
  }

  @Override
  public void setField(int field, int value) {}

  @Override
  public int getField(int field) {
    return 0;
  }
}
