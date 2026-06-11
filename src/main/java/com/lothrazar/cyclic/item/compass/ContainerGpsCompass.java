package com.lothrazar.cyclic.item.compass;

import com.lothrazar.cyclic.gui.ContainerBase;
import com.lothrazar.cyclic.registry.ItemRegistry;
import com.lothrazar.cyclic.registry.MenuTypeRegistry;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.SlotItemHandler;

public class ContainerGpsCompass extends ContainerBase {

  public ItemStack bag = ItemStack.EMPTY;

  public ContainerGpsCompass(int windowId, Inventory playerInventory, Player player) {
    super(MenuTypeRegistry.GPS_COMPASS.get(), windowId);
    this.playerEntity = player;
    this.playerInventory = playerInventory;
    // find the compass in mainhand, offhand, then full inventory
    if (player.getMainHandItem().is(ItemRegistry.GPS_COMPASS.get())) {
      bag = player.getMainHandItem();
    } else if (player.getOffhandItem().is(ItemRegistry.GPS_COMPASS.get())) {
      bag = player.getOffhandItem();
    } else {
      for (int i = 0; i < playerInventory.getContainerSize(); i++) {
        ItemStack s = playerInventory.getItem(i);
        if (s.is(ItemRegistry.GPS_COMPASS.get())) {
          bag = s;
          break;
        }
      }
    }
    IItemHandler h = bag.getCapability(Capabilities.ItemHandler.ITEM);
    if (h != null) {
      this.endInv = 1;
      // single GPS card slot, centered
      this.addSlot(new SlotItemHandler(h, 0, 80, 36){
        @Override
        public int getMaxStackSize() {
          return 1;
        }
      });
    }
    layoutPlayerInventorySlots(8, 84);
  }

  @Override
  public boolean stillValid(Player player) {
    return true;
  }

  @Override
  public void clicked(int slotId, int dragType, ClickType clickType, Player player) {
    // prevent placing the compass inside itself
    if (slotId >= 0 && slotId < this.slots.size()) {
      if (this.slots.get(slotId).getItem().is(ItemRegistry.GPS_COMPASS.get())) {
        return;
      }
    }
    super.clicked(slotId, dragType, clickType, player);
  }
}
