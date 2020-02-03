package com.lothrazar.cyclic.item.scythe;

import java.util.ArrayList;
import java.util.List;
import com.lothrazar.cyclic.util.UtilShape;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;

public enum ScytheType {

  BRUSH, FORAGE, LEAVES;

  public ResourceLocation type() {
    return new ResourceLocation("cyclic", "scythe_" + this.toString().toLowerCase());
  }

  public static List<BlockPos> getShape(BlockPos center, int radius) {
    List<BlockPos> shape = new ArrayList<BlockPos>();
    shape.addAll(UtilShape.squareHorizontalFull(center.down().down(), radius));
    shape.addAll(UtilShape.squareHorizontalFull(center.down(), radius));
    shape.addAll(UtilShape.squareHorizontalFull(center, radius));
    shape.addAll(UtilShape.squareHorizontalFull(center.up(), radius));
    shape.addAll(UtilShape.squareHorizontalFull(center.up().up(), radius));
    return shape;
  }
}