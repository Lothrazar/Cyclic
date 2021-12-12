package com.lothrazar.cyclic.block.tank;

import com.lothrazar.cyclic.base.FluidTankBase;
import com.lothrazar.cyclic.base.TileEntityBase;
import com.lothrazar.cyclic.registry.TileRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;

public class TileTank extends TileEntityBase implements ITickableTileEntity {

  public static final int CAPACITY = 64 * FluidAttributes.BUCKET_VOLUME;
  public static final int TRANSFER_FLUID_PER_TICK = FluidAttributes.BUCKET_VOLUME / 20;
  public FluidTankBase tank = new FluidTankBase(this, CAPACITY, fluidStack -> true) {
    public int getTankCapacity(int tank) {
      int space = super.getSpace();
      final IFluidHandler above = getTankAbove();
      if (above != null && above.isFluidValid(tank, getFluidInTank(tank))) {
        final int spaceAbove = above.getTankCapacity(tank);
        space = (int) Math.min(Integer.MAX_VALUE, (long) space + (long) spaceAbove);
      }
      return space;
    }

    public int fill(FluidStack resource, FluidAction action) {
      final int amount = resource.getAmount();
      int filled = super.fill(resource, action);
      final IFluidHandler above = getTankAbove();
      if (filled < amount && above != null) {
        final FluidStack remaining = resource.copy();
        remaining.setAmount(Math.min(TRANSFER_FLUID_PER_TICK, amount - filled));
        filled = (int) Math.min(Integer.MAX_VALUE, (long) filled + (long) above.fill(remaining, action));
      }
      return filled;
    }
  };
  private final LazyOptional<IFluidHandler> fluidCap = LazyOptional.of(() -> tank);
  private IFluidHandler fluidHandlerAbove = null;
  private IFluidHandler fluidHandlerBelow = null;
  public boolean isTankAbove = true;
  public boolean isTankBelow = true;

  public TileTank() {
    super(TileRegistry.tank);
  }

  private IFluidHandler getTankAbove() {
    if (fluidHandlerAbove != null) {
      return fluidHandlerAbove;
    }
    if (!isTankAbove || world == null) {
      return null;
    }
    final TileEntity tileEntityAbove = world.getTileEntity(pos.up());
    if (!(tileEntityAbove instanceof TileTank)) {
      return null;
    }
    final TileTank tankAbove = (TileTank) tileEntityAbove;
    final LazyOptional<IFluidHandler> fluidCapAbove = tankAbove.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, Direction.DOWN);
    fluidHandlerAbove = fluidCapAbove.resolve().orElse(null);
    if (fluidHandlerAbove == null) {
      isTankAbove = false;
    }
    else {
      fluidCapAbove.addListener(o -> {
        fluidHandlerAbove = null;
        isTankAbove = false;
      });
    }
    return fluidHandlerAbove;
  }

  private IFluidHandler getTankBelow() {
    if (fluidHandlerBelow != null) {
      return fluidHandlerBelow;
    }
    if (!isTankBelow || world == null) {
      return null;
    }
    final TileEntity tileEntityBelow = world.getTileEntity(pos.down());
    if (!(tileEntityBelow instanceof TileTank)) {
      return null;
    }
    final TileTank tankBelow = (TileTank) tileEntityBelow;
    final LazyOptional<IFluidHandler> fluidCapBelow = tankBelow.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, Direction.UP);
    fluidHandlerBelow = fluidCapBelow.resolve().orElse(null);
    if (fluidHandlerBelow == null) {
      isTankBelow = false;
    }
    else {
      fluidCapBelow.addListener(o -> {
        fluidHandlerBelow = null;
        isTankBelow = false;
      });
    }
    return fluidHandlerBelow;
  }

  @Override
  public IFluidHandler getFluidHandler() {
    return tank;
  }

  @Override
  public void read(BlockState bs, CompoundNBT tag) {
    tank.readFromNBT(tag.getCompound(NBTFLUID));
    isTankAbove = tag.getBoolean("istankabove");
    isTankBelow = tag.getBoolean("istankbelow");
    super.read(bs, tag);
  }

  @Override
  public CompoundNBT write(CompoundNBT tag) {
    tag.put(NBTFLUID, tank.writeToNBT(new CompoundNBT()));
    tag.putBoolean("istankabove", isTankAbove);
    tag.putBoolean("istankbelow", isTankBelow);
    return super.write(tag);
  }

  @Override
  public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
    if (cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
      return fluidCap.cast();
    }
    return super.getCapability(cap, side);
  }

  @Override
  public void invalidateCaps() {
    fluidCap.invalidate();
    super.invalidateCaps();
  }

  @Override
  public void setField(int field, int value) {
  }

  @Override
  public int getField(int field) {
    return 0;
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

    //drain below but only to one of myself
    final IFluidHandler below = getTankBelow();
    if (below != null) {
      FluidUtil.tryFluidTransfer(below, tank, TRANSFER_FLUID_PER_TICK, true);
    }
  }
}
