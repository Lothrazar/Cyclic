package com.lothrazar.cyclic.item.scythe;

import com.lothrazar.cyclic.util.UtilShape;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.core.BlockPos;

public enum ScytheType {

  BRUSH, FORAGE, LEAVES;

  public static List<BlockPos> getShape(BlockPos center, int radius) {
    List<BlockPos> shape = new ArrayList<BlockPos>();
    shape.addAll(UtilShape.squareHorizontalFull(center.below().below(), radius));
    shape.addAll(UtilShape.squareHorizontalFull(center.below(), radius));
    shape.addAll(UtilShape.squareHorizontalFull(center, radius));
    shape.addAll(UtilShape.squareHorizontalFull(center.above(), radius));
    shape.addAll(UtilShape.squareHorizontalFull(center.above().above(), radius));
    return shape;
  }
}
