package com.lothrazar.cyclic.block.tank;

import com.lothrazar.cyclic.block.TileBlockEntityCyclic;
import com.lothrazar.cyclic.capabilities.block.FluidTankBase;
import com.lothrazar.cyclic.registry.TileRegistry;
import com.lothrazar.cyclic.util.FluidHelpers;
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

public class TileTank extends TileBlockEntityCyclic {

  public static final int CAPACITY = 64 * FluidType.BUCKET_VOLUME;
  public static final int TRANSFER_FLUID_PER_TICK = FluidType.BUCKET_VOLUME / 20;
  public FluidTankBase tank = new FluidTankBase(this, CAPACITY, p -> true);
  LazyOptional<FluidTankBase> fluidCap = LazyOptional.of(() -> tank);

  public TileTank(BlockPos pos, BlockState state) {
    super(TileRegistry.TANK.get(), pos, state);
  }

  public static void serverTick(Level level, BlockPos blockPos, BlockState blockState, TileTank e) {
    e.tick();
  }

  public static <E extends BlockEntity> void clientTick(Level level, BlockPos blockPos, BlockState blockState, TileTank e) {
    //    e.tick();
  }

  @Override
  public void load(CompoundTag tag) {
    CompoundTag fluid = tag.getCompound(NBTFLUID);
    tank.readFromNBT(fluid);
    super.load(tag);
  }

  @Override
  public void saveAdditional(CompoundTag tag) {
    CompoundTag fluid = new CompoundTag();
    tank.writeToNBT(fluid);
    tag.put(NBTFLUID, fluid);
    super.saveAdditional(tag);
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

  @Override
  public void setField(int field, int value) {}

  @Override
  public int getField(int field) {
    return 0;
  }

  @Override
  public void setFluid(FluidStack fluid) {
    tank.setFluid(fluid);
  }

  //  @Override
  public void tick() {
    //drain below but only to one of myself
    BlockEntity below = this.level.getBlockEntity(this.worldPosition.below());
    if (below != null && below instanceof TileTank) {
      FluidHelpers.tryFillPositionFromTank(level, this.worldPosition.below(), Direction.UP, tank, TRANSFER_FLUID_PER_TICK);
    }
  }
}
