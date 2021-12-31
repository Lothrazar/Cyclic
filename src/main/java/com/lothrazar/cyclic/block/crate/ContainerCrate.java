package com.lothrazar.cyclic.block.crate;

import com.lothrazar.cyclic.gui.ContainerBase;
import com.lothrazar.cyclic.registry.BlockRegistry;
import com.lothrazar.cyclic.registry.ContainerScreenRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;

public class ContainerCrate extends ContainerBase {

  TileCrate tile;

  public ContainerCrate(int windowId, Level world, BlockPos pos, Inventory playerInventory, Player player) {
    super(ContainerScreenRegistry.CRATE, windowId);
    tile = (TileCrate) world.getBlockEntity(pos);
    this.playerEntity = player;
    this.playerInventory = playerInventory;
    ItemStackHandler h = tile.inventory;
    this.endInv = h.getSlots();
    for (int j = 0; j < 9; ++j) {
      for (int k = 0; k < 9; ++k) {
        this.addSlot(new SlotItemHandler(h,
            k + j * 9,
            8 + k * 18,
            8 + j * 18) {

          @Override
          public void setChanged() {
            tile.setChanged();
          }
        });
      }
    }
    layoutPlayerInventorySlots(8, 174);
  }

  @Override
  public boolean stillValid(Player playerIn) {
    return stillValid(ContainerLevelAccess.create(tile.getLevel(), tile.getBlockPos()), playerEntity, BlockRegistry.CRATE.get());
  }
}
