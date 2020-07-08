package com.lothrazar.cyclic.block.structurewriter;

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
      addSlot(new SlotItemHandler(h, 0, 78, 18));
      addSlot(new SlotItemHandler(h, 1, 98, 18));
    });
    layoutPlayerInventorySlots(8, 84);
    this.trackIntField(tile, TileWriter.Fields.REDSTONE.ordinal());
    this.trackIntField(tile, TileWriter.Fields.STATUS.ordinal());
    this.trackIntField(tile, TileWriter.Fields.MODE.ordinal());
  }

  @Override
  public boolean canInteractWith(PlayerEntity playerIn) {
    return isWithinUsableDistance(IWorldPosCallable.of(tile.getWorld(), tile.getPos()), playerEntity, BlockRegistry.structure_writer);
  }
}