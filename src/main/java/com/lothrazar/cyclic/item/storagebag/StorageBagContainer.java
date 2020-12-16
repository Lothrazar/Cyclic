package com.lothrazar.cyclic.item.storagebag;

import javax.annotation.Nonnull;
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

public class StorageBagContainer extends ContainerBase {

  public ItemStack bag;
  public int slot;
  public int slots;
  //  public CompoundNBT nbt;

  public StorageBagContainer(int i, PlayerInventory playerInventory, PlayerEntity player) {
    super(ContainerScreenRegistry.storage_bag, i);
    if (player.getHeldItemMainhand().getItem() instanceof StorageBagItem) {
      this.bag = player.getHeldItemMainhand();
      this.slot = player.inventory.currentItem;
    }
    else if (player.getHeldItemOffhand().getItem() instanceof StorageBagItem) {
      this.bag = player.getHeldItemOffhand();
      this.slot = 40;
    }
    else {
      for (int x = 0; x < playerInventory.getSizeInventory(); x++) {
        ItemStack stack = playerInventory.getStackInSlot(x);
        if (stack.getItem() instanceof StorageBagItem) {
          bag = stack;
          slot = x;
          break;
        }
      }
    }
    //    this.nbt = bag.getOrCreateTag();
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
        this.addSlot(new SlotItemHandler(h, j, xPos, yPos));
      }
    });
    layoutPlayerInventorySlots(8, 174);
  }

  @Override
  public boolean canInteractWith(PlayerEntity playerIn) {
    return true;
  }

  @Nonnull
  @Override
  public ItemStack slotClick(int slotId, int dragType, ClickType clickTypeIn, PlayerEntity player) {
    if (!(slotId < 0 || slotId >= this.inventorySlots.size())) {
      ItemStack myBag = this.inventorySlots.get(slotId).getStack();
      if (myBag.getItem() instanceof StorageBagItem) {
        // if its a normal click with a Dye item, then update stack color
        if (clickTypeIn == ClickType.PICKUP) {
          ItemStack mouseStack = player.inventory.getItemStack();
          if (mouseStack.getItem() instanceof DyeItem) {
            DyeItem dye = (DyeItem) mouseStack.getItem();
            StorageBagItem.setColour(myBag, dye.getDyeColor());
          }
        }
        //lock the bag in place by returning empty
        return ItemStack.EMPTY;
      }
    }
    return super.slotClick(slotId, dragType, clickTypeIn, player);
  }
}
