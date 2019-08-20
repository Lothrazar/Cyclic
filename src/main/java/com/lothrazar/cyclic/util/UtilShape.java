package com.lothrazar.cyclic.util;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;

public class UtilShape {

  public static List<BlockPos> line(BlockPos pos, Direction efacing, int want) {
    List<BlockPos> shape = new ArrayList<BlockPos>();
    int skip = 1;
    for (int i = 1; i < want + 1; i = i + skip) {
      shape.add(pos.offset(efacing, i));
    }
    return shape;
  }
}
