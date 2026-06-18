package com.lothrazar.cyclic.block.cable.item;

import com.lothrazar.cyclic.block.cable.TileCableBase;
import com.lothrazar.cyclic.registry.BlockRegistry;
import com.lothrazar.cyclic.registry.TileRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class TileCableItem extends TileCableBase implements MenuProvider {

  public TileCableItem(BlockPos pos, BlockState state) {
    super(TileRegistry.ITEM_PIPE.get(), pos, state);
    setIsItem();
  }


  public static void serverTick(Level level, BlockPos blockPos, BlockState blockState, TileCableItem e) {
    e.tickItem();
  }

  @Override
  public Component getDisplayName() {
    return BlockRegistry.ITEM_PIPE.get().getName();
  }

  @Override
  public AbstractContainerMenu createMenu(int i, Inventory playerInventory, Player playerEntity) {
    return new ContainerCableItem(i, level, worldPosition, playerInventory, playerEntity);
  }

}
