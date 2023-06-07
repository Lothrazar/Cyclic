package com.lothrazar.cyclic.block.tp;

import com.lothrazar.cyclic.block.TileBlockEntityCyclic;
import com.lothrazar.cyclic.capabilities.block.CustomEnergyStorage;
import com.lothrazar.cyclic.data.BlockPosDim;
import com.lothrazar.cyclic.item.datacard.LocationGpsCard;
import com.lothrazar.cyclic.registry.BlockRegistry;
import com.lothrazar.cyclic.registry.TileRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class TileTeleport extends TileBlockEntityCyclic implements MenuProvider {

  static enum Fields {
    REDSTONE;
  }

  public TileTeleport(BlockPos pos, BlockState state) {
    super(TileRegistry.TELEPORT.get(), pos, state);
    this.needsRedstone = 0;
  }

  static final int MAX = 64000;
  public static final int MAX_TRANSFER = MAX;
  CustomEnergyStorage energy = new CustomEnergyStorage(MAX, MAX / 4);
  private LazyOptional<IEnergyStorage> energyCap = LazyOptional.of(() -> energy);
  ItemStackHandler gpsSlots = new ItemStackHandler(1) {

    @Override
    public boolean isItemValid(int slot, ItemStack stack) {
      return stack.getItem() instanceof LocationGpsCard;
    }

    @Override
    public int getSlotLimit(int slot) {
      return 1;
    }
  };
  private LazyOptional<IItemHandler> inventoryCap = LazyOptional.of(() -> gpsSlots);

  @Override
  public Component getDisplayName() {
    return BlockRegistry.TELEPORT.get().getName();
  }

  @Override
  public AbstractContainerMenu createMenu(int i, Inventory playerInventory, Player playerEntity) {
    return new ContainerTeleport(i, level, worldPosition, playerInventory, playerEntity);
  }

  @Override
  public void invalidateCaps() {
    inventoryCap.invalidate();
    energyCap.invalidate();
    super.invalidateCaps();
  }

  @Override
  public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
    if (cap == ForgeCapabilities.ITEM_HANDLER) {
      return inventoryCap.cast();
    }
    if (cap == ForgeCapabilities.ENERGY) {
      return energyCap.cast();
    }
    return super.getCapability(cap, side);
  }

  @Override
  public void load(CompoundTag tag) {
    gpsSlots.deserializeNBT(tag.getCompound(NBTINV));
    energy.deserializeNBT(tag.getCompound(NBTENERGY));
    super.load(tag);
  }

  @Override
  public void saveAdditional(CompoundTag tag) {
    tag.put(NBTINV, gpsSlots.serializeNBT());
    tag.put(NBTENERGY, energy.serializeNBT());
    super.saveAdditional(tag);
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
    }
  }

  @Override
  public int getField(int field) {
    switch (Fields.values()[field]) {
      case REDSTONE:
        return this.needsRedstone;
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
