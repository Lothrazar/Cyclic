package com.lothrazar.cyclic.block.sprinkler;

import com.lothrazar.cyclic.base.FluidTankBase;
import com.lothrazar.cyclic.base.TileEntityBase;
import com.lothrazar.cyclic.block.terrasoil.TileTerraPreta;
import com.lothrazar.cyclic.registry.TileRegistry;
import com.lothrazar.cyclic.util.UtilFluid;
import com.lothrazar.cyclic.util.UtilShape;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;

public class TileSprinkler extends TileEntityBase {

  public static final int CAPACITY = FluidAttributes.BUCKET_VOLUME;
  public static IntValue TIMER_FULL;
  public static IntValue WATERCOST;
  private static final int RAD = 4;
  public FluidTankBase tank;
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
    List<BlockPos> shape = UtilShape.squareHorizontalFull(worldPosition, RAD);
    shapeIndex++;
    if (shapeIndex >= shape.size()) {
      shapeIndex = 0;
    }
    //drain is a per tick cost. at least per timer
    //otherwise if its slowly skipping over lots of empty plots it doesnt seem "turned off"
    //since no animation
    tank.drain(WATERCOST.get(), FluidAction.EXECUTE);
    final double d = 0.001;
    if (TileTerraPreta.grow(level, shape.get(shapeIndex), d)) {
      //it worked, so double drain
      tank.drain(WATERCOST.get(), FluidAction.EXECUTE);
    }
  }

  private void grabWater() {
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
      UtilFluid.tryFillPositionFromTank(level, this.worldPosition, Direction.DOWN, below.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY).orElse(null), CAPACITY);
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
  public CompoundTag save(CompoundTag tag) {
    CompoundTag fluid = new CompoundTag();
    tank.writeToNBT(fluid);
    tag.put(NBTFLUID, fluid);
    tag.putInt("shapeIndex", shapeIndex);
    return super.save(tag);
  }

  @Override
  public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
    if (cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
      return LazyOptional.of(() -> tank).cast();
    }
    return super.getCapability(cap, side);
  }

  @Override
  public void setFluid(FluidStack fluid) {
    tank.setFluid(fluid);
  }

  @Override
  public void setField(int field, int value) {
  }

  @Override
  public int getField(int field) {
    return 0;
  }
}
