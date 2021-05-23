package com.lothrazar.cyclic.item.datacard.filter;

import com.lothrazar.cyclic.base.ContainerBase;
import com.lothrazar.cyclic.data.Const;
import com.lothrazar.cyclic.registry.ContainerScreenRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ClickType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class ContainerFilterCard extends ContainerBase {

  public ItemStack bag;
  public int slot;
  public int slots;
  public CompoundNBT nbt;

  public ContainerFilterCard(int id, PlayerInventory playerInventory, PlayerEntity player) {
    super(ContainerScreenRegistry.filter_data, id);
    this.playerEntity = player;
    this.playerInventory = playerInventory;
    this.endInv = CapabilityProviderFilterCard.SLOTS;
    if (player.getHeldItemMainhand().getItem() instanceof FilterCardItem) {
      this.bag = player.getHeldItemMainhand();
      this.slot = player.inventory.currentItem;
    }
    else if (player.getHeldItemOffhand().getItem() instanceof FilterCardItem) {
      this.bag = player.getHeldItemOffhand();
      this.slot = 40;
    }
    //
    this.nbt = bag.getOrCreateTag();
    bag.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h -> {
      this.slots = h.getSlots();
      for (int j = 0; j < h.getSlots(); j++) {
        int row = j / 9;
        int col = j % 9;
        int xPos = 8 + col * Const.SQ;
        int yPos = 32 + row * Const.SQ;
        this.addSlot(new SlotItemHandler(h, j, xPos, yPos));
        //        ItemStack inBag = h.getStackInSlot(j);
        //        if (!inBag.isEmpty()) {
        //          h.setInventorySlotContents(j, h.getStackInSlot(j));
        //        }
      }
    });
    layoutPlayerInventorySlots(8, 84);
  }

  @Override
  public boolean canInteractWith(PlayerEntity playerIn) {
    return true;
  }

  @Override
  public ItemStack slotClick(int slotId, int dragType, ClickType clickTypeIn, PlayerEntity player) {
    if (!(slotId < 0 || slotId >= this.inventorySlots.size())) {
      ItemStack myBag = this.inventorySlots.get(slotId).getStack();
      if (myBag.getItem() instanceof FilterCardItem) {
        //lock the bag in place by returning empty
        return ItemStack.EMPTY;
      }
    }
    return super.slotClick(slotId, dragType, clickTypeIn, player);
  }
}
