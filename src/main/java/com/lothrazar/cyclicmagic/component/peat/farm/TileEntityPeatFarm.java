package com.lothrazar.cyclicmagic.component.peat.farm;
import java.util.Arrays;
import java.util.List;
import javax.annotation.Nullable;
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.block.EnergyStore;
import com.lothrazar.cyclicmagic.block.base.TileEntityBaseMachineInvo;
import com.lothrazar.cyclicmagic.data.Const;
import com.lothrazar.cyclicmagic.fluid.FluidTankBase;
import com.lothrazar.cyclicmagic.gui.ITileRedstoneToggle;
import com.lothrazar.cyclicmagic.util.UtilShape;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.FluidTankProperties;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;

public class TileEntityPeatFarm extends TileEntityBaseMachineInvo implements ITileRedstoneToggle, ITickable, IFluidHandler {
  private static final int DRAIN_COST = 1000;
  public static final int TANK_FULL = DRAIN_COST * 10;
  public static final int TIMER_FULL = 5;
  private static final int PER_TICK = 5;
  private static final int CAPACITY = 60 * 1000;
  public static enum Fields {
    REDSTONE, TIMER, FLUID;
  }
  private int needsRedstone = 1;
  public FluidTankBase tank = new FluidTankBase(TANK_FULL);
  private EnergyStore energy;
  private int blockPointer = 0;
  public TileEntityPeatFarm() {
    super(8);//inventory, water
    tank.setTileEntity(this);
    tank.setFluidAllowed(FluidRegistry.WATER);
    energy = new EnergyStore(CAPACITY);
    timer = TIMER_FULL;
    this.setSlotsForInsert(Arrays.asList(0, 1, 2, 3));
    this.setSlotsForExtract(Arrays.asList(4, 5, 6, 7));
  }
  Block baked = null;
  Block unbaked = null;
  // Item plantMatter = null;
  List<BlockPos> outer = null;
  private void init() {
    if (baked == null)
      baked = Block.getBlockFromName(Const.MODRES + "peat_baked");
    if (unbaked == null)
      unbaked = Block.getBlockFromName(Const.MODRES + "peat_unbaked");
    //    if (plantMatter == null)
    //      plantMatter = Item.getByNameOrId(Const.MODRES + "peat_biomass");
    if (outer == null) {
      outer = getShape();
      List<BlockPos> waterShape = UtilShape.squareHorizontalHollow(this.pos, 6);
      outer.addAll(waterShape);
    }
  }
  @Override
  public void update() {
    this.init();
    if (isRunning() == false) {
      return;
    }
    energy.extractEnergy(PER_TICK, false);
    if (energy.getEnergyStored() == 0) {
      return;
    }
    //GET VALIDATE ITEM
    this.timer--;
    if (this.timer > 0) {
      return;
    }
    //now loop for build/harvest 
    if (blockPointer < outer.size()) {
      BlockPos target = outer.get(blockPointer);
    //  ModCyclic.logger.error("?"+blockPointer+"  "+target);
      boolean placeWater = (target.getX() - pos.getX()) % 3 == 0
          && (target.getZ() - pos.getZ()) % 3 == 0;
      //based on pattern, try to place either water or the block
      if (placeWater) {
        tryPlaceWater(target);
      }
      else {
        tryPlacePeat(target);
      }
      //and move along
      this.timer = TIMER_FULL;
      blockPointer++;
    }
    else {
      blockPointer = 0;
      //    ModCyclic.logger.error("RESET");
    }
  }
  private void tryPlacePeat(BlockPos target) {
    for (int i = 0; i < 4; i++) {
      if (this.getStackInSlot(i).isItemEqual(new ItemStack(unbaked))) {
        tryHarvest(target);
        if (world.getBlockState(target).getBlock().isReplaceable(world, target)) {
          world.setBlockState(target, unbaked.getDefaultState());
          this.decrStackSize(i);
          return;
        }
      }
    }
    //  ModCyclic.logger.error("not enough blocks");
  }
  private void tryPlaceWater(BlockPos target) {
    if (world.isAirBlock(target) && tank.getFluidAmount() > DRAIN_COST) {
      tank.drain(DRAIN_COST, false);
      world.setBlockState(target, Blocks.FLOWING_WATER.getDefaultState());
    }
  }
  private void tryHarvest(BlockPos target) {
    if (world.getBlockState(target).getBlock() == baked) {
      //    ModCyclic.logger.error("HARVEST peat " + target);
      world.destroyBlock(target, true);
    }
  }
  private List<BlockPos> getShape() {
    List<BlockPos> outer = UtilShape.squareHorizontalHollow(this.pos, 7);
    outer.addAll(UtilShape.squareHorizontalHollow(this.pos, 5));
    return outer;
  }
  public int[] getFieldOrdinals() {
    return super.getFieldArray(Fields.values().length);
  }
  @Override
  public int getFieldCount() {
    return getFieldOrdinals().length;
  }
  @Override
  public void readFromNBT(NBTTagCompound compound) {
    super.readFromNBT(compound);
    this.needsRedstone = compound.getInteger(NBT_REDST);
    blockPointer = compound.getInteger("blockPointer");
    tank.readFromNBT(compound.getCompoundTag(NBT_TANK));
    CapabilityEnergy.ENERGY.readNBT(energy, null, compound.getTag("powercable"));
  }
  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound compound) {
    compound.setInteger(NBT_REDST, this.needsRedstone);
    compound.setTag(NBT_TANK, tank.writeToNBT(new NBTTagCompound()));
    compound.setTag("powercable", CapabilityEnergy.ENERGY.writeNBT(energy, null));
    compound.setInteger("blockPointer", blockPointer);
    return super.writeToNBT(compound);
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
  @Override
  public int getField(int id) {
    switch (Fields.values()[id]) {
      case REDSTONE:
        return this.needsRedstone;
      case TIMER:
        return this.timer;
      case FLUID:
        return this.getCurrentFluid();
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
    }
  }
  private int getCurrentFluid() {
    IFluidHandler fluidHandler = this.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, EnumFacing.UP);
    if (fluidHandler == null || fluidHandler.getTankProperties() == null || fluidHandler.getTankProperties().length == 0) {
      return 0;
    }
    FluidStack fluid = fluidHandler.getTankProperties()[0].getContents();
    return (fluid == null) ? 0 : fluid.amount;
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
    this.tank.setFluid(fluid);
  }
  @Override
  public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
    if (capability == CapabilityEnergy.ENERGY || capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
      return true;
    }
    return super.hasCapability(capability, facing);
  }
  @Override
  public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
    if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
      return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(tank);
    }
    if (capability == CapabilityEnergy.ENERGY) {
      return CapabilityEnergy.ENERGY.cast(this.energy);
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
    int result = tank.fill(resource, doFill);
    this.world.markChunkDirty(pos, this);
    this.setField(Fields.FLUID.ordinal(), result);
    return result;
  }
  @Override
  public FluidStack drain(FluidStack resource, boolean doDrain) {
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
