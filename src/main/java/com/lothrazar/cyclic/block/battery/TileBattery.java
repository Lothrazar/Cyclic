package com.lothrazar.cyclic.block.battery;

import com.lothrazar.cyclic.base.TileEntityBase;
import com.lothrazar.cyclic.capability.CustomEnergyStorage;
import com.lothrazar.cyclic.registry.TileRegistry;
import com.lothrazar.cyclic.util.UtilDirection;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.ItemStackHandler;

public class TileBattery extends TileEntityBase implements INamedContainerProvider, ITickableTileEntity {

  public static final int MAX = 6400000;
  public static IntValue SLOT_CHARGING_RATE;
  private Map<Direction, Boolean> poweredSides;
  CustomEnergyStorage energy = new CustomEnergyStorage(MAX, MAX / 4);
  private LazyOptional<IEnergyStorage> energyCap = LazyOptional.of(() -> energy);
  ItemStackHandler batterySlots = new ItemStackHandler(1) {

    @Override
    public boolean isItemValid(int slot, ItemStack stack) {
      return stack.isEmpty() || stack.getCapability(CapabilityEnergy.ENERGY, null).isPresent();
    }

    @Override
    public int getSlotLimit(int slot) {
      return 1;
    }
  };

  static enum Fields {
    FLOWING, N, E, S, W, U, D;
  }

  public TileBattery() {
    super(TileRegistry.batterytile);
    flowing = 0;
    poweredSides = new HashMap<Direction, Boolean>();
    for (Direction f : Direction.values()) {
      poweredSides.put(f, false);
    }
  }

  @Override
  public void tick() {
    if (world == null || world.isRemote) {
      return;
    }
    this.syncEnergy();
    setPercentFilled();
    boolean isFlowing = this.getFlowing() == 1;
    setLitProperty(isFlowing);
    if (isFlowing) {
      this.tickCableFlow();
    }
    this.chargeSlot();
  }

  private void chargeSlot() {
    if (world.isRemote) {
      return;
    }
    ItemStack targ = this.batterySlots.getStackInSlot(0);
    IEnergyStorage storage = targ.getCapability(CapabilityEnergy.ENERGY, null).orElse(null);
    if (storage != null) {
      //
      int extracted = this.energy.extractEnergy(SLOT_CHARGING_RATE.get(), true);
      if (extracted > 0 && storage.getEnergyStored() + extracted <= storage.getMaxEnergyStored()) {
        // no sim, fo real
        energy.extractEnergy(extracted, false);
        storage.receiveEnergy(extracted, false);
      }
    }
  }

  public void setPercentFilled() {
    BlockState st = this.getBlockState();
    if (st.hasProperty(BlockBattery.PERCENT)) {
      EnumBatteryPercent previousPercent = st.get(BlockBattery.PERCENT);
      EnumBatteryPercent percent = calculateRoundedPercentFilled();
      if (percent != previousPercent) {
        this.world.setBlockState(pos, st.with(BlockBattery.PERCENT, percent));
      }
    }
  }

  public EnumBatteryPercent calculateRoundedPercentFilled() {
    int percent = (int) Math.floor((this.getEnergy() * 1.0F) / MAX * 10.0) * 10;
    //    ut.printf("%d / %d = %d percent%n", this.getEnergy(), MAX, percent);
    if (percent >= 100) {
      return EnumBatteryPercent.ONEHUNDRED;
    }
    else if (percent >= 90) {
      return EnumBatteryPercent.NINETY;
    }
    else if (percent >= 80) {
      return EnumBatteryPercent.EIGHTY;
    }
    else if (percent >= 60) {
      return EnumBatteryPercent.SIXTY;
    }
    else if (percent >= 40) {
      return EnumBatteryPercent.FOURTY;
    }
    else if (percent >= 20) {
      return EnumBatteryPercent.TWENTY;
    }
    return EnumBatteryPercent.ZERO;
  }

  public boolean getSideHasPower(Direction side) {
    return this.poweredSides.get(side);
  }

  public int getSideField(Direction side) {
    return this.getSideHasPower(side) ? 1 : 0;
  }

  public void setSideField(Direction side, int pow) {
    this.poweredSides.put(side, (pow == 1));
  }

  @Override
  public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
    if (cap == CapabilityEnergy.ENERGY) {
      return energyCap.cast();
    }
    return super.getCapability(cap, side);
  }

  @Override
  public void invalidateCaps() {
    energyCap.invalidate();
    super.invalidateCaps();
  }

  @Override
  public void read(BlockState bs, CompoundNBT tag) {
    for (Direction f : Direction.values()) {
      poweredSides.put(f, tag.getBoolean("flow_" + f.getName2()));
    }
    energy.deserializeNBT(tag.getCompound(NBTENERGY));
    batterySlots.deserializeNBT(tag.getCompound(NBTINV + "batt"));
    super.read(bs, tag);
  }

  @Override
  public CompoundNBT write(CompoundNBT tag) {
    for (Direction f : Direction.values()) {
      tag.putBoolean("flow_" + f.getName2(), poweredSides.get(f));
    }
    tag.putInt("flowing", getFlowing());
    tag.put(NBTENERGY, energy.serializeNBT());
    tag.put(NBTINV + "batt", batterySlots.serializeNBT());
    return super.write(tag);
  }

  @Override
  public ITextComponent getDisplayName() {
    return new StringTextComponent(getType().getRegistryName().getPath());
  }

  @Override
  public Container createMenu(int i, PlayerInventory playerInventory, PlayerEntity playerEntity) {
    return new ContainerBattery(i, world, pos, playerInventory, playerEntity);
  }

  private void tickCableFlow() {
    for (final Direction exportToSide : UtilDirection.getAllInDifferentOrder()) {
      if (this.poweredSides.get(exportToSide)) {
        moveEnergy(exportToSide, MAX / 4);
      }
    }
  }

  public int getFlowing() {
    return flowing;
  }

  public void setFlowing(int flowing) {
    this.flowing = flowing;
  }

  @Override
  public int getField(int id) {
    switch (Fields.values()[id]) {
      case D:
        return this.getSideField(Direction.DOWN);
      case E:
        return this.getSideField(Direction.EAST);
      case N:
        return this.getSideField(Direction.NORTH);
      case S:
        return this.getSideField(Direction.SOUTH);
      case U:
        return this.getSideField(Direction.UP);
      case W:
        return this.getSideField(Direction.WEST);
      case FLOWING:
        return flowing;
    }
    return -1;
  }

  @Override
  public void setField(int field, int value) {
    switch (Fields.values()[field]) {
      case FLOWING:
        flowing = value;
      break;
      case D:
        this.setSideField(Direction.DOWN, value % 2);
      break;
      case E:
        this.setSideField(Direction.EAST, value % 2);
      break;
      case N:
        this.setSideField(Direction.NORTH, value % 2);
      break;
      case S:
        this.setSideField(Direction.SOUTH, value % 2);
      break;
      case U:
        this.setSideField(Direction.UP, value % 2);
      break;
      case W:
        this.setSideField(Direction.WEST, value % 2);
      break;
    }
  }
}
