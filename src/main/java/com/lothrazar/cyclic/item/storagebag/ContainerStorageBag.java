package com.lothrazar.cyclic.item.storagebag;

import com.lothrazar.cyclic.data.Const;
import com.lothrazar.cyclic.gui.ContainerBase;
import com.lothrazar.cyclic.registry.MenuTypeRegistry;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class ContainerStorageBag extends ContainerBase {

  public ItemStack bag = ItemStack.EMPTY;
  public int slot;
  public int slotCount;

  public ContainerStorageBag(int i, Inventory playerInventory, Player player) {
    super(MenuTypeRegistry.STORAGE_BAG.get(), i);
    if (player.getMainHandItem().getItem() instanceof ItemStorageBag) {
      this.bag = player.getMainHandItem();
      this.slot = player.getInventory().selected;
    }
    else if (player.getOffhandItem().getItem() instanceof ItemStorageBag) {
      this.bag = player.getOffhandItem();
      this.slot = 40;
    }
    else {
      for (int x = 0; x < playerInventory.getContainerSize(); x++) {
        ItemStack stack = playerInventory.getItem(x);
        if (stack.getItem() instanceof ItemStorageBag) {
          bag = stack;
          slot = x;
          break;
        }
      }
    }
    this.playerEntity = player;
    this.playerInventory = playerInventory;
    bag.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h -> {
      this.slotCount = h.getSlots();
      this.endInv = h.getSlots();
      for (int j = 0; j < h.getSlots(); j++) {
        int row = j / 9;
        int col = j % 9;
        int xPos = 8 + col * Const.SQ;
        int yPos = 8 + row * Const.SQ;
        this.addSlot(new SlotItemHandler(h, j, xPos, yPos) {

          @Override
          public void onQuickCraft(ItemStack oldStackIn, ItemStack newStackIn) {
            ItemStorageBag.setTimestamp(bag);
            super.onQuickCraft(oldStackIn, newStackIn);
          }
        });
      }
    });
    layoutPlayerInventorySlots(8, 174);
  }

  @Override
  public boolean stillValid(Player playerIn) {
    return true;
  }

  @Override
  public void clicked(int slotId, int dragType, ClickType clickTypeIn, Player player) {
    ItemStorageBag.setTimestamp(bag);
    if (!(slotId < 0 || slotId >= this.slots.size())) {
      if (this.slots.get(slotId).getItem().getItem() instanceof ItemStorageBag) {
        // if its a normal click with a Dye item, then update stack color
        if (clickTypeIn == ClickType.PICKUP) {
          ItemStack mouseStack = player.containerMenu.getCarried();
          if (mouseStack.getItem() instanceof DyeItem) {
            DyeItem dye = (DyeItem) mouseStack.getItem();
            ItemStorageBag.setColour(slots.get(slotId).getItem(), dye.getDyeColor());
          }
        }
        //lock the bag in place by returning  
        return;
      }
    }
    super.clicked(slotId, dragType, clickTypeIn, player);
  }
}
