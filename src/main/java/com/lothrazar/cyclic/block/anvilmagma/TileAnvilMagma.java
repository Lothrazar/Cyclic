package com.lothrazar.cyclic.block.anvilmagma;

import java.util.function.Predicate;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import com.lothrazar.cyclic.base.FluidTankBase;
import com.lothrazar.cyclic.base.TileEntityBase;
import com.lothrazar.cyclic.fluid.FluidMagmaHolder;
import com.lothrazar.cyclic.registry.DataTags;
import com.lothrazar.cyclic.registry.TileRegistry;
import com.lothrazar.cyclic.util.UtilItemStack;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.fluid.Fluid;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class TileAnvilMagma extends TileEntityBase implements INamedContainerProvider, ITickableTileEntity {

  private static final int OUT = 1;
  private static final int IN = 0;
  public static final int CAPACITY = 64 * FluidAttributes.BUCKET_VOLUME;
  ItemStackHandler inventory = new ItemStackHandler(2) {

    @Override
    public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
      if (slot == IN)
        return stack.isRepairable() &&
            stack.getDamage() > 0;
      if (slot == OUT)
        return stack.isRepairable() &&
            stack.getDamage() == 0;
      return true;
    }
  };
  private LazyOptional<IItemHandler> inventoryCap = LazyOptional.of(() -> inventory);
  public FluidTankBase tank;

  static enum Fields {
    TIMER, REDSTONE;
  }

  public TileAnvilMagma() {
    super(TileRegistry.anvil_magma);
    tank = new FluidTankBase(this, CAPACITY, isFluidValid());
    this.needsRedstone = 0;//default on
  }

  @Override
  public void tick() {
    if (this.requiresRedstone() && !this.isPowered()) {
      setLitProperty(false);
      return;
    }
    setLitProperty(true);
    ItemStack stack = inventory.getStackInSlot(0);
    if (stack.isEmpty() || stack.getItem().isIn(DataTags.IMMUNE)) {
      return;
    }
    final int repair = 100;//not power, fluid.
    boolean work = false;
    if (tank != null &&
        tank.getFluidAmount() >= repair &&
        stack.isRepairable() &&
        stack.getDamage() > 0) {
      //we can repair so steal some power 
      //ok drain power  
      work = true;
      tank.drain(repair, FluidAction.EXECUTE);
    }
    //shift to other slot
    if (work) {
      UtilItemStack.repairItem(stack);
      boolean done = stack.getDamage() == 0;
      //
      if (done && inventory.getStackInSlot(OUT).isEmpty()) {
        inventory.insertItem(1, stack.copy(), false);
        inventory.extractItem(0, stack.getCount(), false);
      }
    }
  }

  public Predicate<FluidStack> isFluidValid() {
    return p -> {
      Fluid fluid = p.getFluid();
      return fluid == FluidMagmaHolder.STILL.get()
      //          || fluid == Fluids.LAVA
      //          || fluid == Fluids.FLOWING_LAVA
      ;
    };
  }

  @Override
  public ITextComponent getDisplayName() {
    return new StringTextComponent(getType().getRegistryName().getPath());
  }

  @Nullable
  @Override
  public Container createMenu(int i, PlayerInventory playerInventory, PlayerEntity playerEntity) {
    return new ContainerAnvilMagma(i, world, pos, playerInventory, playerEntity);
  }

  @Override
  public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, Direction side) {
    if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
      return inventoryCap.cast();
    }
    if (cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
      return LazyOptional.of(() -> tank).cast();
    }
    return super.getCapability(cap, side);
  }

  @Override
  public void read(BlockState bs, CompoundNBT tag) {
    inventory.deserializeNBT(tag.getCompound(NBTINV));
    tank.readFromNBT(tag.getCompound("fluid"));
    super.read(bs, tag);
  }

  @Override
  public CompoundNBT write(CompoundNBT tag) {
    CompoundNBT fluid = new CompoundNBT();
    tank.writeToNBT(fluid);
    tag.put("fluid", fluid);
    tag.put(NBTINV, inventory.serializeNBT());
    return super.write(tag);
  }

  @Override
  public int getField(int id) {
    switch (Fields.values()[id]) {
      case REDSTONE:
        return this.needsRedstone;
      case TIMER:
        return this.timer;
      default:
      break;
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
        this.timer = value;
      break;
    }
  }

  public FluidStack getFluid() {
    return tank == null ? FluidStack.EMPTY : tank.getFluid();
  }
}
