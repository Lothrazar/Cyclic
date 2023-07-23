package com.lothrazar.cyclic.block.melter;

import java.util.List;
import java.util.function.Predicate;
import com.lothrazar.cyclic.block.TileBlockEntityCyclic;
import com.lothrazar.cyclic.capabilities.block.FluidTankBase;
import com.lothrazar.cyclic.registry.BlockRegistry;
import com.lothrazar.cyclic.registry.CyclicRecipeType;
import com.lothrazar.cyclic.registry.TileRegistry;
import com.lothrazar.library.cap.CustomEnergyStorage;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
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
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class TileMelter extends TileBlockEntityCyclic implements MenuProvider {

  static enum Fields {
    REDSTONE, TIMER, RENDER, BURNMAX;
  }

  static final int MAX = 64000;
  public static final int CAPACITY = 64 * FluidType.BUCKET_VOLUME;
  public static final int TRANSFER_FLUID_PER_TICK = FluidType.BUCKET_VOLUME / 20;
  public FluidTankBase tank = new FluidTankBase(this, CAPACITY, isFluidValid());
  CustomEnergyStorage energy = new CustomEnergyStorage(MAX, MAX);
  ItemStackHandler inventory = new ItemStackHandler(2);
  private LazyOptional<IEnergyStorage> energyCap = LazyOptional.of(() -> energy);
  private LazyOptional<IItemHandler> inventoryCap = LazyOptional.of(() -> inventory);
  private final LazyOptional<IFluidHandler> fluidCap = LazyOptional.of(() -> tank);
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
    if (currentRecipe == null || !currentRecipe.matches(this, level)) {
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

  public Predicate<FluidStack> isFluidValid() {
    return p -> true;
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
  public void load(CompoundTag tag) {
    tank.readFromNBT(tag.getCompound(NBTFLUID));
    energy.deserializeNBT(tag.getCompound(NBTENERGY));
    inventory.deserializeNBT(tag.getCompound(NBTINV));
    burnTimeMax = tag.getInt("burnTimeMax");
    super.load(tag);
  }

  @Override
  public void saveAdditional(CompoundTag tag) {
    CompoundTag fluid = new CompoundTag();
    tank.writeToNBT(fluid);
    tag.put(NBTFLUID, fluid);
    tag.put(NBTENERGY, energy.serializeNBT());
    tag.put(NBTINV, inventory.serializeNBT());
    tag.putInt("burnTimeMax", this.burnTimeMax);
    super.saveAdditional(tag);
  }

  @Override
  public void invalidateCaps() {
    energyCap.invalidate();
    inventoryCap.invalidate();
    fluidCap.invalidate();
    super.invalidateCaps();
  }

  @Override
  public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
    if (cap == ForgeCapabilities.FLUID_HANDLER) {
      return fluidCap.cast();
    }
    if (cap == ForgeCapabilities.ENERGY) {
      return energyCap.cast();
    }
    if (cap == ForgeCapabilities.ITEM_HANDLER) {
      return inventoryCap.cast();
    }
    return super.getCapability(cap, side);
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
    IItemHandler inv = inventoryCap.orElse(null);
    return (inv == null) ? ItemStack.EMPTY : inv.getStackInSlot(slot);
  }

  private void findMatchingRecipe() {
    if (currentRecipe != null && currentRecipe.matches(this, level)) {
      return;
    }
    currentRecipe = null;
    this.burnTimeMax = 0;
    this.timer = 0;
    List<RecipeMelter> recipes = level.getRecipeManager().getAllRecipesFor(CyclicRecipeType.MELTER.get());
    for (RecipeMelter rec : recipes) {
      if (rec.matches(this, level)) {
        if (this.tank.getFluid() != null && !this.tank.getFluid().isEmpty()) {
          if (rec.getRecipeFluid().getFluid() != this.tank.getFluid().getFluid()) {
            continue;
            //fluid wont fit
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
    int test = tank.fill(this.currentRecipe.getRecipeFluid(), FluidAction.SIMULATE);
    if (test == this.currentRecipe.getRecipeFluid().getAmount()
        && currentRecipe.matches(this, level)) {
      //ok it has room for all the fluid none will be wasted
      inventory.getStackInSlot(0).shrink(1);
      inventory.getStackInSlot(1).shrink(1);
      tank.fill(this.currentRecipe.getRecipeFluid(), FluidAction.EXECUTE);
      return true;
    }
    return false;
  }
}
