package com.lothrazar.cyclic.block.uncrafter;

import com.lothrazar.cyclic.base.ContainerBase;
import com.lothrazar.cyclic.data.Const;
import com.lothrazar.cyclic.registry.BlockRegistry;
import com.lothrazar.cyclic.registry.ContainerScreenRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;

public class ContainerUncraft extends ContainerBase {

  protected TileUncraft tile;

  public ContainerUncraft(int windowId, World world, BlockPos pos, PlayerInventory playerInventory, PlayerEntity player) {
    super(ContainerScreenRegistry.uncraft, windowId);
    tile = (TileUncraft) world.getTileEntity(pos);
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
  public boolean canInteractWith(PlayerEntity playerIn) {
    return isWithinUsableDistance(IWorldPosCallable.of(tile.getWorld(), tile.getPos()), playerEntity, BlockRegistry.uncrafter);
  }
}
