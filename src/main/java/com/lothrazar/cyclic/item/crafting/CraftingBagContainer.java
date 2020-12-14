package com.lothrazar.cyclic.item.crafting;

import java.util.Optional;
import javax.annotation.Nonnull;
import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.base.ContainerBase;
import com.lothrazar.cyclic.registry.ContainerScreenRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.CraftResultInventory;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.ClickType;
import net.minecraft.inventory.container.CraftingResultSlot;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.ICraftingRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.play.server.SSetSlotPacket;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;

public class CraftingBagContainer extends ContainerBase {

  private final CraftingInventory craftMatrix = new CraftingInventory(this, 3, 3);
  private final CraftResultInventory craftResult = new CraftResultInventory();
  //
  public ItemStack bag;
  public int slot;
  public int slots;
  public CompoundNBT nbt;

  public CraftingBagContainer(int id, PlayerInventory playerInventory, PlayerEntity player) {
    super(ContainerScreenRegistry.crafting_bag, id);
    this.playerEntity = player;
    this.playerInventory = playerInventory;
    //result first
    this.addSlot(new CraftingResultSlot(playerInventory.player, this.craftMatrix, this.craftResult, 0, 124, 35));
    //
    // 
    //
    if (player.getHeldItemMainhand().getItem() instanceof CraftingBagItem) {
      this.bag = player.getHeldItemMainhand();
      this.slot = player.inventory.currentItem;
    }
    else if (player.getHeldItemOffhand().getItem() instanceof CraftingBagItem) {
      this.bag = player.getHeldItemOffhand();
      this.slot = 40;
    }
    //    else {
    //      for (int x = 0; x < playerInventory.getSizeInventory(); x++) {
    //        ItemStack stack = playerInventory.getStackInSlot(x);
    //        if (stack.getItem() instanceof CraftingBagItem) {
    //          bag = stack;
    //          slot = x;
    //          break;
    //        }
    //      }
    //    }
    for (int i = 0; i < 3; i++) {
      for (int j = 0; j < 3; j++) {
        addSlot(new Slot(craftMatrix, j + i * 3, 30 + j * 18, 17 + i * 18));
      }
    }
    //
    this.nbt = bag.getOrCreateTag();
    bag.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h -> {
      this.slots = h.getSlots();
      this.endInv = h.getSlots();
      //      for (int j = 0; j < h.getSlots(); j++) {
      //        int row = j / 3;
      //        int col = j % 3;
      //        int xPos = 8 + col * Const.SQ;
      //        int yPos = 8 + row * Const.SQ;
      //        //        this.addSlot(new SlotItemHandler(h, j, xPos, yPos));
      //        this.craftMatrix.setInventorySlotContents(j, h.getStackInSlot(j));
      //        this.addSlot(new Slot(this.craftMatrix, j, xPos, yPos) {
      //
      //          @Override
      //          public void onSlotChange(ItemStack oldStackIn, ItemStack newStackIn) {
      //            ModCyclic.LOGGER.info(oldStackIn + " slotchange matrix -> " + newStackIn);
      //            super.onSlotChange(oldStackIn, newStackIn);
      //          }
      //        });
      //      }
    });
    layoutPlayerInventorySlots(8, 84);
  }

  @Override
  public void onContainerClosed(PlayerEntity playerIn) {
    super.onContainerClosed(playerIn);
    //    clearContainer(playerIn, playerIn.world, craftMatrix);
  }

  @Override
  public void onCraftMatrixChanged(IInventory inventory) {
    ModCyclic.LOGGER.info("changed");
    // 
    World world = playerInventory.player.world;
    if (!world.isRemote) {
      ServerPlayerEntity player = (ServerPlayerEntity) playerInventory.player;
      ItemStack itemstack = ItemStack.EMPTY;
      Optional<ICraftingRecipe> optional = world.getServer().getRecipeManager().getRecipe(IRecipeType.CRAFTING, craftMatrix, world);
      if (optional.isPresent()) {
        ICraftingRecipe icraftingrecipe = optional.get();
        if (craftResult.canUseRecipe(world, player, icraftingrecipe)) {
          itemstack = icraftingrecipe.getCraftingResult(craftMatrix);
        }
      }
      craftResult.setInventorySlotContents(0, itemstack);
      player.connection.sendPacket(new SSetSlotPacket(windowId, 0, itemstack));
    }
  }

  @Override
  public boolean canMergeSlot(ItemStack itemStack, Slot slot) {
    return slot.inventory != craftResult && super.canMergeSlot(itemStack, slot);
  }

  @Override
  public boolean canInteractWith(PlayerEntity playerIn) {
    return true;
  }

  @Nonnull
  @Override
  public ItemStack slotClick(int slotId, int dragType, ClickType clickTypeIn, PlayerEntity player) {
    if (slotId < 0 || slotId >= this.inventorySlots.size()) {
      return ItemStack.EMPTY;
    }
    ItemStack myBag = this.inventorySlots.get(slotId).getStack();
    if (myBag.getItem() instanceof CraftingBagItem) {
      //lock the bag in place by returning empty
      return ItemStack.EMPTY;
    }
    return super.slotClick(slotId, dragType, clickTypeIn, player);
  }
}
