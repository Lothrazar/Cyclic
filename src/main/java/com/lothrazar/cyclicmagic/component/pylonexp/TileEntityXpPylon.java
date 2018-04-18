/*******************************************************************************
 * The MIT License (MIT)
 * 
 * Copyright (C) 2014-2018 Sam Bassett (aka Lothrazar)
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 ******************************************************************************/
package com.lothrazar.cyclicmagic.component.pylonexp;

import java.util.List;
import javax.annotation.Nullable;
import com.lothrazar.cyclicmagic.block.base.TileEntityBaseMachineInvo;
import com.lothrazar.cyclicmagic.fluid.FluidTankBase;
import com.lothrazar.cyclicmagic.gui.ITileRedstoneToggle;
import com.lothrazar.cyclicmagic.util.UtilItemStack;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.FluidTankProperties;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;

public class TileEntityXpPylon extends TileEntityBaseMachineInvo implements ITickable, IFluidHandler, ITileRedstoneToggle {

  public static final int TANK_FULL = 1000000;
  private static final int XP_PER_SPEWORB = 50;
  //20mb per xp following convention set by EnderIO; OpenBlocks; and Reliquary https://github.com/PrinceOfAmber/Cyclic/issues/599
  public static final int FLUID_PER_EXP = 20;
  private static final int VRADIUS = 2;
  private static final int XP_PER_BOTTLE = 11; // On impact with any non-liquid block it will drop experience orbs worth 3–11 experience points. 
  public static final int TIMER_FULL = 22;
  public static final int SLOT_INPUT = 0;
  public static final int SLOT_OUTPUT = 1;
  private static final String NBT_TIMER = "Timer";
  private static final String NBT_COLLECT = "collect";
  public final static int RADIUS = 16;

  public static enum Fields {
    TIMER, EXP, COLLECT, REDSTONE;
  }

  public static enum ActionMode {
    SPRAY, COLLECT;
  }

  private int timer = 0;
  private int collect = 1;
  private int needsRedstone = 0;
  private boolean isLegacy = false;//newly placed ones are NOT legacy for sure
  public FluidTankBase tank = new FluidTankBase(TANK_FULL);

  public TileEntityXpPylon() {
    super(2);
    this.setSlotsForExtract(SLOT_OUTPUT);
    this.setSlotsForInsert(SLOT_INPUT);
    tank.setFluidAllowed(FluidRegistry.getFluid("xpjuice"));
  }

  @Override
  public boolean isItemValidForSlot(int index, ItemStack stack) {
    if (index == SLOT_INPUT) {
      return stack.getItem() == Items.GLASS_BOTTLE;
    }
    return super.isItemValidForSlot(index, stack);
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
    if (this.isLegacy) {
      int current = this.getCurrentFluid();
      //I used to hold EXP. now I hold fluid that has an exp value with this ratio
      this.isLegacy = false;//not legacy anymore , 100%
      this.setCurrentFluid(Math.min(current * FLUID_PER_EXP, TANK_FULL));
    }
    if (this.getCurrentFluid() < 0) {
      this.setCurrentFluid(0);
    }
    this.timer--;
    if (this.timer <= 0) {
      this.timer = TIMER_FULL;
      if (this.collect == ActionMode.COLLECT.ordinal()) {
        updateCollection();
      }
      if (this.collect == ActionMode.SPRAY.ordinal()) {
        updateSpray();
      }
      updateBottle();
    }
  }

  /**
   * outgoing: convert fluid to EXP in a bottle
   */
  private void updateBottle() {
    //so 11*20 is the fluid per bottle
    int fluidToDrain = XP_PER_BOTTLE * FLUID_PER_EXP;
    if (outputSlotHasRoom() && inputSlotHasSome() && this.getCurrentFluid() >= fluidToDrain) {
      //pay the cost first
      FluidStack actuallyDrained = this.tank.drain(fluidToDrain, true);
      if (actuallyDrained == null || actuallyDrained.amount == 0) {
        return;
      }
      this.outputSlotIncrement();
      this.inputSlotDecrement();
    }
  }

  /**
   * outgoing: convert fluid to EXP
   */
  private void updateSpray() {
    //for example if we drain 200 fluid units, which gives us 10 xp in the orb
    int fluidToDrain = XP_PER_SPEWORB * FLUID_PER_EXP;
    int toSpew = Math.min(fluidToDrain, this.getCurrentFluid());
    int expToRelease = toSpew / XP_PER_SPEWORB;
    if (expToRelease > 0 && this.getCurrentFluid() >= toSpew) {
      FluidStack actuallyDrained = this.tank.drain(toSpew, true);
      //was the correct amount drained
      if (actuallyDrained == null || actuallyDrained.amount == 0) {
        return;
      }
      if (world.isRemote == false) {
        EntityXPOrb orb = new EntityXPOrb(world);
        orb.setPositionAndUpdate(this.pos.getX() + 0.5, this.pos.getY() + 0.5, this.pos.getZ() + 0.5);
        orb.xpValue = expToRelease;
        orb.delayBeforeCanPickup = 0;
        world.spawnEntity(orb);
        orb.addVelocity(Math.random() / 1000, 0.01, Math.random() / 1000);
      }
    }
  }

  /**
   * incoming: convert EXP to fluid
   */
  private void updateCollection() {
    //expand only goes ONE direction. so expand(3...) goes 3 in + x, but not both ways. for full boc centered at this..!! we go + and -
    AxisAlignedBB region = new AxisAlignedBB(this.getPos().up()).expand(RADIUS, VRADIUS, RADIUS).expand(-1 * RADIUS, -1 * VRADIUS, -1 * RADIUS);//expandXyz
    List<EntityXPOrb> orbs = getWorld().getEntitiesWithinAABB(EntityXPOrb.class, region);
    if (orbs != null) { //no timer just EAT
      for (EntityXPOrb orb : orbs) {
        if (orb.isDead || orb.delayBeforeCanPickup > 0) {
          continue;
        }
        this.tank.fill(new FluidStack(FluidRegistry.getFluid("xpjuice"), orb.getXpValue() * FLUID_PER_EXP), true);
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
    return fullOnes.getCount() < 64;
  }

  private boolean inputSlotHasSome() {
    ItemStack emptyOnes = this.getStackInSlot(SLOT_INPUT);
    if (emptyOnes.getItem() != Items.GLASS_BOTTLE) {
      return false;
    }
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
  public NBTTagCompound writeToNBT(NBTTagCompound tags) {
    tags.setInteger(NBT_TIMER, timer);
    tags.setInteger(NBT_COLLECT, this.collect);
    tags.setTag(NBT_TANK, tank.writeToNBT(new NBTTagCompound()));
    tags.setInteger(NBT_REDST, this.needsRedstone);
    tags.setBoolean("legacy", this.isLegacy);
    return super.writeToNBT(tags);
  }

  @Override
  public void readFromNBT(NBTTagCompound tags) {
    super.readFromNBT(tags);
    timer = tags.getInteger(NBT_TIMER);
    collect = tags.getInteger(NBT_COLLECT);
    tank.readFromNBT(tags.getCompoundTag(NBT_TANK));
    this.needsRedstone = tags.getInteger(NBT_REDST);
    if (tags.hasKey("legacy") == false) {
      this.isLegacy = true;//old ones will not have this, so they ARE legacy
    }
    else
      this.isLegacy = tags.getBoolean("legacy");
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
      case COLLECT:
        return collect;
      case REDSTONE:
        return needsRedstone;
      default:
      break;
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
      case COLLECT:
        this.collect = value % 2;
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
      fluid = new FluidStack(FluidRegistry.getFluid("xpjuice"), amt);
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
    if (resource.getFluid() != FluidRegistry.getFluid("xpjuice")) {
      return 0;
    }
    int result = tank.fill(resource, doFill);
    this.setField(Fields.EXP.ordinal(), result);
    return result;
  }

  @Override
  public FluidStack drain(FluidStack resource, boolean doDrain) {
    if (resource.getFluid() != FluidRegistry.getFluid("xpjuice")) {
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
