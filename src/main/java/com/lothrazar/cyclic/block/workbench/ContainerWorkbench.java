package com.lothrazar.cyclic.block.workbench;

import com.lothrazar.cyclic.base.ContainerBase;
import com.lothrazar.cyclic.data.Const;
import com.lothrazar.cyclic.registry.ContainerScreenRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.CraftResultInventory;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.CraftingResultSlot;
import net.minecraft.inventory.container.RecipeBookContainer;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.*;
import net.minecraft.network.play.server.SSetSlotPacket;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import java.util.Optional;

public class ContainerWorkbench extends RecipeBookContainer<CraftingInventory> {

  private TileWorkbench tile;

  public static final int GRID_START_X = 52;
  public static final int GRID_START_Y = 20;

  public static final int OUTPUT_START_X = 124;
  public static final int OUTPUT_START_Y = 38;

  public static final int GRID_NUM_ROWS = 3;

  private final CraftingInventory craftMatrix = new CraftingInventory(this, GRID_NUM_ROWS, GRID_NUM_ROWS);
  private final CraftResultInventory craftResult = new CraftResultInventory();

  private final PlayerEntity player;
  private final IWorldPosCallable worldPosCallable;
  private boolean doneOpening = false;

  public ContainerWorkbench(int windowId, World world, BlockPos pos, PlayerInventory playerInventory, PlayerEntity player) {
    super(ContainerScreenRegistry.workbench, windowId);
    this.tile = (TileWorkbench) world.getTileEntity(pos);
    this.player = player;
    this.worldPosCallable = IWorldPosCallable.of(world, pos);
    //this.playerInventory = playerInventory;
    this.tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, TileWorkbench.ItemHandlers.OUTPUT).ifPresent(h -> {
      //this.addSlot(new SlotItemHandler(h, 0,
      //        OUTPUT_START_X,
      //        OUTPUT_START_Y));
      this.addSlot(new CraftingResultSlot(playerInventory.player, this.craftMatrix, this.craftResult, 0, OUTPUT_START_X, OUTPUT_START_Y));
    });
    this.tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, TileWorkbench.ItemHandlers.GRID).ifPresent(h -> {
      int index = 0;
      for (int rowPos = 0; rowPos < GRID_NUM_ROWS; rowPos++) {
        for (int colPos = 0; colPos < GRID_NUM_ROWS; colPos++) {
          this.craftMatrix.setInventorySlotContents(index, h.getStackInSlot(index));
          //this.addSlot(new SlotItemHandler(h, index,
          //        GRID_START_X + colPos * Const.SQ,
          //        GRID_START_Y + rowPos * Const.SQ));
          this.addSlot(new Slot(this.craftMatrix, index,
                  GRID_START_X + colPos * Const.SQ,
                  GRID_START_Y + rowPos * Const.SQ));
          index++;
        }
      }
    });
    for(int k = 0; k < 3; ++k) {
      for(int i1 = 0; i1 < 9; ++i1) {
        this.addSlot(new Slot(playerInventory, i1 + k * 9 + 9, 8 + i1 * 18, 84 + k * 18));
      }
    }

    for(int l = 0; l < 9; ++l) {
      this.addSlot(new Slot(playerInventory, l, 8 + l * 18, 142));
    }
    doneOpening = true;
  }

  @Override
  public boolean canInteractWith(PlayerEntity playerIn) {
    return true;
  }

  @Override
  public void fillStackedContents(RecipeItemHelper itemHelperIn) {
    this.craftMatrix.fillStackedContents(itemHelperIn);
  }

  @Override
  public void clear() {
    this.craftMatrix.clear();
    this.craftResult.clear();
  }

  @Override
  public boolean matches(IRecipe<? super CraftingInventory> recipeIn) {
    return recipeIn.matches(this.craftMatrix, this.player.world);
  }

  @Override
  public int getOutputSlot() {
    return 0;
  }

  @Override
  public int getWidth() {
    return 3;
  }

  @Override
  public int getHeight() {
    return 3;
  }

  @Override
  public int getSize() {
    return 10;
  }

  @Override
  public RecipeBookCategory func_241850_m() {
    return RecipeBookCategory.CRAFTING;
  }

  @Override
  public void onCraftMatrixChanged(IInventory inventoryIn) {
    if (!doneOpening)
      return;
    this.tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h -> {
      for (int i = 0; i < craftMatrix.getSizeInventory(); i++) {
        h.extractItem(i, h.getSlotLimit(i), false);
        h.insertItem(i, craftMatrix.getStackInSlot(i), false);
      }
    });
    this.worldPosCallable.consume((p_217069_1_, p_217069_2_) -> {
      updateCraftingResult(this.windowId, p_217069_1_, this.player, this.craftMatrix, this.craftResult);
    });
  }

  @Override
  public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {
    try {
      //if last machine slot is 17, endInv is 18
      int playerStart = 10;
      int playerEnd = 10 + ContainerBase.PLAYERSIZE;//53 = 17 + 36
      //player is 18 to 53
      //TILE is [0, 17]
      ItemStack itemstack = ItemStack.EMPTY;
      Slot slot = this.inventorySlots.get(index);
      if (slot != null && slot.getHasStack()) {
        ItemStack stack = slot.getStack();
        itemstack = stack.copy();
        if (index < playerStart) {
          if (!this.mergeItemStack(stack, playerStart, playerEnd, false)) {
            return ItemStack.EMPTY;
          }
        }
        else if (index <= playerEnd && !this.mergeItemStack(stack, 1, 10, false)) {
          return ItemStack.EMPTY;
        }
        if (stack.isEmpty()) {
          slot.putStack(ItemStack.EMPTY);
        }
        else {
          slot.onSlotChanged();
        }
        if (stack.getCount() == itemstack.getCount()) {
          return ItemStack.EMPTY;
        }
        slot.onTake(playerIn, stack);
      }
      return itemstack;
    }
    catch (Exception e) {
      //      ModCyclic.LOGGER.error("Shift click error", e);
      return ItemStack.EMPTY;
    }
  }

  protected static void updateCraftingResult(int id, World world, PlayerEntity player, CraftingInventory inventory, CraftResultInventory inventoryResult) {
    if (!world.isRemote) {
      ServerPlayerEntity sp = (ServerPlayerEntity)player;
      ItemStack itemstack = ItemStack.EMPTY;
      Optional<ICraftingRecipe> optional = world.getServer().getRecipeManager().getRecipe(IRecipeType.CRAFTING, inventory, world);
      if (optional.isPresent()) {
        ICraftingRecipe recipe = optional.get();
        if (inventoryResult.canUseRecipe(world, sp, recipe)) {
          itemstack = recipe.getCraftingResult(inventory);
        }
      }

      inventoryResult.setInventorySlotContents(0, itemstack);
      sp.connection.sendPacket(new SSetSlotPacket(id, 0, itemstack));
    }
  }
}
