package com.lothrazar.cyclicmagic.component.hydrator;
import java.util.Arrays;
import javax.annotation.Nullable;
import com.lothrazar.cyclicmagic.block.base.TileEntityBaseMachineInvo;
import com.lothrazar.cyclicmagic.gui.ITileRedstoneToggle;
import com.lothrazar.cyclicmagic.util.UtilItemStack;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidActionResult;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.FluidTankProperties;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fluids.capability.IFluidTankProperties;

public class TileEntityHydrator extends TileEntityBaseMachineInvo implements ITileRedstoneToggle, ITickable, IFluidHandler {
  public static final int RECIPE_SIZE = 4;
  public static final int TANK_FULL = 10000;
  private static final int SLOT_INFLUID = 8;
  public final static int TIMER_FULL = 40;
  public static enum Fields {
    REDSTONE, TIMER, FLUID, RECIPELOCKED;
  }
  public FluidTank tank = new FluidTank(TANK_FULL);
  private int recipeIsLocked = 0;
  private InventoryCrafting crafting = new InventoryCrafting(new ContainerDummy(), RECIPE_SIZE / 2, RECIPE_SIZE / 2);
  public TileEntityHydrator() {
    super(2 * RECIPE_SIZE + 1);// in, out,  fluid transfer
    timer = TIMER_FULL;
    this.tank.setTileEntity(this);
    this.setSlotsForInsert(Arrays.asList(0, 1, 2, 3));
    this.setSlotsForExtract(Arrays.asList(4, 5, 6, 7));
  }
  private int needsRedstone = 1;
  @Override
  public int[] getFieldOrdinals() {
    return super.getFieldArray(Fields.values().length);
  }
  @Override
  public void update() {
    tryFillTankFromItems();
    if (!isRunning()) {
      return;
    }
    //ignore timer when filling up water
    if (this.getCurrentFluid() == 0) {
      return;
    }
    if (this.updateTimerIsZero()) { // time to burn!
      this.spawnParticlesAbove();
      if (tryProcessRecipe()) {
        this.timer = TIMER_FULL;
      }
    }
  }
  /**
   * try to match a shaped or shapeless recipe
   * 
   * @return
   */
  private RecipeHydrate findMatchingRecipe() {
    for (int i = 0; i < RECIPE_SIZE; i++) {
      this.crafting.setInventorySlotContents(i, this.getStackInSlot(i).copy());
    }
    for (RecipeHydrate irecipe : BlockHydrator.recipesShaped) {
      if (irecipe.matches(this.crafting, world)) {
        return irecipe;
      }
    }
    for (RecipeHydrate irecipe : BlockHydrator.recipesShapeless) {
      if (irecipe.matches(this.crafting, world)) {
        return irecipe;
      }
    }
    return null;
  }
  public boolean tryProcessRecipe() {
    RecipeHydrate rec = findMatchingRecipe();
    if (rec != null && this.getCurrentFluid() >= rec.getFluidCost()) {
      if (rec.tryPayCost(this, this.tank, this.recipeIsLocked == 1)) {
        //only create the output if cost was successfully paid
        this.sendOutputItem(rec.getRecipeOutput());
      }
      return true;
    }
    return false;
  }
  public void tryFillTankFromItems() {
    ItemStack maybeBucket = this.getStackInSlot(SLOT_INFLUID);
    FluidStack f = FluidUtil.getFluidContained(maybeBucket);
    IFluidHandlerItem bucketHandler = FluidUtil.getFluidHandler(maybeBucket);
    if (f != null && bucketHandler != null && f.getFluid().equals(FluidRegistry.WATER)) {
      //https://github.com/BluSunrize/ImmersiveEngineering/blob/fc022675bb550318cbadc879b3f28dde511e29c3/src/main/java/blusunrize/immersiveengineering/common/blocks/wooden/TileEntityWoodenBarrel.java
      FluidActionResult r = FluidUtil.tryEmptyContainer(maybeBucket, tank, Fluid.BUCKET_VOLUME, null, true);
      //in the case of a full bucket, it becomes empty. 
      //also supports any other fluid holding item, simply draining that fixed amount each round
      if (r.success) {
        this.setInventorySlotContents(SLOT_INFLUID, r.result);
      }
    }
  }
  public void sendOutputItem(ItemStack itemstack) {
    for (int i = 3 + 1; i < 8; i++) {
      if (!itemstack.isEmpty() && itemstack.getMaxStackSize() != 0) {
        itemstack = tryMergeStackIntoSlot(itemstack, i);
      }
    }
    if (!itemstack.isEmpty() && itemstack.getMaxStackSize() != 0) { //FULL
      UtilItemStack.dropItemStackInWorld(this.getWorld(), this.pos.up(), itemstack);
    }
  }
  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound tags) {
    tags.setInteger(NBT_REDST, this.needsRedstone);
    tags.setTag(NBT_TANK, tank.writeToNBT(new NBTTagCompound()));
    tags.setInteger("rlock", recipeIsLocked);
    return super.writeToNBT(tags);
  }
  @Override
  public void readFromNBT(NBTTagCompound tags) {
    super.readFromNBT(tags);
    this.needsRedstone = tags.getInteger(NBT_REDST);
    this.recipeIsLocked = tags.getInteger("rlock");
    tank.readFromNBT(tags.getCompoundTag(NBT_TANK));
  }
  @Override
  public int getField(int id) {
    switch (Fields.values()[id]) {
      case REDSTONE:
        return this.needsRedstone;
      case TIMER:
        return this.timer;
      case FLUID:
        return this.getCurrentFluid();
      case RECIPELOCKED:
        return this.recipeIsLocked;
    }
    return -1;
  }
  @Override
  public void setField(int id, int value) {
    switch (Fields.values()[id]) {
      case REDSTONE:
        this.needsRedstone = value;
      break;
      case TIMER:
        this.timer = value;
      break;
      case FLUID:
        this.setCurrentFluid(value);
      break;
      case RECIPELOCKED:
        this.recipeIsLocked = value % 2;
      break;
    }
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
  private void setCurrentFluid(int amt) {
    IFluidHandler fluidHandler = this.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, EnumFacing.UP);
    if (fluidHandler == null || fluidHandler.getTankProperties() == null || fluidHandler.getTankProperties().length == 0) {
      return;
    }
    FluidStack fluid = fluidHandler.getTankProperties()[0].getContents();
    if (fluid == null) {
      fluid = new FluidStack(FluidRegistry.WATER, amt);
    }
    fluid.amount = amt;
    // ModCyclic.logger.info("setCurrentFluid to " + fluid.amount + " from isClient = " + this.world.isRemote);
    this.tank.setFluid(fluid);
  }
  @Override
  public int getFieldCount() {
    return Fields.values().length;
  }
  @Override
  public void toggleNeedsRedstone() {
    int val = this.needsRedstone + 1;
    if (val > 1) {
      val = 0;//hacky lazy way
    }
    this.setField(Fields.REDSTONE.ordinal(), val);
  }
  public boolean onlyRunIfPowered() {
    return this.needsRedstone == 1;
  }
  public float getFillRatio() {
    return tank.getFluidAmount() / tank.getCapacity();
  }
  /**
   * For the crafting inventory, since its never in GUI and is just used for auto processing
   * 
   * @author Sam
   */
  public static class ContainerDummy extends Container {
    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
      return false;
    }
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
    if (resource.getFluid() != FluidRegistry.WATER) {
      return 0;
    }
    int result = tank.fill(resource, doFill);
    this.world.markChunkDirty(pos, this);
    this.setField(Fields.FLUID.ordinal(), result);
    return result;
  }
  @Override
  public FluidStack drain(FluidStack resource, boolean doDrain) {
    if (resource.getFluid() != FluidRegistry.WATER) {
      return resource;
    }
    FluidStack result = tank.drain(resource, doDrain);
    this.world.markChunkDirty(pos, this);
    this.setField(Fields.FLUID.ordinal(), result.amount);
    return result;
  }
  @Override
  public FluidStack drain(int maxDrain, boolean doDrain) {
    FluidStack result = tank.drain(maxDrain, doDrain);
    this.world.markChunkDirty(pos, this);
    this.setField(Fields.FLUID.ordinal(), result.amount);
    return result;
  }
}
