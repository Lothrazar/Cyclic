package com.lothrazar.cyclic.block.workbench;

import com.lothrazar.cyclic.block.TileBlockEntityCyclic;
import com.lothrazar.cyclic.registry.BlockRegistry;
import com.lothrazar.cyclic.registry.TileRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.ItemStackHandler;

public class TileWorkbench extends TileBlockEntityCyclic implements MenuProvider {

  protected ItemStackHandler inventory = new ItemStackHandler(9);

  @Override
  public Component getDisplayName() {
    return BlockRegistry.WORKBENCH.get().getName();
  }

  @Override
  public AbstractContainerMenu createMenu(int i, Inventory playerInventory, Player playerEntity) {
    return new ContainerWorkbench(i, level, worldPosition, playerInventory, playerEntity);
  }

  public TileWorkbench(BlockPos pos, BlockState state) {
    super(TileRegistry.WORKBENCH.get(), pos, state);
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

  @Override
  public void setField(int field, int value) {
    //
  }

  @Override
  public int getField(int field) {
    return 0;
  }
}
