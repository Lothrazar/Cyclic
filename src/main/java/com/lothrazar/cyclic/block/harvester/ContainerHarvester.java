package com.lothrazar.cyclic.block.harvester;

import com.lothrazar.cyclic.CyclicRegistry;
import com.lothrazar.cyclic.base.ContainerBase;
import com.lothrazar.cyclic.base.CustomEnergyStorage;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.IntReferenceHolder;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.wrapper.InvWrapper;

public class ContainerHarvester extends ContainerBase {

  private TileEntity tileEntity;

  public ContainerHarvester(int windowId, World world, BlockPos pos, PlayerInventory playerInventory, PlayerEntity player) {
    super(CyclicRegistry.ContainerScreens.harvester, windowId);
    tileEntity = world.getTileEntity(pos);
    this.playerEntity = player;
    this.playerInventory = new InvWrapper(playerInventory);
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
  }

  public int getEnergy() {
    return tileEntity.getCapability(CapabilityEnergy.ENERGY).map(IEnergyStorage::getEnergyStored).orElse(0);
  }

  @Override
  public boolean canInteractWith(PlayerEntity playerIn) {
    return isWithinUsableDistance(IWorldPosCallable.of(tileEntity.getWorld(), tileEntity.getPos()), playerEntity, CyclicRegistry.Blocks.harvester);
  }
}