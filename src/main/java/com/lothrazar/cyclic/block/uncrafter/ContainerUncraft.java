package com.lothrazar.cyclic.block.uncrafter;

import com.lothrazar.cyclic.base.ContainerBase;
import com.lothrazar.cyclic.data.Const;
import com.lothrazar.cyclic.registry.BlockRegistry;
import com.lothrazar.cyclic.registry.ContainerScreenRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;

public class ContainerUncraft extends ContainerBase {

  protected TileUncraft tile;

  public ContainerUncraft(int windowId, Level world, BlockPos pos, Inventory playerInventory, Player player) {
    super(ContainerScreenRegistry.UNCRAFT, windowId);
    tile = (TileUncraft) world.getBlockEntity(pos);
    this.playerEntity = player;
    this.playerInventory = playerInventory;
    ItemStackHandler inputSlots = tile.inputSlots;
    ItemStackHandler outputSlots = tile.outputSlots;
    this.endInv = inputSlots.getSlots() + outputSlots.getSlots();
    //the main slot
    addSlot(new SlotItemHandler(inputSlots, 0, 39, 19));
    //the rows
    int index = 0;
    for (int j = 0; j < 2; ++j) {
      for (int k = 0; k < 8; ++k) {
        this.addSlot(new SlotItemHandler(outputSlots,
            index,
            8 + k * Const.SQ,
            27 + (j + 1) * Const.SQ));
        index++;
      }
    }
    layoutPlayerInventorySlots(8, 84);
    this.trackAllIntFields(tile, TileUncraft.Fields.values().length);
    trackEnergy(tile);
  }

  @Override
  public boolean stillValid(Player playerIn) {
    return stillValid(ContainerLevelAccess.create(tile.getLevel(), tile.getBlockPos()), playerEntity, BlockRegistry.UNCRAFTER);
  }
}
