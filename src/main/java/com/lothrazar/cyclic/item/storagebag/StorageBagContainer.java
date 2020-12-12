package com.lothrazar.cyclic.item.storagebag;

import com.lothrazar.cyclic.base.ContainerBase;
import com.lothrazar.cyclic.data.Const;
import com.lothrazar.cyclic.registry.ContainerScreenRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ClickType;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;

public class StorageBagContainer extends ContainerBase {

  public ItemStack bag;
  public int slot;
  public int slots;
  public CompoundNBT nbt;

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
    this.nbt = bag.getOrCreateTag();
    this.playerEntity = player;
    this.playerInventory = playerInventory;

    bag.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h -> {
      this.slots = h.getSlots();
      this.endInv = h.getSlots();
      for (int j = 0; j < h.getSlots(); j++) {
        int row = (int) j / 9;
        int col = j % 9;

        int xPos = 8 + col * Const.SQ;
        int yPos = 19 + row * Const.SQ;
        this.addSlot(new SlotItemHandler(h, j, xPos, yPos));
      }
    });
    layoutPlayerInventorySlots(8, 84);
  }

  @Override
  public boolean canInteractWith(PlayerEntity playerIn) {
    return true;
  }

  @Nonnull
  @Override
  public ItemStack slotClick(int slotId, int dragType, ClickType clickTypeIn, PlayerEntity player) {
    if (slotId >= 0 && slotId < this.inventorySlots.size() && this.inventorySlots.get(slotId).getStack().getItem() instanceof StorageBagItem)
      return ItemStack.EMPTY;
    return super.slotClick(slotId, dragType, clickTypeIn, player);
  }
}
