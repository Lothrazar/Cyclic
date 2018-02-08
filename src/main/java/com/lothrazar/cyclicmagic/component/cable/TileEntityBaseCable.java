package com.lothrazar.cyclicmagic.component.cable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import javax.annotation.Nullable;
import com.google.common.collect.Maps;
import com.lothrazar.cyclicmagic.block.EnergyStore;
import com.lothrazar.cyclicmagic.block.base.TileEntityBaseMachineFluid; 
import com.lothrazar.cyclicmagic.component.cable.fluid.TileEntityFluidCable;
import com.lothrazar.cyclicmagic.component.cable.item.TileEntityItemCable;
import com.lothrazar.cyclicmagic.util.UtilChat;
import com.lothrazar.cyclicmagic.util.UtilFluid;
import com.lothrazar.cyclicmagic.util.UtilItemStack;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fluids.FluidStack;

public class TileEntityBaseCable extends TileEntityBaseMachineFluid implements ITickable {
  private static final int TIMER_SIDE_INPUT = 15;
  private static final int TRANSFER_FLUID_PER_TICK = 500;
  private static final int TRANSFER_ENERGY_PER_TICK = 8 * 1000;
  private static final int TICKS_TEXT_CACHED = TIMER_SIDE_INPUT * 2;
  private int labelTimer = 0;
  private String labelText = "";
  private boolean itemTransport = false;
  private boolean fluidTransport = false;
  private boolean energyTransport = false;
  private Map<EnumFacing, Integer> mapIncomingFluid = Maps.newHashMap();
  protected Map<EnumFacing, Integer> mapIncomingItems = Maps.newHashMap();
  private Map<EnumFacing, Integer> mapIncomingEnergy = Maps.newHashMap();
  private EnergyStore cableEnergyStore;
 // public EnumConnectType north, south, east, west, up, down;
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
 
  @Override
  public void readFromNBT(NBTTagCompound compound) {
    super.readFromNBT(compound);
    labelText = compound.getString("label");
    labelTimer = compound.getInteger("labelt");
    for (EnumFacing f : EnumFacing.values()) {
      mapIncomingItems.put(f, compound.getInteger(f.getName() + "_incoming"));
      mapIncomingFluid.put(f, compound.getInteger(f.getName() + "_incfluid"));
      mapIncomingEnergy.put(f, compound.getInteger(f.getName() + "_incenergy"));
    }
 
    if (this.cableEnergyStore != null && compound.hasKey("powercable")) {
      CapabilityEnergy.ENERGY.readNBT(cableEnergyStore, null, compound.getTag("powercable"));
    }
  }
  public String getLabelTextOrEmpty() {
    return labelText.isEmpty() ? UtilChat.lang("cyclic.item.empty") : this.labelText;
  }
  /**
   * with normal item movement it moves too fast for user to read cache the current item for a few ticks so full item pipes dont show empty or flashing fast text
   */
  private void tickLabelText() {
    this.labelTimer--;
    if (this.labelTimer > 0) {
      return;
    }
    this.labelTimer = 0;
    this.labelText = "";
    List<String> validLabels = new ArrayList<String>();
    if (this.isItemPipe() && this.getStackInSlot(0).isEmpty() == false) {
      validLabels.add(this.getIncomingStringsItem());
    }
    if (this.isFluidPipe() && this.getCurrentFluidStack() != null) {
      FluidStack fs = this.getCurrentFluidStack();
      validLabels.add(this.getIncomingStringsFluid());
    }
    if (this.isEnergyPipe() &&
        this.cableEnergyStore != null && this.cableEnergyStore.getEnergyStored() > 0) {
      validLabels.add(this.getIncomingStringsEnergy());
    }
    //if its a multi pipe pick a random one
    if (validLabels.size() > 0) {
      this.labelText = validLabels.get(MathHelper.getInt(this.world.rand, 0, validLabels.size() - 1));
      this.labelTimer = TICKS_TEXT_CACHED;
    }
  }
  private String getIncomingStringsFromMap(Map<EnumFacing, Integer> map) {
    String in = "";
    for (EnumFacing f : EnumFacing.values()) {
      if (map.get(f) > 0)
        in += f.name().toLowerCase() + " ";
    }
    return in.trim();
  }
  private String getIncomingStringsFluid() {
    String tmpName = this.getCurrentFluidStack().getLocalizedName();
    String incoming = getIncomingStringsFromMap(this.mapIncomingFluid);
    if (incoming.isEmpty() == false) {
      tmpName += " " + UtilChat.lang("cyclic.fluid.flowing") + incoming;
    }
    return tmpName;
  }
  private String getIncomingStringsItem() {
    String tmpName = this.getStackInSlot(0).getDisplayName();
    String incoming = getIncomingStringsFromMap(this.mapIncomingItems);
    if (incoming.isEmpty() == false) {
      tmpName += " " + UtilChat.lang("cyclic.item.flowing") + incoming;
    }
    return tmpName;
  }
  private String getIncomingStringsEnergy() {
    String tmpName = this.cableEnergyStore.getEnergyStored() + "";
    String incoming = getIncomingStringsFromMap(this.mapIncomingEnergy);
    if (incoming.isEmpty() == false) {
      tmpName += " " + UtilChat.lang("cyclic.fluid.flowing") + incoming;
    }
    return tmpName;
  }
  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound compound) {
    super.writeToNBT(compound);
    compound.setString("label", labelText);
    compound.setInteger("labelt", labelTimer);
    for (EnumFacing f : EnumFacing.values()) {
      compound.setInteger(f.getName() + "_incoming", mapIncomingItems.get(f));
      compound.setInteger(f.getName() + "_incfluid", mapIncomingFluid.get(f));
      compound.setInteger(f.getName() + "_incenergy", mapIncomingEnergy.get(f));
    }
    if (cableEnergyStore != null) {
      compound.setTag("powercable", CapabilityEnergy.ENERGY.writeNBT(cableEnergyStore, null));
    }
 
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
    this.tickLabelText();
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
          
          tileTarget = world.getTileEntity(posTarget);
          if (tileTarget instanceof TileEntityBaseCable) {
            //TODO: not so compatible with other fluid systems. itl do i guess
            TileEntityBaseCable cable = (TileEntityBaseCable) tileTarget;
            if (outputSuccess&& cable.isItemPipe())
              cable.updateIncomingItemFace(f.getOpposite());
          }
          
//          if (outputSuccess && world.getTileEntity(posTarget) instanceof TileEntityItemCable) {
//            TileEntityItemCable cable = (TileEntityItemCable) world.getTileEntity(posTarget);
//            cable.updateIncomingItemFace(f.getOpposite());
//          }
        }
      }
      if (this.isFluidPipe()) {
        if (this.isFluidIncomingFromFace(f) == false) {
          //ok, fluid is not incoming from here. so lets output some
          posTarget = pos.offset(f);
          int toFlow = TRANSFER_FLUID_PER_TICK;
          if (hasAnyIncomingFluidFaces() && toFlow >= tank.getFluidAmount()) {
            toFlow = tank.getFluidAmount();//NOPE// - 1;//keep at least 1 unit in the tank if flow is moving
          }
          boolean outputSuccess = UtilFluid.tryFillPositionFromTank(world, posTarget, f.getOpposite(), tank, toFlow);

          tileTarget = world.getTileEntity(posTarget);
          if (tileTarget instanceof TileEntityBaseCable) {
            //TODO: not so compatible with other fluid systems. itl do i guess
            TileEntityBaseCable cable = (TileEntityBaseCable) tileTarget;
            if (outputSuccess&& cable.isFluidPipe())
              cable.updateIncomingFluidFace(f.getOpposite());
          }
          
//          if (outputSuccess && world.getTileEntity(posTarget) instanceof TileEntityFluidCable) {
//            //TODO: not so compatible with other fluid systems. itl do i guess
//            TileEntityBaseCable cable = (TileEntityBaseCable) world.getTileEntity(posTarget);
//            cable.updateIncomingFluidFace(f.getOpposite());
//          }
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
              if (tileTarget instanceof TileEntityBaseCable) {
                //TODO: not so compatible with other fluid systems. itl do i guess
                TileEntityBaseCable cable = (TileEntityBaseCable) tileTarget;
                if (cable.isEnergyPipe())
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
