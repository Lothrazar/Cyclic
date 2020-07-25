package com.lothrazar.cyclic.block.structurewritercopy;

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

public class ContainerReaderCopy extends ContainerBase {

  protected TileReaderCopy tile;

  public ContainerReaderCopy(int windowId, World world, BlockPos pos, PlayerInventory playerInventory, PlayerEntity player) {
    super(BlockRegistry.ContainerScreens.structure_copy, windowId);
    tile = (TileReaderCopy) world.getTileEntity(pos);
    this.playerEntity = player;
    this.playerInventory = new InvWrapper(playerInventory);
    tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h -> {
      this.endInv = h.getSlots();
      addSlot(new SlotItemHandler(h, 0, 26, 29));
      addSlot(new SlotItemHandler(h, 1, 44, 29));
      addSlot(new SlotItemHandler(h, 2, 68, 29));
      for (int s = 0; s < 9; s++) {
        addSlot(new SlotItemHandler(h, s + 3, 8 + 18 * s, 49));
      }
      for (int s = 0; s < 9; s++) {
        addSlot(new SlotItemHandler(h, s + 3 + 9, 8 + 18 * s, 69));
      }
    });
    layoutPlayerInventorySlots(8, 153);
  }

  @Override
  public boolean canInteractWith(PlayerEntity playerIn) {
    return isWithinUsableDistance(IWorldPosCallable.of(tile.getWorld(), tile.getPos()), playerEntity, BlockRegistry.structure_copy);
  }
}