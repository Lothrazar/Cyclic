package com.lothrazar.cyclicmagic.block.tileentity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class TileEntityBucketStorage extends TileEntity {
  public static final String NBT_ID = "buckets";
//  public static final String NBT_TIME = "time";
//  public static final long TIMEOUT = 15;
  private int buckets = 0;
//  private long timeLastRemoved = 0;
  public TileEntityBucketStorage() {
    super();
  }
  public TileEntityBucketStorage(World worldIn, int in) {
    super();
    buckets = in;
  }
  @Override
  public int getBlockMetadata() {
    return getBuckets();
  }
  public void addBucket() {
    buckets++;
  }

  public int getBuckets() {
    return buckets;
  }
  public void setBuckets(int b) {
    buckets = b;
  }
  public boolean removeBucket() {
    if (buckets <= 0) { return false; }
    buckets--;
    return true;
  }
  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
    nbt.setInteger(TileEntityBucketStorage.NBT_ID, this.buckets);
    return super.writeToNBT(nbt);
  }
  @Override
  public void readFromNBT(NBTTagCompound nbt) {
    this.buckets = nbt.getInteger(TileEntityBucketStorage.NBT_ID);
    super.readFromNBT(nbt);
  }
}
