package com.lothrazar.cyclic.block.workbench;

import com.lothrazar.cyclic.block.TileBlockEntityCyclic;
import com.lothrazar.cyclic.registry.BlockRegistry;
import com.lothrazar.cyclic.registry.TileRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;

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
  public void loadAdditional(ValueInput input) {
    inventory.deserialize(input.childOrEmpty(NBTINV));
    super.loadAdditional(input);
  }

  @Override
  public void saveAdditional(ValueOutput output) {
    inventory.serialize(output.child(NBTINV));
    super.saveAdditional(output);
  }

  @Override
  public void setField(int field, int value) {
    //
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
