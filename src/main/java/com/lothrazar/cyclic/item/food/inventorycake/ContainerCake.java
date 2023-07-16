package com.lothrazar.cyclic.item.food.inventorycake;

import com.lothrazar.cyclic.event.PlayerDataEvents;
import com.lothrazar.cyclic.filesystem.CyclicFile;
import com.lothrazar.cyclic.gui.ContainerBase;
import com.lothrazar.cyclic.registry.MenuTypeRegistry;
import com.lothrazar.library.core.Const;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;

public class ContainerCake extends ContainerBase {

  private CyclicFile datFile;
  private ItemStackHandler mirror;

  public ContainerCake(int id, Inventory playerInventory, Player player) {
    super(MenuTypeRegistry.INVENTORY_CAKE.get(), id);
    this.playerEntity = player;
    this.playerInventory = playerInventory;
    this.datFile = PlayerDataEvents.getOrCreate(player);
    this.endInv = 3 * 9;
    //copy to this MIRROR inventory
    mirror = new ItemStackHandler(endInv);
    for (int j = 0; j < endInv; j++) {
      mirror.setStackInSlot(j, datFile.inventory.getStackInSlot(j));
      int row = j / 9;
      int col = j % 9;
      int xPos = 8 + col * Const.SQ;
      int yPos = 17 + row * Const.SQ;
      this.addSlot(new SlotItemHandler(mirror, j, xPos, yPos) {

        @Override
        public void setChanged() {
          super.setChanged();
          //sync it up with file system vers
          datFile.inventory.setStackInSlot(this.getSlotIndex(), this.getItem());
        }
      });
    }
    layoutPlayerInventorySlots(8, 84);
  }

  @Override
  public boolean stillValid(Player playerIn) {
    return true;
  }
}
