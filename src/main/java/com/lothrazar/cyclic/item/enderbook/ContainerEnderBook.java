package com.lothrazar.cyclic.item.enderbook;

import javax.annotation.Nonnull;
import com.lothrazar.cyclic.data.Const;
import com.lothrazar.cyclic.gui.ContainerBase;
import com.lothrazar.cyclic.registry.ContainerScreenRegistry;
import com.lothrazar.cyclic.registry.ItemRegistry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class ContainerEnderBook extends ContainerBase {

  public ItemStack bag;
  public int slot;
  public int slotcount;
  public CompoundTag nbt;

  public ContainerEnderBook(int id, Inventory playerInventory, Player player) {
    super(ContainerScreenRegistry.ENDER_BOOK, id);
    this.playerEntity = player;
    this.playerInventory = playerInventory;
    this.endInv = CapabilityProviderEnderBook.SLOTS;
    if (player.getMainHandItem().getItem() instanceof EnderBookItem) {
      this.bag = player.getMainHandItem();
      this.slot = player.getInventory().selected;
    }
    else if (player.getOffhandItem().getItem() instanceof EnderBookItem) {
      this.bag = player.getOffhandItem();
      this.slot = 40;
    }
    //
    this.nbt = bag.getOrCreateTag();
    bag.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h -> {
      this.slotcount = h.getSlots();
      for (int j = 0; j < h.getSlots(); j++) {
        int row = j / 9;
        int col = j % 9;
        int xPos = 8 + col * Const.SQ;
        int yPos = 32 + row * Const.SQ;
        this.addSlot(new SlotItemHandler(h, j, xPos, yPos) {
          //
          //            @Override
          //            public void setChanged() {
          //              tile.setChanged();
          //            }

          @Override
          public int getMaxStackSize() {
            return 1;
          }

          @Override
          public boolean mayPlace(@Nonnull ItemStack stack) {
            return stack.getItem() == ItemRegistry.LOCATION_DATA.get() && super.mayPlace(stack);
          }
        });
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
      ItemStack myBag = this.slots.get(slotId).getItem();
      if (myBag.getItem() instanceof EnderBookItem) {
        //lock the bag in place by returning empty
        return; //ItemStack.EMPTY;
      }
    }
    super.clicked(slotId, dragType, clickTypeIn, player);
  }
}
