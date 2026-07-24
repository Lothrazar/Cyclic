package com.lothrazar.cyclic.block.tank;

import com.lothrazar.cyclic.block.TileBlockEntityCyclic;
import com.lothrazar.cyclic.capabilities.block.FluidTankBase;
import com.lothrazar.cyclic.registry.TileRegistry;
import com.lothrazar.cyclic.util.FluidHelpers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;

public class TileTank extends TileBlockEntityCyclic {

  public static final int CAPACITY = 64 * FluidType.BUCKET_VOLUME;
  public static final int TRANSFER_FLUID_PER_TICK = FluidType.BUCKET_VOLUME / 20;
  public FluidTankBase tank = new FluidTankBase(this, CAPACITY, p -> true);

  public TileTank(BlockPos pos, BlockState state) {
    super(TileRegistry.TANK.get(), pos, state);
    this.needsRedstone = 0;
  }

  public static void serverTick(Level level, BlockPos blockPos, BlockState blockState, TileTank e) {
    e.tick();
  }

  @Override
  public void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
    CompoundTag fluid = tag.getCompoundOrEmpty(NBTFLUID);
    tank.readFromNBT(registries,fluid);
    super.loadAdditional(tag,registries);
  }

  @Override
  public void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
    CompoundTag fluid = new CompoundTag();
    tank.writeToNBT(registries,fluid);
    tag.put(NBTFLUID, fluid);
    super.saveAdditional(tag,registries);
  }

  @Override
  public void setField(int field, int value) {}

  @Override
  public int getField(int field) {
    return 0;
  }

  @Override
  public IFluidHandler getFluidHandler(Direction side) {
    return tank;
  }

  @Override
  public void setFluid(FluidStack fluid) {
    tank.setFluid(fluid);
    this.updateComparatorOutputLevel();
  }

  //  @Override
  public void tick() {
    //drain below but only to one of myself 
    BlockEntity below = this.level.getBlockEntity(this.worldPosition.below());
    if (below != null && below instanceof TileTank) {
      FluidHelpers.tryFillPositionFromTank(level, this.worldPosition.below(), Direction.UP, tank, TRANSFER_FLUID_PER_TICK);
      this.updateComparatorOutputLevel();
      this.updateComparatorOutputLevelAt(worldPosition.below());
    }
  }
}
