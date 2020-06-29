package com.lothrazar.cyclic.block.autouser;

import com.lothrazar.cyclic.base.ContainerBase;
import com.lothrazar.cyclic.registry.BlockRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.IntReferenceHolder;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;

public class ContainerUser extends ContainerBase {

  protected TileUser tile;

  public ContainerUser(int windowId, World world, BlockPos pos, PlayerInventory playerInventory, PlayerEntity player) {
    super(BlockRegistry.ContainerScreens.user, windowId);
    tile = (TileUser) world.getTileEntity(pos);
    this.playerEntity = player;
    this.playerInventory = new InvWrapper(playerInventory);
    tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h -> {
      this.endInv = h.getSlots();
      addSlot(new SlotItemHandler(h, 0, 80, 29));
    });
    layoutPlayerInventorySlots(8, 84);
    trackInt(new IntReferenceHolder() {

      @Override
      public int get() {
        return tile.getField(TileUser.Fields.REDSTONE.ordinal());
      }

      @Override
      public void set(int value) {
        tile.setField(TileUser.Fields.REDSTONE.ordinal(), value);
      }
    });
    trackInt(new IntReferenceHolder() {

      @Override
      public int get() {
        return tile.getField(TileUser.Fields.TIMERDEL.ordinal());
      }

      @Override
      public void set(int value) {
        tile.setField(TileUser.Fields.TIMERDEL.ordinal(), value);
      }
    });
  }

  public int getNeedsRedstone() {
    return tile.getNeedsRedstone();
  }

  @Override
  public boolean canInteractWith(PlayerEntity playerIn) {
    return isWithinUsableDistance(IWorldPosCallable.of(tile.getWorld(), tile.getPos()), playerEntity, BlockRegistry.user);
  }
}