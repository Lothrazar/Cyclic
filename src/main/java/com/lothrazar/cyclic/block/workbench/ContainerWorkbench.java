package com.lothrazar.cyclic.block.workbench;

import com.lothrazar.cyclic.base.ContainerBase;
import com.lothrazar.cyclic.data.Const;
import com.lothrazar.cyclic.registry.ContainerScreenRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.CraftResultInventory;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.container.CraftingResultSlot;
import net.minecraft.inventory.container.Slot;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class ContainerWorkbench extends ContainerBase {

  private TileWorkbench tile;

  public static final int GRID_START_X = 52;
  public static final int GRID_START_Y = 20;

  public static final int OUTPUT_START_X = 124;
  public static final int OUTPUT_START_Y = 38;

  public static final int GRID_NUM_ROWS = 3;

  private final CraftingInventory craftMatrix = new CraftingInventory(this, GRID_NUM_ROWS, GRID_NUM_ROWS);
  private final CraftResultInventory craftResult = new CraftResultInventory();

  public ContainerWorkbench(int windowId, World world, BlockPos pos, PlayerInventory playerInventory, PlayerEntity player) {
    super(ContainerScreenRegistry.workbench, windowId);
    this.tile = (TileWorkbench) world.getTileEntity(pos);
    this.playerEntity = player;
    this.playerInventory = playerInventory;
    this.tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, TileWorkbench.ItemHandlers.GRID).ifPresent(h -> {
      int index = 0;
      for (int rowPos = 0; rowPos < GRID_NUM_ROWS; rowPos++) {
        for (int colPos = 0; colPos < GRID_NUM_ROWS; colPos++) {
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
    this.tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, TileWorkbench.ItemHandlers.OUTPUT).ifPresent(h -> {
          //this.addSlot(new SlotItemHandler(h, 0,
          //        OUTPUT_START_X,
          //        OUTPUT_START_Y));
      this.addSlot(new CraftingResultSlot(playerInventory.player, this.craftMatrix, this.craftResult, 0, OUTPUT_START_X, OUTPUT_START_Y));
    });
    this.endInv = inventorySlots.size();
    layoutPlayerInventorySlots(8, 84);
  }

  @Override
  public boolean canInteractWith(PlayerEntity playerIn) {
    return true;
  }
}
