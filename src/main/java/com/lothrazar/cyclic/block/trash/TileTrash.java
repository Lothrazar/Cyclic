package com.lothrazar.cyclic.block.trash;

import com.lothrazar.cyclic.block.TileBlockEntityCyclic;
import com.lothrazar.cyclic.capabilities.block.FluidTankBase;
import com.lothrazar.cyclic.registry.TileRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.items.ItemStackHandler;


public class TileTrash extends TileBlockEntityCyclic {

  public static final int CAPACITY = 64 * FluidType.BUCKET_VOLUME;
  ItemStackHandler inventory = new ItemStackHandler(1) {

    @Override
    public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
      return ItemStack.EMPTY;
    }
  };
  FluidTankBase tank;

  public TileTrash(BlockPos pos, BlockState state) {
    super(TileRegistry.TRASH.get(), pos, state);
    tank = new FluidTankBase(this, CAPACITY, p -> true);
  }

  @Override
  public void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
    inventory.deserializeNBT(registries,tag.getCompound(NBTINV));
    super.loadAdditional(tag,registries);
  }

  @Override
  public void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
    tag.put(NBTINV, inventory.serializeNBT(registries));
    super.saveAdditional(tag,registries);
  }

  public static void serverTick(Level level, BlockPos blockPos, BlockState blockState, TileTrash e) {
    e.tick();
  }

  public static <E extends BlockEntity> void clientTick(Level level, BlockPos blockPos, BlockState blockState, TileTrash e) {
    e.tick();
  }

  public void tick() {
    inventory.extractItem(0, 64, false);
    tank.drain(CAPACITY, IFluidHandler.FluidAction.EXECUTE);
  }

  @Override
  public void setField(int field, int value) {}

  @Override
  public int getField(int field) {
    return 0;
  }

  @Override
  public net.neoforged.neoforge.items.IItemHandler getItemHandler(net.minecraft.core.Direction side) {
    return inventory;
  }

}
