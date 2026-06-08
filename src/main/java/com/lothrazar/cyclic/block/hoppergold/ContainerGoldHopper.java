package com.lothrazar.cyclic.block.hoppergold;

import com.lothrazar.cyclic.gui.ContainerBase;
import com.lothrazar.cyclic.registry.BlockRegistry;
import com.lothrazar.cyclic.registry.MenuTypeRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.items.SlotItemHandler;

public class ContainerGoldHopper extends ContainerBase {

  TileGoldHopper tile;

  public ContainerGoldHopper(int windowId, Level world, BlockPos pos, Inventory playerInventory, Player player) {
    super(MenuTypeRegistry.HOPPER_GOLD.get(), windowId);
    tile = (TileGoldHopper) world.getBlockEntity(pos);
    this.playerEntity = player;
    this.playerInventory = playerInventory;
    var h = tile.getFilterSlot();
    this.endInv = h.getSlots();
    addSlot(new SlotItemHandler(h, 0, 80, 24) {

      @Override
      public void setChanged() {
        tile.setChanged();
      }
    });
    layoutPlayerInventorySlots(8, 84);
  }

  @Override
  public boolean stillValid(Player playerIn) {
    return stillValid(ContainerLevelAccess.create(tile.getLevel(), tile.getBlockPos()), playerEntity, BlockRegistry.HOPPER_GOLD.get());
  }
}
