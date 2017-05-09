package com.lothrazar.cyclicmagic.component.workbench;
import com.lothrazar.cyclicmagic.block.tileentity.TileEntityBaseMachineInvo;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;

public class TileEntityWorkbench extends TileEntityBaseMachineInvo {
  public static final int ROWS = 3;
  public static final int COLS = 3;
  public static final int SIZE_GRID = 3 * 3;
  public TileEntityWorkbench() {
    super(SIZE_GRID);//left and right side both have a tall rectangle. then 3x3 crafting 
  }
  @Override
  public int[] getSlotsForFace(EnumFacing side) {
    return new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 };
  }
  @Override
  public void readFromNBT(NBTTagCompound tagCompound) {
    super.readFromNBT(tagCompound);
  }
  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound tagCompound) {
    return super.writeToNBT(tagCompound);
  }
}
