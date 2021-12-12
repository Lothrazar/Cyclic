package com.lothrazar.cyclic.item.scythe;

import com.lothrazar.cyclic.util.UtilShape;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.util.math.BlockPos;

public enum ScytheType {

  BRUSH, FORAGE, LEAVES;

  public static List<BlockPos> getShape(BlockPos center, int radius) {
    List<BlockPos> shape = new ArrayList<>();
    shape.addAll(UtilShape.squareHorizontalFull(center.down().down(), radius));
    shape.addAll(UtilShape.squareHorizontalFull(center.down(), radius));
    shape.addAll(UtilShape.squareHorizontalFull(center, radius));
    shape.addAll(UtilShape.squareHorizontalFull(center.up(), radius));
    shape.addAll(UtilShape.squareHorizontalFull(center.up().up(), radius));
    return shape;
  }
}
