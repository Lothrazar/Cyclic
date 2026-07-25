package com.lothrazar.cyclic.item.datacard.fluid;

import com.lothrazar.cyclic.gui.ContainerBase;
import com.lothrazar.cyclic.item.datacard.filter.GhostFilterSlot;
import com.lothrazar.cyclic.registry.ItemRegistry;
import com.lothrazar.cyclic.registry.MenuTypeRegistry;
import com.lothrazar.cyclic.util.CapabilityUtil;
import com.lothrazar.library.core.Const;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerInput;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidUtil;

public class ContainerFluidFilterCard extends ContainerBase {

  public ItemStack bag;
  public int slot;
  public int slotcount;

  public ContainerFluidFilterCard(int id, Inventory playerInventory, Player player) {
    super(MenuTypeRegistry.FLUID_DATA.get(), id);
    this.playerEntity = player;
    this.playerInventory = playerInventory;
    this.endInv = 9;
    if (player.getMainHandItem().getItem() instanceof FluidFilterCardItem) {
      this.bag = player.getMainHandItem();
      this.slot = player.getInventory().getSelectedSlot();
    }
    else if (player.getOffhandItem().getItem() instanceof FluidFilterCardItem) {
      this.bag = player.getOffhandItem();
      this.slot = 40;
    }
    var h = CapabilityUtil.item(bag);
    if (h != null) {
      this.slotcount = h.getSlots();
      for (int j = 0; j < h.getSlots(); j++) {
        int xPos = 8 + j * Const.SQ;
        int yPos = 32;
        this.addSlot(new GhostFilterSlot(h, j, xPos, yPos) {

          @Override
          public boolean mayPlace(ItemStack stack) {
            if (stack.getItem() == ItemRegistry.FILTER_FLUID.get()) {
              return false;
            }
            //only accept items that expose a fluid handler capability (buckets, bottles, etc.)
            return !stack.isEmpty() && FluidUtil.getFluidHandler(stack).isPresent();
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
    //ghost behavior for the bucket slot - don't consume the player's bucket
    if (slotId >= 0 && slotId < this.slotcount) {
      ItemStack carried = player.containerMenu.getCarried().copy();
      if (!carried.isEmpty()) {
        if (carried.getItem() == ItemRegistry.FILTER_FLUID.get()) {
          return;
        }
        //reject items without a fluid handler so the slot stays meaningful
        if (FluidUtil.getFluidHandler(carried).isEmpty()) {
          return;
        }
        carried.setCount(1);
      }
      this.slots.get(slotId).set(carried);
      return;
    }
    //lock the held fluid card itself in place if it shows up in another slot
    if (!(slotId < 0 || slotId >= this.slots.size())) {
      ItemStack myBag = this.slots.get(slotId).getItem();
      if (myBag.getItem() instanceof FluidFilterCardItem) {
        return;
      }
    }
    super.clicked(slotId, dragType, clickTypeIn, player);
  }
}
