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
package com.lothrazar.cyclicmagic.block.exppylon;

import java.util.List;
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.block.core.TileEntityBaseMachineFluid;
import com.lothrazar.cyclicmagic.data.ITileRedstoneToggle;
import com.lothrazar.cyclicmagic.liquid.FluidTankFixDesync;
import com.lothrazar.cyclicmagic.util.UtilExperience;
import com.lothrazar.cyclicmagic.util.UtilItemStack;
import com.lothrazar.cyclicmagic.util.UtilSound;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

public class TileEntityXpPylon extends TileEntityBaseMachineFluid implements ITickable, ITileRedstoneToggle {

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
    SPRAY, COLLECT, NONE, BOTTLE;
  }

  private int collect = ActionMode.COLLECT.ordinal();

  public TileEntityXpPylon() {
    super(2);
    this.setSlotsForExtract(SLOT_OUTPUT);
    this.setSlotsForInsert(SLOT_INPUT);
    tank = new FluidTankFixDesync(TANK_FULL, this);
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
    if (this.collect == ActionMode.SPRAY.ordinal()) {
      updateSpray();
    }
    else {
      //do not collect from player while spraying, thats just a loop
      collectPlayerExperience();
    }
    this.timer--;
    if (this.timer <= 0) {
      //collecting and bottling on same timer loop
      this.timer = TIMER_FULL;
      if (this.collect == ActionMode.COLLECT.ordinal()) {
        updateCollection();
      }
      else if (this.collect == ActionMode.BOTTLE.ordinal()) {
        updateBottle();
      }
    }
  }

  private void collectPlayerExperience() {
    if (world.isRemote) {
      return;
    }
    List<EntityPlayer> players = world.getEntitiesWithinAABB(EntityPlayer.class, new AxisAlignedBB(this.getPos().up()));
    for (EntityPlayer p : players) {
      if (p.isSneaking() && UtilExperience.getExpTotal(p) > 0) {
        //go
        int addMeXp = 1;
        //if large total
        if (UtilExperience.getExpTotal(p) > 100) {
          addMeXp = 10;//drain fast if you have a large dump 
        }
        if (UtilExperience.getExpTotal(p) > 500) {
          addMeXp = 50;//drain fast if you have a large dump 
        }
        if (UtilExperience.getExpTotal(p) > 7000) {
          addMeXp = 500;//drain fast if you have a large dump 
        }
        int addMeFluid = addMeXp * FLUID_PER_EXP;
        //        ModCyclic.logger.log("BEFORE player  .getExpTotal() = " + UtilExperience.getExpTotal(p) + " DRAIN BY addMeXp=" + addMeXp);
        if (tank.getFluidAmount() + addMeFluid <= tank.getCapacity()) {
          UtilExperience.drainExp(p, addMeXp);
          //drain from player done  NOW go with fluid
          //          ModCyclic.logger.log("BEFORE .getFluidAmount() = " + tank.getFluidAmount());
          tank.fill(new FluidStack(FluidRegistry.getFluid("xpjuice"), addMeFluid), true);
          //          ModCyclic.logger.log("AFTER .getFluidAmount() = " + tank.getFluidAmount());
          //          ModCyclic.logger.log("AFTER player  .getExpTotal() = " + UtilExperience.getExpTotal(p));
          //          ModCyclic.logger.log("drainy tank.getFluidAmount() = " + tank.getFluidAmount());
          UtilSound.playSound(p, SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP);
          this.markDirty();
        }
      }
    }
  }

  /**
   * outgoing: convert fluid to EXP in a bottle
   */
  private void updateBottle() {
    if (world.isRemote) {
      return;
    }
    //so 11*20 is the fluid per bottle
    int fluidToDrain = XP_PER_BOTTLE * FLUID_PER_EXP;
    if (outputSlotHasRoom() && inputSlotHasSome() && this.getCurrentFluidStackAmount() >= fluidToDrain) {
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
    if (world.isRemote) {
      return;
    }
    //for example if we drain 200 fluid units, which gives us 10 xp in the orb
    int spewPerOrb = XP_PER_SPEWORB;
    int fluidToDrain = spewPerOrb * FLUID_PER_EXP;
    if (this.getCurrentFluidStackAmount() < fluidToDrain) {
      spewPerOrb = 1;//tank is low, do one tick drains
    }
    fluidToDrain = spewPerOrb * FLUID_PER_EXP;
    if (this.getCurrentFluidStackAmount() >= fluidToDrain) {
      FluidStack actuallyDrained = this.tank.drain(fluidToDrain, true);
      //was the correct amount drained
      if (actuallyDrained == null || actuallyDrained.amount == 0 || fluidToDrain != actuallyDrained.amount) {
        return;
      }
      EntityXPOrb orb = new EntityXPOrb(world);
      orb.setPositionAndUpdate(this.pos.getX() + 0.5, this.pos.getY() + 0.5, this.pos.getZ() + 0.5);
      ModCyclic.logger.log(actuallyDrained.amount + " = actaullyDrained; and SPEW = " + spewPerOrb);
      orb.xpValue = spewPerOrb;
      orb.delayBeforeCanPickup = 0;
      world.spawnEntity(orb);
      orb.addVelocity(Math.random() / 1000, 0.01, Math.random() / 1000);
    }
  }

  /**
   * incoming: convert EXP to fluid
   */
  private void updateCollection() {
    if (world.isRemote) {
      return;
    }
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
    tags.setInteger(NBT_REDST, this.needsRedstone);
    return super.writeToNBT(tags);
  }

  @Override
  public void readFromNBT(NBTTagCompound tags) {
    super.readFromNBT(tags);
    timer = tags.getInteger(NBT_TIMER);
    collect = tags.getInteger(NBT_COLLECT);
    needsRedstone = tags.getInteger(NBT_REDST);
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
        return this.getCurrentFluidStackAmount();
      case COLLECT:
        return collect;
      case REDSTONE:
        return needsRedstone;
      default:
      break;
    }
    return -1;
  }

  @Override
  public void setField(int id, int value) {
    switch (Fields.values()[id]) {
      case TIMER:
        this.timer = value;
      break;
      case EXP:
        if (this.tank.getFluid() == null) {
          this.tank.setFluid(new FluidStack(FluidRegistry.getFluid("xpjuice"), 0));
        }
        this.tank.setFluidAmount(value);
      break;
      case COLLECT:
        this.collect = value % ActionMode.values().length;
      break;
      case REDSTONE:
        this.needsRedstone = value % 2;
      break;
      default:
      break;
    }
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
