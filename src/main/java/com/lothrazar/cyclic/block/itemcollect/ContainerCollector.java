package com.lothrazar.cyclic.block.itemcollect;

import com.lothrazar.cyclic.CyclicRegistry;
import com.lothrazar.cyclic.util.ContainerBase;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;

public class ContainerCollector extends ContainerBase {

  private TileEntity tileEntity;

  public ContainerCollector(int windowId, World world, BlockPos pos, PlayerInventory playerInventory, PlayerEntity player) {
    super(CyclicRegistry.collectortileContainer, windowId);
    tileEntity = world.getTileEntity(pos);
    this.playerEntity = player;
    this.playerInventory = new InvWrapper(playerInventory);
    tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h -> {
      addSlot(new SlotItemHandler(h, 0, 64, 24));
    });
    layoutPlayerInventorySlots(10, 70);
  }

  @Override
  public boolean canInteractWith(PlayerEntity playerIn) {
    return isWithinUsableDistance(IWorldPosCallable.of(tileEntity.getWorld(), tileEntity.getPos()), playerEntity, CyclicRegistry.collector);
  }
}