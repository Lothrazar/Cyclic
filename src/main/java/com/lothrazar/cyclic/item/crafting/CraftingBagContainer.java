package com.lothrazar.cyclic.item.crafting;

import com.lothrazar.cyclic.base.ContainerBase;
import com.lothrazar.cyclic.data.IContainerCraftingAction;
import com.lothrazar.cyclic.registry.ContainerScreenRegistry;
import java.util.Optional;
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
import net.minecraft.network.play.server.SSetSlotPacket;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public class CraftingBagContainer extends ContainerBase implements IContainerCraftingAction {

  private final CraftingInventory craftMatrix = new CraftingInventory(this, 3, 3);
  private final CraftResultInventory craftResult = new CraftResultInventory();
  //
  public ItemStack bag;
  public int slot;
  public int slots;
  //  public CompoundNBT nbt;
  private IItemHandler handler;

  public CraftingBagContainer(int id, PlayerInventory playerInventory, PlayerEntity player) {
    super(ContainerScreenRegistry.CRAFTING_BAG, id);
    this.playerEntity = player;
    this.playerInventory = playerInventory;
    this.endInv = 10;
    //result first
    this.addSlot(new CraftingResultSlot(playerInventory.player, this.craftMatrix, this.craftResult, 0, 124, 35));
    //
    if (player.getHeldItemMainhand().getItem() instanceof CraftingBagItem) {
      this.bag = player.getHeldItemMainhand();
      this.slot = player.inventory.currentItem;
    }
    else if (player.getHeldItemOffhand().getItem() instanceof CraftingBagItem) {
      this.bag = player.getHeldItemOffhand();
      this.slot = 40;
    }
    //grid
    for (int i = 0; i < 3; i++) {
      for (int j = 0; j < 3; j++) {
        addSlot(new Slot(craftMatrix, j + i * 3, 30 + j * 18, 17 + i * 18) {

          @Override
          public boolean isItemValid(ItemStack stack) {
            return !(stack.getItem() instanceof CraftingBagItem);
          }
        });
      }
    }
    //
    //    this.nbt = bag.getOrCreateTag();
    bag.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h -> {
      this.handler = h;
      this.slots = h.getSlots();
      for (int j = 0; j < h.getSlots(); j++) {
        ItemStack inBag = h.getStackInSlot(j);
        if (!inBag.isEmpty()) {
          this.craftMatrix.setInventorySlotContents(j, h.getStackInSlot(j));
        }
      }
    });
    layoutPlayerInventorySlots(8, 84);
  }

  @Override
  public void onContainerClosed(PlayerEntity playerIn) {
    super.onContainerClosed(playerIn);
    this.craftResult.setInventorySlotContents(0, ItemStack.EMPTY);
    //this is not the saving version 
    if (!playerIn.world.isRemote) {
      if (this.handler == null) {
        this.handler = bag.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).orElse(null);
        if (this.handler == null) {
          return;
        }
      }
      for (int i = 0; i < 9; i++) {
        ItemStack crafty = this.craftMatrix.getStackInSlot(i);
        //        if (crafty.isEmpty()) {
        //          continue;
        //        }
        handler.extractItem(i, 64, false);
        ItemStack failtest = handler.insertItem(i, crafty, false);
        if (!failtest.isEmpty()) {
          //
        }
        //
        //          ItemStack doubleTest = h.extractItem(i, 64, true);
        //          ModCyclic.LOGGER.info(doubleTest + " got saved in " + i);
      }
    }
    //    clearContainer(playerIn, playerIn.world, craftMatrix);
  }

  @Override
  public void onCraftMatrixChanged(IInventory inventory) {
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

  @Override
  public ItemStack slotClick(int slotId, int dragType, ClickType clickTypeIn, PlayerEntity player) {
    if (!(slotId < 0 || slotId >= this.inventorySlots.size())) {
      ItemStack myBag = this.inventorySlots.get(slotId).getStack();
      if (myBag.getItem() instanceof CraftingBagItem) {
        //lock the bag in place by returning empty
        return ItemStack.EMPTY;
      }
    }
    return super.slotClick(slotId, dragType, clickTypeIn, player);
  }

  @Override
  public ItemStack transferStack(PlayerEntity playerIn, int index) {
    return super.transferStackInSlot(playerIn, index);
  }

  @Override
  public CraftingInventory getCraftMatrix() {
    return this.craftMatrix;
  }

  @Override
  public CraftResultInventory getCraftResult() {
    return this.craftResult;
  }
}
