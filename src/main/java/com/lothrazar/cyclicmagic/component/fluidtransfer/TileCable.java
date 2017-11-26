package com.lothrazar.cyclicmagic.component.fluidtransfer;
import java.util.List;
import java.util.Map;
import com.google.common.collect.Maps;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.lothrazar.cyclicmagic.component.fluidtransfer.BlockCable.EnumConnectType;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.items.IItemHandler;

public class TileCable extends TileEntity {
  private BlockPos connectedInventory;
  private EnumFacing inventoryFace;
  private NonNullList<ItemStack> upgrades = NonNullList.withSize(4, ItemStack.EMPTY);
  private boolean mode = true;
  private int limit = 0;
  public EnumConnectType north, south, east, west, up, down;
  ItemStack stack = null;
  public enum CableKind {
    kabel, exKabel, imKabel, storageKabel;
  }
  public TileCable() {
 
  }
  public int getUpgradesOfType(int num) {
    int res = 0;
    for (ItemStack s : upgrades) {
      if (s != null && !s.isEmpty() && s.getItemDamage() == num) {
        res += s.getCount();
        break;
      }
    }
    return res;
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
    connectedInventory = new Gson().fromJson(compound.getString("connectedInventory"), new TypeToken<BlockPos>() {}.getType());
    inventoryFace = EnumFacing.byName(compound.getString("inventoryFace"));
    mode = compound.getBoolean("mode");
    limit = compound.getInteger("limit");
    if (compound.hasKey("stack", 10))
      stack = (new ItemStack(compound.getCompoundTag("stack")));
    else
      stack = null;
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
    NBTTagList nbttaglist = compound.getTagList("Items", 10);
    upgrades = NonNullList.withSize(4, ItemStack.EMPTY);
    for (int i = 0; i < nbttaglist.tagCount(); ++i) {
      NBTTagCompound nbttagcompound = nbttaglist.getCompoundTagAt(i);
      int j = nbttagcompound.getByte("Slot") & 255;
      if (j >= 0 && j < 4) {
        upgrades.set(j, new ItemStack(nbttagcompound));
      }
    }
  }
  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound compound) {
    super.writeToNBT(compound);
    compound.setString("connectedInventory", new Gson().toJson(connectedInventory));
    if (inventoryFace != null)
      compound.setString("inventoryFace", inventoryFace.toString());
    compound.setBoolean("mode", mode);
    compound.setInteger("limit", limit);
    if (stack != null)
      compound.setTag("stack", stack.writeToNBT(new NBTTagCompound()));
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
    NBTTagList nbttaglist = new NBTTagList();
    for (int i = 0; i < upgrades.size(); ++i) {
      if (upgrades.get(i) != null) {
        NBTTagCompound nbttagcompound = new NBTTagCompound();
        nbttagcompound.setByte("Slot", (byte) i);
        upgrades.get(i).writeToNBT(nbttagcompound);
        nbttaglist.appendTag(nbttagcompound);
      }
    }
    compound.setTag("Items", nbttaglist);
    return compound;
  }
  @Override
  public AxisAlignedBB getRenderBoundingBox() {
    double renderExtention = 1.0d;
    AxisAlignedBB bb = new AxisAlignedBB(pos.getX() - renderExtention, pos.getY() - renderExtention, pos.getZ() - renderExtention, pos.getX() + 1 + renderExtention, pos.getY() + 1 + renderExtention, pos.getZ() + 1 + renderExtention);
    return bb;
  }
 
  public BlockPos getConnectedInventory() {
    return connectedInventory;
  }
  public void setConnectedInventory(BlockPos connectedInventory) {
    this.connectedInventory = connectedInventory;
  }
  public EnumFacing getInventoryFace() {
    return inventoryFace;
  }
  public void setInventoryFace(EnumFacing inventoryFace) {
    this.inventoryFace = inventoryFace;
  }
  public List<ItemStack> getUpgrades() {
    return upgrades;
  }
  public void setUpgrades(List<ItemStack> upgrades) {
    upgrades = NonNullList.withSize(4, ItemStack.EMPTY);
    int i = 0;
    for (ItemStack s : upgrades) {
      if (s != null && !s.isEmpty()) {
        this.upgrades.set(i, s);
      }
      i++;
    }
  }
  public boolean isMode() {
    return mode;
  }
  public void setMode(boolean mode) {
    this.mode = mode;
  }
  public int getLimit() {
    return limit;
  }
  public void setLimit(int limit) {
    this.limit = limit;
  }
  public ItemStack getOperationStack() {
    return stack;
  }
  public void setOperationStack(ItemStack stack) {
    this.stack = stack;
  }
//  @Override
//  public IItemHandler getInventory() {
//    if (getConnectedInventory() != null)
//      return UtilInventory.getItemHandler(world.getTileEntity(getConnectedInventory()), inventoryFace.getOpposite());
//    return null;
//  }
//  @Override
//  public BlockPos getSource() {
//    return getConnectedInventory();
//  }
//  @Override
//  public boolean isStorage() {
//    return getKind() == CableKind.storageKabel;
//  }
}
