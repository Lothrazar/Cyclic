package com.lothrazar.cyclic.block.workbench;

import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.base.ContainerBase;
import com.lothrazar.cyclic.data.Const;
import com.lothrazar.cyclic.data.IContainerCraftingAction;
import com.lothrazar.cyclic.registry.ContainerScreenRegistry;
import java.util.Optional;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.ResultContainer;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.ResultSlot;
import net.minecraft.world.inventory.RecipeBookMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.inventory.RecipeBookType;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.CapabilityItemHandler;

public class ContainerWorkbench extends RecipeBookMenu<CraftingContainer>    implements IContainerCraftingAction {

  private TileWorkbench tile;
  public static final int GRID_START_X = 30;
  public static final int GRID_START_Y = 17;
  public static final int OUTPUT_START_X = 124;
  public static final int OUTPUT_START_Y = 35;
  public static final int GRID_NUM_ROWS = 3;
  private final CraftingContainer craftMatrix = new CraftingContainer(this, GRID_NUM_ROWS, GRID_NUM_ROWS);
  private final ResultContainer craftResult = new ResultContainer();
  private final Player player;
  private final ContainerLevelAccess worldPosCallable;
  private boolean doneOpening = false;

  public ContainerWorkbench(int windowId, Level world, BlockPos pos, Inventory playerInventory, Player player) {
    super(ContainerScreenRegistry.WORKBENCH, windowId);
    this.tile = (TileWorkbench) world.getBlockEntity(pos);
    this.player = player;
    this.worldPosCallable = ContainerLevelAccess.create(world, pos);
    this.tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, TileWorkbench.ItemHandlers.OUTPUT).ifPresent(h -> {
      this.addSlot(new ResultSlot(playerInventory.player, this.craftMatrix, this.craftResult, 0, OUTPUT_START_X, OUTPUT_START_Y));
    });
    this.tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, TileWorkbench.ItemHandlers.GRID).ifPresent(h -> {
      int index = 0;
      for (int rowPos = 0; rowPos < GRID_NUM_ROWS; rowPos++) {
        for (int colPos = 0; colPos < GRID_NUM_ROWS; colPos++) {
          this.craftMatrix.setItem(index, h.getStackInSlot(index));
          this.addSlot(new Slot(this.craftMatrix, index,
              GRID_START_X + colPos * Const.SQ,
              GRID_START_Y + rowPos * Const.SQ));
          index++;
        }
      }
    });
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
  public void fillCraftSlotsStackedContents(StackedContents itemHelperIn) {
    this.craftMatrix.fillStackedContents(itemHelperIn);
  }

  @Override
  public void clearCraftingContent() {
    this.craftMatrix.clearContent();
    this.craftResult.clearContent();
  }

  @Override
  public boolean recipeMatches(Recipe<? super CraftingContainer> recipeIn) {
    return recipeIn.matches(this.craftMatrix, this.player.level);
  }

  @Override
  public int getResultSlotIndex() {
    return 0;
  }

  @Override
  public int getGridWidth() {
    return 3;
  }

  @Override
  public int getGridHeight() {
    return 3;
  }

  @Override
  public int getSize() {
    return 10;
  }

  @Override
  public RecipeBookType getRecipeBookType() {
    return RecipeBookType.CRAFTING;
  }

  @Override
  public boolean shouldMoveToInventory(int s) {
    System.out.println("WAT is this shouldMoveToInventory"+s);
    return false;
  }

  @Override
  public void slotsChanged(Container inventoryIn) {
    if (!doneOpening) {
      return;
    }
    this.tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h -> {
      for (int i = 0; i < craftMatrix.getContainerSize(); i++) {
        h.extractItem(i, h.getSlotLimit(i), false);
        h.insertItem(i, craftMatrix.getItem(i), false);
      }
    });
    this.worldPosCallable.execute((wrld, posIn) -> {
      updateCraftingResult(this.containerId,this.getStateId(), wrld, this.player, this.craftMatrix, this.craftResult);
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
      ModCyclic.LOGGER.error(index + "Shift click error", e);
      return ItemStack.EMPTY;
    }
  }

  protected static void updateCraftingResult(int id,int stateId, Level world, Player player, CraftingContainer inventory, ResultContainer inventoryResult) {
    if (!world.isClientSide) {
      ServerPlayer sp = (ServerPlayer) player;
      ItemStack itemstack = ItemStack.EMPTY;
      Optional<CraftingRecipe> optional = world.getServer().getRecipeManager().getRecipeFor(RecipeType.CRAFTING, inventory, world);
      if (optional.isPresent()) {
        CraftingRecipe recipe = optional.get();
        if (inventoryResult.setRecipeUsed(world, sp, recipe)) {
          itemstack = recipe.assemble(inventory);
        }
      }
      inventoryResult.setItem(0, itemstack);
      sp.connection.send(new ClientboundContainerSetSlotPacket(id,stateId, 0, itemstack));
    }
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
