package com.lothrazar.cyclic.item.lunchbox;

import com.lothrazar.cyclic.data.Const;
import com.lothrazar.cyclic.gui.ContainerBase;
import com.lothrazar.cyclic.registry.ItemRegistry;
import com.lothrazar.cyclic.registry.MenuTypeRegistry;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.SlotItemHandler;

public class ContainerLunchbox extends ContainerBase {

  public ItemStack bag = ItemStack.EMPTY;
  public int slot;
  public int slotCount;

  public ContainerLunchbox(int i, Inventory playerInventory, Player player) {
    super(MenuTypeRegistry.LUNCHBOX.get(), i);
    if (player.getMainHandItem().getItem() instanceof ItemLunchbox) {
      this.bag = player.getMainHandItem();
      this.slot = player.getInventory().selected;
    }
    else if (player.getOffhandItem().getItem() instanceof ItemLunchbox) {
      this.bag = player.getOffhandItem();
      this.slot = 40;
    }
    else {
      for (int x = 0; x < playerInventory.getContainerSize(); x++) {
        ItemStack stack = playerInventory.getItem(x);
        if (stack.getItem() instanceof ItemLunchbox) {
          bag = stack;
          slot = x;
          break;
        }
      }
    }
    this.playerEntity = player;
    this.playerInventory = playerInventory;
    bag.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(h -> {
      this.slotCount = h.getSlots();
      this.endInv = h.getSlots();
      for (int j = 0; j < h.getSlots(); j++) {
        this.addSlot(new SlotItemHandler(h, j,
            26 + j * Const.SQ,
            36));
      }
    });
    layoutPlayerInventorySlots(8, 84);
  }

  @Override
  public boolean stillValid(Player playerIn) {
    return true;
  }

  @Override
  public void clicked(int slotId, int dragType, ClickType clickTypeIn, Player player) {
    if (!(slotId < 0 || slotId >= this.slots.size())) {
      if (this.slots.get(slotId).getItem().is(ItemRegistry.LUNCHBOX.get())) {
        //lock the bag in place by returning  
        return;
      }
    }
    super.clicked(slotId, dragType, clickTypeIn, player);
  }
}
