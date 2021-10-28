package com.lothrazar.cyclic.item.crafting;

import java.util.Optional;
import com.lothrazar.cyclic.base.ContainerBase;
import com.lothrazar.cyclic.data.IContainerCraftingAction;
import com.lothrazar.cyclic.registry.ContainerScreenRegistry;
import net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.inventory.ResultContainer;
import net.minecraft.world.inventory.ResultSlot;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public class CraftingBagContainer extends ContainerBase implements IContainerCraftingAction {

  private final CraftingContainer craftMatrix = new CraftingContainer(this, 3, 3);
  private final ResultContainer craftResult = new ResultContainer();
  //
  public ItemStack bag;
  private IItemHandler handler;
  //  public int slot;

  public CraftingBagContainer(int id, Inventory playerInventory, Player player) {
    super(ContainerScreenRegistry.CRAFTING_BAG, id);
    this.playerEntity = player;
    this.playerInventory = playerInventory;
    this.endInv = 10;
    //result first
    this.addSlot(new ResultSlot(playerInventory.player, this.craftMatrix, this.craftResult, 0, 124, 35));
    //
    if (player.getMainHandItem().getItem() instanceof CraftingBagItem) {
      this.bag = player.getMainHandItem();
      //      this.slot = player.getInventory().selected;
    }
    else if (player.getOffhandItem().getItem() instanceof CraftingBagItem) {
      this.bag = player.getOffhandItem();
      //      this.slot = 40;
    }
    //grid
    for (int i = 0; i < 3; i++) {
      for (int j = 0; j < 3; j++) {
        addSlot(new Slot(craftMatrix, j + i * 3, 30 + j * 18, 17 + i * 18) {

          @Override
          public boolean mayPlace(ItemStack stack) {
            return !(stack.getItem() instanceof CraftingBagItem);
          }
        });
      }
    }
    bag.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h -> {
      this.handler = h;
      for (int j = 0; j < h.getSlots(); j++) {
        ItemStack inBag = h.getStackInSlot(j);
        if (!inBag.isEmpty()) {
          this.craftMatrix.setItem(j, h.getStackInSlot(j));
        }
      }
    });
    layoutPlayerInventorySlots(8, 84);
  }

  @Override
  public void removed(Player playerIn) { // onContainerClosed
    super.removed(playerIn);
    this.craftResult.setItem(0, ItemStack.EMPTY);
    if (playerIn.level.isClientSide == false) {
      if (handler == null) {
        handler = bag.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).orElse(null);
      }
      for (int i = 0; i < 9; i++) {
        ItemStack crafty = this.craftMatrix.getItem(i);
        handler.extractItem(i, 64, false);
        handler.insertItem(i, crafty, false);
      }
    }
  }

  @Override
  public void slotsChanged(Container inventory) {
    Level world = playerInventory.player.level;
    if (!world.isClientSide) {
      ServerPlayer player = (ServerPlayer) playerInventory.player;
      ItemStack itemstack = ItemStack.EMPTY;
      Optional<CraftingRecipe> optional = world.getServer().getRecipeManager().getRecipeFor(RecipeType.CRAFTING, craftMatrix, world);
      if (optional.isPresent()) {
        CraftingRecipe icraftingrecipe = optional.get();
        if (craftResult.setRecipeUsed(world, player, icraftingrecipe)) {
          itemstack = icraftingrecipe.assemble(craftMatrix);
        }
      }
      craftResult.setItem(0, itemstack);
      player.connection.send(new ClientboundContainerSetSlotPacket(containerId, this.getStateId(), 0, itemstack));
    }
  }

  @Override
  public boolean canTakeItemForPickAll(ItemStack itemStack, Slot slot) {
    return slot.container != craftResult && super.canTakeItemForPickAll(itemStack, slot);
  }

  @Override
  public boolean stillValid(Player playerIn) {
    return true;
  }

  @Override
  public void clicked(int slotId, int dragType, ClickType clickTypeIn, Player player) {
    if (!(slotId < 0 || slotId >= this.slots.size())) {
      ItemStack myBag = this.slots.get(slotId).getItem();
      if (myBag.getItem() instanceof CraftingBagItem) {
        //lock the bag in place by returning empty
        return; // ItemStack.EMPTY;
      }
    }
    super.clicked(slotId, dragType, clickTypeIn, player);
  }

  @Override
  public ItemStack transferStack(Player playerIn, int index) {
    return super.quickMoveStack(playerIn, index);
  }

  @Override
  public CraftingContainer getCraftMatrix() {
    return this.craftMatrix;
  }

  @Override
  public ResultContainer getCraftResult() {
    return this.craftResult;
  }
}
