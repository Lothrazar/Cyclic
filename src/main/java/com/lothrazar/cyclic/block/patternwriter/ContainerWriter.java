package com.lothrazar.cyclic.block.patternwriter;

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

public class ContainerWriter extends ContainerBase {

  protected TileWriter tile;

  public ContainerWriter(int windowId, World world, BlockPos pos, PlayerInventory playerInventory, PlayerEntity player) {
    super(BlockRegistry.ContainerScreens.structure_writer, windowId);
    tile = (TileWriter) world.getTileEntity(pos);
    this.playerEntity = player;
    this.playerInventory = new InvWrapper(playerInventory);
    tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h -> {
      this.endInv = h.getSlots();
      addSlot(new SlotItemHandler(h, 0, 4, 62));
      for (int s = 0; s < 9; s++) {
        addSlot(new SlotItemHandler(h, s + 1, 8 + 18 * s, 112));
      }
      for (int s = 0; s < 9; s++) {
        addSlot(new SlotItemHandler(h, s + 10, 8 + 18 * s, 112 + 18));
      }
    });
    layoutPlayerInventorySlots(8, 174 - 21);
    this.trackIntField(tile, TileWriter.Fields.REDSTONE.ordinal());
    this.trackIntField(tile, TileWriter.Fields.STATUS.ordinal());
  }

  @Override
  public boolean canInteractWith(PlayerEntity playerIn) {
    return isWithinUsableDistance(IWorldPosCallable.of(tile.getWorld(), tile.getPos()), playerEntity, BlockRegistry.structure_writer);
  }
}