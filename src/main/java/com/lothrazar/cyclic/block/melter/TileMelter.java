package com.lothrazar.cyclic.block.melter;

import java.util.function.Predicate;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import com.lothrazar.cyclic.base.FluidTankBase;
import com.lothrazar.cyclic.base.TileEntityBase;
import com.lothrazar.cyclic.capability.CustomEnergyStorage;
import com.lothrazar.cyclic.data.Const;
import com.lothrazar.cyclic.registry.TileRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;
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

@SuppressWarnings("rawtypes")
public class TileMelter extends TileEntityBase implements ITickableTileEntity, INamedContainerProvider {

  static final int MAX = 64000;
  public static final int CAPACITY = 64 * FluidAttributes.BUCKET_VOLUME;
  public static final int TRANSFER_FLUID_PER_TICK = FluidAttributes.BUCKET_VOLUME / 20;
  public static IntValue POWERCONF;
  public FluidTankBase tank;
  CustomEnergyStorage energy = new CustomEnergyStorage(MAX, MAX);
  ItemStackHandler inventory = new ItemStackHandler(2);
  private LazyOptional<IEnergyStorage> energyCap = LazyOptional.of(() -> energy);
  private LazyOptional<IItemHandler> inventoryCap = LazyOptional.of(() -> inventory);
  private RecipeMelter currentRecipe;
  public final static int TIMER_FULL = Const.TICKS_PER_SEC * 3;

  static enum Fields {
    REDSTONE, TIMER, RENDER;
  }

  public TileMelter() {
    super(TileRegistry.melter);
    tank = new FluidTankBase(this, CAPACITY, isFluidValid());
  }

  @Override
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
    final int cost = POWERCONF.get();
    if (energy.getEnergyStored() < cost && cost > 0) {
      return;//broke
    }
    if (timer == 0 && this.tryProcessRecipe()) {
      this.timer = TIMER_FULL;
      energy.extractEnergy(cost, false);
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
    }
    return 0;
  }

  public Predicate<FluidStack> isFluidValid() {
    return p -> true;
  }

  @Override
  public ITextComponent getDisplayName() {
    return new StringTextComponent(getType().getRegistryName().getPath());
  }

  @Nullable
  @Override
  public Container createMenu(int i, PlayerInventory playerInventory, PlayerEntity playerEntity) {
    return new ContainerMelter(i, world, pos, playerInventory, playerEntity);
  }

  @Override
  public void read(BlockState bs, CompoundNBT tag) {
    tank.readFromNBT(tag.getCompound("fluid"));
    energy.deserializeNBT(tag.getCompound(NBTENERGY));
    inventory.deserializeNBT(tag.getCompound(NBTINV));
    super.read(bs, tag);
  }

  @Override
  public CompoundNBT write(CompoundNBT tag) {
    CompoundNBT fluid = new CompoundNBT();
    tank.writeToNBT(fluid);
    tag.put("fluid", fluid);
    tag.put(NBTENERGY, energy.serializeNBT());
    tag.put(NBTINV, inventory.serializeNBT());
    return super.write(tag);
  }

  @Override
  public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, Direction side) {
    if (cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
      return LazyOptional.of(() -> tank).cast();
    }
    if (cap == CapabilityEnergy.ENERGY && POWERCONF.get() > 0) {
      return energyCap.cast();
    }
    if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
      return inventoryCap.cast();
    }
    return super.getCapability(cap, side);
  }

  public float getCapacity() {
    return CAPACITY;
  }

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
    if (currentRecipe != null && currentRecipe.matches(this, world)) {
      return;// its valid
    }
    currentRecipe = null;
    for (RecipeMelter rec : RecipeMelter.RECIPES) {
      if (rec.matches(this, world)) {
        currentRecipe = rec;
      }
    }
  }

  private boolean tryProcessRecipe() {
    int test = tank.fill(this.currentRecipe.getRecipeFluid(), FluidAction.SIMULATE);
    if (test == this.currentRecipe.getRecipeFluid().getAmount()) {
      //ok it has room for all the fluid none will be wasted
      inventory.getStackInSlot(0).shrink(1);
      inventory.getStackInSlot(1).shrink(1);
      tank.fill(this.currentRecipe.getRecipeFluid(), FluidAction.EXECUTE);
      return true;
    }
    return false;
  }
}
