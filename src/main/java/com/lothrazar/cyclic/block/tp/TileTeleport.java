package com.lothrazar.cyclic.block.tp;

import com.lothrazar.cyclic.block.TileBlockEntityCyclic;
import com.lothrazar.cyclic.item.datacard.LocationGpsCard;
import com.lothrazar.cyclic.registry.BlockRegistry;
import com.lothrazar.cyclic.registry.TileRegistry;
import com.lothrazar.library.cap.EnergyStorageWrapper;
import com.lothrazar.library.data.BlockPosDim;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.energy.IEnergyStorage;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;

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
  EnergyStorageWrapper energy = new EnergyStorageWrapper(MAX, MAX / 4);
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

  @Override
  public Component getDisplayName() {
    return BlockRegistry.TELEPORT.get().getName();
  }

  @Override
  public AbstractContainerMenu createMenu(int i, Inventory playerInventory, Player playerEntity) {
    return new ContainerTeleport(i, level, worldPosition, playerInventory, playerEntity);
  }


  @Override
  public void loadAdditional(ValueInput input) {
    gpsSlots.deserialize(input.childOrEmpty(NBTINV));
    energy.deserialize(input.childOrEmpty(NBTENERGY));
    super.loadAdditional(input);
  }

  @Override
  public void saveAdditional(ValueOutput output) {
    gpsSlots.serialize(output.child(NBTINV));
    energy.serialize(output.child(NBTENERGY));
    super.saveAdditional(output);
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

  @Override
  public IItemHandler getItemHandler(Direction side) {
    return gpsSlots;
  }


  @Override
  public IEnergyStorage getEnergyHandler(Direction side) {
    return energy;
  }

}
