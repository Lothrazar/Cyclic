package com.lothrazar.cyclic.block.generatorfluid;

import com.lothrazar.cyclic.base.FluidTankBase;
import com.lothrazar.cyclic.base.TileEntityBase;
import com.lothrazar.cyclic.block.battery.TileBattery;
import com.lothrazar.cyclic.capability.CustomEnergyStorage;
import com.lothrazar.cyclic.capability.ItemStackHandlerWrapper;
import com.lothrazar.cyclic.recipe.CyclicRecipeType;
import com.lothrazar.cyclic.registry.TileRegistry;
import java.util.List;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.MenuProvider;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.TickableBlockEntity;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class TileGeneratorFluid extends TileEntityBase implements MenuProvider, TickableBlockEntity {

  static enum Fields {
    TIMER, REDSTONE, BURNMAX, FLOWING;
  }

  public static final int CAPACITY = 64 * FluidAttributes.BUCKET_VOLUME;
  static final int MAX = TileBattery.MENERGY * 10;
  CustomEnergyStorage energy = new CustomEnergyStorage(MAX, MAX);
  private LazyOptional<IEnergyStorage> energyCap = LazyOptional.of(() -> energy);
  ItemStackHandler inputSlots = new ItemStackHandler(0);
  FluidTankBase tank;
  private final LazyOptional<FluidTankBase> tankWrapper = LazyOptional.of(() -> tank);
  ItemStackHandler outputSlots = new ItemStackHandler(0);
  private ItemStackHandlerWrapper inventory = new ItemStackHandlerWrapper(inputSlots, outputSlots);
  private LazyOptional<IItemHandler> inventoryCap = LazyOptional.of(() -> inventory);
  private int burnTimeMax = 0; //only non zero if processing
  private int burnTime = 0; //how much of current fuel is left
  private RecipeGeneratorFluid<?> currentRecipe;

  public TileGeneratorFluid() {
    super(TileRegistry.GENERATOR_FLUID.get());
    tank = new FluidTankBase(this, CAPACITY, p -> true);
    this.needsRedstone = 0;
  }

  public FluidStack getFluid() {
    return tank == null ? FluidStack.EMPTY : tank.getFluid();
  }

  @Override
  public void setFluid(FluidStack fluid) {
    tank.setFluid(fluid);
  }

  @Override
  public void tick() {
    this.syncEnergy();
    if (this.flowing == 1) {
      this.exportEnergyAllSides();
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
    this.findMatchingRecipe();
    if (currentRecipe == null) {
      this.burnTime = 0;
      return;
    }
    if (this.burnTime > 0 && this.energy.getEnergyStored() + currentRecipe.getRfpertick() <= this.energy.getMaxEnergyStored()) {
      setLitProperty(true);
      this.burnTime--;
      //we have room in the tank, burn one tck and fill up 
      energy.receiveEnergy(currentRecipe.getRfpertick(), false);
    }
  }

  private void findMatchingRecipe() {
    if (currentRecipe != null && currentRecipe.matches(this, level)) {
      return;
    }
    currentRecipe = null;
    List<RecipeGeneratorFluid<TileEntityBase>> recipes = level.getRecipeManager().getAllRecipesFor(CyclicRecipeType.GENERATOR_FLUID);
    for (RecipeGeneratorFluid<?> rec : recipes) {
      if (rec.matches(this, level)) {
        this.currentRecipe = rec;
        this.burnTimeMax = this.currentRecipe.getTicks();
        this.burnTime = this.burnTimeMax;
        //        this.inputSlots.extractItem(0, 1, false);
        //no items to extract
        tank.drain(this.currentRecipe.getRecipeFluid(), FluidAction.EXECUTE);
        // ash?
        //        ModCyclic.LOGGER.info("found genrecipe" + currentRecipe.getId());
        return;
      }
    }
  }

  @Override
  public Component getDisplayName() {
    return new TextComponent(getType().getRegistryName().getPath());
  }

  @Override
  public AbstractContainerMenu createMenu(int i, Inventory playerInventory, Player playerEntity) {
    return new ContainerGeneratorFluid(i, level, worldPosition, playerInventory, playerEntity);
  }

  @Override
  public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
    if (cap == CapabilityEnergy.ENERGY) {
      return energyCap.cast();
    }
    if (cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
      return tankWrapper.cast();
    }
    if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
      return inventoryCap.cast();
    }
    return super.getCapability(cap, side);
  }

  @Override
  public void load(BlockState bs, CompoundTag tag) {
    tank.readFromNBT(tag.getCompound(NBTFLUID));
    energy.deserializeNBT(tag.getCompound(NBTENERGY));
    inventory.deserializeNBT(tag.getCompound(NBTINV));
    super.load(bs, tag);
  }

  @Override
  public CompoundTag save(CompoundTag tag) {
    CompoundTag fluid = new CompoundTag();
    tank.writeToNBT(fluid);
    tag.put(NBTFLUID, fluid);
    tag.put(NBTENERGY, energy.serializeNBT());
    tag.put(NBTINV, inventory.serializeNBT());
    return super.save(tag);
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
}
