package com.lothrazar.cyclic.block.tankcask;

import java.util.function.Predicate;
import javax.annotation.Nonnull;
import com.lothrazar.cyclic.base.FluidTankBase;
import com.lothrazar.cyclic.base.TileEntityBase;
import com.lothrazar.cyclic.registry.BlockRegistry;
import com.lothrazar.cyclic.util.UtilFluid;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;

public class TileCask extends TileEntityBase implements ITickableTileEntity {

  public static final int CAPACITY = 8 * FluidAttributes.BUCKET_VOLUME;
  public static final int TRANSFER_FLUID_PER_TICK = CAPACITY / 2;
  public FluidTankBase tank;

  public TileCask() {
    super(BlockRegistry.Tiles.cask);
    tank = new FluidTankBase(this, CAPACITY, isFluidValid());
  }

  public Predicate<FluidStack> isFluidValid() {
    return p -> true;
  }

  @Override
  public void read(CompoundNBT tag) {
    CompoundNBT fluid = tag.getCompound("fluid");
    tank.readFromNBT(fluid);
    super.read(tag);
  }

  @Override
  public CompoundNBT write(CompoundNBT tag) {
    CompoundNBT fluid = new CompoundNBT();
    tank.writeToNBT(fluid);
    tag.put("fluid", fluid);
    return super.write(tag);
  }

  @Override
  public boolean hasFastRenderer() {
    return true;
  }

  @Override
  public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, Direction side) {
    if (cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
      return LazyOptional.of(() -> tank).cast();
    }
    return super.getCapability(cap, side);
  }

  @Override
  public void setField(int field, int value) {}

  @Override
  public void setFluid(FluidStack fluid) {
    tank.setFluid(fluid);
  }

  @Override
  public void tick() {
    //drain below but only to one of myself
    TileEntity below = this.world.getTileEntity(this.pos.down());
    if (below != null && below instanceof TileCask) {
      UtilFluid.tryFillPositionFromTank(world, this.pos.down(), Direction.UP, tank, TRANSFER_FLUID_PER_TICK);
    }
    //TESTING ONLY  
    //    if (below != null && below instanceof TileCableFluid) {
    //      UtilFluid.tryFillPositionFromTank(world, this.pos.down(), Direction.UP, tank, TRANSFER_FLUID_PER_TICK);
    //    }
  }
}
