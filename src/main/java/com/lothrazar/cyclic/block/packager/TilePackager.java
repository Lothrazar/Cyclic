package com.lothrazar.cyclic.block.packager;

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
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.minecraft.core.Direction;
import net.neoforged.neoforge.energy.IEnergyStorage;
import net.neoforged.neoforge.items.IItemHandler;


public class TilePackager extends TileBlockEntityCyclic implements MenuProvider, WorldlyContainer {

  static enum Fields {
    TIMER, REDSTONE, BURNMAX;
  }

  static final int MAX = TileBattery.MENERGY * 10;
  public static ModConfigSpec.IntValue POWERCONF;
  public static ModConfigSpec.IntValue TIMERCONF;
  EnergyStorageWrapper energy = new EnergyStorageWrapper(MAX, MAX);
  ItemStackHandler inputSlots = new ItemStackHandler(1) {

    @Override
    public boolean isItemValid(int slot, ItemStack stack) {
      return isItemStackValid(stack);
    }
  };
  ItemStackHandler outputSlots = new ItemStackHandler(1);
  final ItemStackHandlerWrapper inventory = new ItemStackHandlerWrapper(inputSlots, outputSlots);

  public TilePackager(BlockPos pos, BlockState state) {
    super(TileRegistry.PACKAGER.get(), pos, state);
    this.needsRedstone = 0;
  }

  private boolean isItemStackValid(ItemStack stack) {
    if (level == null) {
      return false;
    }
    return UtilPackager.isItemStackValid(level.getServer().getRecipeManager(), level.registryAccess(), stack);
  }

  public static void serverTick(Level level, BlockPos blockPos, BlockState blockState, TilePackager e) {
    e.tick();
  }

  public void tick() {
    this.syncEnergy();
    if (this.requiresRedstone() && !this.isPowered()) {
      setLitProperty(false);
      return;
    }
    ItemStack stack = inputSlots.getStackInSlot(0);
    if (stack.isEmpty()) {
      timer = TIMERCONF.get();
      setLitProperty(false);
      return;
    }
    if (POWERCONF.get() > 0 && energy.getEnergyStored() < POWERCONF.get()) {
      return;
    }
    setLitProperty(true);
    if (--timer > 0) {
      return;
    }
    timer = TIMERCONF.get();
    tryDoPackage();
  }

  private void tryDoPackage() {
    ItemStack stack = inputSlots.getStackInSlot(0);
    if (level == null) {
      return;
    }
    final CraftingRecipe recipe = UtilPackager.getRecipeForItemStack(level.getServer().getRecipeManager(), level.registryAccess(), stack);
    if (recipe == null) {
      return;
    }
    // 26.1: Recipe#getResultItem removed - assemble(CraftingInput.EMPTY) is a safe stand-in since
    // CraftingRecipe#assemble ignores the actual input for standard recipes (same pattern as UtilPackager).
    if (outputSlots.insertItem(0, recipe.assemble(CraftingInput.EMPTY).copy(), true).isEmpty()) {
      final int total = UtilPackager.getIngredientsInRecipe(recipe);
      final ItemStack output = recipe.assemble(CraftingInput.EMPTY).copy();
      inputSlots.extractItem(0, total, false);
      outputSlots.insertItem(0, output, false);
      energy.extractEnergy(POWERCONF.get(), false);
      this.updateComparatorOutputLevel();
    }
  }

  @Override
  public Component getDisplayName() {
    return BlockRegistry.PACKAGER.get().getName();
  }

  @Override
  public AbstractContainerMenu createMenu(int i, Inventory playerInventory, Player playerEntity) {
    return new ContainerPackager(i, level, worldPosition, playerInventory, playerEntity);
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
        return this.timer;
      case BURNMAX:
        return TIMERCONF.get();
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
        this.timer = value;
      break;
      case BURNMAX:
      break;
    }
  }

  @Override
  public IItemHandler getItemHandler(Direction side) {
    return inventory;
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
