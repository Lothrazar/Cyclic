package com.lothrazar.cyclic.data;

import java.util.ArrayList;
import java.util.List;
import com.lothrazar.cyclic.item.datacard.ShapeCard;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class RelativeShape {

  //  @Nullable
  private String structure = null;
  //corner small always has the lowest Y point, 
  //if same y level, whoever has smallest sum of x+z, closest to negative infinity
  //should not allow points outside corners
  private List<BlockPos> shape;
  private int count;

  public RelativeShape() {
    shape = new ArrayList<>();
  }

  /**
   * Does not compute any offset, takes list as is. assumes it is pre-offset from some other center
   * 
   * @param sh
   */
  public RelativeShape(List<BlockPos> sh) {
    shape = sh;
  }

  /**
   * if world is null, it will not check for air blocks. if world is provided. will delete any spots of air. Does change all positions to offset from center
   * 
   * @param world
   * @param options
   * @param center
   */
  public RelativeShape(World world, List<BlockPos> options, BlockPos center) {
    this();
    for (BlockPos pos : options) {
      if (world == null || !world.isAirBlock(pos)) {
        shape.add(pos.add(-1 * center.getX(), -1 * center.getY(), -1 * center.getZ()));
      }
    }
    count = shape.size();
  }

  public int getCount() {
    return count;
  }

  public String getStructure() {
    return structure;
  }

  public void setStructure(String structure) {
    this.structure = structure;
  }

  public List<BlockPos> getShape() {
    return shape;
  }

  public static RelativeShape read(CompoundNBT tag) {
    if (tag.getBoolean(ShapeCard.VALID_SHAPE) == false) {
      return null;
    }
    int count = tag.getInt("count");
    List<BlockPos> shapeList = new ArrayList<>();
    for (int i = 0; i < count; i++) {
      shapeList.add(new BlockPos(tag.getInt("x" + i), tag.getInt("y" + i), tag.getInt("z" + i)));
    }
    RelativeShape shape = new RelativeShape();
    shape.shape = shapeList;
    shape.count = shapeList.size();
    return shape;
  }

  public static RelativeShape read(ItemStack item) {
    CompoundNBT tag = item.getOrCreateTag();
    return read(tag);
  }

  public CompoundNBT write(CompoundNBT tag) {
    int i = 0;
    int count = 0;
    for (BlockPos p : shape) {
      // 
      tag.putInt("x" + i, p.getX());
      tag.putInt("y" + i, p.getY());
      tag.putInt("z" + i, p.getZ());
      //      p.re
      i++;
      count = i;
    }
    tag.putInt("count", count);
    tag.putBoolean(ShapeCard.VALID_SHAPE, true);
    return tag;
  }

  public void write(ItemStack shapeCard) {
    CompoundNBT tag = shapeCard.getOrCreateTag();
    write(tag);
  }
}
