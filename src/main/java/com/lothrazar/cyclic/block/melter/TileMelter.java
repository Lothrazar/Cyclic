package com.lothrazar.cyclic.block.melter;

import java.util.function.Predicate;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import com.lothrazar.cyclic.ConfigManager;
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
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.INBTSerializable;
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
  public FluidTankBase tank;
  private LazyOptional<IEnergyStorage> energy = LazyOptional.of(this::createEnergy);
  private LazyOptional<IItemHandler> inventory = LazyOptional.of(this::createHandler);
  private RecipeMelter currentRecipe;
  public final static int TIMER_FULL = Const.TICKS_PER_SEC * 3;

  public static enum Fields {
    REDSTONE, TIMER, RENDER;
  }

  public TileMelter() {
    super(TileRegistry.melter);
    tank = new FluidTankBase(this, CAPACITY, isFluidValid());
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
    return super.getField(field);
  }

  private IEnergyStorage createEnergy() {
    return new CustomEnergyStorage(MAX, MAX);
  }

  private IItemHandler createHandler() {
    return new ItemStackHandler(2);
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
    energy.ifPresent(h -> ((INBTSerializable<CompoundNBT>) h).deserializeNBT(tag.getCompound("energy")));
    inventory.ifPresent(h -> ((INBTSerializable<CompoundNBT>) h).deserializeNBT(tag.getCompound("inv")));
    super.read(bs, tag);
  }

  @Override
  public CompoundNBT write(CompoundNBT tag) {
    CompoundNBT fluid = new CompoundNBT();
    tank.writeToNBT(fluid);
    tag.put("fluid", fluid);
    energy.ifPresent(h -> {
      CompoundNBT compound = ((INBTSerializable<CompoundNBT>) h).serializeNBT();
      tag.put("energy", compound);
    });
    inventory.ifPresent(h -> {
      CompoundNBT compound = ((INBTSerializable<CompoundNBT>) h).serializeNBT();
      tag.put("inv", compound);
    });
    return super.write(tag);
  }

  @Override
  public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, Direction side) {
    if (cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
      return LazyOptional.of(() -> tank).cast();
    }
    if (cap == CapabilityEnergy.ENERGY) {
      return energy.cast();
    }
    if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
      return inventory.cast();
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
    IItemHandler inv = inventory.orElse(null);
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

  @Override
  public void tick() {
    IEnergyStorage en = this.energy.orElse(null);
    if (en == null) {
      return;
    }
    this.findMatchingRecipe();
    if (currentRecipe == null) {
      return;
    }
    this.timer--;
    if (timer < 0) {
      timer = 0;
    }
    final int cost = ConfigManager.MELTERPOWER.get();
    if (en.getEnergyStored() < cost) {
      return;//broke
    }
    if (timer == 0 && this.tryProcessRecipe()) {
      this.timer = TIMER_FULL;
      en.extractEnergy(cost, false);
    }
  }

  private boolean tryProcessRecipe() {
    //    IItemHandler itemsHere = this.inventory.orElse(null);
    int test = tank.fill(this.currentRecipe.getRecipeFluid(), FluidAction.SIMULATE);
    if (test == this.currentRecipe.getRecipeFluid().getAmount()) {
      //ok it has room for all the fluid none will be wasted
      this.getStackInputSlot(0).shrink(1);
      this.getStackInputSlot(1).shrink(1);
      tank.fill(this.currentRecipe.getRecipeFluid(), FluidAction.EXECUTE);
      return true;
    }
    return false;
  }
}
