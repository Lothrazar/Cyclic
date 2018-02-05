package com.lothrazar.cyclicmagic.component.cable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import javax.annotation.Nullable;
import com.google.common.collect.Maps;
import com.lothrazar.cyclicmagic.block.EnergyStore;
import com.lothrazar.cyclicmagic.block.base.ITileCable;
import com.lothrazar.cyclicmagic.block.base.TileEntityBaseMachineFluid;
import com.lothrazar.cyclicmagic.component.cable.BlockBaseCable.EnumConnectType;
import com.lothrazar.cyclicmagic.component.cablefluid.TileEntityFluidCable;
import com.lothrazar.cyclicmagic.component.cableitem.TileEntityItemCable;
import com.lothrazar.cyclicmagic.util.UtilFluid;
import com.lothrazar.cyclicmagic.util.UtilItemStack;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

public class TileEntityBaseCable extends TileEntityBaseMachineFluid implements ITickable, ITileCable {
  private static final int TIMER_SIDE_INPUT = 15;
  private static final int TRANSFER_FLUID_PER_TICK = 100;
  private static final int TRANSFER_ENERGY_PER_TICK = 8 * 1000;
  private boolean itemTransport = false;
  private boolean fluidTransport = false;
  private boolean energyTransport = false;
  private Map<EnumFacing, Integer> mapIncomingFluid = Maps.newHashMap();
  protected Map<EnumFacing, Integer> mapIncomingItems = Maps.newHashMap();
  private Map<EnumFacing, Integer> mapIncomingEnergy = Maps.newHashMap();
  private EnergyStore cableEnergyStore;
  public EnumConnectType north, south, east, west, up, down;
  public TileEntityBaseCable(int invoSize, int fluidTankSize, int powerPerTick) {
    super(invoSize, fluidTankSize);
    //TODO: fix input awkwardness 
    if (powerPerTick > 0) {
      cableEnergyStore = new EnergyStore(TRANSFER_ENERGY_PER_TICK);
    }
    for (EnumFacing f : EnumFacing.values()) {
      mapIncomingFluid.put(f, 0);
    }
    for (EnumFacing f : EnumFacing.values()) {
      mapIncomingItems.put(f, 0);
    }
    for (EnumFacing f : EnumFacing.values()) {
      mapIncomingEnergy.put(f, 0);
    }
  }
  public void setItemTransport() {
    this.itemTransport = true;
  }
  public void setFluidTransport() {
    this.fluidTransport = true;
  }
  public void setPowerTransport() {
    this.energyTransport = true;
  }
  public boolean isItemPipe() {
    return this.itemTransport;
  }
  public boolean isFluidPipe() {
    return this.fluidTransport;
  }
  public boolean isEnergyPipe() {
    return this.energyTransport;
  }
  public Map<EnumFacing, EnumConnectType> getConnects() {
    Map<EnumFacing, EnumConnectType> map = Maps.newHashMap();
    map.put(EnumFacing.NORTH, north);
    map.put(EnumFacing.SOUTH, south);
    map.put(EnumFacing.EAST, east);
    map.put(EnumFacing.WEST, west);
    map.put(EnumFacing.UP, up);
    map.put(EnumFacing.DOWN, down);
    return map;
  }
  public void setConnects(Map<EnumFacing, EnumConnectType> map) {
    north = map.get(EnumFacing.NORTH);
    south = map.get(EnumFacing.SOUTH);
    east = map.get(EnumFacing.EAST);
    west = map.get(EnumFacing.WEST);
    up = map.get(EnumFacing.UP);
    down = map.get(EnumFacing.DOWN);
  }
  @Override
  public void readFromNBT(NBTTagCompound compound) {
    super.readFromNBT(compound);
    for (EnumFacing f : EnumFacing.values()) {
      mapIncomingItems.put(f, compound.getInteger(f.getName() + "_incoming"));
    }
    for (EnumFacing f : EnumFacing.values()) {
      mapIncomingFluid.put(f, compound.getInteger(f.getName() + "_incfluid"));
    }
    for (EnumFacing f : EnumFacing.values()) {
      mapIncomingEnergy.put(f, compound.getInteger(f.getName() + "_incenergy"));
    }
    if (compound.hasKey("north"))
      north = EnumConnectType.valueOf(compound.getString("north"));
    if (compound.hasKey("south"))
      south = EnumConnectType.valueOf(compound.getString("south"));
    if (compound.hasKey("east"))
      east = EnumConnectType.valueOf(compound.getString("east"));
    if (compound.hasKey("west"))
      west = EnumConnectType.valueOf(compound.getString("west"));
    if (compound.hasKey("up"))
      up = EnumConnectType.valueOf(compound.getString("up"));
    if (compound.hasKey("down"))
      down = EnumConnectType.valueOf(compound.getString("down"));
    if (this.cableEnergyStore != null && compound.hasKey("powercable")) {
      CapabilityEnergy.ENERGY.readNBT(cableEnergyStore, null, compound.getTag("powercable"));
    }
  }
  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound compound) {
    super.writeToNBT(compound);
    for (EnumFacing f : EnumFacing.values()) {
      compound.setInteger(f.getName() + "_incoming", mapIncomingItems.get(f));
    }
    for (EnumFacing f : EnumFacing.values()) {
      compound.setInteger(f.getName() + "_incfluid", mapIncomingFluid.get(f));
    }
    for (EnumFacing f : EnumFacing.values()) {
      compound.setInteger(f.getName() + "_incenergy", mapIncomingEnergy.get(f));
    }
    if (cableEnergyStore != null) {
      compound.setTag("powercable", CapabilityEnergy.ENERGY.writeNBT(cableEnergyStore, null));
    }
    if (north != null)
      compound.setString("north", north.toString());
    if (south != null)
      compound.setString("south", south.toString());
    if (east != null)
      compound.setString("east", east.toString());
    if (west != null)
      compound.setString("west", west.toString());
    if (up != null)
      compound.setString("up", up.toString());
    if (down != null)
      compound.setString("down", down.toString());
    return compound;
  }
  public void updateIncomingFluidFace(EnumFacing inputFrom) {
    mapIncomingFluid.put(inputFrom, TIMER_SIDE_INPUT);
  }
  private boolean isFluidIncomingFromFace(EnumFacing face) {
    return mapIncomingFluid.get(face) > 0;
  }
  private boolean isEnergyIncomingFromFace(EnumFacing face) {
    return mapIncomingEnergy.get(face) > 0;
  }
  public void updateIncomingEnergyFace(EnumFacing inputFrom) {
    mapIncomingEnergy.put(inputFrom, TIMER_SIDE_INPUT);
  }
  public void updateIncomingItemFace(EnumFacing inputFrom) {
    this.mapIncomingItems.put(inputFrom, TIMER_SIDE_INPUT);
  }
  private boolean isItemIncomingFromFace(EnumFacing face) {
    return mapIncomingItems.get(face) > 0;
  }
  @Override
  public void update() {
    if (this.fluidTransport)
      this.tickDownIncomingFluidFaces();
    if (this.itemTransport)
      this.tickDownIncomingItemFaces();
    if (this.energyTransport)
      this.tickDownIncomingPowerFaces();
    //tick down any incoming sides
    //now look over any sides that are NOT incoming, try to export
    BlockPos posTarget;
    //Actually shuffle the positions. if we are at a 3 way juncture, spread out where it goes first
    ArrayList<Integer> shuffledFaces = new ArrayList<>();
    for (int i = 0; i < EnumFacing.values().length; i++) {
      shuffledFaces.add(i);
    }
    TileEntity tileTarget;
    Collections.shuffle(shuffledFaces);
    for (int i : shuffledFaces) {
      EnumFacing f = EnumFacing.values()[i];
      if (this.isItemPipe()) {
        if (this.isItemIncomingFromFace(f) == false) {
          ItemStack stackToExport = this.getStackInSlot(0).copy();
          //ok,  not incoming from here. so lets output some
          posTarget = pos.offset(f);
          tileTarget = world.getTileEntity(posTarget);
          if (tileTarget == null) {
            continue;
          }
          boolean outputSuccess = false;
          ItemStack leftAfterDeposit = UtilItemStack.tryDepositToHandler(world, posTarget, f.getOpposite(), stackToExport);
          if (leftAfterDeposit.getCount() < stackToExport.getCount()) { //something moved!
            //then save result
            this.setInventorySlotContents(0, leftAfterDeposit);
            outputSuccess = true;
          }
          if (outputSuccess && world.getTileEntity(posTarget) instanceof TileEntityItemCable) {
            TileEntityItemCable cable = (TileEntityItemCable) world.getTileEntity(posTarget);
            cable.updateIncomingItemFace(f.getOpposite());
          }
        }
      }
      if (this.isFluidPipe()) {
        if (this.isFluidIncomingFromFace(f) == false) {
          //ok, fluid is not incoming from here. so lets output some
          posTarget = pos.offset(f);
          int toFlow = TRANSFER_FLUID_PER_TICK;
          if (hasAnyIncomingFluidFaces() && toFlow >= tank.getFluidAmount()) {
            toFlow = tank.getFluidAmount() - 1;//keep at least 1 unit in the tank if flow is moving
          }
          boolean outputSuccess = UtilFluid.tryFillPositionFromTank(world, posTarget, f.getOpposite(), tank, toFlow);
          if (outputSuccess && world.getTileEntity(posTarget) instanceof TileEntityFluidCable) {
            //TODO: not so compatible with other fluid systems. itl do i guess
            TileEntityBaseCable cable = (TileEntityBaseCable) world.getTileEntity(posTarget);
            cable.updateIncomingFluidFace(f.getOpposite());
          }
        }
      }
      if (this.isEnergyPipe()) {
        if (this.isEnergyIncomingFromFace(f) == false) {
          posTarget = pos.offset(f);
          IEnergyStorage handlerHere = this.getCapability(CapabilityEnergy.ENERGY, f);
          tileTarget = world.getTileEntity(posTarget);
          if (tileTarget == null) {
            continue;
          }
          IEnergyStorage handlerOutput = tileTarget.getCapability(CapabilityEnergy.ENERGY, f);
          if (handlerHere != null && handlerOutput != null
              && handlerHere.canExtract() && handlerOutput.canReceive()) {
            //first simulate
            int drain = handlerHere.extractEnergy(TRANSFER_ENERGY_PER_TICK, true);
            if (drain > 0) {
              //now push it into output, but find out what was ACTUALLY taken
              int filled = handlerOutput.receiveEnergy(drain, false);
              //now actually drain that much from here
              handlerHere.extractEnergy(filled, false);
              // ModCyclic.logger.log("power transfer " + filled + " into target   " +f);
              //              ModCyclic.logger.log("result handlerOutput" +handlerOutput.getEnergyStored());
              //              ModCyclic.logger.log("result handlerHere" +handlerHere.getEnergyStored());
              if (tileTarget instanceof TileEntityBaseCable) {
                //TODO: not so compatible with other fluid systems. itl do i guess
                TileEntityBaseCable cable = (TileEntityBaseCable) tileTarget;
                if (cable.isPowered())
                  cable.updateIncomingEnergyFace(f.getOpposite());
              }
              //              return;// stop now because only pull from one side at a time
            }
          }
        }
        //        else 
        //          ModCyclic.logger.log("power blocked going out to this face " + f);
      }
    }
  }
  public boolean hasAnyIncomingFluidFaces() {
    for (EnumFacing f : EnumFacing.values()) {
      if (mapIncomingFluid.get(f) > 0)
        return true;
    }
    return false;
  }
  public boolean hasAnyIncomingEnergyFaces() {
    for (EnumFacing f : EnumFacing.values()) {
      if (mapIncomingEnergy.get(f) > 0)
        return true;
    }
    return false;
  }
  public void tickDownIncomingFluidFaces() {
    for (EnumFacing f : EnumFacing.values()) {
      if (mapIncomingFluid.get(f) > 0)
        mapIncomingFluid.put(f, mapIncomingFluid.get(f) - 1);
    }
  }
  public void tickDownIncomingItemFaces() {
    for (EnumFacing f : EnumFacing.values()) {
      if (mapIncomingItems.get(f) > 0)
        mapIncomingItems.put(f, mapIncomingItems.get(f) - 1);
    }
  }
  public void tickDownIncomingPowerFaces() {
    for (EnumFacing f : EnumFacing.values()) {
      if (mapIncomingEnergy.get(f) > 0)
        mapIncomingEnergy.put(f, mapIncomingEnergy.get(f) - 1);
    }
  }
  public String getIncomingStrings() {
    String in = "";
    for (EnumFacing f : EnumFacing.values()) {
      if (mapIncomingFluid.get(f) > 0)
        in += f.name().toLowerCase() + " ";
    }
    return in.trim();
  }
  @Override
  public EnumConnectType north() {
    return north;
  }
  @Override
  public EnumConnectType south() {
    return south;
  }
  @Override
  public EnumConnectType east() {
    return east;
  }
  @Override
  public EnumConnectType west() {
    return west;
  }
  @Override
  public EnumConnectType up() {
    return up;
  }
  @Override
  public EnumConnectType down() {
    return down;
  }
  @Override
  public AxisAlignedBB getRenderBoundingBox() {
    double renderExtention = 1.0d;
    AxisAlignedBB bb = new AxisAlignedBB(pos.getX() - renderExtention, pos.getY() - renderExtention, pos.getZ() - renderExtention, pos.getX() + 1 + renderExtention, pos.getY() + 1 + renderExtention, pos.getZ() + 1 + renderExtention);
    return bb;
  }
  @Override
  public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
    if (this.isEnergyPipe() && capability == CapabilityEnergy.ENERGY) {
      return true;
    }
    return super.hasCapability(capability, facing);
  }
  @Override
  public <T> T getCapability(net.minecraftforge.common.capabilities.Capability<T> capability, @javax.annotation.Nullable net.minecraft.util.EnumFacing facing) {
    if (this.isEnergyPipe() && capability == CapabilityEnergy.ENERGY) {
      return CapabilityEnergy.ENERGY.cast(this.cableEnergyStore);
    }
    return super.getCapability(capability, facing);
  }
}
