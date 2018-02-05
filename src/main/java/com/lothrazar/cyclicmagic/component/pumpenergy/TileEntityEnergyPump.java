package com.lothrazar.cyclicmagic.component.pumpenergy;
import javax.annotation.Nullable;
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.block.EnergyStore;
import com.lothrazar.cyclicmagic.block.base.TileEntityBaseMachine;
import com.lothrazar.cyclicmagic.block.base.TileEntityBaseMachineFluid;
import com.lothrazar.cyclicmagic.component.cable.TileEntityBaseCable;
import com.lothrazar.cyclicmagic.component.cablefluid.TileEntityFluidCable;
import com.lothrazar.cyclicmagic.util.UtilFluid;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fluids.Fluid;

public class TileEntityEnergyPump extends TileEntityBaseMachine implements ITickable {
  private static final int TRANSFER_ENERGY_PER_TICK = 100;
  private EnergyStore pumpEnergyStore;
  public TileEntityEnergyPump() {
    super();
    pumpEnergyStore = new EnergyStore(5000);
  }
  @Override
  public void update() {
    if (this.isPowered() == false) {
      return;
    }
    EnumFacing side = this.getCurrentFacing();
    IEnergyStorage handlerHere = this.getCapability(CapabilityEnergy.ENERGY, side);
//    BlockPos posInsertInto = pos.offset(side);
//    EnumFacing facingTo = this.getCurrentFacing().getOpposite();
//    BlockPos posPullFrom = pos.offset(side.getOpposite());
    TileEntity tileInsert = world.getTileEntity(pos.offset(side.getOpposite()));
    IEnergyStorage handlerInsertInto = null;
    if (tileInsert != null) {
      handlerInsertInto = tileInsert.getCapability(CapabilityEnergy.ENERGY, side.getOpposite());

      ModCyclic.logger.error("tileInsert  "+tileInsert.getBlockType().getLocalizedName());
    }
    TileEntity tilePull = world.getTileEntity(pos.offset(side));
    IEnergyStorage handlerPullFrom = null;
    if (tilePull != null) {
      handlerPullFrom = tilePull.getCapability(CapabilityEnergy.ENERGY, side);
      ModCyclic.logger.error("tilePull   "+tilePull.getBlockType().getLocalizedName());
    }
    //first pull in power
    if (handlerPullFrom != null && handlerPullFrom.canExtract()) {
      int drain = handlerPullFrom.extractEnergy(TRANSFER_ENERGY_PER_TICK, true);
      if (drain > 0) {
        //now push it into output, but find out what was ACTUALLY taken
        int filled = handlerHere.receiveEnergy(drain, false);
        //now actually drain that much  
        handlerPullFrom.extractEnergy(filled, false);
      }
    }
    if (handlerInsertInto != null && handlerInsertInto.canReceive()) {
      int drain = handlerHere.extractEnergy(TRANSFER_ENERGY_PER_TICK, true);
      if (drain > 0) {
        //now push it into output, but find out what was ACTUALLY taken
        int filled = handlerInsertInto.receiveEnergy(drain, false);
        //now actually drain that much  
        handlerHere.extractEnergy(filled, false);
      }
    }
  }
  @Override
  public <T> T getCapability(net.minecraftforge.common.capabilities.Capability<T> capability, @javax.annotation.Nullable net.minecraft.util.EnumFacing facing) {
    if (capability == CapabilityEnergy.ENERGY) {
      return CapabilityEnergy.ENERGY.cast(this.pumpEnergyStore);
    }
    return super.getCapability(capability, facing);
  }
  @Override
  public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
    if (capability == CapabilityEnergy.ENERGY) {
      return true;
    }
    return super.hasCapability(capability, facing);
  }
}
