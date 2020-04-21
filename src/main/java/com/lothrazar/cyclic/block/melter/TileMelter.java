package com.lothrazar.cyclic.block.melter;

import java.util.function.Predicate;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import com.lothrazar.cyclic.base.FluidTankBase;
import com.lothrazar.cyclic.base.TileEntityBase;
import com.lothrazar.cyclic.capability.CustomEnergyStorage;
import com.lothrazar.cyclic.data.Const;
import com.lothrazar.cyclic.recipe.RecipeMelter;
import com.lothrazar.cyclic.registry.BlockRegistry;
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

public class TileMelter extends TileEntityBase implements ITickableTileEntity, INamedContainerProvider {

  public static int SLOT_INPUT = 0;
  public static int SLOT_OUT = 0;
  static final int MAX = 64000;
  public static final int CAPACITY = 64 * FluidAttributes.BUCKET_VOLUME;
  public static final int TRANSFER_FLUID_PER_TICK = FluidAttributes.BUCKET_VOLUME / 20;
  public FluidTankBase tank;
  private LazyOptional<IEnergyStorage> energy = LazyOptional.of(this::createEnergy);
  private LazyOptional<IItemHandler> inventory = LazyOptional.of(this::createHandler);
  private RecipeMelter currentRecipe;
  private int timer = 0;
  public final static int TIMER_FULL = Const.TICKS_PER_SEC * 8;

  public TileMelter() {
    super(BlockRegistry.Tiles.melter);
    tank = new FluidTankBase(this, CAPACITY, isFluidValid());
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
  public void read(CompoundNBT tag) {
    CompoundNBT fluid = tag.getCompound("fluid");
    tank.readFromNBT(fluid);
    energy.ifPresent(h -> ((INBTSerializable<CompoundNBT>) h).deserializeNBT(tag.getCompound("energy")));
    inventory.ifPresent(h -> ((INBTSerializable<CompoundNBT>) h).deserializeNBT(tag.getCompound("inv")));
    timer = tag.getInt("timer");
    super.read(tag);
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
    tag.putInt("timer", timer);
    return super.write(tag);
  }

  @Override
  public boolean hasFastRenderer() {
    return true;
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

  @Override
  public void setField(int field, int value) {}

  public float getCapacity() {
    return CAPACITY;
  }

  public FluidStack getFluid() {
    return tank.getFluid();
  }

  @Override
  public void setFluid(FluidStack fluid) {
    tank.setFluid(fluid);
  }

  public ItemStack getStackInputSlot() {
    IItemHandler inv = inventory.orElse(null);
    return (inv == null) ? ItemStack.EMPTY : inv.getStackInSlot(SLOT_INPUT);
  }

  private void findMatchingRecipe() {
    for (RecipeMelter rec : RecipeMelter.RECIPES) {
      if (rec.matches(this, world)) {
        currentRecipe = rec;
      }
    }
  }

  public boolean tryProcessRecipe() {
    if (currentRecipe != null) {
      int test = tank.fill(this.currentRecipe.getRecipeFluidOutput(), FluidAction.SIMULATE);
      if (test == this.currentRecipe.getRecipeFluidOutput().getAmount()) {
        //ok it has room for all the fluid none will be wasted
        this.getStackInputSlot().shrink(this.currentRecipe.getRecipeInput().getCount());
        tank.fill(this.currentRecipe.getRecipeFluidOutput(), FluidAction.EXECUTE);
        return true;
      }
    }
    return false;
  }

  @Override
  public void tick() {
    //TODO: do i have enough power
    // TODO: then drain power
    this.timer--;
    if (timer < 0) {
      timer = 0;
    }
    //look for a match
    this.findMatchingRecipe();
    if (timer == 0 && this.tryProcessRecipe()) {
      //reset timer
      this.timer = TIMER_FULL;
    }
  }
}
