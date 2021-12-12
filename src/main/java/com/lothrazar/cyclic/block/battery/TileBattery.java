package com.lothrazar.cyclic.block.battery;

import com.lothrazar.cyclic.base.BlockBase;
import com.lothrazar.cyclic.base.TileEntityBase;
import com.lothrazar.cyclic.block.breaker.BlockBreaker;
import com.lothrazar.cyclic.capability.CustomEnergyStorage;
import com.lothrazar.cyclic.net.PacketEnergySync;
import com.lothrazar.cyclic.registry.PacketRegistry;
import com.lothrazar.cyclic.registry.TileRegistry;
import com.lothrazar.cyclic.util.UtilDirection;
import com.lothrazar.cyclic.util.UtilEnergyStorage;
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
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.ItemStackHandler;

public class TileBattery extends TileEntityBase implements INamedContainerProvider, ITickableTileEntity {

  public static final int MAX = 6400000;
  private static final int SLOT_CHARGING_RATE = 8000;
  private final Map<Direction, Boolean> poweredSides = new HashMap<>();
  private final CustomEnergyStorage energy = new CustomEnergyStorage(MAX, MAX / 4);
  private final LazyOptional<IEnergyStorage> energyCap = LazyOptional.of(() -> energy);
  private IEnergyStorage batterySlotItemHandler = null;
  private Boolean isLit = null;
  private EnumBatteryPercent lastPercentFilled = null;
  protected final ItemStackHandler batterySlots = new ItemStackHandler(1) {
    @Override
    protected void onContentsChanged(int slot) {
      batterySlotItemHandler = getStackInSlot(slot).getCapability(CapabilityEnergy.ENERGY, null).resolve().orElse(null);
    }

    @Override
    public boolean isItemValid(int slot, final ItemStack stack) {
      return stack.getCapability(CapabilityEnergy.ENERGY, null).isPresent();
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
    for (Direction f : Direction.values()) {
      poweredSides.put(f, false);
    }
  }

  @Override
  public IEnergyStorage getEnergyStorage() {
    return energy;
  }

  @Override
  public void tick() {
    if (world == null || world.isRemote) {
      return;
    }

    //Actively export energy if enabled
    boolean isFlowing = this.getFlowing() == 1;
    if (isFlowing) {
      for (final Direction exportToSide : UtilDirection.getAllInDifferentOrder()) {
        if (poweredSides.get(exportToSide)) {
          moveEnergyToAdjacent(energy, exportToSide, MAX / 4);
        }
      }
    }

    //Charge the battery slot
    if (batterySlotItemHandler != null) {
      UtilEnergyStorage.moveEnergy(energy, batterySlotItemHandler, SLOT_CHARGING_RATE);
    }

    if (world.getGameTime() % 20 != 0) {
      return;
    }

    setLitProperty(isFlowing);

    //Update clients with new energy level
    final int currentEnergy = energy.getEnergyStored();
    if (currentEnergy != energy.energyLastSynced) {
      final PacketEnergySync packetEnergySync = new PacketEnergySync(this.getPos(), currentEnergy);
      PacketRegistry.sendToAllClients(world, packetEnergySync);
      energy.energyLastSynced = currentEnergy;
    }

    //Update the percent filled bar
    final EnumBatteryPercent percentFilled = calculateRoundedPercentFilled();
    if (percentFilled != lastPercentFilled) {
      final BlockState blockState = getBlockState();
      if (blockState.hasProperty(BlockBattery.PERCENT)) {
        //2 will send the change to clients.
        world.setBlockState(pos, blockState.with(BlockBattery.PERCENT, percentFilled), 2);
      }
      lastPercentFilled = percentFilled;
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
    poweredSides.put(side, pow == 1);
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

  public int getFlowing() {
    return flowing;
  }

  public void setFlowing(int flowing) {
    this.flowing = flowing;
  }

  @Override
  public void setLitProperty(final boolean lit) {
    if (isLit != null && isLit == lit) {
      return;
    }
    if (world == null) {
      return;
    }
    final BlockState blockState = getBlockState();
    if (!blockState.hasProperty(BlockBase.LIT)) {
      return;
    }
    final boolean previous = blockState.get(BlockBreaker.LIT);
    if (previous != lit) {
      // 1 will cause a block update.
      // 2 will send the change to clients.
      world.setBlockState(pos, blockState.with(BlockBreaker.LIT, lit), 1 | 2);
    }
    isLit = lit;
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
