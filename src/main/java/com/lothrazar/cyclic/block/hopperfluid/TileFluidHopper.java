package com.lothrazar.cyclic.block.hopperfluid;

import com.lothrazar.cyclic.base.FluidTankBase;
import com.lothrazar.cyclic.base.TileEntityBase;
import com.lothrazar.cyclic.registry.TileRegistry;
import com.lothrazar.cyclic.util.UtilFluid;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;

public class TileFluidHopper extends TileEntityBase implements ITickableTileEntity {

  private static final int FLOW = FluidAttributes.BUCKET_VOLUME;
  public static final int CAPACITY = FluidAttributes.BUCKET_VOLUME;
  public FluidTankBase tank = new FluidTankBase(this, CAPACITY, p -> true);

  public TileFluidHopper() {
    super(TileRegistry.FLUIDHOPPER.get());
  }

  @Override
  public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
    if (cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
      return LazyOptional.of(() -> tank).cast();
    }
    return super.getCapability(cap, side);
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
    if (this.isPowered()) {
      return;
    }
    if (world.isRemote) {
      return;
    }
    //first pull down from above
    tryExtract(Direction.UP);
    //then pull from hopper facey side
    Direction exportToSide = this.getBlockState().get(BlockFluidHopper.FACING);
    if (exportToSide != null && exportToSide != Direction.UP) {
      moveFluids(exportToSide, pos.offset(exportToSide), FLOW, tank);
    }
  }

  private void tryExtract(Direction extractSide) {
    if (extractSide == null || tank == null) {
      return;
    }
    BlockPos target = this.pos.offset(extractSide);
    Direction incomingSide = extractSide.getOpposite();
    IFluidHandler stuff = UtilFluid.getTank(world, target, incomingSide);
    boolean success = false;
    if (stuff != null) {
      success = UtilFluid.tryFillPositionFromTank(world, pos, extractSide, stuff, FLOW);
      if (success) {
        return;
      }
    }
    if (!success && tank.getSpace() >= FluidAttributes.BUCKET_VOLUME) {
      //test if its a source block, or a waterlogged block.
      BlockState targetState = world.getBlockState(target);
      FluidState fluidState = world.getFluidState(target); // targetState.getFluidState();
      //new
      if (targetState.hasProperty(BlockStateProperties.WATERLOGGED) && targetState.get(BlockStateProperties.WATERLOGGED) == true) {
        targetState = targetState.with(BlockStateProperties.WATERLOGGED, false);
        //for waterlogged it is hardcoded to water
        if (world.setBlockState(target, targetState)) {
          tank.fill(new FluidStack(Fluids.WATER, FluidAttributes.BUCKET_VOLUME), FluidAction.EXECUTE);
        }
      }
      else if (fluidState != null && fluidState.isSource()) {
        //not just water. any fluid source block
        if (world.setBlockState(target, Blocks.AIR.getDefaultState())) {
          tank.fill(new FluidStack(fluidState.getFluid(), FluidAttributes.BUCKET_VOLUME), FluidAction.EXECUTE);
        }
      }
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
