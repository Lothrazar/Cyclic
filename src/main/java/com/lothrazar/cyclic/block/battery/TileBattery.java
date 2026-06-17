package com.lothrazar.cyclic.block.battery;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import com.lothrazar.cyclic.block.TileBlockEntityCyclic;
import com.lothrazar.cyclic.util.CapabilityUtil;
import com.lothrazar.cyclic.registry.BlockRegistry;
import com.lothrazar.cyclic.registry.TileRegistry;
import com.lothrazar.library.util.DirectionUtil;
import com.lothrazar.library.cap.EnergyStorageWrapper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.energy.IEnergyStorage;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.IItemHandler;

public class TileBattery extends TileBlockEntityCyclic implements MenuProvider {

  public static ModConfigSpec.IntValue MAX;
  public static ModConfigSpec.IntValue SLOT_CHARGING_RATE;
  private Map<Direction, Boolean> poweredSides;
  final EnergyStorageWrapper energy;
  ItemStackHandler batterySlots = new ItemStackHandler(1) {

    @Override
    public boolean isItemValid(int slot, ItemStack stack) {
      return CapabilityUtil.energy(stack) != null;
      //return stack.getCapability(ForgeCapabilities.ENERGY, null).isPresent();
    }

    @Override
    public int getSlotLimit(int slot) {
      return 1;
    }
  };

  static enum Fields {
    FLOWING, N, E, S, W, U, D;
  }

  public TileBattery(BlockPos pos, BlockState state) {
    super(TileRegistry.BATTERY.get(), pos, state);
    this.needsRedstone = 0;
    energy = new EnergyStorageWrapper(MAX.get(), MAX.get());
    flowing = 0;
    poweredSides = new ConcurrentHashMap<Direction, Boolean>();
    for (Direction f : Direction.values()) {
      poweredSides.put(f, false);
    }
  }

  public static void serverTick(Level level, BlockPos blockPos, BlockState blockState, TileBattery e) {
    e.tick();
  }

  public static <E extends BlockEntity> void clientTick(Level level, BlockPos blockPos, BlockState blockState, TileBattery e) {
    e.tick();
  }

  public void tick() {
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
    if (level.isClientSide) {
      return;
    }
    ItemStack slotItem = this.batterySlots.getStackInSlot(0);
    IEnergyStorage itemStackStorage = CapabilityUtil.energy(slotItem); // slotItem.getCapability(ForgeCapabilities.ENERGY, null).orElse(null);
    if (itemStackStorage != null) {
      int extracted = this.energy.extractEnergy(SLOT_CHARGING_RATE.get(), true);
      int accepted = itemStackStorage.receiveEnergy(extracted, true);
      if (accepted > 0) {
        // no sim, fo real
        energy.extractEnergy(accepted, false);
        itemStackStorage.receiveEnergy(accepted, false);
      }
    }
  }

  public void setPercentFilled() {
    BlockState st = this.getBlockState();
    if (st.hasProperty(BlockBattery.PERCENT)) {
      EnumBatteryPercent previousPercent = st.getValue(BlockBattery.PERCENT);
      EnumBatteryPercent percent = calculateRoundedPercentFilled();
      if (percent != previousPercent) {
        this.level.setBlockAndUpdate(worldPosition, st.setValue(BlockBattery.PERCENT, percent));
      }
    }
  }

  public EnumBatteryPercent calculateRoundedPercentFilled() {
    int max = MAX.get();
    int percent = (int) Math.floor((this.getEnergy() * 1.0F) / max * 10.0) * 10;
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
  public void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
    for (Direction f : Direction.values()) {
      poweredSides.put(f, tag.getBoolean("flow_" + f.getName()));
    }
    if (tag.contains(NBTENERGY)) {
      energy.deserializeNBT(registries, tag.get(NBTENERGY));
    }
    batterySlots.deserializeNBT(registries,tag.getCompound(NBTINV + "batt"));
    super.loadAdditional(tag, registries);
  }

  @Override
  public void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
    for (Direction f : Direction.values()) {
      tag.putBoolean("flow_" + f.getName(), poweredSides.get(f));
    }
    tag.put(NBTINV + "batt", batterySlots.serializeNBT(registries));
    tag.putInt("flowing", getFlowing());
    tag.put(NBTENERGY, energy.serializeNBT(registries));
    super.saveAdditional(tag, registries);
  }

  @Override
  public Component getDisplayName() {
    return BlockRegistry.BATTERY.get().getName();
  }

  @Override
  public AbstractContainerMenu createMenu(int i, Inventory playerInventory, Player playerEntity) {
    return new ContainerBattery(i, level, worldPosition, playerInventory, playerEntity);
  }

  private void tickCableFlow() {
    for (final Direction exportToSide : DirectionUtil.getAllInDifferentOrder()) {
      if (this.poweredSides.get(exportToSide)) {
        moveEnergy(exportToSide, MAX.get() / 4);
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

  @Override
  public IItemHandler getItemHandler(Direction side) {
    return batterySlots;
  }


  @Override
  public IEnergyStorage getEnergyHandler(Direction side) {
    return energy;
  }

}
