package com.lothrazar.cyclic.block.generatorfood;

import com.lothrazar.cyclic.base.TileEntityBase;
import com.lothrazar.cyclic.block.battery.TileBattery;
import com.lothrazar.cyclic.capability.CustomEnergyStorage;
import com.lothrazar.cyclic.capability.ItemStackHandlerWrapper;
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
import net.minecraftforge.common.ForgeConfigSpec.IntValue;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class TileGeneratorFood extends TileEntityBase implements INamedContainerProvider, ITickableTileEntity {

  static enum Fields {
    TIMER, REDSTONE, BURNMAX, FLOWING;
  }

  static final int MAX = TileBattery.MENERGY * 10;
  public static IntValue RF_PER_TICK;
  public static IntValue TICKS_PER_FOOD;
  CustomEnergyStorage energy = new CustomEnergyStorage(MAX, MAX);
  private LazyOptional<IEnergyStorage> energyCap = LazyOptional.of(() -> energy);
  ItemStackHandler inputSlots = new ItemStackHandler(1) {

    @Override
    public boolean isItemValid(int slot, ItemStack stack) {
      return stack.isFood();
    }
  };
  ItemStackHandler outputSlots = new ItemStackHandler(0);
  private ItemStackHandlerWrapper inventory = new ItemStackHandlerWrapper(inputSlots, outputSlots);
  private LazyOptional<IItemHandler> inventoryCap = LazyOptional.of(() -> inventory);
  private int burnTimeMax = 0; //only non zero if processing
  private int burnTime = 0; //how much of current fuel is left

  public TileGeneratorFood() {
    super(TileRegistry.GENERATOR_FOOD.get());
    this.needsRedstone = 0;
  }

  @Override
  public void tick() {
    if (world == null || world.isRemote) {
      return;
    }
    this.syncEnergy();
    if (this.flowing == 1) {
      this.exportEnergyAllSides();
    }
    if (this.requiresRedstone() && !this.isPowered()) {
      setLitProperty(false);
      return;
    }
    //
    //are we EMPTY
    if (this.burnTime == 0) {
      tryConsumeFuel();
    }
    if (this.burnTime > 0 && this.energy.getEnergyStored() + RF_PER_TICK.get() <= this.energy.getMaxEnergyStored()) {
      setLitProperty(true);
      this.burnTime--;
      //we have room in the tank, burn one tck and fill up
      energy.receiveEnergy(RF_PER_TICK.get(), false);
    }
  }

  private void tryConsumeFuel() {
    this.burnTimeMax = 0;
    //pull in new fuel
    ItemStack stack = inputSlots.getStackInSlot(0);
    if (stack.isFood()) {
      float foodVal = stack.getItem().getFood().getHealing() + stack.getItem().getFood().getSaturation();
      //      int testTotal = RF_PER_TICK.get() * burnTimeTicks;
      // BURN IT
      this.burnTimeMax = (int) (TICKS_PER_FOOD.get() * foodVal);
      this.burnTime = this.burnTimeMax;
      stack.shrink(1);
      //nether items, mob drops
      // lava fluid
      //exp fluid
    }
  }

  @Override
  public ITextComponent getDisplayName() {
    return new StringTextComponent(getType().getRegistryName().getPath());
  }

  @Override
  public Container createMenu(int i, PlayerInventory playerInventory, PlayerEntity playerEntity) {
    return new ContainerGeneratorFood(i, world, pos, playerInventory, playerEntity);
  }

  @Override
  public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
    if (cap == CapabilityEnergy.ENERGY) {
      return energyCap.cast();
    }
    if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
      return inventoryCap.cast();
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
    energy.deserializeNBT(tag.getCompound(NBTENERGY));
    inventory.deserializeNBT(tag.getCompound(NBTINV));
    super.read(bs, tag);
  }

  @Override
  public CompoundNBT write(CompoundNBT tag) {
    tag.put(NBTENERGY, energy.serializeNBT());
    tag.put(NBTINV, inventory.serializeNBT());
    return super.write(tag);
  }

  @Override
  public int getField(int id) {
    switch (Fields.values()[id]) {
      case REDSTONE:
        return this.needsRedstone;
      case TIMER:
        return this.burnTime;
      case BURNMAX:
        return this.burnTimeMax;
      case FLOWING:
        return this.flowing;
    }
    return 0;
  }

  @Override
  public void setField(int field, int value) {
    switch (Fields.values()[field]) {
      case REDSTONE:
        this.needsRedstone = value % 2;
      break;
      case TIMER:
        this.burnTime = value;
      break;
      case BURNMAX:
        this.burnTimeMax = value;
      break;
      case FLOWING:
        this.flowing = value;
      break;
    }
  }

  public int getEnergyMax() {
    return TileGeneratorFood.MAX;
  }
}
