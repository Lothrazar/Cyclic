package com.lothrazar.cyclicmagic.component.anvil;
import javax.annotation.Nullable;
import com.lothrazar.cyclicmagic.block.base.TileEntityBaseMachineInvo;
import com.lothrazar.cyclicmagic.fluid.FluidTankBase;
import com.lothrazar.cyclicmagic.gui.ITileRedstoneToggle;
import com.lothrazar.cyclicmagic.registry.FluidsRegistry;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.world.DifficultyInstance;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.FluidTankProperties;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;

public class TileEntityAnvilAuto extends TileEntityBaseMachineInvo implements ITickable, IFluidHandler {
  public static final int TANK_FULL = 10000;
  public static final int TIMER_FULL = 15;
  public static final int SLOT_INPUT = 0;
  public static final int SLOT_OUTPUT = 1;
  public static int FLUID_COST = 75;
  public static enum Fields {
    TIMER, FLUID, FUEL, FUELMAX, FUELDISPLAY;//MIGHT remove redstone eh
  }
  private int timer = 0;
  private int needsRedstone = 0;
  //  private boolean isLegacy = false;//newly placed ones are NOT legacy for sure
  public FluidTankBase tank = new FluidTankBase(TANK_FULL);
  public TileEntityAnvilAuto() {
    super(3);
    this.setFuelSlot(2, BlockAnvilAuto.FUEL_COST);
    this.setSlotsForExtract(SLOT_OUTPUT);
    this.setSlotsForInsert(SLOT_INPUT);
    tank.setFluidAllowed(FluidRegistry.LAVA);
  }
  @Override
  public int[] getFieldOrdinals() {
    return super.getFieldArray(Fields.values().length);
  }
  @Override
  public void update() {
    if (this.isRunning() == false) {
      return;
    }
   
    ItemStack inputStack = this.getStackInSlot(SLOT_INPUT);
    if (inputStack.isEmpty()) {
      return;//no paying cost on empty work
    }
    this.spawnParticlesAbove();
    if (this.updateFuelIsBurning() == false) {
      return;
    }
    if (this.getCurrentFluid() < 0) {
      this.setCurrentFluid(0);
    }
    if (inputStack.isItemDamaged() == false) {
      //all done
      this.setInventorySlotContents(SLOT_OUTPUT, this.removeStackFromSlot(SLOT_INPUT));
      //      this.setInventorySlotContents(SLOT_INPUT, ItemStack.EMPTY);
      return;
    }
    this.timer--;
    if (this.timer <= 0) {
      this.timer = TIMER_FULL;
      //ItemStack outputStack = ItemStack.EMPTY;
      if (inputStack.isItemDamaged() &&
          this.hasEnoughFluid()) {
        inputStack.setItemDamage(inputStack.getItemDamage() - 1);
        //pay item cost and build the enchatned output item
        //
        //        inputStack.shrink(1);
        //        outputStack = inputStack.copy();
        //        outputStack.setCount(1);
        // 
        //        outputStack = EnchantmentHelper.addRandomEnchantment(world.rand, outputStack, MAX_LEVEL, true);
        //        this.setInventorySlotContents(SLOT_INPUT, inputStack);
        //        this.setInventorySlotContents(SLOT_OUTPUT, outputStack);
        this.drain(FLUID_COST, true);
      }
    }
  }
  private boolean hasEnoughFluid() {
    FluidStack contains = this.tank.getFluid();
    return ( contains.amount >= FLUID_COST);
  }
  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound tags) {
    tags.setInteger(NBT_TIMER, timer);
    tags.setTag(NBT_TANK, tank.writeToNBT(new NBTTagCompound()));
    tags.setInteger(NBT_REDST, this.needsRedstone);
    return super.writeToNBT(tags);
  }
  @Override
  public void readFromNBT(NBTTagCompound tags) {
    super.readFromNBT(tags);
    timer = tags.getInteger(NBT_TIMER);
    tank.readFromNBT(tags.getCompoundTag(NBT_TANK));
    this.needsRedstone = tags.getInteger(NBT_REDST);
  }
  @Override
  public int getFieldCount() {
    return Fields.values().length;
  }
  public int getCurrentFluid() {
    IFluidHandler fluidHandler = this.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, EnumFacing.UP);
    if (fluidHandler == null || fluidHandler.getTankProperties() == null || fluidHandler.getTankProperties().length == 0) {
      return 0;
    }
    FluidStack fluid = fluidHandler.getTankProperties()[0].getContents();
    return (fluid == null) ? 0 : fluid.amount;
  }
  public FluidStack getCurrentFluidStack() {
    IFluidHandler fluidHandler = this.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, EnumFacing.UP);
    if (fluidHandler == null || fluidHandler.getTankProperties() == null || fluidHandler.getTankProperties().length == 0) {
      return null;
    }
    return fluidHandler.getTankProperties()[0].getContents();
  }
  @Override
  public int getField(int id) {
    switch (Fields.values()[id]) {
      case TIMER:
        return timer;
      case FLUID:
        return this.getCurrentFluid();
      case FUEL:
        return this.getFuelCurrent();
      case FUELMAX:
        return this.getFuelMax();
      case FUELDISPLAY:
        return this.fuelDisplay;
    }
    return -1;
  }
  @Override
  public void setField(int id, int value) {
    switch (Fields.values()[id]) {
      case TIMER:
        this.timer = value;
      break;
      case FLUID:
        this.setCurrentFluid(value);
      break;
      case FUEL:
        this.setFuelCurrent(value);
      break;
      case FUELMAX:
      break;
      case FUELDISPLAY:
        this.fuelDisplay = value % 2;
      break;
    }
  }
  private void setCurrentFluid(int amt) {
    IFluidHandler fluidHandler = this.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, EnumFacing.UP);
    if (fluidHandler == null || fluidHandler.getTankProperties() == null || fluidHandler.getTankProperties().length == 0) {
      return;
    }
    FluidStack fluid = fluidHandler.getTankProperties()[0].getContents();
    if (fluid == null) {
      fluid = new FluidStack(FluidRegistry.LAVA, amt);
    }
    fluid.amount = amt;
    this.tank.setFluid(fluid);
  }
  /******************************
   * fluid properties here
   ******************************/
  @Override
  public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
    if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
      return true;
    }
    return super.hasCapability(capability, facing);
  }
  @Override
  public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
    if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
      return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(tank);
    }
    return super.getCapability(capability, facing);
  }
  @Override
  public IFluidTankProperties[] getTankProperties() {
    FluidTankInfo info = tank.getInfo();
    return new IFluidTankProperties[] { new FluidTankProperties(info.fluid, info.capacity, true, true) };
  }
  @Override
  public int fill(FluidStack resource, boolean doFill) {
//    if (resource.getFluid() != FluidRegistry.LAVA) {
//      return 0;
//    }
    int result = tank.fill(resource, doFill);
    this.setField(Fields.FLUID.ordinal(), result);
    return result;
  }
  @Override
  public FluidStack drain(FluidStack resource, boolean doDrain) {
//    if (resource.getFluid() != FluidRegistry.LAVA) {
//      return resource;
//    }
    FluidStack result = tank.drain(resource, doDrain);
    return result;
  }
  @Override
  public FluidStack drain(int maxDrain, boolean doDrain) {
    FluidStack result = tank.drain(maxDrain, doDrain);
    return result;
  }
}
