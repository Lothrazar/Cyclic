package com.lothrazar.cyclic.block.creativeitem;

import com.lothrazar.cyclic.block.TileBlockEntityCyclic;
import com.lothrazar.cyclic.registry.TileRegistry;
import com.lothrazar.library.cap.ItemStackHandlerWrapper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.ItemStackHandler;


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

  public static <E extends BlockEntity> void clientTick(Level level, BlockPos blockPos, BlockState blockState, TileItemInfinite e) {
    e.tick();
  }

  @Override
  public void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
    super.loadAdditional(tag, registries);
    inventory.deserializeNBT(registries,tag.getCompound(NBTINV));
  }

  @Override
  public void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
    tag.put(NBTINV, inventory.serializeNBT(registries));
    super.saveAdditional(tag, registries);
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
}
