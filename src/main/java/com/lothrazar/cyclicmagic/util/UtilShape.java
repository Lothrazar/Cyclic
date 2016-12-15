package com.lothrazar.cyclicmagic.util;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

public class UtilShape {
  public static List<BlockPos> repeatShapeByHeight(List<BlockPos> shape, int height) {
    List<BlockPos> newShape = new ArrayList<BlockPos>();
    newShape.addAll(shape);//copy it
    for (int i = 1; i <= height; i++)
      for (BlockPos p : shape) {
        newShape.add(p.up(i));
      }
    return newShape;
  }
  public static List<BlockPos> circle(BlockPos pos, int diameter) {
    int centerX = pos.getX();
    int centerZ = pos.getZ();
    int height = (int) pos.getY();
    int radius = diameter / 2;
    int z = radius;
    int x = 0;
    int d = 2 - (2 * radius);//dont use Diameter again, for integer roundoff
    List<BlockPos> circleList = new ArrayList<BlockPos>();
    do {
      circleList.add(new BlockPos(centerX + x, height, centerZ + z));
      circleList.add(new BlockPos(centerX + x, height, centerZ - z));
      circleList.add(new BlockPos(centerX - x, height, centerZ + z));
      circleList.add(new BlockPos(centerX - x, height, centerZ - z));
      circleList.add(new BlockPos(centerX + z, height, centerZ + x));
      circleList.add(new BlockPos(centerX + z, height, centerZ - x));
      circleList.add(new BlockPos(centerX - z, height, centerZ + x));
      circleList.add(new BlockPos(centerX - z, height, centerZ - x));
      if (d < 0) {
        d = d + (4 * x) + 6;
      }
      else {
        d = d + 4 * (x - z) + 10;
        z--;
      }
      x++;
    }
    while (x <= z);
    Collections.sort(circleList, new Comparator<BlockPos>() {
      @Override
      public int compare(final BlockPos object1, final BlockPos object2) {
        return (int) object1.getX() - object2.getX();
      }
    });
    return circleList;
  }
  /**
   * TODO: alter/make version that is cubic top/bottom faces but dyn height
   * @param posCenter
   * @param radius
   * @return
   */
  public static List<BlockPos> cube(final BlockPos posCenter, int radius) {
    BlockPos topCenter = posCenter.add(0, radius, 0);
    BlockPos botCenter = posCenter.add(0, -1 * radius, 0);
    List<BlockPos> cube = squareHorizontalHollow(topCenter, radius);
    cube.addAll(squareHorizontalHollow(botCenter, radius));
    //four walls
    BlockPos b1 = botCenter.add(radius, 1, radius);
    BlockPos b2 = botCenter.add(radius, 1, -1 * radius);
    BlockPos b3 = botCenter.add(-1 * radius, 1, -1 * radius);
    BlockPos b4 = botCenter.add(-1 * radius, 1, radius);
    cube.addAll(line(b1, EnumFacing.UP, radius));
    cube.addAll(line(b2, EnumFacing.UP, radius));
    cube.addAll(line(b3, EnumFacing.UP, radius));
    cube.addAll(line(b4, EnumFacing.UP, radius));
    return cube;
  }
  public static List<BlockPos> squareVerticalHollow(final BlockPos pos, int radius) {
    List<BlockPos> shape = new ArrayList<BlockPos>();
    // search in a cube
    int xMin = pos.getX() - radius;
    int xMax = pos.getX() + radius;
    int yMin = pos.getY() - radius;
    int yMax = pos.getY() + radius;
    int z = pos.getZ();
    //first, leave x fixed and track along +/- y
    for (int x = xMin; x <= xMax; x++) {
      shape.add(new BlockPos(x, yMin, z));
      shape.add(new BlockPos(x, yMax, z));
    }
    //corners are done so offset
    for (int y = yMin + 1; y < yMax; y++) {
      shape.add(new BlockPos(xMin, y, z));
      shape.add(new BlockPos(xMax, y, z));
    }
    return shape;
  }
  public static List<BlockPos> squareHorizontalHollow(final BlockPos pos, int radius) {
    List<BlockPos> shape = new ArrayList<BlockPos>();
    // search in a cube
    int xMin = pos.getX() - radius;
    int xMax = pos.getX() + radius;
    int zMin = pos.getZ() - radius;
    int zMax = pos.getZ() + radius;
    int y = pos.getY();
    //first, leave x fixed and track along +/- y
    for (int x = xMin; x <= xMax; x++) {
      shape.add(new BlockPos(x, y, zMin));
      shape.add(new BlockPos(x, y, zMax));
    }
    //corners are done so offset
    for (int z = zMin + 1; z < zMax; z++) {
      shape.add(new BlockPos(xMin, y, z));
      shape.add(new BlockPos(xMax, y, z));
    }
    return shape;
  }
  public static List<BlockPos> stairway(BlockPos position, EnumFacing pfacing, int want, boolean isLookingUp) {
    List<BlockPos> shape = new ArrayList<BlockPos>();
    BlockPos posCurrent = position.down().offset(pfacing);
    boolean goVert = false;
    for (int i = 1; i < want + 1; i++) {
      if (goVert) {
        if (isLookingUp)
          posCurrent = posCurrent.up();
        else
          posCurrent = posCurrent.down();
      }
      else {
        posCurrent = posCurrent.offset(pfacing);
      }
      shape.add(posCurrent);
      goVert = (i % 2 == 0);// alternate between going forward vertical
    }
    return shape;
  }
  public static List<BlockPos> line(BlockPos pos, EnumFacing efacing, int want) {
    List<BlockPos> shape = new ArrayList<BlockPos>();
    int skip = 1;
    for (int i = 1; i < want + 1; i = i + skip) {
      shape.add(pos.offset(efacing, i));
    }
    return shape;
  }
}
