package com.lothrazar.cyclic.util;

import com.lothrazar.cyclic.data.Const;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import net.minecraft.util.Direction;
import net.minecraft.util.Direction.AxisDirection;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

public class UtilShape {

  public static List<BlockPos> cubeSquareBase(final BlockPos pos, int radius, int height) {
    List<BlockPos> shape = new ArrayList<BlockPos>();
    // search in a cube
    int xMin = pos.getX() - radius;
    int xMax = pos.getX() + radius;
    int zMin = pos.getZ() - radius;
    int zMax = pos.getZ() + radius;
    for (int x = xMin; x <= xMax; x++) {
      for (int z = zMin; z <= zMax; z++) {
        for (int y = pos.getY(); y <= pos.getY() + height; y++) {
          //now go max height on each pillar for sort order
          shape.add(new BlockPos(x, y, z));
        }
      }
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
      for (int z = zMin; z <= zMax; z++) {
        shape.add(new BlockPos(x, y, z));
      }
    }
    //corners are done so offset
    return shape;
  }

  public static List<BlockPos> squareVerticalX(final BlockPos pos, int xRadius, int yRadius) {
    List<BlockPos> shape = new ArrayList<BlockPos>();
    // search in a cube
    int xMin = pos.getX() - xRadius;
    int xMax = pos.getX() + xRadius;
    int yMin = pos.getY() - yRadius;
    int yMax = pos.getY() + yRadius;
    int z = pos.getZ();
    //first, leave x fixed and track along +/- y
    for (int y = yMin + 1; y < yMax; y++) {
      for (int x = xMin; x <= xMax; x++) {
        shape.add(new BlockPos(x, y, z));
        shape.add(new BlockPos(x, y, z));
      }
    }
    //corners are done so offset
    for (int x = xMin; x <= xMax; x++) {
      for (int y = yMin + 1; y < yMax; y++) {
        shape.add(new BlockPos(x, y, z));
        shape.add(new BlockPos(x, y, z));
      }
    }
    return shape;
  }

  //SHOLD TO-DO: merge x/z vers
  public static List<BlockPos> squareVerticalZ(final BlockPos pos, int yRadius, int zRadius) {
    List<BlockPos> shape = new ArrayList<BlockPos>();
    // search in a cube
    int x = pos.getX();
    int zMin = pos.getZ() - zRadius;
    int zMax = pos.getZ() + zRadius;
    int yMin = pos.getY() - yRadius;
    int yMax = pos.getY() + yRadius;
    //first, leave x fixed and track along +/- y
    for (int y = yMin + 1; y < yMax; y++) {
      for (int z = zMin; z <= zMax; z++) {
        shape.add(new BlockPos(x, y, z));
        shape.add(new BlockPos(x, y, z));
      }
    }
    //corners are done so offset
    for (int z = zMin; z <= zMax; z++) {
      for (int y = yMin + 1; y < yMax; y++) {
        shape.add(new BlockPos(x, y, z));
        shape.add(new BlockPos(x, y, z));
      }
    }
    return shape;
  }

  public static List<BlockPos> line(BlockPos pos, Direction efacing, int want) {
    List<BlockPos> shape = new ArrayList<BlockPos>();
    int skip = 1;
    for (int i = 1; i < want + 1; i = i + skip) {
      shape.add(pos.offset(efacing, i));
    }
    return shape;
  }

  public static List<BlockPos> squareHorizontalHollow(final BlockPos pos, int radius) {
    return rectHollow(pos, radius, radius);
  }

  public static List<BlockPos> rect(final BlockPos pos, final BlockPos target) {
    List<BlockPos> shape = new ArrayList<BlockPos>();
    //
    if (pos == null || target == null) {
      return shape;
    }
    int xMin = Math.min(pos.getX(), target.getX());
    int yMin = Math.min(pos.getY(), target.getY());
    int zMin = Math.min(pos.getZ(), target.getZ());
    int xMax = Math.max(pos.getX(), target.getX());
    int yMax = Math.max(pos.getY(), target.getY());
    int zMax = Math.max(pos.getZ(), target.getZ());
    for (int x = xMin; x <= xMax; x++) {
      for (int z = zMin; z <= zMax; z++) {
        for (int y = yMin; y <= yMax; y++) {
          //now go max height on each pillar for sort order
          shape.add(new BlockPos(x, y, z));
        }
      }
    }
    return shape;
  }

  public static List<BlockPos> rectHollow(final BlockPos pos, int radiusX, int radiusZ) {
    List<BlockPos> shape = new ArrayList<BlockPos>();
    // search in a cube
    int xMin = pos.getX() - radiusX;
    int xMax = pos.getX() + radiusX;
    int zMin = pos.getZ() - radiusZ;
    int zMax = pos.getZ() + radiusZ;
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

  public static List<BlockPos> getShape(AxisAlignedBB ab, int y) {
    List<BlockPos> shape = new ArrayList<>();
    int xMin = (int) ab.minX;
    int xMax = (int) ab.maxX - 1;
    int zMin = (int) ab.minZ;
    int zMax = (int) ab.maxZ - 1;
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

  public static List<BlockPos> circleHorizontal(BlockPos pos, final int diameter) {
    int centerX = pos.getX();
    int centerZ = pos.getZ();
    int height = pos.getY();
    int radius = diameter / 2;
    int z = radius;
    int x = 0;
    int d = 2 - (2 * radius); //dont use Diameter again, for integer roundoff
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
        return object1.getX() - object2.getX();
      }
    });
    return circleList;
  }

  public static List<BlockPos> repeatShapeByHeight(List<BlockPos> shape, final int height) {
    List<BlockPos> newShape = new ArrayList<BlockPos>();
    newShape.addAll(shape); //copy it
    for (int i = 1; i <= Math.abs(height); i++) {
      for (BlockPos p : shape) {
        BlockPos newOffset = null;
        if (height > 0) {
          newOffset = p.up(i);
        }
        else {
          newOffset = p.down(i);
        }
        //        if (newOffset.getY() > 3) {}
        // TODO: put in const, 
        if (newOffset.getY() >= 0 && newOffset.getY() <= Const.WORLDHEIGHT) {
          newShape.add(newOffset);
        }
      }
    }
    return newShape;
  }

  public static List<BlockPos> sphereDome(BlockPos pos, int radius) {
    return sphere(pos, radius, true, false);
  }

  public static List<BlockPos> sphereCup(BlockPos pos, int radius) {
    return sphere(pos, radius, false, true);
  }

  public static List<BlockPos> sphere(BlockPos pos, int radius) {
    return sphere(pos, radius, false, false);
  }

  /**
   * top and bottom should not be both true
   * 
   * @param pos
   * @param radius
   * @param topHalfOnly
   * @param bottomHalfOnly
   * @return
   */
  public static List<BlockPos> sphere(BlockPos pos, int radius, boolean topHalfOnly, boolean bottomHalfOnly) {
    List<BlockPos> shape = new ArrayList<BlockPos>();
    //http://www.minecraftforge.net/forum/index.php?topic=24403.0
    int x = pos.getX(), y = pos.getY(), z = pos.getZ();
    int squareDistance;
    int radiusInner = radius - 1;
    int xCurr, yCurr, zCurr;
    int yMin = y - radius;
    int yMax = y + radius;
    if (topHalfOnly) {
      yMin = pos.getY();
    }
    else if (bottomHalfOnly) {
      yMax = pos.getY();
    }
    for (xCurr = x - radius; xCurr <= x + radius; xCurr++) {
      for (yCurr = yMin; yCurr <= yMax; yCurr++) {
        for (zCurr = z - radius; zCurr <= z + radius; zCurr++) {
          squareDistance = (xCurr - x) * (xCurr - x) + (yCurr - y) * (yCurr - y) + (zCurr - z) * (zCurr - z);
          if (squareDistance <= (radius * radius)
              && squareDistance >= (radiusInner * radiusInner)) { //just to get the outline
            shape.add(new BlockPos(xCurr, yCurr, zCurr));
          }
        }
      }
    }
    return shape;
  }

  public static List<BlockPos> squarePyramid(final BlockPos pos, final int radius, final int height) {
    List<BlockPos> shape = new ArrayList<BlockPos>();
    int radiusCurrent = radius;
    BlockPos posCurrent = new BlockPos(pos);
    for (int i = 0; i < radius; i++) {
      shape.addAll(rectHollow(posCurrent, radiusCurrent, radiusCurrent));
      radiusCurrent--;
      posCurrent = posCurrent.up();
    }
    return shape;
  }

  public static List<BlockPos> diagonal(BlockPos posCurrent, Direction pfacing, int want, boolean isLookingUp) {
    List<BlockPos> shape = new ArrayList<BlockPos>();
    for (int i = 1; i < want + 1; i++) {
      if (isLookingUp) {
        posCurrent = posCurrent.up();
      }
      else {
        posCurrent = posCurrent.down();
      } //go up and over each time
      posCurrent = posCurrent.offset(pfacing);
      shape.add(posCurrent);
    }
    return shape;
  }

  public static List<BlockPos> circleVertical(BlockPos pos, int diameter, int maxHeight, Direction dir) {
    int centerH;
    int diroff = dir.getAxisDirection() == AxisDirection.NEGATIVE ? -1 : 1;
    final int centerY = pos.getY();
    final int radius = diameter / 2;
    int radOffset = radius;
    int otherOff = 0;
    int d = 2 - (2 * radius); //dont use Diameter again, for integer roundoff
    List<BlockPos> circleList = new ArrayList<BlockPos>();
    for (int i = 0; i < maxHeight; i++) {
      radOffset = radius;
      otherOff = 0;
      d = 2 - (2 * radius); //dont use Diameter again, for integer roundoff
      do {
        if (dir.getAxis() == Direction.Axis.Z) {
          centerH = pos.getX();
          circleList.add(new BlockPos(centerH + otherOff, centerY + radOffset, pos.getZ() + diroff * i));
          circleList.add(new BlockPos(centerH + otherOff, centerY - radOffset, pos.getZ() + diroff * i));
          circleList.add(new BlockPos(centerH - otherOff, centerY + radOffset, pos.getZ() + diroff * i));
          circleList.add(new BlockPos(centerH - otherOff, centerY - radOffset, pos.getZ() + diroff * i));
          circleList.add(new BlockPos(centerH + radOffset, centerY + otherOff, pos.getZ() + diroff * i));
          circleList.add(new BlockPos(centerH + radOffset, centerY - otherOff, pos.getZ() + diroff * i));
          circleList.add(new BlockPos(centerH - radOffset, centerY + otherOff, pos.getZ() + diroff * i));
          circleList.add(new BlockPos(centerH - radOffset, centerY - otherOff, pos.getZ() + diroff * i));
        }
        else { // lazy way
          centerH = pos.getZ();
          circleList.add(new BlockPos(pos.getX() + diroff * i, centerY + radOffset, centerH + otherOff));
          circleList.add(new BlockPos(pos.getX() + diroff * i, centerY - radOffset, centerH + otherOff));
          circleList.add(new BlockPos(pos.getX() + diroff * i, centerY + radOffset, centerH - otherOff));
          circleList.add(new BlockPos(pos.getX() + diroff * i, centerY - radOffset, centerH - otherOff));
          circleList.add(new BlockPos(pos.getX() + diroff * i, centerY + otherOff, centerH + radOffset));
          circleList.add(new BlockPos(pos.getX() + diroff * i, centerY - otherOff, centerH + radOffset));
          circleList.add(new BlockPos(pos.getX() + diroff * i, centerY + otherOff, centerH - radOffset));
          circleList.add(new BlockPos(pos.getX() + diroff * i, centerY - otherOff, centerH - radOffset));
        }
        if (d < 0) {
          d = d + (4 * otherOff) + 6;
        }
        else {
          d = d + 4 * (otherOff - radOffset) + 10;
          radOffset--;
        }
        otherOff++;
      }
      while (otherOff <= radOffset);
    } // height loop
    Collections.sort(circleList, new Comparator<BlockPos>() {

      @Override
      public int compare(final BlockPos object1, final BlockPos object2) {
        return object1.getX() - object2.getX();
      }
    });
    return circleList;
  }
  //  public static List<BlockPos> circleVertical(BlockPos posTarget, int size, int height, Direction currentFacing) {
  //    // TODO Auto-generated method stub
  //    return null;
  //  }
}
