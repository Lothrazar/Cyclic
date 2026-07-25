package com.lothrazar.cyclic.item.enderbook;

import javax.annotation.Nonnull;
import com.lothrazar.cyclic.util.CapabilityUtil;
import com.lothrazar.cyclic.gui.ContainerBase;
import com.lothrazar.cyclic.registry.ItemRegistry;
import com.lothrazar.cyclic.registry.MenuTypeRegistry;
import com.lothrazar.library.core.Const;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerInput;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.SlotItemHandler;

public class EnderBookContainer extends ContainerBase {

  public ItemStack bag = ItemStack.EMPTY;
  public int slot;
  public int slotcount;

  public EnderBookContainer(int id, Inventory playerInventory, Player player) {
    super(MenuTypeRegistry.ENDER_BOOK.get(), id);
    this.playerEntity = player;
    this.playerInventory = playerInventory;
    this.endInv = EnderBookCapability.SLOTS;
    if (player.getMainHandItem().getItem() instanceof EnderBookItem) {
      this.bag = player.getMainHandItem();
      this.slot = player.getInventory().getSelectedSlot();
    }
    else if (player.getOffhandItem().getItem() instanceof EnderBookItem) {
      this.bag = player.getOffhandItem();
      this.slot = 40;
    }
    IItemHandler h = CapabilityUtil.item(bag);
    if (h != null) {
      this.slotcount = h.getSlots();
      for (int j = 0; j < h.getSlots(); j++) {
        int row = j / 9;
        int col = j % 9;
        int xPos = 8 + col * Const.SQ;
        int yPos = 32 + row * Const.SQ;
        this.addSlot(new SlotItemHandler(h, j, xPos, yPos) {

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
    }
    layoutPlayerInventorySlots(8, 84);
  }

  @Override
  public boolean stillValid(Player playerIn) {
    return true;
  }

  @Override
  public void clicked(int slotId, int dragType, ContainerInput clickTypeIn, Player player) {
    if (!(slotId < 0 || slotId >= this.slots.size())) {
      ItemStack myBag = this.slots.get(slotId).getItem();
      if (myBag.getItem() instanceof EnderBookItem) {
        return;
      }
    }
    super.clicked(slotId, dragType, clickTypeIn, player);
  }
}
