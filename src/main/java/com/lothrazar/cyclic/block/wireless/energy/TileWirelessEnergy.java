package com.lothrazar.cyclic.block.wireless.energy;

import java.util.HashSet;
import java.util.Set;
import com.lothrazar.cyclic.block.TileBlockEntityCyclic;
import com.lothrazar.cyclic.capabilities.block.CustomEnergyStorage;
import com.lothrazar.cyclic.data.BlockPosDim;
import com.lothrazar.cyclic.item.datacard.LocationGpsCard;
import com.lothrazar.cyclic.registry.BlockRegistry;
import com.lothrazar.cyclic.registry.TileRegistry;
import com.lothrazar.cyclic.util.LevelWorldUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
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
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.ItemStackHandler;

public class TileWirelessEnergy extends TileBlockEntityCyclic implements MenuProvider {

  static enum Fields {
    RENDER, TRANSFER_RATE, REDSTONE;
  }

  public TileWirelessEnergy(BlockPos pos, BlockState state) {
    super(TileRegistry.WIRELESS_ENERGY.get(), pos, state);
    this.needsRedstone = 0;
  }

  static final int MAX = 64000;
  private int transferRate = MAX / 8;
  CustomEnergyStorage energy = new CustomEnergyStorage(MAX, MAX);
  private LazyOptional<IEnergyStorage> energyCap = LazyOptional.of(() -> energy);
  ItemStackHandler gpsSlots = new ItemStackHandler(8) {

    @Override
    public boolean isItemValid(int slot, ItemStack stack) {
      return stack.getItem() instanceof LocationGpsCard;
    }

    @Override
    public int getSlotLimit(int slot) {
      return 1;
    }
  };

  @Override
  public Component getDisplayName() {
    return BlockRegistry.WIRELESS_ENERGY.get().getName();
  }

  @Override
  public AbstractContainerMenu createMenu(int i, Inventory playerInventory, Player playerEntity) {
    return new ContainerWirelessEnergy(i, level, worldPosition, playerInventory, playerEntity);
  }

  @Override
  public void invalidateCaps() {
    energyCap.invalidate();
    super.invalidateCaps();
  }

  @Override
  public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
    if (cap == ForgeCapabilities.ENERGY) {
      return energyCap.cast();
    }
    return super.getCapability(cap, side);
  }

  @Override
  public void load(CompoundTag tag) {
    gpsSlots.deserializeNBT(tag.getCompound(NBTINV));
    energy.deserializeNBT(tag.getCompound(NBTENERGY));
    //    this.transferRate = tag.getInt("transferRate");
    super.load(tag);
  }

  @Override
  public void saveAdditional(CompoundTag tag) {
    //    tag.putInt("transferRate", transferRate);
    tag.put(NBTINV, gpsSlots.serializeNBT());
    tag.put(NBTENERGY, energy.serializeNBT());
    super.saveAdditional(tag);
  }

  public static void serverTick(Level level, BlockPos blockPos, BlockState blockState, TileWirelessEnergy e) {
    e.tick();
  }

  public static <E extends BlockEntity> void clientTick(Level level, BlockPos blockPos, BlockState blockState, TileWirelessEnergy e) {
    //   e.tick();
  }

  public void tick() {
    this.syncEnergy();
    if (this.requiresRedstone() && !this.isPowered()) {
      setLitProperty(false);
      return;
    }
    boolean moved = false;
    //run the transfer. one slot only
    Set<BlockPosDim> used = new HashSet<>();
    for (int slot = 0; slot < gpsSlots.getSlots(); slot++) {
      BlockPosDim loc = getTargetInSlot(slot);
      if (used.contains(loc)) {
        continue;
      }
      if (loc != null && LevelWorldUtil.dimensionIsEqual(loc, level)) {
        if (moveEnergy(loc.getSide(), loc.getPos(), transferRate)) {
          used.add(loc);
          moved = true;
        }
      }
    }
    this.setLitProperty(moved);
  }

  BlockPosDim getTargetInSlot(int s) {
    return LocationGpsCard.getPosition(gpsSlots.getStackInSlot(s));
  }

  @Override
  public void setField(int field, int value) {
    switch (Fields.values()[field]) {
      case REDSTONE:
        this.needsRedstone = value % 2;
      break;
      case RENDER:
        this.render = value % 2;
      break;
      case TRANSFER_RATE:
        //        transferRate = value;
        //      break;
    }
  }

  @Override
  public int getField(int field) {
    switch (Fields.values()[field]) {
      case REDSTONE:
        return this.needsRedstone;
      case RENDER:
        return render;
      case TRANSFER_RATE:
        //        return this.transferRate;
    }
    return 0;
  }

  public float getRed() {
    return 0.89F;
  }

  public float getBlue() {
    return 0;
  }

  public float getGreen() {
    return 0.12F;
  }

  public float getAlpha() {
    return 0.9F;
  }

  public float getThick() {
    return 0.065F;
  }
}
