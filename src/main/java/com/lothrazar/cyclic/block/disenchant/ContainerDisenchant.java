package com.lothrazar.cyclic.block.disenchant;

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

public class ContainerDisenchant extends ContainerBase {

  protected TileDisenchant tile;

  public ContainerDisenchant(int windowId, World world, BlockPos pos, PlayerInventory playerInventory, PlayerEntity player) {
    super(BlockRegistry.ContainerScreens.disenchanter, windowId);
    tile = (TileDisenchant) world.getTileEntity(pos);
    this.playerEntity = player;
    this.playerInventory = new InvWrapper(playerInventory);
    tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h -> {
      this.endInv = h.getSlots();
      addSlot(new SlotItemHandler(h, 0, 70, 29));
      addSlot(new SlotItemHandler(h, 1, 100, 29));
      addSlot(new SlotItemHandler(h, 2, 120, 29));
    });
    layoutPlayerInventorySlots(8, 84);
    this.trackIntField(tile, TileDisenchant.Fields.REDSTONE.ordinal());
    trackInt(new IntReferenceHolder() {

      @Override
      public int get() {
        return tile.getEnergy();
      }

      @Override
      public void set(int value) {
        //        tile.getCapability(CapabilityEnergy.ENERGY).ifPresent(h -> ((CustomEnergyStorage) h).setEnergy(value));
      }
    });
  }

  @Override
  public boolean canInteractWith(PlayerEntity playerIn) {
    return isWithinUsableDistance(IWorldPosCallable.of(tile.getWorld(), tile.getPos()), playerEntity, BlockRegistry.disenchanter);
  }
}