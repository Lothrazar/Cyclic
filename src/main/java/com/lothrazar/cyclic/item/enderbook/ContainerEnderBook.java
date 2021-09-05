package com.lothrazar.cyclic.item.enderbook;

import com.lothrazar.cyclic.base.ContainerBase;
import com.lothrazar.cyclic.data.Const;
import com.lothrazar.cyclic.registry.ContainerScreenRegistry;
import com.lothrazar.cyclic.registry.ItemRegistry;
import javax.annotation.Nonnull;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ClickType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class ContainerEnderBook extends ContainerBase {

  public ItemStack bag;
  public int slot;
  public int slots;
  public CompoundNBT nbt;

  public ContainerEnderBook(int id, PlayerInventory playerInventory, PlayerEntity player) {
    super(ContainerScreenRegistry.ender_book, id);
    this.playerEntity = player;
    this.playerInventory = playerInventory;
    this.endInv = CapabilityProviderEnderBook.SLOTS;
    if (player.getHeldItemMainhand().getItem() instanceof EnderBookItem) {
      this.bag = player.getHeldItemMainhand();
      this.slot = player.inventory.currentItem;
    }
    else if (player.getHeldItemOffhand().getItem() instanceof EnderBookItem) {
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
        this.addSlot(new SlotItemHandler(h, j, xPos, yPos) {

          @Override
          public int getSlotStackLimit() {
            return 1;
          }

          @Override
          public boolean isItemValid(@Nonnull ItemStack stack) {
            return stack.getItem() == ItemRegistry.location && super.isItemValid(stack);
          }
        });
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
      if (myBag.getItem() instanceof EnderBookItem) {
        //lock the bag in place by returning empty
        return ItemStack.EMPTY;
      }
    }
    return super.slotClick(slotId, dragType, clickTypeIn, player);
  }
}
