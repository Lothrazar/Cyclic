package com.lothrazar.cyclic.item.craftingsimple;

import java.util.Optional;
import com.lothrazar.cyclic.base.ContainerBase;
import com.lothrazar.cyclic.data.Const;
import com.lothrazar.cyclic.data.IContainerCraftingAction;
import com.lothrazar.cyclic.registry.ContainerScreenRegistry;
import net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.inventory.ResultContainer;
import net.minecraft.world.inventory.ResultSlot;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

public class CraftingStickContainer extends ContainerBase implements IContainerCraftingAction {

  private final CraftingContainer craftMatrix = new CraftingContainer(this, 3, 3);
  final ResultContainer craftResult = new ResultContainer();
  private InteractionHand hand;

  //does NOT save inventory into the stack, very simple and plain
  public CraftingStickContainer(int id, Inventory playerInventory, Player player, InteractionHand hand) {
    super(ContainerScreenRegistry.CRAFTING_STICK, id);
    this.hand = hand;
    this.playerEntity = player;
    this.playerInventory = playerInventory;
    this.endInv = 10;
    this.addSlot(new ResultSlot(playerInventory.player, this.craftMatrix, this.craftResult, 0, 124, 35));
    for (int i = 0; i < 3; i++) {
      for (int j = 0; j < 3; j++) {
        addSlot(new Slot(craftMatrix, j + i * 3, 30 + j * Const.SQ, 17 + i * Const.SQ));
      }
    }
    layoutPlayerInventorySlots(8, 84);
  }

  @Override
  public void removed(Player playerIn) {
    super.removed(playerIn);
    //this is not the saving version
    clearContainer(playerIn, craftMatrix);
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
    return hand != null && playerIn.getItemInHand(hand).getItem() instanceof CraftingStickItem;
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
