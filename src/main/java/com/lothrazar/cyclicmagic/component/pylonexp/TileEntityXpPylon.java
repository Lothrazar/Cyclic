package com.lothrazar.cyclicmagic.component.pylonexp;
import java.util.List;
import javax.annotation.Nullable;
import com.lothrazar.cyclicmagic.block.base.TileEntityBaseMachineInvo;
import com.lothrazar.cyclicmagic.registry.FluidsRegistry;
import com.lothrazar.cyclicmagic.util.UtilItemStack;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.FluidTankProperties;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;

public class TileEntityXpPylon extends TileEntityBaseMachineInvo implements ITickable, IFluidHandler {
  public static final int TANK_FULL = 20000;
  private static final int VRADIUS = 2;
  private static final int XP_PER_SPEWORB = 50;
  private static final int XP_PER_BOTTLE = 11; // On impact with any non-liquid block it will drop experience orbs worth 3–11 experience points. 
  public static final int TIMER_FULL = 18;
  public static final int SLOT_INPUT = 0;
  public static final int SLOT_OUTPUT = 1;
  private static final String NBT_TIMER = "Timer";
  private static final String NBT_COLLECT = "collect";
  private static final String NBT_SPRAY = "spray";
  private static final String NBT_BOTTLE = "bottle";
  public final static int RADIUS = 16;
  private static final int[] SLOTS_EXTRACT = new int[] { SLOT_OUTPUT };
  private static final int[] SLOTS_INSERT = new int[] { SLOT_INPUT };
  public static enum Fields {
    TIMER, EXP, COLLECT, SPRAY, BOTTLE;//MIGHT remove redstone eh
  }
  private int timer = 0;
  private int collect = 1;
  private int bottle = 0;
  private int spray = 0;
  public FluidTank tank = new FluidTank(TANK_FULL);
  public TileEntityXpPylon() {
    super(2);
  }
  @Override
  public int[] getFieldOrdinals() {
    return super.getFieldArray(Fields.values().length);
  }
  @Override
  public void update() {
    if (this.collect == 1) {
      updateCollection();
    }
    if (this.bottle == 1) {
      updateBottle();
    }
    if (this.spray == 1) {
      updateSpray();
    }
  }
  private void updateSpray() {
    int toSpew = Math.min(XP_PER_SPEWORB, this.getCurrentFluid());
    if (toSpew > 0 && this.getCurrentFluid() >= toSpew) {
      FluidStack actuallyDrained = this.tank.drain(toSpew, true);
      //was the correct amount drained
      if (actuallyDrained == null || actuallyDrained.amount == 0) {
        return;
      }
      if (world.isRemote == false) {
        EntityXPOrb orb = new EntityXPOrb(world);
        orb.setPositionAndUpdate(this.pos.getX() + 0.5, this.pos.getY() + 0.5, this.pos.getZ() + 0.5);
        orb.xpValue = toSpew;
        orb.delayBeforeCanPickup = 0;
        world.spawnEntity(orb);
        setOrbVelocity(orb);
      }
    }
  }
  private void updateBottle() {
    this.timer--;
    if (this.timer <= 0) {
      this.timer = TIMER_FULL;
      if (outputSlotHasRoom() && inputSlotHasSome() && this.getCurrentFluid() > XP_PER_BOTTLE) {
        //pay the cost first
        FluidStack actuallyDrained = this.tank.drain(XP_PER_BOTTLE, true);
        if (actuallyDrained == null || actuallyDrained.amount == 0) {
          return;
        }
        outputSlotIncrement();
        inputSlotDecrement();
      }
    }
  }
  private void updateCollection() {
    //expand only goes ONE direction. so expand(3...) goes 3 in + x, but not both ways. for full boc centered at this..!! we go + and -
    AxisAlignedBB region = new AxisAlignedBB(this.getPos().up()).expand(RADIUS, VRADIUS, RADIUS).expand(-1 * RADIUS, -1 * VRADIUS, -1 * RADIUS);//expandXyz
    List<EntityXPOrb> orbs = getWorld().getEntitiesWithinAABB(EntityXPOrb.class, region);
    if (orbs != null) { //no timer just EAT
      for (EntityXPOrb orb : orbs) {
        if (orb.isDead || orb.delayBeforeCanPickup > 0) {
          continue;
        }
        this.tank.fill(new FluidStack(FluidsRegistry.fluid_exp, orb.getXpValue()), true);
        //we have no "set exp value" function so this is workaround to set value to zero
        orb.delayBeforeCanPickup = 9999;//set delay because it will be isDead=true for a little while until actually removed. prevent other mods getting dead orbs
        orb.xpValue = 0;
        getWorld().removeEntity(orb);//calls     orb.setDead(); for me
      }
    }
  }
  private void outputSlotIncrement() {
    ItemStack fullOnes = this.getStackInSlot(SLOT_OUTPUT);
    if (UtilItemStack.isEmpty(fullOnes)) {
      fullOnes = new ItemStack(Items.EXPERIENCE_BOTTLE);
    }
    else {
      fullOnes.grow(1);
    }
    this.setInventorySlotContents(SLOT_OUTPUT, fullOnes);
  }
  private boolean outputSlotHasRoom() {
    ItemStack fullOnes = this.getStackInSlot(SLOT_OUTPUT);
    return UtilItemStack.isEmpty(fullOnes) || (fullOnes.getCount() < 64);
  }
  private boolean inputSlotHasSome() {
    ItemStack emptyOnes = this.getStackInSlot(SLOT_INPUT);
    return !UtilItemStack.isEmpty(emptyOnes) && (emptyOnes.getCount() > 0);
  }
  private void inputSlotDecrement() {
    ItemStack fullOnes = this.getStackInSlot(SLOT_INPUT);
    fullOnes.shrink(1);
    if (fullOnes.getCount() == 0) {
      fullOnes = ItemStack.EMPTY;
    }
    this.setInventorySlotContents(SLOT_INPUT, fullOnes);
  }
  @Override
  public int[] getSlotsForFace(EnumFacing side) {
    if (side == EnumFacing.DOWN) {
      return SLOTS_EXTRACT;
    }
    else {
      return SLOTS_INSERT;
    }
  }
  //  private boolean tryDecrExp(int xpValue) {
  //    if (this.currentExp - xpValue < 0) { return false; }
  //    this.currentExp -= xpValue;
  //    return true;
  //  }
  //  private boolean tryIncrExp(int xpValue) {
  //    if (this.currentExp + xpValue > MAX_EXP_HELD) { return false; }
  //    this.currentExp += xpValue;
  //    return true;
  //  }
  private void setOrbVelocity(EntityXPOrb orb) {
    orb.addVelocity(Math.random() / 1000, 0.01, Math.random() / 1000);
  }
  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound tags) {
    tags.setInteger(NBT_TIMER, timer);
    tags.setInteger(NBT_COLLECT, this.collect);
    tags.setInteger(NBT_SPRAY, this.spray);
    tags.setInteger(NBT_BOTTLE, this.bottle);
    tags.setTag(NBT_TANK, tank.writeToNBT(new NBTTagCompound()));
    return super.writeToNBT(tags);
  }
  @Override
  public void readFromNBT(NBTTagCompound tags) {
    super.readFromNBT(tags);
    timer = tags.getInteger(NBT_TIMER);
    collect = tags.getInteger(NBT_COLLECT);
    spray = tags.getInteger(NBT_SPRAY);
    bottle = tags.getInteger(NBT_BOTTLE);
    tank.readFromNBT(tags.getCompoundTag(NBT_TANK));
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
      case BOTTLE:
        return bottle;
      case COLLECT:
        return collect;
      case SPRAY:
        return spray;
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
      case BOTTLE:
        this.bottle = value % 2;
      break;
      case COLLECT:
        this.collect = value % 2;
      break;
      case SPRAY:
        this.spray = value % 2;
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
    this.world.markChunkDirty(pos, this);
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
    //   this.world.markChunkDirty(pos, this);
    this.setField(Fields.EXP.ordinal(), result);
    return result;
  }
  @Override
  public FluidStack drain(FluidStack resource, boolean doDrain) {
    if (resource.getFluid() != FluidsRegistry.fluid_exp) {
      return resource;
    }
    FluidStack result = tank.drain(resource, doDrain);
    //   this.world.markChunkDirty(pos, this);
    this.setField(Fields.EXP.ordinal(), result.amount);
    return result;
  }
  @Override
  public FluidStack drain(int maxDrain, boolean doDrain) {
    FluidStack result = tank.drain(maxDrain, doDrain);
    // this.world.markChunkDirty(pos, this);
    this.setField(Fields.EXP.ordinal(), result.amount);
    return result;
  }
}
