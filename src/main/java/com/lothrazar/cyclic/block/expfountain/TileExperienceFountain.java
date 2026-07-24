package com.lothrazar.cyclic.block.expfountain;

import java.util.function.Predicate;
import com.lothrazar.cyclic.block.TileBlockEntityCyclic;
import com.lothrazar.cyclic.block.expcollect.TileExpPylon;
import com.lothrazar.cyclic.capabilities.block.FluidTankBase;
import com.lothrazar.cyclic.data.DataTags;
import com.lothrazar.cyclic.registry.TileRegistry;
import com.lothrazar.cyclic.util.FluidHelpers;
import com.lothrazar.cyclic.util.CapabilityUtil;
import com.lothrazar.library.util.FluidHelpersUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;

public class TileExperienceFountain extends TileBlockEntityCyclic {

  public static final int CAPACITY = FluidType.BUCKET_VOLUME;
  public static ModConfigSpec.IntValue TIMER_FULL;
  public static ModConfigSpec.IntValue XP_MIN;
  public static ModConfigSpec.IntValue XP_MAX;
  public FluidTankBase tank = new FluidTankBase(this, CAPACITY, isFluidValid());

  public TileExperienceFountain(BlockPos pos, BlockState state) {
    super(TileRegistry.EXPERIENCE_FOUNTAIN.get(), pos, state);
    this.needsRedstone = 1;
  }

  @Override
  public IFluidHandler getFluidHandler(Direction side) {
    return tank;
  }

  public static void serverTick(Level level, BlockPos blockPos, BlockState blockState, TileExperienceFountain e) {
    e.tick();
  }

  public void tick() {
    if (level == null || level.isClientSide()) {
      return;
    }
    tryPullFromBelow();
    if (!this.isPowered()) {
      return;
    }
    timer--;
    if (timer > 0) {
      return;
    }
    timer = TIMER_FULL.get();
    int min = XP_MIN.get();
    int max = XP_MAX.get();
    int xpValue = min + level.getRandom().nextInt(Math.max(1, max - min + 1));
    int fluidCost = xpValue * TileExpPylon.FLUID_PER_EXP;
    if (tank.getFluidAmount() < fluidCost) {
      return;
    }
    tank.drain(fluidCost, IFluidHandler.FluidAction.EXECUTE);
    double dx = worldPosition.getX() + 0.5D + (level.getRandom().nextDouble() * 2.0D - 1.0D);
    double dy = worldPosition.getY() + 1.0D;
    double dz = worldPosition.getZ() + 0.5D + (level.getRandom().nextDouble() * 2.0D - 1.0D);
    ExperienceOrb orb = new ExperienceOrb(level, dx, dy, dz, xpValue);
    level.addFreshEntity(orb);
    this.setChanged();
  }

  private void tryPullFromBelow() {
    if (tank.getSpace() <= 0) {
      return;
    }
    IFluidHandler below = CapabilityUtil.fluid(level, worldPosition.below(), Direction.UP);
    if (below == null) {
      return;
    }
    boolean filled = FluidHelpers.tryFillPositionFromTank(level, worldPosition, Direction.DOWN, below, CAPACITY);
    if (filled) {
      this.setChanged();
    }
  }

  public Predicate<FluidStack> isFluidValid() {
    return p -> FluidHelpersUtil.matches(p.getFluid(), DataTags.EXPERIENCE);
  }

  @Override
  public void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
    tank.readFromNBT(registries, tag.getCompound(NBTFLUID));
    super.loadAdditional(tag, registries);
  }

  @Override
  public void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
    CompoundTag fluid = new CompoundTag();
    tank.writeToNBT(registries, fluid);
    tag.put(NBTFLUID, fluid);
    super.saveAdditional(tag, registries);
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
