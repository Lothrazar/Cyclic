package com.lothrazar.cyclic.item.datacard.filter;

import javax.annotation.Nonnull;
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

public class ContainerFilterCard extends ContainerBase {

  public ItemStack bag;
  public int slot;
  public int slotcount;
  //  public CompoundTag nbt;

  public ContainerFilterCard(int id, Inventory playerInventory, Player player) {
    super(MenuTypeRegistry.FILTER_DATA.get(), id);
    this.playerEntity = player;
    this.playerInventory = playerInventory;
    this.endInv = CapabilityProviderFilterCard.SLOTS;
    if (player.getMainHandItem().getItem() instanceof FilterCardItem) {
      this.bag = player.getMainHandItem();
      this.slot = player.getInventory().selected;
    }
    else if (player.getOffhandItem().getItem() instanceof FilterCardItem) {
      this.bag = player.getOffhandItem();
      this.slot = 40;
    }
    //
    //    this.nbt = bag.getOrCreateTag();
    bag.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(h -> {
      this.slotcount = h.getSlots();
      for (int j = 0; j < h.getSlots(); j++) {
        int row = j / 9;
        int col = j % 9;
        int xPos = 8 + col * Const.SQ;
        int yPos = 32 + row * Const.SQ;
        this.addSlot(new SlotItemHandler(h, j, xPos, yPos) {

          @Override
          public boolean mayPlace(@Nonnull ItemStack stack) {
            if (stack.getItem() == ItemRegistry.FILTER_DATA.get()) {
              return false;
            }
            return super.mayPlace(stack);
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
      if (myBag.getItem() instanceof FilterCardItem) {
        //lock the bag in place by returning empty
        return; //ItemStack.EMPTY;
      }
    }
    super.clicked(slotId, dragType, clickTypeIn, player);
  }
}
