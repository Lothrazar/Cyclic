package com.lothrazar.cyclic.block.melter;

import com.lothrazar.cyclic.block.TileBlockEntityCyclic;
import com.lothrazar.cyclic.capabilities.block.FluidTankBase;
import com.lothrazar.cyclic.registry.BlockRegistry;
import com.lothrazar.cyclic.registry.CyclicRecipeType;
import com.lothrazar.cyclic.registry.TileRegistry;
import com.lothrazar.cyclic.capabilities.CustomEnergyStorage;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
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

public class TileMelter extends TileBlockEntityCyclic implements MenuProvider {

  static enum Fields {
    REDSTONE, TIMER, RENDER, BURNMAX;
  }

  static final int MAX = 64000;
  public static final int CAPACITY = 64 * FluidType.BUCKET_VOLUME;
  public static final int TRANSFER_FLUID_PER_TICK = FluidType.BUCKET_VOLUME / 20;
  FluidTankBase tank = new FluidTankBase(this, CAPACITY, p -> true);
  CustomEnergyStorage energy = new CustomEnergyStorage(MAX, MAX);
  ItemStackHandler inventory = new ItemStackHandler(2);
  private RecipeMelter currentRecipe;
  private int burnTimeMax = 0; //only non zero if processing

  public TileMelter(BlockPos pos, BlockState state) {
    super(TileRegistry.MELTER.get(), pos, state);
  }

  public static void serverTick(Level level, BlockPos blockPos, BlockState blockState, TileMelter e) {
    e.tick();
  }

  public static <E extends BlockEntity> void clientTick(Level level, BlockPos blockPos, BlockState blockState, TileMelter e) {
    e.tick();
  }

  public void tick() {
    this.syncEnergy();
    this.findMatchingRecipe();
    if (currentRecipe == null) {
      return;
    }
    //Fixes sync issue entirely
    if (level.isClientSide()) {
      return;
    }
    this.timer--;
    if (timer < 0) {
      timer = 0;
    }
    final int cost = this.currentRecipe.getEnergy().getRfPertick();
    if (energy.getEnergyStored() < cost && cost > 0) {
      this.timer = 0;
      return;
    }
    energy.extractEnergy(cost, false);
    if (currentRecipe == null || !currentRecipe.matches(new MelterRecipeInput(inventory.getStackInSlot(0), inventory.getStackInSlot(1)), level)) {
      this.findMatchingRecipe();
      if (currentRecipe == null) {
        this.timer = 0;
        return;
      }
    }
    if (timer == 0 && this.tryProcessRecipe()) {
      if (this.currentRecipe != null) {
        this.timer = this.currentRecipe.getEnergy().getTicks(); // may also reset during findRecipe
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
    return BlockRegistry.MELTER.get().getName();
  }

  @Override
  public AbstractContainerMenu createMenu(int i, Inventory playerInventory, Player playerEntity) {
    return new ContainerMelter(i, level, worldPosition, playerInventory, playerEntity);
  }

  @Override
  public void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
    tank.readFromNBT(registries,tag.getCompound(NBTFLUID));
    if (tag.contains(NBTENERGY)) {
      energy.deserializeNBT(registries, tag.get(NBTENERGY));
    }
    inventory.deserializeNBT(registries,tag.getCompound(NBTINV));
    burnTimeMax = tag.getInt("burnTimeMax");
    super.loadAdditional(tag,registries);
  }

  @Override
  public void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
    CompoundTag fluid = new CompoundTag();
    tank.writeToNBT(registries,fluid);
    tag.put(NBTFLUID, fluid);
    tag.put(NBTENERGY, energy.serializeNBT(registries));
    tag.put(NBTINV, inventory.serializeNBT(registries));
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

  public ItemStack getStackInputSlot(int slot) {
    return (inventory == null) ? ItemStack.EMPTY : inventory.getStackInSlot(slot);
  }

  private void findMatchingRecipe() {
    MelterRecipeInput input = new MelterRecipeInput(inventory.getStackInSlot(0), inventory.getStackInSlot(1));
    if (currentRecipe != null && currentRecipe.matches(input, level)) {
      return;
    }
    currentRecipe = null;
    this.burnTimeMax = 0;
    this.timer = 0;
    var recipes = level.getRecipeManager().getAllRecipesFor(CyclicRecipeType.MELTER.get());
    for (var holder : recipes) {
      RecipeMelter rec = holder.value();
      if (rec.matches(input, level)) {
        if (this.tank.getFluid() != null && !this.tank.getFluid().isEmpty()) {
          if (rec.getRecipeFluid().getFluid() != this.tank.getFluid().getFluid()) {
            continue;
          }
        }
        currentRecipe = rec;
        this.burnTimeMax = this.currentRecipe.getEnergy().getTicks();
        this.timer = this.burnTimeMax;
        return;
      }
    }
  }

  private boolean tryProcessRecipe() {
    MelterRecipeInput input = new MelterRecipeInput(inventory.getStackInSlot(0), inventory.getStackInSlot(1));
    int test = tank.fill(this.currentRecipe.getRecipeFluid(), IFluidHandler.FluidAction.SIMULATE);
    if (test == this.currentRecipe.getRecipeFluid().getAmount()
        && currentRecipe.matches(input, level)) {
      inventory.getStackInSlot(0).shrink(1);
      inventory.getStackInSlot(1).shrink(1);
      tank.fill(this.currentRecipe.getRecipeFluid(), IFluidHandler.FluidAction.EXECUTE);
      updateComparatorOutputLevel();
      return true;
    }
    return false;
  }
}
