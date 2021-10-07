package com.lothrazar.cyclic.block.wireless.energy;

import com.lothrazar.cyclic.base.TileEntityBase;
import com.lothrazar.cyclic.capability.CustomEnergyStorage;
import com.lothrazar.cyclic.data.BlockPosDim;
import com.lothrazar.cyclic.item.datacard.LocationGpsCard;
import com.lothrazar.cyclic.registry.TileRegistry;
import com.lothrazar.cyclic.util.UtilWorld;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class TileWirelessEnergy extends TileEntityBase implements MenuProvider {

  static enum Fields {
    RENDER, TRANSFER_RATE, REDSTONE;
  }

  public TileWirelessEnergy(BlockPos pos, BlockState state) {
    super(TileRegistry.WIRELESS_ENERGY.get(),pos,state);
    this.needsRedstone = 0;
  }

  static final int MAX = 64000;
  public static final int MAX_TRANSFER = MAX;
  private int transferRate = MAX_TRANSFER / 4;
  CustomEnergyStorage energy = new CustomEnergyStorage(MAX, MAX / 4);
  private LazyOptional<IEnergyStorage> energyCap = LazyOptional.of(() -> energy);
  ItemStackHandler inventory = new ItemStackHandler(1) {

    @Override
    public boolean isItemValid(int slot, ItemStack stack) {
      return stack.getItem() instanceof LocationGpsCard;
    }
  };
  private LazyOptional<IItemHandler> inventoryCap = LazyOptional.of(() -> inventory);

  @Override
  public Component getDisplayName() {
    return new TextComponent(getType().getRegistryName().getPath());
  }

  @Override
  public AbstractContainerMenu createMenu(int i, Inventory playerInventory, Player playerEntity) {
    return new ContainerWirelessEnergy(i, level, worldPosition, playerInventory, playerEntity);
  }

  @Override
  public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
    if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
      return inventoryCap.cast();
    }
    if (cap == CapabilityEnergy.ENERGY) {
      return energyCap.cast();
    }
    return super.getCapability(cap, side);
  }

  @Override
  public void load( CompoundTag tag) {
    inventory.deserializeNBT(tag.getCompound(NBTINV));
    energy.deserializeNBT(tag.getCompound(NBTENERGY));
    this.transferRate = tag.getInt("transferRate");
    super.load(tag);
  }

  @Override
  public CompoundTag save(CompoundTag tag) {
    tag.putInt("transferRate", transferRate);
    tag.put(NBTINV, inventory.serializeNBT());
    tag.put(NBTENERGY, energy.serializeNBT());
    return super.save(tag);
  }

//  @Override
  public void tick() {
    this.syncEnergy();
    if (this.requiresRedstone() && !this.isPowered()) {
      setLitProperty(false);
      return;
    }
    if (level.isClientSide) {
      return;
    }
    boolean moved = false;
    //run the transfer. one slot only
    BlockPosDim loc = getTargetInSlot(0);
    if (loc != null && UtilWorld.dimensionIsEqual(loc, level)) {
      moved = moveEnergy(Direction.UP, loc.getPos(), transferRate);
    }
    this.setLitProperty(moved);
  }

  BlockPosDim getTargetInSlot(int s) {
    return LocationGpsCard.getPosition(inventory.getStackInSlot(s));
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
        transferRate = value;
      break;
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
        return this.transferRate;
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
