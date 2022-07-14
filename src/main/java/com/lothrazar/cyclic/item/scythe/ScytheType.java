package com.lothrazar.cyclic.item.scythe;

import java.util.ArrayList;
import java.util.List;
import com.lothrazar.cyclic.util.ShapeUtil;
import net.minecraft.core.BlockPos;

public enum ScytheType {

  BRUSH, FORAGE, LEAVES;

  public static List<BlockPos> getShape(BlockPos center, int radius) {
    List<BlockPos> shape = new ArrayList<BlockPos>();
    shape.addAll(ShapeUtil.squareHorizontalFull(center.below().below(), radius));
    shape.addAll(ShapeUtil.squareHorizontalFull(center.below(), radius));
    shape.addAll(ShapeUtil.squareHorizontalFull(center, radius));
    shape.addAll(ShapeUtil.squareHorizontalFull(center.above(), radius));
    shape.addAll(ShapeUtil.squareHorizontalFull(center.above().above(), radius));
    return shape;
  }
}
