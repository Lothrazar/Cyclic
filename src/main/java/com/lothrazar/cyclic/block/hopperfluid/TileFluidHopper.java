package com.lothrazar.cyclic.block.hopperfluid;

import com.lothrazar.cyclic.block.TileBlockEntityCyclic;
import com.lothrazar.cyclic.capabilities.FluidTankBase;
import com.lothrazar.cyclic.registry.TileRegistry;
import com.lothrazar.cyclic.util.UtilFluid;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;

public class TileFluidHopper extends TileBlockEntityCyclic {

  private static final int FLOW = FluidAttributes.BUCKET_VOLUME;
  public static final int CAPACITY = FluidAttributes.BUCKET_VOLUME;
  public FluidTankBase tank = new FluidTankBase(this, CAPACITY, p -> true);

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
  public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
    if (cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
      return LazyOptional.of(() -> tank).cast();
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
    tryExtract(Direction.UP);
    //then pull from hopper facey side
    Direction exportToSide = this.getBlockState().getValue(BlockFluidHopper.FACING);
    if (exportToSide != null && exportToSide != Direction.UP) {
      moveFluids(exportToSide, worldPosition.relative(exportToSide), FLOW, tank);
    }
  }

  private void tryExtract(Direction extractSide) {
    if (extractSide == null || tank == null) {
      return;
    }
    BlockPos target = this.worldPosition.relative(extractSide);
    Direction incomingSide = extractSide.getOpposite();
    IFluidHandler sideHandler = UtilFluid.getTank(level, target, incomingSide);
    boolean success = false;
    if (sideHandler != null) {
      success = UtilFluid.tryFillPositionFromTank(level, worldPosition, extractSide, sideHandler, FLOW);
      if (success) {
        return;
      }
    }
    if (!success && tank.getSpace() >= FluidAttributes.BUCKET_VOLUME) {
      //test if its a source block, or a waterlogged block. 
      BlockState targetState = level.getBlockState(target);
      FluidState fluidState = level.getFluidState(target); // targetState.getFluidState();
      //new
      if (targetState.hasProperty(BlockStateProperties.WATERLOGGED) && targetState.getValue(BlockStateProperties.WATERLOGGED) == true) {
        targetState = targetState.setValue(BlockStateProperties.WATERLOGGED, false);
        //for waterlogged it is hardcoded to water
        if (level.setBlockAndUpdate(target, targetState)) {
          sideHandler.fill(new FluidStack(Fluids.WATER, FluidAttributes.BUCKET_VOLUME), FluidAction.EXECUTE);
        }
      }
      else if (fluidState != null && fluidState.isSource()) {
        //not just water. any fluid source block
        if (level.setBlockAndUpdate(target, Blocks.AIR.defaultBlockState())) {
          sideHandler.fill(new FluidStack(fluidState.getType(), FluidAttributes.BUCKET_VOLUME), FluidAction.EXECUTE);
        }
      }
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
