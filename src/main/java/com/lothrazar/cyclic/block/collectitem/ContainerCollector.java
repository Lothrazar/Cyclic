package com.lothrazar.cyclic.block.collectitem;

import com.lothrazar.cyclic.base.ContainerBase;
import com.lothrazar.cyclic.registry.BlockRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;

public class ContainerCollector extends ContainerBase {

  TileCollector tile;

  public ContainerCollector(int windowId, World world, BlockPos pos, PlayerInventory playerInventory, PlayerEntity player) {
    super(BlockRegistry.ContainerScreens.collectortileContainer, windowId);
    tile = (TileCollector) world.getTileEntity(pos);
    this.playerEntity = player;
    this.playerInventory = new InvWrapper(playerInventory);
    tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h -> {
      this.endInv = h.getSlots();
      int numRows = 2;
      for (int j = 0; j < numRows; ++j) {
        for (int k = 0; k < 9; ++k) {
          this.addSlot(new SlotItemHandler(h,
              k + j * 9,
              8 + k * 18,
              17 + (j + 1) * 18));
        }
      }
    });
    layoutPlayerInventorySlots(8, 84);
  }

  public int getNeedsRedstone() {
    return tile.getNeedsRedstone();
  }

  @Override
  public boolean canInteractWith(PlayerEntity playerIn) {
    return isWithinUsableDistance(IWorldPosCallable.of(tile.getWorld(), tile.getPos()), playerEntity, BlockRegistry.collector);
  }
}