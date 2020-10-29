package com.lothrazar.cyclic.data;

import java.util.ArrayList;
import java.util.List;
import com.lothrazar.cyclic.item.datacard.ShapeCard;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BuildShape {

  //  @Nullable
  private String structure = null;
  //corner small always has the lowest Y point, 
  //if same y level, whoever has smallest sum of x+z, closest to negative infinity
  //  private BlockPos cornerSmall;
  //  private BlockPos cornerLarge; 
  //should not allow points outside corners
  private List<BlockPos> shape;
  private int count;

  public BuildShape() {
    shape = new ArrayList<>();
  }

  public BuildShape(ItemStack stack) {
    this();
    //    shape = pos;
    //    count = shape.size();
  }

  public BuildShape(List<BlockPos> pos) {
    this();
    shape = pos;
    count = shape.size();
  }

  public BuildShape(World world, List<BlockPos> options) {
    this();
    for (BlockPos pos : options) {
      if (!world.isAirBlock(pos)) {
        shape.add(pos);
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

  public static BuildShape read(ItemStack item) {
    CompoundNBT tag = item.getOrCreateTag();
    if (tag.getBoolean(ShapeCard.VALID_SHAPE) == false) {
      return null;
    }
    int count = tag.getInt("count");
    List<BlockPos> shapeList = new ArrayList<>();
    for (int i = 0; i < count; i++) {
      shapeList.add(new BlockPos(tag.getInt("x" + i), tag.getInt("y" + i), tag.getInt("z" + i)));
    }
    BuildShape shape = new BuildShape(shapeList);
    return shape;
  }

  public void write(ItemStack shapeCard) {
    CompoundNBT tag = shapeCard.getOrCreateTag();
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
  }
}
