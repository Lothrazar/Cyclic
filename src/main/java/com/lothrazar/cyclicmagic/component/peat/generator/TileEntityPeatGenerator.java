package com.lothrazar.cyclicmagic.component.peat.generator;
import javax.annotation.Nullable;
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.block.EnergyStore;
import com.lothrazar.cyclicmagic.block.base.TileEntityBaseMachineInvo;
import com.lothrazar.cyclicmagic.data.Const;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;

public class TileEntityPeatGenerator extends TileEntityBaseMachineInvo implements ITickable {
  public final static int TIMER_FULL = 200;
  public final static int SLOT_INPUT = 0;
  private static final int CAPACITY = 64 * 1000;
  private static final int PER_TICK = 1000;
  public static enum Fields {
    TIMER;
  }
  private EnergyStore cableEnergyStore;
  public TileEntityPeatGenerator() {
    super(1);
    this.setSlotsForExtract(SLOT_INPUT);
    cableEnergyStore = new EnergyStore(CAPACITY);
    timer = 0;
  }
  @Override
  public void update() {
    if (isRunning() == false) {
      return;
    }
    this.spawnParticlesAbove();
    // only if burning peat 
    if (timer > 0) {
      ModCyclic.logger.error("generate energy");
      this.cableEnergyStore.receiveEnergy(PER_TICK, false);
      timer--;
    }
    if (timer == 0) {
      //timer is zero. ok so it STAYS zero unless we can eat another peat brick
      // update fuel stuffs 
      ModCyclic.logger.error("decrStackSize");
      ItemStack peat = this.getStackInSlot(SLOT_INPUT);
      if (this.isValidFuel(peat)) {
        this.decrStackSize(SLOT_INPUT);
        timer = TIMER_FULL;
      }
    }
  }
  private boolean isValidFuel(ItemStack peat) {
    return peat.getItem().equals(Item.getByNameOrId(Const.MODRES + "peat_fuel"));
  }
  @Override
  public int[] getFieldOrdinals() {
    return super.getFieldArray(Fields.values().length);
  }
  @Override
  public int getFieldCount() {
    return getFieldOrdinals().length;
  }
  @Override
  public int getField(int id) {
    switch (Fields.values()[id]) {
      case TIMER:
        return timer;
    }
    return -1;
  }
  @Override
  public void setField(int id, int value) {
    switch (Fields.values()[id]) {
      case TIMER:
        timer = value;
    }
  }
  @Override
  public void readFromNBT(NBTTagCompound compound) {
    super.readFromNBT(compound);
    if (this.cableEnergyStore != null && compound.hasKey("powercable")) {
      CapabilityEnergy.ENERGY.readNBT(cableEnergyStore, null, compound.getTag("powercable"));
    }
  }
  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound compound) {
    if (cableEnergyStore != null) {
      compound.setTag("powercable", CapabilityEnergy.ENERGY.writeNBT(cableEnergyStore, null));
    }
    return super.writeToNBT(compound);
  }
  @Override
  public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
    if (capability == CapabilityEnergy.ENERGY) {
      return true;
    }
    return super.hasCapability(capability, facing);
  }
  @Override
  public <T> T getCapability(net.minecraftforge.common.capabilities.Capability<T> capability, @javax.annotation.Nullable net.minecraft.util.EnumFacing facing) {
    if (capability == CapabilityEnergy.ENERGY) {
      return CapabilityEnergy.ENERGY.cast(this.cableEnergyStore);
    }
    return super.getCapability(capability, facing);
  }
}
