package com.lothrazar.cyclic.block.generatorfuel;

import com.lothrazar.cyclic.block.TileBlockEntityCyclic;
import com.lothrazar.cyclic.block.battery.TileBattery;
import com.lothrazar.cyclic.capabilities.ItemStackHandlerWrapper;
import com.lothrazar.cyclic.capabilities.block.CustomEnergyStorage;
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
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class TileGeneratorFuel extends TileBlockEntityCyclic implements MenuProvider {

  static enum Fields {
    TIMER, REDSTONE, BURNMAX, FLOWING;
  }

  static final int MAX = TileBattery.MENERGY * 10;
  public static IntValue RF_PER_TICK;
  CustomEnergyStorage energy = new CustomEnergyStorage(MAX, MAX);
  private LazyOptional<IEnergyStorage> energyCap = LazyOptional.of(() -> energy);
  ItemStackHandler inputSlots = new ItemStackHandler(1) {

    @Override
    public boolean isItemValid(int slot, ItemStack stack) {
      return ForgeHooks.getBurnTime(stack, RecipeType.SMELTING) > 0; //stack.getBurnTime(IRecipeType.SMELTING) >= 0;
    }
  };
  ItemStackHandler outputSlots = new ItemStackHandler(0);
  private ItemStackHandlerWrapper inventory = new ItemStackHandlerWrapper(inputSlots, outputSlots);
  private LazyOptional<IItemHandler> inventoryCap = LazyOptional.of(() -> inventory);
  final int factor = 1;
  private int burnTimeMax = 0; //only non zero if processing
  private int burnTime = 0; //how much of current fuel is left

  public TileGeneratorFuel(BlockPos pos, BlockState state) {
    super(TileRegistry.GENERATOR_FUEL.get(), pos, state);
    this.needsRedstone = 0;
  }

  public static void serverTick(Level level, BlockPos blockPos, BlockState blockState, TileGeneratorFuel e) {
    e.tick();
  }

  public static <E extends BlockEntity> void clientTick(Level level, BlockPos blockPos, BlockState blockState, TileGeneratorFuel e) {
    e.tick();
  }

  public void tick() {
    this.syncEnergy();
    if (this.flowing == 1) {
      this.exportEnergyAllSides();
    }
    if (this.requiresRedstone() && !this.isPowered()) {
      setLitProperty(false);
      return;
    }
    if (this.burnTime == 0) {
      setLitProperty(false);
    }
    if (level.isClientSide) {
      return;
    }
    //are we EMPTY
    if (this.burnTime == 0) {
      tryConsumeFuel();
    }
    if (this.burnTime > 0 && this.energy.getEnergyStored() + RF_PER_TICK.get() <= this.energy.getMaxEnergyStored()) {
      this.burnTime--;
      //we have room in the tank, burn one tck and fill up
      boolean burnt = energy.receiveEnergy(RF_PER_TICK.get(), false) > 0;
      setLitProperty(burnt);
    }
  }

  private void tryConsumeFuel() {
    this.burnTimeMax = 0;
    //pull in new fuel
    ItemStack stack = inputSlots.getStackInSlot(0);
    final int factor = 1;
    int burnTimeTicks = factor * ForgeHooks.getBurnTime(stack, RecipeType.SMELTING);
    if (burnTimeTicks > 0) {
      // BURN IT
      this.burnTimeMax = burnTimeTicks;
      this.burnTime = this.burnTimeMax;
      if (stack.getCount() == 1 && stack.hasCraftingRemainingItem()) {
        inputSlots.setStackInSlot(0, stack.getCraftingRemainingItem().copy());
      }
      else {
        stack.shrink(1);
      }
    }
  }

  @Override
  public Component getDisplayName() {
    return BlockRegistry.GENERATOR_FUEL.get().getName();
  }

  @Override
  public AbstractContainerMenu createMenu(int i, Inventory playerInventory, Player playerEntity) {
    return new ContainerGeneratorFuel(i, level, worldPosition, playerInventory, playerEntity);
  }

  @Override
  public void invalidateCaps() {
    energyCap.invalidate();
    inventoryCap.invalidate();
    super.invalidateCaps();
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
  public void load(CompoundTag tag) {
    energy.deserializeNBT(tag.getCompound(NBTENERGY));
    inventory.deserializeNBT(tag.getCompound(NBTINV));
    super.load(tag);
  }

  @Override
  public void saveAdditional(CompoundTag tag) {
    tag.put(NBTENERGY, energy.serializeNBT());
    tag.put(NBTINV, inventory.serializeNBT());
    super.saveAdditional(tag);
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
      default:
      break;
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
    return TileGeneratorFuel.MAX;
  }
}
