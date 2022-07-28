package com.lothrazar.cyclic.block.hopperfluid;

import com.lothrazar.cyclic.block.TileBlockEntityCyclic;
import com.lothrazar.cyclic.capabilities.block.FluidTankBase;
import com.lothrazar.cyclic.registry.TileRegistry;
import com.lothrazar.cyclic.util.FluidHelpers;
import com.lothrazar.cyclic.util.FluidHelpers.FluidAttributes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;

public class TileFluidHopper extends TileBlockEntityCyclic {

  private static final int FLOW = FluidType.BUCKET_VOLUME;
  public static final int CAPACITY = FluidType.BUCKET_VOLUME;
  public FluidTankBase tank = new FluidTankBase(this, CAPACITY, p -> true);
  LazyOptional<FluidTankBase> fluidCap = LazyOptional.of(() -> tank);

  public TileFluidHopper(BlockPos pos, BlockState state) {
    super(TileRegistry.FLUIDHOPPER.get(), pos, state);
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
  public void invalidateCaps() {
    fluidCap.invalidate();
    super.invalidateCaps();
  }

  @Override
  public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
    if (cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
      return fluidCap.cast();
    }
    return super.getCapability(cap, side);
  }

  public static void serverTick(Level level, BlockPos blockPos, BlockState blockState, TileFluidHopper e) {
    e.tick();
  }

  public static <E extends BlockEntity> void clientTick(Level level, BlockPos blockPos, BlockState blockState, TileFluidHopper e) {
    e.tick();
  }

  public void tick() {
    if (this.isPowered()) {
      return;
    }
    if (level.isClientSide) {
      return;
    }
    //first pull down from above
    tryExtract();
    //then pull from hopper facey side
    Direction exportToSide = this.getBlockState().getValue(BlockFluidHopper.FACING);
    if (exportToSide != null && exportToSide != Direction.UP) {
      moveFluids(exportToSide, worldPosition.relative(exportToSide), FLOW, tank);
    }
  }

  private void tryExtract() {
    if (tank == null) {
      return;
    }
    BlockPos target = this.worldPosition.relative(Direction.UP);
    IFluidHandler tankAbove = FluidHelpers.getTank(level, target, Direction.DOWN);
    boolean success = FluidHelpers.tryFillPositionFromTank(level, worldPosition, Direction.UP, tankAbove, FLOW);
    if (success) {
      return;
    }
    //try from the world
    if (tank.getSpace() >= FluidAttributes.BUCKET_VOLUME) {
      FluidHelpers.extractSourceWaterloggedCauldron(level, target, tank);
    }
  }

  @Override
  public void load(CompoundTag tag) {
    tank.readFromNBT(tag.getCompound(NBTFLUID));
    super.load(tag);
  }

  @Override
  public void saveAdditional(CompoundTag tag) {
    CompoundTag fluid = new CompoundTag();
    tank.writeToNBT(fluid);
    tag.put(NBTFLUID, fluid);
    super.saveAdditional(tag);
  }

  public int getFill() {
    if (tank == null || tank.getFluidInTank(0).isEmpty()) {
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
