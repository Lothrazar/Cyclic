package com.lothrazar.cyclic.block.wireless.energy;

import java.util.HashSet;
import java.util.Set;
import com.lothrazar.cyclic.block.TileBlockEntityCyclic;
import com.lothrazar.cyclic.config.ConfigRegistry;
import com.lothrazar.cyclic.data.PreviewOutlineType;
import com.lothrazar.cyclic.item.datacard.LocationGpsCard;
import com.lothrazar.cyclic.registry.BlockRegistry;
import com.lothrazar.cyclic.registry.TileRegistry;
import com.lothrazar.library.cap.EnergyStorageWrapper;
import com.lothrazar.library.data.BlockPosDim;
import com.lothrazar.library.util.LevelWorldUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.minecraft.core.Direction;
import net.neoforged.neoforge.energy.IEnergyStorage;
import net.neoforged.neoforge.items.IItemHandler;

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
  EnergyStorageWrapper energy = new EnergyStorageWrapper(MAX, MAX);
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
  public void loadAdditional(ValueInput input) {
    gpsSlots.deserialize(input.childOrEmpty(NBTINV));
          energy.deserialize(input.childOrEmpty(NBTENERGY));
    //    this.transferRate = input.getInt("transferRate");
    super.loadAdditional(input);
  }

  @Override
  public void saveAdditional(ValueOutput output) {
    //    output.putInt("transferRate", transferRate);
    gpsSlots.serialize(output.child(NBTINV));
    energy.serialize(output.child(NBTENERGY));
    super.saveAdditional(output);
  }

  public static void serverTick(Level level, BlockPos blockPos, BlockState blockState, TileWirelessEnergy e) {
    e.tick();
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
      if (loc == null || used.contains(loc)) {
        continue;
      }
      if (LevelWorldUtil.dimensionIsEqual(loc, level)) {
        // assume position is in the same level/dimension/world
        moved = moveEnergy(loc.getSide(), loc.getPos(), transferRate);
      }
      else if (ConfigRegistry.TRANSFER_NODES_DIMENSIONAL.get()) {
        //allows config to disable this cross dimension feature for modpack balance purposes
        moved = moveEnergyDimensional(loc, transferRate);
      }
      this.setLitProperty(moved);
    }
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
        this.render = value % PreviewOutlineType.values().length;
      break;
      case TRANSFER_RATE: // TODO: transfer rate GUI slider screen
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

  @Override
  public IItemHandler getItemHandler(Direction side) {
    return gpsSlots;
  }


  @Override
  public IEnergyStorage getEnergyHandler(Direction side) {
    return energy;
  }

}
