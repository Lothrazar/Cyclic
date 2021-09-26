package com.lothrazar.cyclic.item.craftingsimple;

import com.lothrazar.cyclic.base.ContainerBase;
import com.lothrazar.cyclic.data.Const;
import com.lothrazar.cyclic.data.IContainerCraftingAction;
import com.lothrazar.cyclic.registry.ContainerScreenRegistry;
import java.util.Optional;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.CraftResultInventory;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.CraftingResultSlot;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.ICraftingRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.network.play.server.SSetSlotPacket;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public class CraftingStickContainer extends ContainerBase implements IContainerCraftingAction {

  private final CraftingInventory craftMatrix = new CraftingInventory(this, 3, 3);
  final CraftResultInventory craftResult = new CraftResultInventory();
  private Hand hand;

  //does NOT save inventory into the stack, very simple and plain
  public CraftingStickContainer(int id, PlayerInventory playerInventory, PlayerEntity player, Hand hand) {
    super(ContainerScreenRegistry.CRAFTING_STICK, id);
    this.hand = hand;
    this.playerEntity = player;
    this.playerInventory = playerInventory;
    this.endInv = 10;
    this.addSlot(new CraftingResultSlot(playerInventory.player, this.craftMatrix, this.craftResult, 0, 124, 35));
    for (int i = 0; i < 3; i++) {
      for (int j = 0; j < 3; j++) {
        addSlot(new Slot(craftMatrix, j + i * 3, 30 + j * Const.SQ, 17 + i * Const.SQ));
      }
    }
    layoutPlayerInventorySlots(8, 84);
  }

  @Override
  public void onContainerClosed(PlayerEntity playerIn) {
    super.onContainerClosed(playerIn);
    //this is not the saving version
    clearContainer(playerIn, playerIn.world, craftMatrix);
  }

  @Override
  public void onCraftMatrixChanged(IInventory inventory) {
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
    return hand != null && playerIn.getHeldItem(hand).getItem() instanceof CraftingStickItem;
  }
  //  @Override
  //  public ItemStack slotClick(int slotId, int dragType, ClickType clickTypeIn, PlayerEntity player) {
  //    if (!(slotId < 0 || slotId >= this.inventorySlots.size())) {
  //      ItemStack myBag = this.inventorySlots.get(slotId).getStack();
  //      if (myBag.getItem() instanceof CraftingStickItem) {
  //        //lock the bag in place by returning empty
  //        return ItemStack.EMPTY;
  //      }
  //    }
  //    return super.slotClick(slotId, dragType, clickTypeIn, player);
  //  }

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
