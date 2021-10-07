package com.lothrazar.cyclic.block.trash;

import com.lothrazar.cyclic.base.FluidTankBase;
import com.lothrazar.cyclic.base.TileEntityBase;
import com.lothrazar.cyclic.registry.TileRegistry;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.TickableBlockEntity;
import net.minecraft.core.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class TileTrash extends TileEntityBase implements TickableBlockEntity {

  public static final int CAPACITY = 64 * FluidAttributes.BUCKET_VOLUME;
  ItemStackHandler inventory = new ItemStackHandler(1);
  private LazyOptional<IItemHandler> inventoryCap = LazyOptional.of(() -> inventory);
  FluidTankBase tank;
  private final LazyOptional<FluidTankBase> tankWrapper = LazyOptional.of(() -> tank);

  public TileTrash() {
    super(TileRegistry.trashtile);
    tank = new FluidTankBase(this, CAPACITY, p -> true);
  }

  @Override
  public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
    if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
      return inventoryCap.cast();
    }
    if (cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
      return tankWrapper.cast();
    }
    return super.getCapability(cap, side);
  }

  @Override
  public void load(BlockState bs, CompoundTag tag) {
    inventory.deserializeNBT(tag.getCompound(NBTINV));
    super.load(bs, tag);
  }

  @Override
  public CompoundTag save(CompoundTag tag) {
    tag.put(NBTINV, inventory.serializeNBT());
    return super.save(tag);
  }

  @Override
  public void tick() {
    inventory.extractItem(0, 64, false);
    tank.drain(CAPACITY, FluidAction.EXECUTE);
  }

  @Override
  public void setField(int field, int value) {}

  @Override
  public int getField(int field) {
    return 0;
  }
}
