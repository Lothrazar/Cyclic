package com.lothrazar.cyclic.block.breaker;

import com.lothrazar.cyclic.base.ContainerBase;
import com.lothrazar.cyclic.registry.BlockRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.IntReferenceHolder;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.wrapper.InvWrapper;

public class ContainerBreaker extends ContainerBase {

  protected TileBreaker tile;

  public ContainerBreaker(int windowId, World world, BlockPos pos, PlayerInventory playerInventory, PlayerEntity player) {
    super(BlockRegistry.ContainerScreens.breaker, windowId);
    tile = (TileBreaker) world.getTileEntity(pos);
    this.playerEntity = player;
    this.playerInventory = new InvWrapper(playerInventory);
    //    tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h -> {
    //      this.endInv = h.getSlots();
    //      addSlot(new SlotItemHandler(h, 0, 120, 35));
    //    });
    layoutPlayerInventorySlots(8, 84);
    trackInt(new IntReferenceHolder() {

      @Override
      public int get() {
        return tile.getField(TileBreaker.Fields.REDSTONE.ordinal());
      }

      @Override
      public void set(int value) {
        tile.setField(TileBreaker.Fields.REDSTONE.ordinal(), value);
      }
    });
  }

  @Override
  public boolean canInteractWith(PlayerEntity playerIn) {
    return isWithinUsableDistance(IWorldPosCallable.of(tile.getWorld(), tile.getPos()), playerEntity, BlockRegistry.breaker);
  }
}