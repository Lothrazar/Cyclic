package com.lothrazar.cyclic.block.melter;

import com.lothrazar.cyclic.block.TileBlockEntityCyclic;
import com.lothrazar.cyclic.capabilities.block.FluidTankBase;
import com.lothrazar.cyclic.registry.BlockRegistry;
import com.lothrazar.cyclic.registry.CyclicRecipeType;
import com.lothrazar.cyclic.registry.TileRegistry;
import com.lothrazar.library.cap.EnergyStorageWrapper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.energy.IEnergyStorage;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;

public class TileMelter extends TileBlockEntityCyclic implements MenuProvider {

  static enum Fields {
    REDSTONE, TIMER, RENDER, BURNMAX, LOCK;
  }

  private static final String NBT_LOCK = "lock";
  private int lock = 0;

  static final int MAX = 64000;
  public static final int CAPACITY = 64 * FluidType.BUCKET_VOLUME;
  public static final int TRANSFER_FLUID_PER_TICK = FluidType.BUCKET_VOLUME / 20;
  FluidTankBase tank = new FluidTankBase(this, CAPACITY, p -> true);

  @Override
  public IFluidHandler getFluidHandler(Direction side) {
    return tank;
  }

  EnergyStorageWrapper energy = new EnergyStorageWrapper(MAX, MAX);
  ItemStackHandler inventory = new ItemStackHandler(1);
  private RecipeMelter currentRecipe;
  private int burnTimeMax = 0; //only non zero if processing

  public TileMelter(BlockPos pos, BlockState state) {
    super(TileRegistry.MELTER.get(), pos, state);
    this.needsRedstone = 0;
  }

  public static void serverTick(Level level, BlockPos blockPos, BlockState blockState, TileMelter e) {
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
    if (this.lock == 1 && anyInputAtOne(inventory)) {
      //lock engaged and a stack would shrink to zero: halt before consuming any energy
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
    if (currentRecipe == null || !currentRecipe.matches(new MelterRecipeInput(inventory.getStackInSlot(0)), level)) {
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
      case LOCK:
        this.lock = value % 2;
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
      case LOCK:
        return this.lock;
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
  public void loadAdditional(ValueInput input) {
    tank.deserialize(input.childOrEmpty(NBTFLUID));
    energy.deserialize(input.childOrEmpty(NBTENERGY));
    inventory.deserialize(input.childOrEmpty(NBTINV));
    burnTimeMax = input.getIntOr("burnTimeMax", 0);
    lock = input.getIntOr(NBT_LOCK, 0);
    super.loadAdditional(input);
  }

  @Override
  public void saveAdditional(ValueOutput output) {
    tank.serialize(output.child(NBTFLUID));
    energy.serialize(output.child(NBTENERGY));
    inventory.serialize(output.child(NBTINV));
    output.putInt("burnTimeMax", this.burnTimeMax);
    output.putInt(NBT_LOCK, this.lock);
    super.saveAdditional(output);
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
    MelterRecipeInput input = new MelterRecipeInput(inventory.getStackInSlot(0));
    if (currentRecipe != null && currentRecipe.matches(input, level)) {
      return;
    }
    currentRecipe = null;
    this.burnTimeMax = 0;
    this.timer = 0;
    var recipes = level.getServer().getRecipeManager().recipeMap().byType(CyclicRecipeType.MELTER.get());
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

  private static boolean anyInputAtOne(ItemStackHandler inv) {
    for (int i = 0; i < inv.getSlots(); i++) {
      ItemStack s = inv.getStackInSlot(i);
      if (!s.isEmpty() && s.getCount() <= 1) {
        return true;
      }
    }
    return false;
  }

  private boolean tryProcessRecipe() {
    MelterRecipeInput input = new MelterRecipeInput(inventory.getStackInSlot(0));
    int test = tank.fill(this.currentRecipe.getRecipeFluid(), IFluidHandler.FluidAction.SIMULATE);
    if (test == this.currentRecipe.getRecipeFluid().getAmount()
        && currentRecipe.matches(input, level)) {
      //lock: refuse to drain any non-empty input slot down to zero, so automation can't refill with a different item
      if (this.lock == 1 && anyInputAtOne(inventory)) {
        return false;
      }
      inventory.getStackInSlot(0).shrink(1);
      tank.fill(this.currentRecipe.getRecipeFluid(), IFluidHandler.FluidAction.EXECUTE);
      updateComparatorOutputLevel();
      return true;
    }
    return false;
  }

  @Override
  public IItemHandler getItemHandler(Direction side) {
    return inventory;
  }


  @Override
  public IEnergyStorage getEnergyHandler(Direction side) {
    return energy;
  }

}
