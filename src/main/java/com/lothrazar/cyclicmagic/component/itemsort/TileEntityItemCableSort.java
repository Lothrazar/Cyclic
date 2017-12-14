package com.lothrazar.cyclicmagic.component.itemsort;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import com.google.common.collect.Maps;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.lothrazar.cyclicmagic.block.base.BlockBaseCable.EnumConnectType;
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.block.base.ITileCable;
import com.lothrazar.cyclicmagic.block.base.TileEntityBaseMachineInvo;
import com.lothrazar.cyclicmagic.component.itemtransfer.TileEntityItemCable;
import com.lothrazar.cyclicmagic.util.UtilItemStack;
import com.lothrazar.cyclicmagic.util.UtilOreDictionary;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.oredict.OreDictionary;

public class TileEntityItemCableSort extends TileEntityBaseMachineInvo implements ITickable, ITileCable {
  private static final int FILTER_SIZE = 3;
  private static final int TICKS_TEXT_CACHED = 7;
  private static final int TIMER_SIDE_INPUT = 15;
  private Map<EnumFacing, Integer> mapIncoming = Maps.newHashMap();
  private Map<EnumFacing, NonNullList<ItemStack>> filters = Maps.newHashMap();
  private BlockPos connectedInventory;
  private int labelTimer = 0;
  private String labelText = "";
  //  private static IInventory filterNorth = new InventoryBasic("[Null]", false, 3);
  public EnumConnectType north, south, east, west, up, down;
  public TileEntityItemCableSort() {
    super(1);
    this.setSlotsForBoth();
    for (EnumFacing f : EnumFacing.values()) {
      mapIncoming.put(f, 0);
    }
    for (EnumFacing f : EnumFacing.values()) {
      filters.put(f, NonNullList.withSize(FILTER_SIZE, ItemStack.EMPTY));
    }
    //TESTING
    filters.get(EnumFacing.NORTH).set(0, new ItemStack(Blocks.STONE));
    filters.get(EnumFacing.SOUTH).set(0, new ItemStack(Blocks.DIRT));
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
  //  @Override
  //  public AxisAlignedBB getRenderBoundingBox() {
  //    double renderExtention = 1.0d;
  //    AxisAlignedBB bb = new AxisAlignedBB(pos.getX() - renderExtention, pos.getY() - renderExtention, pos.getZ() - renderExtention, pos.getX() + 1 + renderExtention, pos.getY() + 1 + renderExtention, pos.getZ() + 1 + renderExtention);
  //    return bb;
  //  }
  public BlockPos getConnectedPos() {
    return connectedInventory;
  }
  public void setConnectedPos(BlockPos connectedInventory) {
    this.connectedInventory = connectedInventory;
  }
  public void updateIncomingFace(EnumFacing inputFrom) {
    mapIncoming.put(inputFrom, TIMER_SIDE_INPUT);
  }
  private boolean isIncomingFromFace(EnumFacing face) {
    return mapIncoming.get(face) > 0;
  }
  private List<EnumFacing> getValidSidesForStack(ItemStack stackToExport) {
    List<EnumFacing> faces = new ArrayList<EnumFacing>();
    for (EnumFacing f : EnumFacing.values()) {
      NonNullList<ItemStack> inventoryContents = filters.get(f);
      if (OreDictionary.containsMatch(true, inventoryContents, stackToExport)) {
        //        ModCyclic.logger.log(" isValidForStack yes  " + stackToExport.toString() + f.toString());
        faces.add(f);
      }
    }
    return faces;
  }
  private List<EnumFacing> getEmptySides() {
    int countEmpty;
    List<EnumFacing> faces = new ArrayList<EnumFacing>();
    for (EnumFacing f : EnumFacing.values()) {
      countEmpty = 0;
      NonNullList<ItemStack> inventoryContents = filters.get(f);
      for (int i = 0; i < inventoryContents.size(); i++) {
        if (inventoryContents.get(i).isEmpty()) {
          countEmpty++;
        } //empty -> nofilter -> all things match
      }
      if (countEmpty == inventoryContents.size())
        faces.add(f);
    }
    return faces;
  }
  @Override
  public void update() {
    this.tickLabelText();
    tickDownIncomingFaces();
    ItemStack stackToExport = this.getStackInSlot(0).copy();
    if (stackToExport.isEmpty()) {
      return;
    }
    //tick down any incoming sides
    //now look over any sides that are NOT incoming, try to export
    //Actually shuffle the positions. if we are at a 3 way juncture, spread out where it goes first
    List<EnumFacing> targetFaces = getValidSidesForStack(stackToExport);
    Collections.shuffle(targetFaces);
    //TODO: maybe if still empty then.. drop? on ground?
    //1. get valid output sides
    //2. pick one at random
    //3. try to export 
    ////alternate: if there are no valid output sides, then look for all the empty sides
    tryExportToTheseFaces(targetFaces);
    if (targetFaces.isEmpty()) {
      targetFaces = getEmptySides();
      Collections.shuffle(targetFaces);
      tryExportToTheseFaces(targetFaces);
    }
  }
  public void tryExportToTheseFaces(List<EnumFacing> targetFaces) {
    BlockPos posTarget;
    TileEntity tileTarget;
    ItemStack stackToExport;
    for (EnumFacing f : targetFaces) {
      stackToExport = this.getStackInSlot(0).copy();
      if (stackToExport.isEmpty()) {
        return;
      }
      //      EnumFacing f = EnumFacing.values()[i];
      ModCyclic.logger.log("TRY " + f);
      if (this.isIncomingFromFace(f) == false) {//this.canExportThisInDirection(f, stackToExport)
        ModCyclic.logger.log("YES we can move this way " + stackToExport.toString() + f);
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
        if (outputSuccess && world.getTileEntity(posTarget) instanceof TileEntityItemCableSort) {
          TileEntityItemCableSort cable = (TileEntityItemCableSort) world.getTileEntity(posTarget);
          cable.updateIncomingFace(f.getOpposite());
        }
        if (outputSuccess && world.getTileEntity(posTarget) instanceof TileEntityItemCable) {
          //TODO: holy balls do we need a base class
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
