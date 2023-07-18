package com.lothrazar.cyclic.block.solidifier;

import java.util.List;
import com.lothrazar.cyclic.block.TileBlockEntityCyclic;
import com.lothrazar.cyclic.capabilities.block.FluidTankBase;
import com.lothrazar.cyclic.registry.BlockRegistry;
import com.lothrazar.cyclic.registry.CyclicRecipeType;
import com.lothrazar.cyclic.registry.TileRegistry;
import com.lothrazar.library.cap.CustomEnergyStorage;
import com.lothrazar.library.cap.ItemStackHandlerWrapper;
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
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class TileSolidifier extends TileBlockEntityCyclic implements MenuProvider {

  static enum Fields {
    REDSTONE, TIMER, RENDER, BURNMAX;
  }

  public static final int MAX = 64000;
  public static final int CAPACITY = 64 * FluidType.BUCKET_VOLUME;
  public static final int TRANSFER_FLUID_PER_TICK = FluidType.BUCKET_VOLUME / 20;
  private RecipeSolidifier currentRecipe;
  FluidTankBase tank;
  ItemStackHandler inputSlots = new ItemStackHandler(3);
  ItemStackHandler outputSlots = new ItemStackHandler(1);
  private ItemStackHandlerWrapper inventory = new ItemStackHandlerWrapper(inputSlots, outputSlots);
  private final LazyOptional<IItemHandler> inventoryCap = LazyOptional.of(() -> inventory);
  CustomEnergyStorage energy = new CustomEnergyStorage(MAX, MAX);
  private final LazyOptional<FluidTankBase> fluidCap = LazyOptional.of(() -> tank);
  private final LazyOptional<IEnergyStorage> energyCap = LazyOptional.of(() -> energy);
  private int burnTimeMax = 0; //only non zero if processing

  public TileSolidifier(BlockPos pos, BlockState state) {
    super(TileRegistry.SOLIDIFIER.get(), pos, state);
    tank = new FluidTankBase(this, CAPACITY, p -> true);
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
    final int cost = this.currentRecipe.getEnergy().getRfPertick();
    if (energy.getEnergyStored() < cost && cost > 0) {
      return;
    }
    energy.extractEnergy(cost, false);
    this.timer--;
    if (timer < 0) {
      timer = 0;
    }
    if (timer == 0 && this.tryProcessRecipe()) {
      if (this.currentRecipe != null)
        this.timer = this.currentRecipe.getEnergy().getTicks();
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
  public void load(CompoundTag tag) {
    tank.readFromNBT(tag.getCompound(NBTFLUID));
    energy.deserializeNBT(tag.getCompound(NBTENERGY));
    inputSlots.deserializeNBT(tag.getCompound(NBTINV));
    outputSlots.deserializeNBT(tag.getCompound("invoutput"));
    burnTimeMax = tag.getInt("burnTimeMax");
    super.load(tag);
  }

  @Override
  public void saveAdditional(CompoundTag tag) {
    CompoundTag fluid = new CompoundTag();
    tank.writeToNBT(fluid);
    tag.put(NBTFLUID, fluid);
    tag.put(NBTENERGY, energy.serializeNBT());
    tag.put(NBTINV, inputSlots.serializeNBT());
    //    ModCyclic.LOGGER.info("saveAd: " + inputSlots.serializeNBT().toString());
    tag.put("invoutput", outputSlots.serializeNBT());
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

  private void findMatchingRecipe() {
    if (currentRecipe != null && currentRecipe.matches(this, level)) {
      return;
    }
    currentRecipe = null;
    this.burnTimeMax = 0;
    this.timer = 0;
    List<RecipeSolidifier> recipes = level.getRecipeManager().getAllRecipesFor(CyclicRecipeType.SOLID.get());
    for (RecipeSolidifier rec : recipes) {
      if (rec.matches(this, level)) {
        currentRecipe = rec;
        this.burnTimeMax = this.currentRecipe.getEnergy().getTicks();
        this.timer = this.burnTimeMax;
        break;
      }
    }
  }

  private boolean tryProcessRecipe() {
    FluidStack test = tank.drain(this.currentRecipe.getRecipeFluid().getAmount(), FluidAction.SIMULATE);
    if (test.getAmount() >= this.currentRecipe.getRecipeFluid().getAmount()) {
      // wait is output slot compatible
      if (!outputSlots.insertItem(0, currentRecipe.getResultItem(level.registryAccess()), true).isEmpty()) {
        return false;
        // there was non-empty left after this, so no room for all
      }
      // ok it has room for all the fluid none will be wasted
      inputSlots.getStackInSlot(0).shrink(1);
      inputSlots.getStackInSlot(1).shrink(1);
      inputSlots.getStackInSlot(2).shrink(1);
      tank.drain(this.currentRecipe.fluidIngredient.getAmount(), FluidAction.EXECUTE);
      outputSlots.insertItem(0, currentRecipe.getResultItem(level.registryAccess()), false);
      return true;
    }
    return false;
  }

  public ItemStack getStackInputSlot(int slot) {
    return inputSlots.getStackInSlot(slot);
  }
}
