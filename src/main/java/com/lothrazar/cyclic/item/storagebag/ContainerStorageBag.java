package com.lothrazar.cyclic.item.storagebag;

import com.lothrazar.cyclic.base.ContainerBase;
import com.lothrazar.cyclic.data.Const;
import com.lothrazar.cyclic.registry.ContainerScreenRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ClickType;
import net.minecraft.item.DyeItem;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class ContainerStorageBag extends ContainerBase {

  public ItemStack bag = ItemStack.EMPTY;
  public int slot;
  public int slots;

  public ContainerStorageBag(int i, PlayerInventory playerInventory, PlayerEntity player) {
    super(ContainerScreenRegistry.storage_bag, i);
    if (player.getHeldItemMainhand().getItem() instanceof ItemStorageBag) {
      this.bag = player.getHeldItemMainhand();
      this.slot = player.inventory.currentItem;
    }
    else if (player.getHeldItemOffhand().getItem() instanceof ItemStorageBag) {
      this.bag = player.getHeldItemOffhand();
      this.slot = 40;
    }
    else {
      for (int x = 0; x < playerInventory.getSizeInventory(); x++) {
        ItemStack stack = playerInventory.getStackInSlot(x);
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
      this.slots = h.getSlots();
      this.endInv = h.getSlots();
      for (int j = 0; j < h.getSlots(); j++) {
        int row = j / 9;
        int col = j % 9;
        int xPos = 8 + col * Const.SQ;
        int yPos = 8 + row * Const.SQ;
        this.addSlot(new SlotItemHandler(h, j, xPos, yPos) {

          @Override
          public void onSlotChange(ItemStack oldStackIn, ItemStack newStackIn) {
            ItemStorageBag.setTimestamp(bag);
            super.onSlotChange(oldStackIn, newStackIn);
          }
        });
      }
    });
    layoutPlayerInventorySlots(8, 174);
  }

  @Override
  public boolean canInteractWith(PlayerEntity playerIn) {
    return true;
  }

  @Override
  public ItemStack slotClick(int slotId, int dragType, ClickType clickTypeIn, PlayerEntity player) {
    ItemStorageBag.setTimestamp(bag);
    if (!(slotId < 0 || slotId >= this.inventorySlots.size())) {
      if (this.inventorySlots.get(slotId).getStack().getItem() instanceof ItemStorageBag) {
        // if its a normal click with a Dye item, then update stack color
        if (clickTypeIn == ClickType.PICKUP) {
          ItemStack mouseStack = player.inventory.getItemStack();
          if (mouseStack.getItem() instanceof DyeItem) {
            DyeItem dye = (DyeItem) mouseStack.getItem();
            ItemStorageBag.setColour(inventorySlots.get(slotId).getStack(), dye.getDyeColor());
          }
        }
        //lock the bag in place by returning empty
        return ItemStack.EMPTY;
      }
    }
    return super.slotClick(slotId, dragType, clickTypeIn, player);
  }
}
