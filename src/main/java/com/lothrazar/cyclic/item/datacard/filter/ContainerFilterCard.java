package com.lothrazar.cyclic.item.datacard.filter;

import com.lothrazar.cyclic.gui.ContainerBase;
import com.lothrazar.cyclic.registry.ItemRegistry;
import com.lothrazar.cyclic.registry.MenuTypeRegistry;
import com.lothrazar.cyclic.util.CapabilityUtil;
import com.lothrazar.library.core.Const;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.item.ItemStack;

public class ContainerFilterCard extends ContainerBase {

  public ItemStack bag;
  public int slot;
  public int slotcount;

  public ContainerFilterCard(int id, Inventory playerInventory, Player player) {
    super(MenuTypeRegistry.FILTER_DATA.get(), id);
    this.playerEntity = player;
    this.playerInventory = playerInventory;
    this.endInv = 9;
    if (player.getMainHandItem().getItem() instanceof FilterCardItem) {
      this.bag = player.getMainHandItem();
      this.slot = player.getInventory().selected;
    }
    else if (player.getOffhandItem().getItem() instanceof FilterCardItem) {
      this.bag = player.getOffhandItem();
      this.slot = 40;
    }
    var h = CapabilityUtil.item(bag);
    if (h != null) {
      this.slotcount = h.getSlots();
      for (int j = 0; j < h.getSlots(); j++) {
        int row = j / 9;
        int col = j % 9;
        int xPos = 8 + col * Const.SQ;
        int yPos = 32 + row * Const.SQ;
        //ghost slot: doesn't actually hold a real item; cursor stack is never drained
        this.addSlot(new GhostFilterSlot(h, j, xPos, yPos) {

          @Override
          public boolean mayPlace(ItemStack stack) {
            //don't allow recursive filter cards
            if (stack.getItem() == ItemRegistry.FILTER_DATA.get()) {
              return false;
            }
            return super.mayPlace(stack);
          }
        });
      }
    }
    layoutPlayerInventorySlots(8, 84);
  }

  @Override
  public boolean stillValid(Player playerIn) {
    return true;
  }

  @Override
  public void clicked(int slotId, int dragType, ClickType clickTypeIn, Player player) {
    //filter slots are ghost-slots: don't go through the normal pickup/place path that would
    //consume from / give to the cursor. Just snapshot the cursor item type at count=1.
    if (slotId >= 0 && slotId < this.slotcount) {
      ItemStack carried = player.containerMenu.getCarried().copy();
      if (!carried.isEmpty()) {
        //block recursive filter cards
        if (carried.getItem() == ItemRegistry.FILTER_DATA.get()) {
          return;
        }
        carried.setCount(1);
      }
      this.slots.get(slotId).set(carried);
      return;
    }
    //lock the held card itself in place if it somehow shows up in another slot
    if (!(slotId < 0 || slotId >= this.slots.size())) {
      ItemStack myBag = this.slots.get(slotId).getItem();
      if (myBag.getItem() instanceof FilterCardItem) {
        return;
      }
    }
    super.clicked(slotId, dragType, clickTypeIn, player);
  }
}
