package com.lothrazar.cyclic.data;

import com.lothrazar.cyclic.item.datacard.ShapeCard;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class RelativeShape {

  private String structure = null;
  //corner small always has the lowest Y point, 
  //if same y level, whoever has smallest sum of x+z, closest to negative infinity
  //should not allow points outside corners
  private List<BlockPos> shape;
  private int count;

  public RelativeShape(RelativeShape other) {
    this.shape = other.shape;
    this.count = other.count;
  }

  public RelativeShape() {
    shape = new ArrayList<>();
  }

  public void merge(RelativeShape other) {
    shape.addAll(other.shape);
    count = shape.size();
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
    this.shape = options;
    if (world != null && center != null) {
      setWorldCenter(world, center);
    }
  }

  public void setWorldCenter(World world, BlockPos center) {
    if (world != null) {
      List<BlockPos> options = shape;
      shape = new ArrayList<>();
      for (BlockPos pos : options) {
        BlockState bs = world.getBlockState(pos);
        if (bs.getBlock() != Blocks.AIR) {
          shape.add(pos.add(-1 * center.getX(), -1 * center.getY(), -1 * center.getZ()));
        }
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
    if (tag == null || tag.getBoolean(ShapeCard.VALID_SHAPE) == false) {
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
    CompoundNBT tag = item.getTag();
    return read(tag);
  }

  public CompoundNBT write(CompoundNBT tag) {
    int i = 0;
    int count = 0;
    for (BlockPos p : shape) {
      tag.putInt("x" + i, p.getX());
      tag.putInt("y" + i, p.getY());
      tag.putInt("z" + i, p.getZ());
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

  public void setShape(List<BlockPos> list) {
    this.shape = list;
    this.count = this.shape.size();
  }
}
