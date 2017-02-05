package com.lothrazar.cyclicmagic.block.tileentity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class TileEntityBucketStorage extends TileEntity {
  public static final String NBT_ID = "buckets";
  private int buckets = 0;
  public TileEntityBucketStorage() {
    super();
  }
  public TileEntityBucketStorage(int in) {
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
