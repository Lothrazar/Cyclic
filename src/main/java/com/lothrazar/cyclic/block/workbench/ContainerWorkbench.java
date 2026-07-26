package com.lothrazar.cyclic.block.workbench;

import java.util.List;
import java.util.Optional;
import com.lothrazar.cyclic.data.IContainerCraftingAction;
import com.lothrazar.cyclic.gui.ContainerBase;
import com.lothrazar.cyclic.registry.MenuTypeRegistry;
import com.lothrazar.library.core.Const;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractCraftingMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.inventory.RecipeBookType;
import net.minecraft.world.inventory.ResultContainer;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.items.ItemStackHandler;

public class ContainerWorkbench extends AbstractCraftingMenu implements IContainerCraftingAction {

  private TileWorkbench tile;
  public static final int GRID_START_X = 30;
  public static final int GRID_START_Y = 17;
  public static final int OUTPUT_START_X = 124;
  public static final int OUTPUT_START_Y = 35;
  public static final int GRID_NUM_ROWS = 3;
  private final Player player;
  private final ContainerLevelAccess worldPosCallable;
  private boolean doneOpening = false;

  public ContainerWorkbench(int windowId, Level world, BlockPos pos, Inventory playerInventory, Player player) {
    super(MenuTypeRegistry.WORKBENCH.get(), windowId, GRID_NUM_ROWS, GRID_NUM_ROWS);
    this.tile = (TileWorkbench) world.getBlockEntity(pos);
    this.player = player;
    this.worldPosCallable = ContainerLevelAccess.create(world, pos);
    this.addResultSlot(playerInventory.player, OUTPUT_START_X, OUTPUT_START_Y);
    int index = 0;
    for (int rowPos = 0; rowPos < GRID_NUM_ROWS; rowPos++) {
      for (int colPos = 0; colPos < GRID_NUM_ROWS; colPos++) {
        this.craftSlots.setItem(index, tile.inventory.getStackInSlot(index));
        this.addSlot(new Slot(this.craftSlots, index,
            GRID_START_X + colPos * Const.SQ,
            GRID_START_Y + rowPos * Const.SQ));
        index++;
      }
    }
    for (int k = 0; k < 3; ++k) {
      for (int i1 = 0; i1 < 9; ++i1) {
        this.addSlot(new Slot(playerInventory, i1 + k * 9 + 9, 8 + i1 * 18, 84 + k * 18));
      }
    }
    for (int l = 0; l < 9; ++l) {
      this.addSlot(new Slot(playerInventory, l, 8 + l * 18, 142));
    }
    doneOpening = true;
    //if i have a valid recipe, and i close and re-open, reset the output slot
    slotsChanged(tile);
  }

  @Override
  public boolean stillValid(Player playerIn) {
    return true;
  }

  @Override
  public RecipeBookType getRecipeBookType() {
    return RecipeBookType.CRAFTING;
  }

  @Override
  public Slot getResultSlot() {
    return this.slots.get(0);
  }

  @Override
  public List<Slot> getInputGridSlots() {
    return this.slots.subList(1, 10);
  }

  @Override
  protected Player owner() {
    return this.player;
  }

  @Override
  public void slotsChanged(Container inventoryIn) {
    if (!doneOpening) {
      return;
    }
    ItemStackHandler inventory = tile.inventory;
    for (int i = 0; i < craftSlots.getContainerSize(); i++) {
      inventory.extractItem(i, inventory.getSlotLimit(i), false);
      inventory.insertItem(i, craftSlots.getItem(i), false);
    }
    this.worldPosCallable.execute((wrld, posIn) -> {
      if (wrld instanceof ServerLevel serverLevel) {
        updateCraftingResult(this, serverLevel, this.player, this.craftSlots, this.resultSlots);
      }
    });
  }

  @Override
  public ItemStack transferStack(Player playerIn, int index) {
    return this.quickMoveStack(playerIn, index);
  }

  @Override
  public ItemStack quickMoveStack(Player playerIn, int index) {
    try {
      //if last machine slot is 17, endInv is 18
      int playerStart = 10;
      int playerEnd = 10 + ContainerBase.PLAYERSIZE;
      //player is 18 to 53
      //TILE is [0, 17]
      ItemStack itemstack = ItemStack.EMPTY;
      Slot slot = this.slots.get(index);
      if (slot != null && slot.hasItem()) {
        ItemStack stack = slot.getItem();
        itemstack = stack.copy();
        if (index < playerStart) {
          if (!this.moveItemStackTo(stack, playerStart, playerEnd, false)) {
            return ItemStack.EMPTY;
          }
        }
        else if (index <= playerEnd && !this.moveItemStackTo(stack, 1, 10, false)) {
          return ItemStack.EMPTY;
        }
        if (stack.isEmpty()) {
          slot.set(ItemStack.EMPTY);
        }
        else {
          slot.setChanged();
        }
        if (stack.getCount() == itemstack.getCount()) {
          return ItemStack.EMPTY;
        }
        slot.onTake(playerIn, stack);
      }
      return itemstack;
    }
    catch (Exception e) {
      return ItemStack.EMPTY;
    }
  }

  protected static void updateCraftingResult(ContainerWorkbench menu, ServerLevel level, Player player, CraftingContainer inventory, ResultContainer inventoryResult) {
    ServerPlayer sp = (ServerPlayer) player;
    ItemStack itemstack = ItemStack.EMPTY;
    CraftingInput craftingInput = CraftingInput.of(3, 3, java.util.List.of(
        inventory.getItem(0), inventory.getItem(1), inventory.getItem(2),
        inventory.getItem(3), inventory.getItem(4), inventory.getItem(5),
        inventory.getItem(6), inventory.getItem(7), inventory.getItem(8)
    ));
    Optional<RecipeHolder<CraftingRecipe>> optional = level.getServer().getRecipeManager().getRecipeFor(RecipeType.CRAFTING, craftingInput, level);
    if (optional.isPresent()) {
      RecipeHolder<CraftingRecipe> recipe = optional.get();
      if (inventoryResult.setRecipeUsed(sp, recipe)) {
        itemstack = recipe.value().assemble(craftingInput);
      }
    }
    inventoryResult.setItem(0, itemstack);
    menu.setRemoteSlot(0, itemstack);
    sp.connection.send(new ClientboundContainerSetSlotPacket(menu.containerId, menu.incrementStateId(), 0, itemstack));
  }

  @Override
  public CraftingContainer getCraftMatrix() {
    return this.craftSlots;
  }

  @Override
  public ResultContainer getCraftResult() {
    return this.resultSlots;
  }
}
