package com.lothrazar.cyclic.block.solidifier;

import com.lothrazar.cyclic.block.TileBlockEntityCyclic;
import com.lothrazar.cyclic.capabilities.block.FluidTankBase;
import com.lothrazar.cyclic.registry.BlockRegistry;
import com.lothrazar.cyclic.registry.CyclicRecipeType;
import com.lothrazar.cyclic.registry.TileRegistry;
import com.lothrazar.library.cap.EnergyStorageWrapper;
import com.lothrazar.library.cap.ItemStackHandlerWrapper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.items.ItemStackHandler;

import net.neoforged.neoforge.fluids.FluidStack;
import net.minecraft.core.Direction;
import net.neoforged.neoforge.energy.IEnergyStorage;
import net.neoforged.neoforge.items.IItemHandler;

public class TileSolidifier extends TileBlockEntityCyclic implements MenuProvider, WorldlyContainer {

  static enum Fields {
    REDSTONE, TIMER, RENDER, BURNMAX;
  }

  public static final int MAX = 64000;
  public static final int CAPACITY = 64 * FluidType.BUCKET_VOLUME;
  public static final int TRANSFER_FLUID_PER_TICK = FluidType.BUCKET_VOLUME / 20;
  private RecipeSolidifier currentRecipe;
  FluidTankBase tank = new FluidTankBase(this, CAPACITY, p -> true);

  @Override
  public IFluidHandler getFluidHandler(Direction side) {
    return tank;
  }

  ItemStackHandler inputSlots = new ItemStackHandler(3);
  ItemStackHandler outputSlots = new ItemStackHandler(1);
  private ItemStackHandlerWrapper inventory = new ItemStackHandlerWrapper(inputSlots, outputSlots);
  EnergyStorageWrapper energy = new EnergyStorageWrapper(MAX, MAX);
  private int burnTimeMax = 0; //only non zero if processing

  public TileSolidifier(BlockPos pos, BlockState state) {
    super(TileRegistry.SOLIDIFIER.get(), pos, state);

  }

  public static void serverTick(Level level, BlockPos blockPos, BlockState blockState, TileSolidifier e) {
    e.tick();
  }

  public static <E extends BlockEntity> void clientTick(Level level, BlockPos blockPos, BlockState blockState,
      TileSolidifier e) {
    e.tick();
  }

  public void tick() {
    this.syncEnergy();
    this.findMatchingRecipe();
    if (currentRecipe == null) {
      this.timer = 0;
      return;
    }
    //Fixes sync issue entirely
    if (level.isClientSide()) {
      return;
    }
    //Checks if there is space in the output slot
    var res = currentRecipe.getResultItem(level.registryAccess());
    var max = res.getMaxStackSize();
    if (this.outputSlots.getStackInSlot(0).getCount() > max - res.getCount()) {
      return;
    }
    final int energyCost = this.currentRecipe.getEnergy().getRfPertick();
    final int fluidCost = this.currentRecipe.getAmount();
    if ((energy.getEnergyStored() < energyCost && energyCost > 0)
        || (tank.getFluidAmount() < fluidCost && fluidCost > 0)) {
      return; // not enough resources for the recipe
    }
    energy.extractEnergy(energyCost, false);
    this.timer--;
    if (timer < 0) {
      timer = 0;
    }
    if (timer == 0 && this.tryProcessRecipe()) {
      if (this.currentRecipe != null) { //if we processed AND looped back to it again
        this.timer = this.currentRecipe.getEnergy().getTicks();
      }
    }
  }

  @Override
  public void setField(int field, int value) {
    switch (Fields.values()[field]) {
      case TIMER:
        this.timer = value;
      break;
      case REDSTONE:
        this.needsRedstone = value % 2;
      break;
      case RENDER:
        this.render = value % 2;
      break;
      case BURNMAX:
        this.burnTimeMax = value;
      break;
    }
  }

  @Override
  public int getField(int field) {
    switch (Fields.values()[field]) {
      case TIMER:
        return timer;
      case REDSTONE:
        return this.needsRedstone;
      case RENDER:
        return this.render;
      case BURNMAX:
        return this.burnTimeMax;
    }
    return 0;
  }

  @Override
  public Component getDisplayName() {
    return BlockRegistry.SOLIDIFIER.get().getName();
  }

  @Override
  public AbstractContainerMenu createMenu(int i, Inventory playerInventory, Player playerEntity) {
    return new ContainerSolidifier(i, level, worldPosition, playerInventory, playerEntity);
  }

  @Override
  public void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
    tank.readFromNBT(registries,tag.getCompound(NBTFLUID));
    if (tag.contains(NBTENERGY)) {
      energy.deserializeNBT(registries, tag.get(NBTENERGY));
    }
    inputSlots.deserializeNBT(registries,tag.getCompound(NBTINV));
    outputSlots.deserializeNBT(registries,tag.getCompound("invoutput"));
    burnTimeMax = tag.getInt("burnTimeMax");
    super.loadAdditional(tag,registries);
  }

  @Override
  public void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
    CompoundTag fluid = new CompoundTag();
    tank.writeToNBT(registries,fluid);
    tag.put(NBTFLUID, fluid);
    tag.put(NBTENERGY, energy.serializeNBT(registries));
    tag.put(NBTINV, inputSlots.serializeNBT(registries));
    tag.put("invoutput", outputSlots.serializeNBT(registries));
    tag.putInt("burnTimeMax", this.burnTimeMax);
    super.saveAdditional(tag,registries);
  }

  public float getCapacity() {
    return CAPACITY;
  }

  @Override
  public FluidStack getFluid() {
    return tank == null ? FluidStack.EMPTY : tank.getFluid();
  }

  @Override
  public void setFluid(FluidStack fluid) {
    tank.setFluid(fluid);
  }

  private void findMatchingRecipe() {
    SolidifierRecipeInput input = new SolidifierRecipeInput(
        inputSlots.getStackInSlot(0), inputSlots.getStackInSlot(1), inputSlots.getStackInSlot(2), tank.getFluid());
    if (currentRecipe != null && currentRecipe.matches(input, level)) {
      return;
    }
    currentRecipe = null;
    this.burnTimeMax = 0;
    this.timer = 0;
    var recipes = level.getRecipeManager().getAllRecipesFor(CyclicRecipeType.SOLID.get());
    for (var holder : recipes) {
      RecipeSolidifier rec = holder.value();
      if (rec.matches(input, level)) {
        currentRecipe = rec;
        this.burnTimeMax = this.currentRecipe.getEnergy().getTicks();
        this.timer = this.burnTimeMax;
        break;
      }
    }
  }

  private boolean tryProcessRecipe() {
    final int needed = this.currentRecipe.getAmount();
    FluidStack test = tank.drain(needed, IFluidHandler.FluidAction.SIMULATE);
    if (test.getAmount() >= needed) {
      // wait is output slot compatible
      if (!outputSlots.insertItem(0, currentRecipe.getResultItem(level.registryAccess()), true).isEmpty()) {
        return false;
        // there was non-empty left after this, so no room for all
      }
      // ok it has room for all the fluid none will be wasted
      inputSlots.getStackInSlot(0).shrink(1);
      inputSlots.getStackInSlot(1).shrink(1);
      inputSlots.getStackInSlot(2).shrink(1);
      tank.drain(needed, IFluidHandler.FluidAction.EXECUTE);
      outputSlots.insertItem(0, currentRecipe.getResultItem(level.registryAccess()), false);
      updateComparatorOutputLevel();
      return true;
    }
    return false;
  }

  public ItemStack getStackInputSlot(int slot) {
    return inputSlots.getStackInSlot(slot);
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
  public boolean canPlaceItemThroughFace(int i, ItemStack itemStack,  Direction direction) {
    return inventory.canPlaceItemThroughFace(i, itemStack, direction);
  }

  @Override
  public boolean canTakeItemThroughFace(int i, ItemStack itemStack, Direction direction) {
    return inventory.canTakeItemThroughFace(i, itemStack, direction);
  }
}
