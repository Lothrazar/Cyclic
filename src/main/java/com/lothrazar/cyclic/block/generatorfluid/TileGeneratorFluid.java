package com.lothrazar.cyclic.block.generatorfluid;

import java.util.ArrayList;
import com.lothrazar.cyclic.block.TileBlockEntityCyclic;
import com.lothrazar.cyclic.block.battery.TileBattery;
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
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.minecraft.core.Direction;
import net.neoforged.neoforge.energy.IEnergyStorage;
import net.neoforged.neoforge.items.IItemHandler;

public class TileGeneratorFluid extends TileBlockEntityCyclic implements MenuProvider, WorldlyContainer {

  static enum Fields {
    TIMER, REDSTONE, BURNMAX, FLOWING;
  }

  public static final int CAPACITY = 64 * FluidType.BUCKET_VOLUME;
  static final int MAX = TileBattery.MENERGY * 10;
  EnergyStorageWrapper energy = new EnergyStorageWrapper(MAX, MAX);
  ItemStackHandler inputSlots = new ItemStackHandler(0);
  protected final FluidTankBase tank = new FluidTankBase(this, CAPACITY, p -> indexFluidsFromRecipes().contains(p.getFluid()));

  @Override
  public IFluidHandler getFluidHandler(Direction side) {
    return tank;
  }

  ItemStackHandler outputSlots = new ItemStackHandler(0);
  private ItemStackHandlerWrapper inventory = new ItemStackHandlerWrapper(inputSlots, outputSlots);
  private int burnTimeMax = 0; //only non zero if processing
  private int burnTime = 0; //how much of current fuel is left
  private RecipeGeneratorFluid currentRecipe;

  public TileGeneratorFluid(BlockPos pos, BlockState state) {
    super(TileRegistry.GENERATOR_FLUID.get(), pos, state);
    this.needsRedstone = 0;
  }

  public static void serverTick(Level level, BlockPos blockPos, BlockState blockState, TileGeneratorFluid e) {
    e.tick();
  }

  @Override
  public FluidStack getFluid() {
    return tank == null ? FluidStack.EMPTY : tank.getFluid();
  }

  @Override
  public void setFluid(FluidStack fluid) {
    tank.setFluid(fluid);
  }

  //  @Override
  public void tick() {
    this.syncEnergy();
    if (this.flowing == 1) {
      this.exportEnergyAllSides();
    }
    if (this.burnTime == 0) {
      setLitProperty(false);
    }
    if (level.isClientSide) {
      return;
    }
    if (this.requiresRedstone() && !this.isPowered()) {
      setLitProperty(false);
      return;
    }
    if (this.burnTime <= 0) {
      currentRecipe = null;
      this.burnTimeMax = 0;
      this.burnTime = 0;
    }
    tryConsumeFuel();
  }

  private void tryConsumeFuel() {
    //pull in new fuel
    if (this.burnTime > 0 && this.energy.getEnergyStored() + currentRecipe.getRfpertick() <= this.energy.getMaxEnergyStored()) {
      setLitProperty(true);
      this.burnTime--;
      //we have room in the tank, burn one tck and fill up 
      energy.receiveEnergy(currentRecipe.getRfpertick(), false);
    }
    else if (this.burnTime <= 0) {
      this.findMatchingRecipe();
      if (currentRecipe == null) {
        this.burnTime = 0;
        return;
      }
    }
  }

  private ArrayList<Fluid> indexFluidsFromRecipes() {
    var recipes = level.getRecipeManager().getAllRecipesFor(CyclicRecipeType.GENERATOR_FLUID.get());
    ArrayList<Fluid> fluids = new ArrayList<>();
    for (var holder : recipes) {
      RecipeGeneratorFluid recipe = holder.value();
      for (FluidStack s : recipe.getMatchingFluids()) {
        fluids.add(s.getFluid());
      }
    }
    return fluids;
  }

  private void findMatchingRecipe() {
    GeneratorFluidRecipeInput input = new GeneratorFluidRecipeInput(tank.getFluid());
    if (currentRecipe != null && currentRecipe.matches(input, level)) {
      return;
    }
    currentRecipe = null;
    var recipes = level.getRecipeManager().getAllRecipesFor(CyclicRecipeType.GENERATOR_FLUID.get());
    for (var holder : recipes) {
      RecipeGeneratorFluid rec = holder.value();
      if (rec.matches(input, level)) {
        this.currentRecipe = rec;
        this.burnTimeMax = this.currentRecipe.getTicks();
        this.burnTime = this.burnTimeMax;
        tank.drain(this.currentRecipe.getAmount(), IFluidHandler.FluidAction.EXECUTE);
        return;
      }
    }
  }

  @Override
  public Component getDisplayName() {
    return BlockRegistry.GENERATOR_FLUID.get().getName();
  }

  @Override
  public AbstractContainerMenu createMenu(int i, Inventory playerInventory, Player playerEntity) {
    return new ContainerGeneratorFluid(i, level, worldPosition, playerInventory, playerEntity);
  }

  @Override
  public void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
    tank.readFromNBT(registries,tag.getCompound(NBTFLUID));
    if (tag.contains(NBTENERGY)) {
      energy.deserializeNBT(registries, tag.get(NBTENERGY));
    }
    inventory.deserializeNBT(registries,tag.getCompound(NBTINV));
    super.loadAdditional(tag,registries);
  }

  @Override
  public void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
    CompoundTag fluid = new CompoundTag();
    tank.writeToNBT(registries,fluid);
    tag.put(NBTFLUID, fluid);
    tag.put(NBTENERGY, energy.serializeNBT(registries));
    tag.put(NBTINV, inventory.serializeNBT(registries));
    super.saveAdditional(tag,registries);
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
    return TileGeneratorFluid.MAX;
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
