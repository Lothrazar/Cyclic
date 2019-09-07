package com.lothrazar.cyclic.block.generator;

import com.lothrazar.cyclic.CyclicRegistry;
import com.lothrazar.cyclic.base.ContainerBase;
import com.lothrazar.cyclic.base.CustomEnergyStorage;
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

  private TilePeatGenerator tileEntity;

  public ContainerGenerator(int windowId, World world, BlockPos pos, PlayerInventory playerInventory, PlayerEntity player) {
    super(CyclicRegistry.generatorCont, windowId);
    tileEntity = (TilePeatGenerator) world.getTileEntity(pos);
    this.playerEntity = player;
    this.playerInventory = new InvWrapper(playerInventory);
    tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h -> {
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
        tileEntity.getCapability(CapabilityEnergy.ENERGY).ifPresent(h -> ((CustomEnergyStorage) h).setEnergy(value));
      }
    });
    trackInt(new IntReferenceHolder() {

      @Override
      public int get() {
        return getBurnTime();
      }

      @Override
      public void set(int value) {
        tileEntity.setBurnTime(value);
      }
    });
  }

  public int getEnergy() {
    return tileEntity.getCapability(CapabilityEnergy.ENERGY).map(IEnergyStorage::getEnergyStored).orElse(0);
  }

  public int getBurnTime() {
    return tileEntity.getBurnTime();
  }

  @Override
  public boolean canInteractWith(PlayerEntity playerIn) {
    return isWithinUsableDistance(IWorldPosCallable.of(tileEntity.getWorld(), tileEntity.getPos()), playerEntity, CyclicRegistry.peat_generator);
  }
}