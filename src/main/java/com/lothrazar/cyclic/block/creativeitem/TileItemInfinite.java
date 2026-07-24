package com.lothrazar.cyclic.block.creativeitem;

import com.lothrazar.cyclic.block.TileBlockEntityCyclic;
import com.lothrazar.cyclic.registry.TileRegistry;
import com.lothrazar.library.cap.ItemStackHandlerWrapper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.minecraft.core.Direction;
import net.neoforged.neoforge.items.IItemHandler;


public class TileItemInfinite extends TileBlockEntityCyclic {

  public TileItemInfinite(BlockPos pos, BlockState state) {
    super(TileRegistry.ITEM_INFINITE.get(), pos, state);
  }

  ItemStackHandler inputSlots = new ItemStackHandler(1);
  ItemStackHandler outputSlot = new ItemStackHandler(1);
  private ItemStackHandlerWrapper inventory = new ItemStackHandlerWrapper(inputSlots, outputSlot);

  public static void serverTick(Level level, BlockPos blockPos, BlockState blockState, TileItemInfinite e) {
    e.tick();
  }

  @Override
  public void loadAdditional(ValueInput input) {
    super.loadAdditional(input);
    inventory.deserialize(input.childOrEmpty(NBTINV));
  }

  @Override
  public void saveAdditional(ValueOutput output) {
    inventory.serialize(output.child(NBTINV));
    super.saveAdditional(output);
  }

  public void tick() {
    ItemStack stackHere = inputSlots.getStackInSlot(0);
    if (!stackHere.isEmpty()) {
      outputSlot.insertItem(0, stackHere.copy(), false);
    }
  }

  @Override
  public void setField(int field, int value) {}

  @Override
  public int getField(int field) {
    return 0;
  }

  @Override
  public IItemHandler getItemHandler(Direction side) {
    return inputSlots;
  }

}
