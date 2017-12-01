package com.lothrazar.cyclicmagic.component.itemtransfer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import com.google.common.collect.Maps;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.lothrazar.cyclicmagic.block.base.BlockBaseCable.EnumConnectType;
import com.lothrazar.cyclicmagic.block.base.ITileCable;
import com.lothrazar.cyclicmagic.block.base.TileEntityBaseMachineInvo;
import com.lothrazar.cyclicmagic.util.UtilItemStack;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

public class TileEntityItemCable extends TileEntityBaseMachineInvo implements ITickable, ITileCable {
  private static final int TICKS_TEXT_CACHED = 7;
  private static final int TIMER_SIDE_INPUT = 15;
  private Map<EnumFacing, Integer> mapIncoming = Maps.newHashMap();
  private BlockPos connectedInventory;
  private int labelTimer = 0;
  private String labelText = "";
  public EnumConnectType north, south, east, west, up, down;
  public TileEntityItemCable() {
    super(1);
    for (EnumFacing f : EnumFacing.values()) {
      mapIncoming.put(f, 0);
    }
  }
  public String getLabelText() {
    return labelText;
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
    labelText = compound.getString("label");
    labelTimer = compound.getInteger("labelt");
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
    compound.setString("label", labelText);
    compound.setInteger("labelt", labelTimer);
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
    mapIncoming.put(inputFrom, TIMER_SIDE_INPUT);
  }
  private boolean isFluidIncomingFromFace(EnumFacing face) {
    return mapIncoming.get(face) > 0;
  }
  @Override
  public void update() {
    this.tickLabelText();
    tickDownIncomingFaces();
    //tick down any incoming sides
    //now look over any sides that are NOT incoming, try to export
    BlockPos posTarget;
    //Actually shuffle the positions. if we are at a 3 way juncture, spread out where it goes first
    ArrayList<Integer> shuffledFaces = new ArrayList<>();
    for (int i = 0; i < EnumFacing.values().length; i++) {
      shuffledFaces.add(i);
    }
    Collections.shuffle(shuffledFaces);
    ItemStack stackToExport = this.getStackInSlot(0).copy();
    if (stackToExport.isEmpty()) {
      return;
    }
    TileEntity tileTarget;
    for (int i : shuffledFaces) {
      EnumFacing f = EnumFacing.values()[i];
      if (this.isFluidIncomingFromFace(f) == false) {
        //ok, fluid is not incoming from here. so lets output some
        posTarget = pos.offset(f);
        tileTarget = world.getTileEntity(posTarget);
        if (tileTarget == null) {
          continue;
        }
        boolean outputSuccess = false;
        ItemStack pulled = UtilItemStack.tryDepositToHandler(world, posTarget, f.getOpposite(), stackToExport);
        if (pulled.getCount() != stackToExport.getCount()) {
          this.setInventorySlotContents(0, pulled);
          //one or more was put in
          outputSuccess = true;
        }
        if (outputSuccess && world.getTileEntity(posTarget) instanceof TileEntityItemCable) {
          //TODO: not so compatible with other fluid systems. itl do i guess
          TileEntityItemCable cable = (TileEntityItemCable) world.getTileEntity(posTarget);
          cable.updateIncomingFace(f.getOpposite());
        }
      }
    }
  }
  /**
   * with normal item movement it moves too fast for user to read cache the current item for a few ticks so full item pipes dont show empty or flashing fast text
   */
  private void tickLabelText() {
    this.labelTimer--;
    if (this.labelTimer <= 0) {
      this.labelTimer = 0;
      this.labelText = "";
      if (this.getStackInSlot(0).isEmpty() == false) {
        this.labelText = this.getStackInSlot(0).getDisplayName();
        this.labelTimer = TICKS_TEXT_CACHED;
      }
    }
  }
  public boolean hasAnyIncomingFaces() {
    for (EnumFacing f : EnumFacing.values()) {
      if (mapIncoming.get(f) > 0)
        return true;
    }
    return false;
  }
  public void tickDownIncomingFaces() {
    for (EnumFacing f : EnumFacing.values()) {
      if (mapIncoming.get(f) > 0)
        mapIncoming.put(f, mapIncoming.get(f) - 1);
    }
  }
  public String getIncomingStrings() {
    String in = "";
    for (EnumFacing f : EnumFacing.values()) {
      if (mapIncoming.get(f) > 0)
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
  public int[] getSlotsForFace(EnumFacing side) {
    return new int[] { 0 };
  }
}
