package com.lothrazar.cyclicmagic.component.enchanter;
import javax.annotation.Nullable;
import com.lothrazar.cyclicmagic.block.base.TileEntityBaseMachineInvo;
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
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.FluidTankProperties;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;

public class TileEntityEnchanter extends TileEntityBaseMachineInvo implements ITickable, IFluidHandler, ITileRedstoneToggle {
  public static final int TANK_FULL = 10000;
  //20mb per xp following convention set by EnderIO; OpenBlocks; and Reliquary https://github.com/PrinceOfAmber/Cyclic/issues/599
  public static final int FLUID_PER_EXP = 20;
  public static final int TIMER_FULL = 22;
  public static final int SLOT_BOOK = 0;
  public static final int SLOT_OUTPUT = 1;
  private static final int FLUID_COST = FLUID_PER_EXP * 250;//so this many exp points
  private static final  int MAX_LEVEL = 30;
  public static enum Fields {
    TIMER, EXP, REDSTONE;//MIGHT remove redstone eh
  }
 
  private int timer = 0;
  private int needsRedstone = 0;
  //  private boolean isLegacy = false;//newly placed ones are NOT legacy for sure
  public FluidTank tank = new FluidTank(TANK_FULL);
  public TileEntityEnchanter() {
    super(2);
    this.setSlotsForExtract(SLOT_OUTPUT);
    this.setSlotsForInsert(SLOT_BOOK);
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
 
    if (this.getCurrentFluid() < 0) {
      this.setCurrentFluid(0);
    }
    this.timer--;
    if (this.timer <= 0) {
      this.timer = TIMER_FULL;
      ItemStack inputStack = this.getStackInSlot(SLOT_BOOK);
       
      ItemStack outputStack = ItemStack.EMPTY;
      if ((inputStack.getItem() == Items.BOOK || inputStack.isItemEnchantable()) &&
          this.hasEnoughFluid() &&
          this.getStackInSlot(SLOT_OUTPUT).isEmpty()) {
        
        outputStack = inputStack.copy();
        outputStack.setCount(1);
        //reduce it by 1
        inputStack.shrink(1);
 
//(int) (5.0F + diff.getClampedAdditionalDifficulty() * (float) world.rand.nextInt(18));
        outputStack =  EnchantmentHelper.addRandomEnchantment(world.rand, outputStack, MAX_LEVEL, true);
        this.setInventorySlotContents(SLOT_BOOK, inputStack);
        this.setInventorySlotContents(SLOT_OUTPUT, outputStack);
        this.drain(FLUID_COST, true);
        
      }
    }
  }
  private boolean hasEnoughFluid() {
 
    FluidStack contains = this.tank.getFluid();
    return (contains != null && contains.getFluid() == FluidsRegistry.fluid_exp
        && contains.amount > FLUID_COST);
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
  @Override
  public int getField(int id) {
    switch (Fields.values()[id]) {
      case TIMER:
        return timer;
      case EXP:
        return this.getCurrentFluid();
      case REDSTONE:
        return needsRedstone;
    }
    return -1;
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
  public void setField(int id, int value) {
    switch (Fields.values()[id]) {
      case TIMER:
        this.timer = value;
      break;
      case EXP:
        this.setCurrentFluid(value);
      break;
      case REDSTONE:
        this.needsRedstone = value % 2;
      break;
      default:
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
      fluid = new FluidStack(FluidsRegistry.fluid_exp, amt);
    }
    fluid.amount = amt;
    // ModCyclic.logger.info("setCurrentFluid to " + fluid.amount + " from isClient = " + this.world.isRemote);
    this.tank.setFluid(fluid);
  }
  /******************************
   * fluid properties here
   ******************************/
  @Override
  public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
    return (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY);
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
    if (resource.getFluid() != FluidsRegistry.fluid_exp) {
      return 0;
    }
    int result = tank.fill(resource, doFill);
    this.setField(Fields.EXP.ordinal(), result);
    return result;
  }
  @Override
  public FluidStack drain(FluidStack resource, boolean doDrain) {
    if (resource.getFluid() != FluidsRegistry.fluid_exp) {
      return resource;
    }
    FluidStack result = tank.drain(resource, doDrain);
    this.setField(Fields.EXP.ordinal(), result.amount);
    return result;
  }
  @Override
  public FluidStack drain(int maxDrain, boolean doDrain) {
    FluidStack result = tank.drain(maxDrain, doDrain);
    this.setField(Fields.EXP.ordinal(), result.amount);
    return result;
  }
  @Override
  public void toggleNeedsRedstone() {
    this.setField(Fields.REDSTONE.ordinal(), (this.needsRedstone + 1) % 2);
  }
  @Override
  public boolean onlyRunIfPowered() {
    return this.needsRedstone == 1;
  }
}
