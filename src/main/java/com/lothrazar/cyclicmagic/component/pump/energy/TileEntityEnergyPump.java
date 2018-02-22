package com.lothrazar.cyclicmagic.component.pump.energy;
import javax.annotation.Nullable;
import com.lothrazar.cyclicmagic.block.EnergyStore;
import com.lothrazar.cyclicmagic.block.base.TileEntityBaseMachineInvo;
import com.lothrazar.cyclicmagic.component.cable.TileEntityCableBase;
import com.lothrazar.cyclicmagic.component.pump.item.TileEntityItemPump.Fields;
import com.lothrazar.cyclicmagic.gui.ITileRedstoneToggle;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

public class TileEntityEnergyPump extends TileEntityBaseMachineInvo implements ITickable, ITileRedstoneToggle {
  // Thermal does 1k, 4k, 9k, 16k, 25k per tick variants
  private static final int TRANSFER_ENERGY_PER_TICK = 8 * 1000;
  public static enum Fields {
    REDSTONE;
  }
  private EnergyStore pumpEnergyStore;
  private int needsRedstone = 0;
  public TileEntityEnergyPump() {
    super(0);
    pumpEnergyStore = new EnergyStore(TRANSFER_ENERGY_PER_TICK);
  }
  @Override
  public void readFromNBT(NBTTagCompound compound) {
    super.readFromNBT(compound);
    needsRedstone = compound.getInteger(NBT_REDST);
    CapabilityEnergy.ENERGY.readNBT(pumpEnergyStore, null, compound.getTag("powercable"));
  }
  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound compound) {
    super.writeToNBT(compound);
    compound.setInteger(NBT_REDST, needsRedstone);
    compound.setTag("powercable", CapabilityEnergy.ENERGY.writeNBT(pumpEnergyStore, null));
    return compound;
  }
  @Override
  public void update() {
    if (this.isRunning() == false) {
      return;
    }
    EnumFacing side = this.getCurrentFacing();
    IEnergyStorage handlerHere = this.getCapability(CapabilityEnergy.ENERGY, side);
    TileEntity tileInsert = world.getTileEntity(pos.offset(side.getOpposite()));
    IEnergyStorage handlerInsertInto = null;
    if (tileInsert != null) {
      handlerInsertInto = tileInsert.getCapability(CapabilityEnergy.ENERGY, side.getOpposite());
      //   ModCyclic.logger.error("tileInsert  "+tileInsert.getBlockType().getLocalizedName());
    }
    TileEntity tilePull = world.getTileEntity(pos.offset(side));
    IEnergyStorage handlerPullFrom = null;
    if (tilePull != null) {
      handlerPullFrom = tilePull.getCapability(CapabilityEnergy.ENERGY, side);
      //ModCyclic.logger.error("tilePull   "+tilePull.getBlockType().getLocalizedName());
    }
    //first pull in power
    if (handlerPullFrom != null && handlerPullFrom.canExtract()) {
      int drain = handlerPullFrom.extractEnergy(TRANSFER_ENERGY_PER_TICK, true);
      if (drain > 0) {
        //now push it into output, but find out what was ACTUALLY taken
        int filled = handlerHere.receiveEnergy(drain, false);
        //now actually drain that much  
        handlerPullFrom.extractEnergy(filled, false);
        // ModCyclic.logger.error("pump take IN  " + filled + "i am holding" + this.pumpEnergyStore.getEnergyStored());
      }
    }
    if (handlerInsertInto != null && handlerInsertInto.canReceive()) {
      int drain = handlerHere.extractEnergy(TRANSFER_ENERGY_PER_TICK, true);
      if (drain > 0) {
        //now push it into output, but find out what was ACTUALLY taken
        int filled = handlerInsertInto.receiveEnergy(drain, false);
        //now actually drain that much  
        handlerHere.extractEnergy(filled, false);
        if (tileInsert instanceof TileEntityCableBase) {
          //TODO: not so compatible with other fluid systems. itl do i guess
          TileEntityCableBase cable = (TileEntityCableBase) tileInsert;
          //  ModCyclic.logger.error("pump EXPORT  " + filled);
          if (cable.isEnergyPipe()) {
            // ModCyclic.logger.error("cable receive from   "+ side);
            cable.updateIncomingEnergyFace(side); // .getOpposite()
          }
        }
      }
    }
  }
  @Override
  public int getField(int id) {
    switch (Fields.values()[id]) {
      case REDSTONE:
        return this.needsRedstone;
    }
    return 0;
  }
  @Override
  public void setField(int id, int value) {
    switch (Fields.values()[id]) {
      case REDSTONE:
        this.needsRedstone = value % 2;
      break;
    }
  }
  @Override
  public int[] getFieldOrdinals() {
    return super.getFieldArray(Fields.values().length);
  }
  @Override
  public void toggleNeedsRedstone() {
    int val = (this.needsRedstone + 1) % 2;
    this.setField(Fields.REDSTONE.ordinal(), val);
  }
  @Override
  public boolean onlyRunIfPowered() {
    return this.needsRedstone == 1;
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
    if (capability == CapabilityEnergy.ENERGY &&
        (facing == this.getCurrentFacing() || facing == this.getCurrentFacing().getOpposite())) {
      return true;
    }
    return super.hasCapability(capability, facing);
  }
}
