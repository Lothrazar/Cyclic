package com.lothrazar.cyclic.block.structurereadercreative;

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

public class ContainerReader extends ContainerBase {

  protected TileReader tile;

  public ContainerReader(int windowId, World world, BlockPos pos, PlayerInventory playerInventory, PlayerEntity player) {
    super(BlockRegistry.ContainerScreens.structure_reader, windowId);
    tile = (TileReader) world.getTileEntity(pos);
    this.playerEntity = player;
    this.playerInventory = new InvWrapper(playerInventory);
    tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h -> {
      this.endInv = h.getSlots();
      addSlot(new SlotItemHandler(h, 0, 26, 29));
      addSlot(new SlotItemHandler(h, 1, 44, 29));
      addSlot(new SlotItemHandler(h, 2, 68, 29));
    });
    layoutPlayerInventorySlots(8, 84);
  }

  @Override
  public boolean canInteractWith(PlayerEntity playerIn) {
    return isWithinUsableDistance(IWorldPosCallable.of(tile.getWorld(), tile.getPos()), playerEntity, BlockRegistry.structure_reader);
  }
}