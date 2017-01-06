package com.lothrazar.cyclicmagic.block.tileentity;
import java.util.List;
import com.lothrazar.cyclicmagic.util.UtilHarvestCrops;
import com.lothrazar.cyclicmagic.util.UtilParticle;
import com.lothrazar.cyclicmagic.util.UtilShape;
import com.lothrazar.cyclicmagic.util.UtilWorld;
import com.lothrazar.cyclicmagic.util.UtilHarvestCrops.HarestCropsConfig;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;

/**
 * PLAN: gui to change power and vector.
 * 
 * make sure it saves data when you harvest and place
 * 
 * @author Sam
 *
 */
public class TileVector extends TileEntityBaseMachineInvo {
  private int angle = 45;
  private int power = 13;
  private static final String NBT_ANGLE = "Timer";
  private static final String NBT_POWER = "redstone";
  public static enum Fields {
    ANGLE, POWER
  }
  public TileVector() {
  }
  @Override
  public void readFromNBT(NBTTagCompound tagCompound) {
    super.readFromNBT(tagCompound);
    power = tagCompound.getInteger(NBT_POWER);
    angle = tagCompound.getInteger(NBT_ANGLE);
  }
  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound tagCompound) {
    tagCompound.setInteger(NBT_POWER, power);
    tagCompound.setInteger(NBT_ANGLE, angle);
    return super.writeToNBT(tagCompound);
  }
  public float getActualPower(){
    return power/10;
  }
  public int getAngle(){
    return angle;
  }
  @Override
  public int getField(int id) {
    if (id >= 0 && id < this.getFieldCount())
      switch (Fields.values()[id]) {
      case ANGLE:
        return angle;
      case POWER:
        return power;
      default:
        break;
      }
    return -1;
  }
  @Override
  public void setField(int id, int value) {
    if (id >= 0 && id < this.getFieldCount())
      switch (Fields.values()[id]) {
      case ANGLE:
        this.angle = value;
        break;
      case POWER:
        this.power = value;
        break;
      }
  }
  @Override
  public int getFieldCount() {
    return Fields.values().length;
  }
  @Override
  public int getSizeInventory() {
    return 0;
  }
  @Override
  public ItemStack getStackInSlot(int index) {
    return null;
  }
  @Override
  public ItemStack decrStackSize(int index, int count) {
    return null;
  }
  @Override
  public ItemStack removeStackFromSlot(int index) {
    return null;
  }
  @Override
  public void setInventorySlotContents(int index, ItemStack stack) {
  }
  @Override
  public int[] getSlotsForFace(EnumFacing side) {
    return new int[] {};
  }
  //  @Override
  //  public void toggleNeedsRedstone() {
  //    int val = this.needsRedstone + 1;
  //    if (val > 1) {
  //      val = 0;//hacky lazy way
  //    }
  //    this.setField(Fields.REDSTONE.ordinal(), val);
  //  }
  //  private boolean onlyRunIfPowered() {
  //    return this.needsRedstone == 1;
  //  }
}
