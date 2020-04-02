package com.lothrazar.cyclic.block.generator;

import com.lothrazar.cyclic.base.ContainerBase;
import com.lothrazar.cyclic.capability.CustomEnergyStorage;
import com.lothrazar.cyclic.registry.BlockRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.IntReferenceHolder;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;

public class ContainerGenerator extends ContainerBase {

  TilePeatGenerator tile;

  public ContainerGenerator(int windowId, World world, BlockPos pos, PlayerInventory playerInventory, PlayerEntity player) {
    super(BlockRegistry.ContainerScreens.generatorCont, windowId);
    tile = (TilePeatGenerator) world.getTileEntity(pos);
    this.playerEntity = player;
    this.playerInventory = new InvWrapper(playerInventory);
    tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h -> {
      this.endInv = h.getSlots();
      addSlot(new SlotItemHandler(h, 0, 61, 21));
    });
    layoutPlayerInventorySlots(8, 84);
    trackInt(new IntReferenceHolder() {

      @Override
      public int get() {
        return getEnergy();
      }

      @Override
      public void set(int value) {
        tile.getCapability(CapabilityEnergy.ENERGY).ifPresent(h -> ((CustomEnergyStorage) h).setEnergy(value));
      }
    });
    trackInt(new IntReferenceHolder() {

      @Override
      public int get() {
        return getBurnTime();
      }

      @Override
      public void set(int value) {
        tile.setBurnTime(value);
      }
    });
    trackInt(new IntReferenceHolder() {

      @Override
      public int get() {
        return getFlowing();
      }

      @Override
      public void set(int value) {
        tile.setFlowing(value);
      }
    });
  }

  int getFlowing() {
    return tile.getFlowing();
  }

  public int getEnergy() {
    return tile.getCapability(CapabilityEnergy.ENERGY).map(IEnergyStorage::getEnergyStored).orElse(0);
  }

  public int getBurnTime() {
    return tile.getBurnTime();
  }

  @Override
  public boolean canInteractWith(PlayerEntity playerIn) {
    return isWithinUsableDistance(IWorldPosCallable.of(tile.getWorld(), tile.getPos()), playerEntity, BlockRegistry.peat_generator);
  }

  public int getNeedsRedstone() {
    return tile.getNeedsRedstone();
  }
}