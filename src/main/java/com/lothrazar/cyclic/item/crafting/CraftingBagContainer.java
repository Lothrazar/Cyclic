package com.lothrazar.cyclic.item.crafting;

import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.base.ContainerBase;
import com.lothrazar.cyclic.data.IContainerCraftingAction;
import com.lothrazar.cyclic.registry.ContainerScreenRegistry;
import java.util.Optional;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.ResultContainer;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.ResultSlot;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.CapabilityItemHandler;

public class CraftingBagContainer extends ContainerBase implements IContainerCraftingAction {

  private final CraftingContainer craftMatrix = new CraftingContainer(this, 3, 3);
  private final ResultContainer craftResult = new ResultContainer();
  //
  public ItemStack bag;
  public int slot;
  public int slots;
  public CompoundTag nbt;

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
      this.slot = player.getInventory().selected;
    }
    else if (player.getOffhandItem().getItem() instanceof CraftingBagItem) {
      this.bag = player.getOffhandItem();
      this.slot = 40;
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
    //
    this.nbt = bag.getOrCreateTag();
    bag.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h -> {
      this.slots = h.getSlots();
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
  public void removed(Player playerIn) {
    super.removed(playerIn);
    //this is not the saving version
    if (playerIn.level.isClientSide == false) {
      bag.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h -> {
        for (int i = 0; i < 9; i++) {
          ItemStack crafty = this.craftMatrix.getItem(i);
          if (crafty.isEmpty()) {
            continue;
          }
          h.extractItem(i, 64, false);
          ItemStack failtest = h.insertItem(i, crafty, false);
          if (!failtest.isEmpty()) {
            ModCyclic.LOGGER.info(failtest + " why did this fail; client= " + playerIn.level.isClientSide);
          }
          //
          //          ItemStack doubleTest = h.extractItem(i, 64, true);
          //          ModCyclic.LOGGER.info(doubleTest + " got saved in " + i);
        }
      });
    }
    //    clearContainer(playerIn, playerIn.world, craftMatrix);
  }

  @Override
  public void slotsChanged(Container inventory) {
    // 
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
      player.connection.send(new ClientboundContainerSetSlotPacket(containerId, 0, itemstack));
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
  public ItemStack clicked(int slotId, int dragType, ClickType clickTypeIn, Player player) {
    if (!(slotId < 0 || slotId >= this.slots.size())) {
      ItemStack myBag = this.slots.get(slotId).getItem();
      if (myBag.getItem() instanceof CraftingBagItem) {
        //lock the bag in place by returning empty
        return ItemStack.EMPTY;
      }
    }
    return super.clicked(slotId, dragType, clickTypeIn, player);
  }

  @Override
  public ItemStack transferStack(Player playerIn, int index) {
    return super.quickMoveStack(playerIn, index);
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
