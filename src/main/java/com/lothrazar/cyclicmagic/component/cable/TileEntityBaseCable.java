package com.lothrazar.cyclicmagic.component.cable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import com.google.common.collect.Maps;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.lothrazar.cyclicmagic.block.base.ITileCable;
import com.lothrazar.cyclicmagic.block.base.TileEntityBaseMachineFluid;
import com.lothrazar.cyclicmagic.component.cable.BlockBaseCable.EnumConnectType;
import com.lothrazar.cyclicmagic.component.fluidtransfer.TileEntityFluidCable;
import com.lothrazar.cyclicmagic.util.UtilFluid;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

public class TileEntityBaseCable extends TileEntityBaseMachineFluid implements ITickable, ITileCable {
  private static final int TIMER_SIDE_INPUT = 15;
  private static final int TRANSFER_PER_TICK = 100;
  private boolean itemTransport = false;
  private boolean fluidTransport = false;
  private boolean powerTransport = false;
  private Map<EnumFacing, Integer> mapIncomingFluid = Maps.newHashMap();
  private Map<EnumFacing, Integer> mapIncomingItems = Maps.newHashMap();
  private Map<EnumFacing, Integer> mapIncomingPower = Maps.newHashMap();
  private BlockPos connectedInventory;
  public EnumConnectType north, south, east, west, up, down;
  public TileEntityBaseCable(int invoSize, int fluidTankSize) {
    super(invoSize, fluidTankSize);
    for (EnumFacing f : EnumFacing.values()) {
      mapIncomingFluid.put(f, 0);
    }
    for (EnumFacing f : EnumFacing.values()) {
      mapIncomingItems.put(f, 0);
    }
    for (EnumFacing f : EnumFacing.values()) {
      mapIncomingPower.put(f, 0);
    }
  }
  public void setItemTransport() {
    this.itemTransport = true;
  }
  public void setFluidTransport() {
    this.fluidTransport = true;
  }
  public void setPowerTransport() {
    this.powerTransport = true;
  }
  public boolean isItemPipe() {
    return this.itemTransport;
  }
  public boolean isFluidPipe() {
    return this.fluidTransport;
  }
  public boolean isPowerPipe() {
    return this.powerTransport;
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
  @SuppressWarnings("serial")
  @Override
  public void readFromNBT(NBTTagCompound compound) {
    super.readFromNBT(compound);
    for (EnumFacing f : EnumFacing.values()) {
      mapIncomingFluid.put(f, compound.getInteger(f.getName() + "_incoming"));
    }
    connectedInventory = new Gson().fromJson(compound.getString("connectedInventory"), new TypeToken<BlockPos>() {}.getType());
    //  incomingFace = EnumFacing.byName(compound.getString("inventoryFace"));
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
  }
  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound compound) {
    super.writeToNBT(compound);
    for (EnumFacing f : EnumFacing.values()) {
      compound.setInteger(f.getName() + "_incoming", mapIncomingFluid.get(f));
    }
    compound.setString("connectedInventory", new Gson().toJson(connectedInventory));
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
  public BlockPos getConnectedPos() {
    return connectedInventory;
  }
  public void setConnectedPos(BlockPos connectedInventory) {
    this.connectedInventory = connectedInventory;
  }
  public void updateIncomingFluidFace(EnumFacing inputFrom) {
    mapIncomingFluid.put(inputFrom, TIMER_SIDE_INPUT);
  }
  private boolean isFluidIncomingFromFace(EnumFacing face) {
    return mapIncomingFluid.get(face) > 0;
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
    //tick down any incoming sides
    //now look over any sides that are NOT incoming, try to export
    BlockPos posTarget;
    //Actually shuffle the positions. if we are at a 3 way juncture, spread out where it goes first
    ArrayList<Integer> shuffledFaces = new ArrayList<>();
    for (int i = 0; i < EnumFacing.values().length; i++) {
      shuffledFaces.add(i);
    }
    Collections.shuffle(shuffledFaces);
    for (int i : shuffledFaces) {
      EnumFacing f = EnumFacing.values()[i];
      if (this.fluidTransport) {
        if (this.isFluidIncomingFromFace(f) == false) {
          //ok, fluid is not incoming from here. so lets output some
          posTarget = pos.offset(f);
          int toFlow = TRANSFER_PER_TICK;
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
    }
  }
  public boolean hasAnyIncomingFluidFaces() {
    for (EnumFacing f : EnumFacing.values()) {
      if (mapIncomingFluid.get(f) > 0)
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
}
