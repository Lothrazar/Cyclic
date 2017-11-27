package com.lothrazar.cyclicmagic.component.fluidtransfer;
import java.util.Map;
import com.google.common.collect.Maps;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.lothrazar.cyclicmagic.block.base.TileEntityBaseMachineFluid;
import com.lothrazar.cyclicmagic.component.fluidtransfer.BlockFluidCable.EnumConnectType;
import com.lothrazar.cyclicmagic.util.UtilFluid;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
 

public class TileEntityFluidCable extends TileEntityBaseMachineFluid implements ITickable {
  private static final int TIMER_FULL = 80;
  private static final int TRANSFER_PER_TICK = 5;
  private Map<EnumFacing, Integer> mapIncoming = Maps.newHashMap();
  private BlockPos connectedInventory;

  public EnumConnectType north, south, east, west, up, down;
  public TileEntityFluidCable() {
    super(100);
    for (EnumFacing f : EnumFacing.values()) {
      mapIncoming.put(f, 0);
    }
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
      mapIncoming.put(f, compound.getInteger(f.getName() + "_incoming"));
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
      compound.setInteger(f.getName() + "_incoming", mapIncoming.get(f));
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
  @Override
  public AxisAlignedBB getRenderBoundingBox() {
    double renderExtention = 1.0d;
    AxisAlignedBB bb = new AxisAlignedBB(pos.getX() - renderExtention, pos.getY() - renderExtention, pos.getZ() - renderExtention, pos.getX() + 1 + renderExtention, pos.getY() + 1 + renderExtention, pos.getZ() + 1 + renderExtention);
    return bb;
  }
  public BlockPos getConnectedPos() {
    return connectedInventory;
  }
  public void setConnectedPos(BlockPos connectedInventory) {
    this.connectedInventory = connectedInventory;
  }
  public void updateIncomingFace(EnumFacing inputFrom) {
    mapIncoming.put(inputFrom, TIMER_FULL);
  }
  private boolean isFluidIncomingFromFace(EnumFacing face) {
    return mapIncoming.get(face) > 0;
  }
  @Override
  public void update() {
    //tick down any incoming sides
    tickDownIncomingFaces();
    //now look over any sides that are NOT incoming, try to export
    BlockPos posTarget;
    for (EnumFacing f : EnumFacing.values()) {
      if (this.isFluidIncomingFromFace(f) == false) {
        //ok, fluid is not incoming from here. so lets output some
        posTarget = pos.offset(f);
        boolean outputSuccess = UtilFluid.tryFillPositionFromTank(world, posTarget, f.getOpposite(), tank, TRANSFER_PER_TICK);
        if (outputSuccess && world.getTileEntity(posTarget) instanceof TileEntityFluidCable) {
          //TODO: not so compatible with other fluid systems. itl do i guess
          TileEntityFluidCable cable = (TileEntityFluidCable) world.getTileEntity(posTarget);
          cable.updateIncomingFace(f.getOpposite());
        }
      }
    }
  }
  public void tickDownIncomingFaces() {
    for (EnumFacing f : EnumFacing.values()) {
      if (mapIncoming.get(f) > 0)
        mapIncoming.put(f, mapIncoming.get(f) - 1);
    }
  }
}
