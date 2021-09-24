package com.lothrazar.cyclic.item.food;

import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.base.ContainerBase;
import com.lothrazar.cyclic.data.Const;
import com.lothrazar.cyclic.data.CyclicFile;
import com.lothrazar.cyclic.event.PlayerDataEvents;
import com.lothrazar.cyclic.registry.ContainerScreenRegistry;
import javax.annotation.Nonnull;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;

public class CakeContainer extends ContainerBase {

  //  public ItemStack bag;
  //  public int slot;
  public int slots;
  public CompoundNBT nbt;
  private CyclicFile datFile;

  public CakeContainer(int id, PlayerInventory playerInventory, PlayerEntity player) {
    super(ContainerScreenRegistry.inventory_cake, id);
    this.playerEntity = player;
    this.playerInventory = playerInventory;
    //    if (player.getHeldItemMainhand().getItem() instanceof EnderBookItem) {
    //      this.bag = player.getHeldItemMainhand();
    //      this.slot = player.inventory.currentItem;
    //    }
    //    else if (player.getHeldItemOffhand().getItem() instanceof EnderBookItem) {
    //      this.bag = player.getHeldItemOffhand();
    //      this.slot = 40;
    //    }
    //
    //    this.nbt = bag.getOrCreateTag();
    //    bag.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h -> {
    this.datFile = PlayerDataEvents.getOrCreate(player);
    ItemStackHandler h = datFile.inventory;
    this.endInv = 3 * 9;// h.getSlots();
    for (int j = 0; j < endInv; j++) {
      int row = j / 9;
      int col = j % 9;
      int xPos = 8 + col * Const.SQ;
      int yPos = 17 + row * Const.SQ;
      this.addSlot(new SlotItemHandler(h, j, xPos, yPos) {

        @Override
        public void onSlotChange(@Nonnull ItemStack oldStackIn, @Nonnull ItemStack newStackIn) {
          ModCyclic.LOGGER.info("old " + oldStackIn + " -> " + newStackIn);
          System.out.println("slotchange");
          datFile.inventory.setStackInSlot(this.getSlotIndex(), newStackIn);
        }
      });
    }
    layoutPlayerInventorySlots(8, 84);
  }

  @Override
  public boolean canInteractWith(PlayerEntity playerIn) {
    return true;
  }
}
