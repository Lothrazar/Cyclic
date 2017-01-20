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
  public static List<BlockPos> cubeFrame(final BlockPos posCenter, int radius, int height) {
    BlockPos botCenter = posCenter;
    BlockPos topCenter = posCenter.add(0, height, 0);
    List<BlockPos> cube = squareHorizontalHollow(topCenter, radius);
    cube.addAll(squareHorizontalHollow(botCenter, radius));
    //four walls
    BlockPos b1 = botCenter.add(radius, 0, radius);
    BlockPos b2 = botCenter.add(radius, 0, -1 * radius);
    BlockPos b3 = botCenter.add(-1 * radius, 0, -1 * radius);
    BlockPos b4 = botCenter.add(-1 * radius, 0, radius);
    //pillars
    int sideLen = height - 1;
    cube.addAll(line(b1, EnumFacing.UP, sideLen));
    cube.addAll(line(b2, EnumFacing.UP, sideLen));
    cube.addAll(line(b3, EnumFacing.UP, sideLen));
    cube.addAll(line(b4, EnumFacing.UP, sideLen));
    return cube;
  }
  public static List<BlockPos> cubeFilled(final BlockPos posCenter, int radius, int height) {
    BlockPos botCenter = posCenter;
    List<BlockPos> cube = squareHorizontalFull(botCenter, radius);
    BlockPos botCurrent;
    for (int i = 1; i <= height; i++) {
      botCurrent = botCenter.add(0, i, 0);
      cube.addAll(squareHorizontalFull(botCurrent, radius));
    }
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
  public static List<BlockPos> squareHorizontalFull(final BlockPos pos, int radius) {
    List<BlockPos> shape = new ArrayList<BlockPos>();
    // search in a cube
    int xMin = pos.getX() - radius;
    int xMax = pos.getX() + radius;
    int zMin = pos.getZ() - radius;
    int zMax = pos.getZ() + radius;
    int y = pos.getY();
    for (int x = xMin; x <= xMax; x++) {
      for (int z = zMin; z < zMax; z++) {
        shape.add(new BlockPos(x, y, z));
        shape.add(new BlockPos(x, y, z));
      }
    }
    //corners are done so offset
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
  public static List<BlockPos> stairway(BlockPos posCurrent, EnumFacing pfacing, int want, boolean isLookingUp) {
    List<BlockPos> shape = new ArrayList<BlockPos>();
    //    BlockPos posCurrent = position.down().offset(pfacing);
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
  public static List<BlockPos> sphere(BlockPos pos, int radius) {
    List<BlockPos> shape = new ArrayList<BlockPos>();
    //http://www.minecraftforge.net/forum/index.php?topic=24403.0
    int x = pos.getX(), y = pos.getY(), z = pos.getZ();
    int squareDistance;
    int radiusInner = radius - 1;
    int xCurr, yCurr, zCurr;
    for (xCurr = x - radius; xCurr <= x + radius; xCurr++) {
      for (yCurr = y - radius; yCurr <= y + radius; yCurr++) {
        for (zCurr = z - radius; zCurr <= z + radius; zCurr++) {
          squareDistance = (xCurr - x) * (xCurr - x) + (yCurr - y) * (yCurr - y) + (zCurr - z) * (zCurr - z);
          if (squareDistance <= (radius * radius)
              && squareDistance >= (radiusInner * radiusInner)) {//just to get the outline
            shape.add(new BlockPos(xCurr, yCurr, zCurr));
          }
        }
      }
    }
    return shape;
  }
}
