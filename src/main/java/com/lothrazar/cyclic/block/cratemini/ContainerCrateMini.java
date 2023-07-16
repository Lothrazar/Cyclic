package com.lothrazar.cyclic.block.cratemini;

import com.lothrazar.cyclic.gui.ContainerBase;
import com.lothrazar.cyclic.registry.BlockRegistry;
import com.lothrazar.cyclic.registry.MenuTypeRegistry;
import com.lothrazar.library.core.Const;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;

public class ContainerCrateMini extends ContainerBase {

  TileCrateMini tile;

  public ContainerCrateMini(int windowId, Level world, BlockPos pos, Inventory playerInventory, Player player) {
    super(MenuTypeRegistry.CRATE_MINI.get(), windowId);
    tile = (TileCrateMini) world.getBlockEntity(pos);
    this.playerEntity = player;
    this.playerInventory = playerInventory;
    ItemStackHandler h = tile.inventory;
    this.endInv = h.getSlots();
    for (int j = 0; j < 3; ++j) {
      for (int k = 0; k < 5; ++k) {
        this.addSlot(new SlotItemHandler(h,
            k + j * 5,
            8 + (k + 2) * Const.SQ,
            16 + j * Const.SQ) {

          @Override
          public void setChanged() {
            tile.setChanged();
          }
        });
      }
    }
    layoutPlayerInventorySlots(8, 84);
  }

  @Override
  public boolean stillValid(Player playerIn) {
    return stillValid(ContainerLevelAccess.create(tile.getLevel(), tile.getBlockPos()), playerEntity, BlockRegistry.CRATE_MINI.get());
  }
}
