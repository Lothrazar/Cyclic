package com.lothrazar.cyclic.block.trash;

import com.lothrazar.cyclic.block.TileBlockEntityCyclic;
import com.lothrazar.cyclic.capabilities.block.FluidTankBase;
import com.lothrazar.cyclic.registry.TileRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.items.IItemHandler;
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
  public IFluidHandler getFluidHandler(Direction side) {
    return tank;
  }

  @Override
  public void loadAdditional(ValueInput input) {
    inventory.deserialize(input.childOrEmpty(NBTINV));
    super.loadAdditional(input);
  }

  @Override
  public void saveAdditional(ValueOutput output) {
    inventory.serialize(output.child(NBTINV));
    super.saveAdditional(output);
  }

  public static void serverTick(Level level, BlockPos blockPos, BlockState blockState, TileTrash e) {
    e.tick();
  }

  public void tick() {
    inventory.extractItem(0, 64, false);
    tank.drain(CAPACITY, IFluidHandler.FluidAction.EXECUTE);
  }

  @Override
  public void setField(int field, int value) {
  }

  @Override
  public int getField(int field) {
    return 0;
  }

  @Override
  public IItemHandler getItemHandler(Direction side) {
    return inventory;
  }

}
