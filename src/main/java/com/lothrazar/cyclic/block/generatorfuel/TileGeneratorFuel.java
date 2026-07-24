package com.lothrazar.cyclic.block.generatorfuel;

import com.lothrazar.cyclic.block.TileBlockEntityCyclic;
import com.lothrazar.cyclic.block.battery.TileBattery;
import com.lothrazar.cyclic.registry.BlockRegistry;
import com.lothrazar.cyclic.registry.TileRegistry;
import com.lothrazar.library.cap.EnergyStorageWrapper;
import com.lothrazar.library.cap.ItemStackHandlerWrapper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.minecraft.core.Direction;
import net.neoforged.neoforge.energy.IEnergyStorage;
import net.neoforged.neoforge.items.IItemHandler;

public class TileGeneratorFuel extends TileBlockEntityCyclic implements MenuProvider, WorldlyContainer {

  static enum Fields {
    TIMER, REDSTONE, BURNMAX, FLOWING;
  }

  static final int MAX = TileBattery.MENERGY * 10;
  public static ModConfigSpec.IntValue RF_PER_TICK;
  EnergyStorageWrapper energy = new EnergyStorageWrapper(MAX, MAX);
  ItemStackHandler inputSlots = new ItemStackHandler(1) {

    @Override
    public boolean isItemValid(int slot, ItemStack stack) {
      return stack.getBurnTime(RecipeType.SMELTING) > 0;// ForgeHooks.getBurnTime(stack, RecipeType.SMELTING) > 0;
    }
  };
  ItemStackHandler outputSlots = new ItemStackHandler(0);
  private ItemStackHandlerWrapper inventory = new ItemStackHandlerWrapper(inputSlots, outputSlots);
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
    if (level.isClientSide()) {
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
    int burnTimeTicks = factor * stack.getBurnTime(RecipeType.SMELTING); //ForgeHooks.getBurnTime(stack, RecipeType.SMELTING);
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
      updateComparatorOutputLevel();
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
  public void loadAdditional(ValueInput input) {
          energy.deserialize(input.childOrEmpty(NBTENERGY));
    inventory.deserialize(input.childOrEmpty(NBTINV));
    super.loadAdditional(input);
  }

  @Override
  public void saveAdditional(ValueOutput output) {
    energy.serialize(output.child(NBTENERGY));
    inventory.serialize(output.child(NBTINV));
    super.saveAdditional(output);
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

  @Override
  public IItemHandler getItemHandler(Direction side) {
    return inputSlots;
  }

  @Override
  public IEnergyStorage getEnergyHandler(Direction side) {
    return energy;
  }

  @Override
  public int[] getSlotsForFace(Direction direction) {
    return inventory.getSlotsForFace(direction);
  }

  @Override
  public boolean canPlaceItemThroughFace(int i, ItemStack itemStack, Direction direction) {
    return inventory.canPlaceItemThroughFace(i, itemStack, direction);
  }

  @Override
  public boolean canTakeItemThroughFace(int i, ItemStack itemStack, Direction direction) {
    return inventory.canTakeItemThroughFace(i, itemStack, direction);
  }
}
