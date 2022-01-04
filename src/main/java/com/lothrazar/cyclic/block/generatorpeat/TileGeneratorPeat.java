package com.lothrazar.cyclic.block.generatorpeat;

import com.lothrazar.cyclic.base.TileEntityBase;
import com.lothrazar.cyclic.capability.CustomEnergyStorage;
import com.lothrazar.cyclic.item.PeatItem;
import com.lothrazar.cyclic.registry.TileRegistry;
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
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class TileGeneratorPeat extends TileEntityBase implements ITickableTileEntity, INamedContainerProvider {

  static enum Fields {
    FLOWING, REDSTONE, RENDER, BURNTIME;
  }

  public static final int BURNTIME = 40;
  ItemStackHandler inventory = new ItemStackHandler(1);
  CustomEnergyStorage energy = new CustomEnergyStorage(MENERGY, MENERGY / 2);
  private LazyOptional<IEnergyStorage> energyCap = LazyOptional.of(() -> energy);
  private LazyOptional<IItemHandler> inventoryCap = LazyOptional.of(() -> inventory);
  private int fuelRate = 0;

  public TileGeneratorPeat() {
    super(TileRegistry.peat_generator);
    this.setNeedsRedstone(0);
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
  public void invalidateCaps() {
    energyCap.invalidate();
    inventoryCap.invalidate();
    super.invalidateCaps();
  }

  @Override
  public void read(BlockState bs, CompoundNBT tag) {
    setFlowing(tag.getInt("flowing"));
    fuelRate = tag.getInt("fuelRate");
    energy.deserializeNBT(tag.getCompound(NBTENERGY));
    inventory.deserializeNBT(tag.getCompound(NBTINV));
    super.read(bs, tag);
  }

  @Override
  public CompoundNBT write(CompoundNBT tag) {
    tag.putInt("flowing", getFlowing());
    tag.putInt("fuelRate", fuelRate);
    tag.put(NBTENERGY, energy.serializeNBT());
    tag.put(NBTINV, inventory.serializeNBT());
    return super.write(tag);
  }

  private boolean isBurning() {
    return this.timer > 0;
  }

  @Override
  public void tick() {
    this.syncEnergy();
    if (this.requiresRedstone() && !this.isPowered()) {
      return;
    }
    if (this.isFull()) {
      this.exportIfFlowing();
      return;
    }
    this.exportIfFlowing();
    if (this.isBurning() && fuelRate > 0) {
      --this.timer;
      this.addEnergy(fuelRate);
      return;
    }
    fuelRate = 0;
    //now we can add power
    //burnTime is zero grab another 
    ItemStack stack = inventory.getStackInSlot(0);
    if (stack.getItem() instanceof PeatItem) {
      PeatItem peat = (PeatItem) stack.getItem();
      int futureValue = energy.getEnergyStored() + peat.getPeatFuelValue();
      if (futureValue < energy.getMaxEnergyStored()) { // consume fuel only if we have room in the buffer
        fuelRate = peat.getPeatFuelValue();
        inventory.extractItem(0, 1, false);
        this.timer = BURNTIME;
      }
    }
  }

  private void exportIfFlowing() {
    if (this.getFlowing() == 1) {
      exportEnergyAllSides();
    }
  }

  public boolean isFull() {
    return energy.getEnergyStored() >= energy.getMaxEnergyStored();
  }

  private void addEnergy(int i) {
    energy.receiveEnergy(i, false);
  }

  public int getBurnTime() {
    return this.timer;
  }

  public void setBurnTime(int value) {
    timer = value;
  }

  @Override
  public ITextComponent getDisplayName() {
    return new StringTextComponent(getType().getRegistryName().getPath());
  }

  @Override
  public Container createMenu(int i, PlayerInventory playerInventory, PlayerEntity playerEntity) {
    return new ContainerGeneratorPeat(i, world, pos, playerInventory, playerEntity);
  }

  public int getFlowing() {
    return flowing;
  }

  public void setFlowing(int flowing) {
    this.flowing = flowing;
  }

  @Override
  public void setField(int field, int value) {
    switch (Fields.values()[field]) {
      case FLOWING:
        flowing = value;
      break;
      case REDSTONE:
        setNeedsRedstone(value);
      break;
      case RENDER:
        render = value % 2;
      break;
      case BURNTIME:
        this.setBurnTime(value);
      break;
      default:
      break;
    }
  }

  @Override
  public int getField(int field) {
    switch (Fields.values()[field]) {
      case FLOWING:
        return flowing;
      case REDSTONE:
        return this.needsRedstone;
      case RENDER:
        return render;
      case BURNTIME:
        return this.timer;
    }
    return 0;
  }
}
