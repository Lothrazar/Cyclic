package com.lothrazar.cyclic.block.trash;

import com.lothrazar.cyclic.block.TileBlockEntityCyclic;
import com.lothrazar.cyclic.capabilities.FluidTankBase;
import com.lothrazar.cyclic.registry.TileRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class TileTrash extends TileBlockEntityCyclic {

  public static final int CAPACITY = 64 * FluidAttributes.BUCKET_VOLUME;
  ItemStackHandler inventory = new ItemStackHandler(1) {

    @Override
    public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
      return ItemStack.EMPTY;
    }
  };
  private LazyOptional<IItemHandler> inventoryCap = LazyOptional.of(() -> inventory);
  FluidTankBase tank;
  private final LazyOptional<FluidTankBase> fluidCap = LazyOptional.of(() -> tank);

  public TileTrash(BlockPos pos, BlockState state) {
    super(TileRegistry.TRASH.get(), pos, state);
    tank = new FluidTankBase(this, CAPACITY, p -> true);
  }

  @Override
  public void invalidateCaps() {
    inventoryCap.invalidate();
    fluidCap.invalidate();
    super.invalidateCaps();
  }

  @Override
  public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
    if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
      return inventoryCap.cast();
    }
    if (cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
      return fluidCap.cast();
    }
    return super.getCapability(cap, side);
  }

  @Override
  public void load(CompoundTag tag) {
    inventory.deserializeNBT(tag.getCompound(NBTINV));
    super.load(tag);
  }

  @Override
  public void saveAdditional(CompoundTag tag) {
    tag.put(NBTINV, inventory.serializeNBT());
    super.saveAdditional(tag);
  }

  public static void serverTick(Level level, BlockPos blockPos, BlockState blockState, TileTrash e) {
    e.tick();
  }

  public static <E extends BlockEntity> void clientTick(Level level, BlockPos blockPos, BlockState blockState, TileTrash e) {
    e.tick();
  }

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
