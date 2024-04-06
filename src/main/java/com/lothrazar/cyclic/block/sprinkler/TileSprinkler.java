package com.lothrazar.cyclic.block.sprinkler;

import java.util.List;
import com.lothrazar.cyclic.block.TileBlockEntityCyclic;
import com.lothrazar.cyclic.capabilities.block.FluidTankBase;
import com.lothrazar.cyclic.registry.TileRegistry;
import com.lothrazar.cyclic.util.FluidHelpers;
import com.lothrazar.cyclic.util.GrowthUtil;
import com.lothrazar.cyclic.util.ParticleUtil;
import com.lothrazar.cyclic.util.ShapeUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;

public class TileSprinkler extends TileBlockEntityCyclic {

  public static final int CAPACITY = FluidType.BUCKET_VOLUME;
  public static IntValue TIMER_FULL;
  public static IntValue WATERCOST;
  public static IntValue RADIUS;
  public FluidTankBase tank = new FluidTankBase(this, CAPACITY, p -> p.getFluid() == Fluids.WATER);
  LazyOptional<FluidTankBase> fluidCap = LazyOptional.of(() -> tank);
  private int shapeIndex = 0;

  public TileSprinkler(BlockPos pos, BlockState state) {
    super(TileRegistry.SPRINKLER.get(), pos, state);
    tank = new FluidTankBase(this, CAPACITY, p -> p.getFluid() == Fluids.WATER);
  }

  public static void serverTick(Level level, BlockPos blockPos, BlockState blockState, TileSprinkler e) {
    e.tick();
  }

  public static <E extends BlockEntity> void clientTick(Level level, BlockPos blockPos, BlockState blockState, TileSprinkler e) {
    e.tick();
  }

  public void tick() {
    timer--;
    if (timer > 0) {
      return;
    }
    timer = TIMER_FULL.get();
    this.grabWater();
    if (WATERCOST.get() > 0 && tank.getFluidAmount() < WATERCOST.get()) {
      return;
    }
    List<BlockPos> shape = ShapeUtil.squareHorizontalFull(worldPosition, RADIUS.get());
    shapeIndex++;
    if (shapeIndex >= shape.size()) {
      shapeIndex = 0;
    }
    if (level.isClientSide && GrowthUtil.isValidGrow(level, shape.get(shapeIndex))) {
      ParticleUtil.spawnParticle(level, ParticleTypes.FALLING_WATER, shape.get(shapeIndex), 9);
    }
    if (level instanceof ServerLevel sl) {
      if (GrowthUtil.tryGrow(sl, shape.get(shapeIndex), 1)) {
        //it worked so pay
        tank.drain(WATERCOST.get(), FluidAction.EXECUTE);
        //run it again since sprinkler costs fluid and therefore should double what the glass and soil do 
        GrowthUtil.tryGrow(sl, shape.get(shapeIndex), 1);
      }
    }
  }

  private void grabWater() {
    if (level.isClientSide) {
      return;
    }
    //only drink from below. similar to but updated from 1.12.2
    BlockState down = level.getBlockState(worldPosition.below());
    if (tank.isEmpty() && down.getBlock() == Blocks.WATER
        && down.getFluidState().isSource()) {
      tank.fill(new FluidStack(Fluids.WATER, CAPACITY), FluidAction.EXECUTE);
      level.setBlockAndUpdate(worldPosition.below(), Blocks.AIR.defaultBlockState());
      return;
    }
    BlockEntity below = this.level.getBlockEntity(this.worldPosition.below());
    if (below != null) {
      //from below, fill this.pos 
      FluidHelpers.tryFillPositionFromTank(level, this.worldPosition, Direction.DOWN, below.getCapability(ForgeCapabilities.FLUID_HANDLER).orElse(null), CAPACITY);
    }
  }

  @Override
  public void load(CompoundTag tag) {
    CompoundTag fluid = tag.getCompound(NBTFLUID);
    tank.readFromNBT(fluid);
    shapeIndex = tag.getInt("shapeIndex");
    super.load(tag);
  }

  @Override
  public void saveAdditional(CompoundTag tag) {
    CompoundTag fluid = new CompoundTag();
    tank.writeToNBT(fluid);
    tag.put(NBTFLUID, fluid);
    tag.putInt("shapeIndex", shapeIndex);
    super.saveAdditional(tag);
  }

  @Override
  public void invalidateCaps() {
    fluidCap.invalidate();
    super.invalidateCaps();
  }

  @Override
  public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
    if (cap == ForgeCapabilities.FLUID_HANDLER) {
      return fluidCap.cast();
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
  public void setField(int field, int value) {}

  @Override
  public int getField(int field) {
    return 0;
  }
}
