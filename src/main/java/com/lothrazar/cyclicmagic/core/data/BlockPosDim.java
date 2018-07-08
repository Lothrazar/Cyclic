package com.lothrazar.cyclicmagic.core.data;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;

public class BlockPosDim {

  public double X;
  public double Y;
  public double Z;
  public int id;
  public int dimension;
  public String display;

  public BlockPosDim(int idx, BlockPos pos, int dimension, String d) {
    X = pos.getX();
    Y = pos.getY();
    Z = pos.getZ();
    id = idx;
    this.dimension = dimension;
    display = d;
  }

  public BlockPosDim(int idx, EntityPlayer p, String d) {
    X = p.posX;
    Y = p.posY;
    Z = p.posZ;
    id = idx;
    dimension = p.dimension;
    display = d;
  }

  public BlockPosDim(String csv) {
    String[] pts = csv.split(",");
    id = Integer.parseInt(pts[0]);
    X = Double.parseDouble(pts[1]);
    Y = Double.parseDouble(pts[2]);
    Z = Double.parseDouble(pts[3]);
    dimension = Integer.parseInt(pts[4]);
    if (pts.length > 5)
      display = pts[5];
  }

  public String toCSV() {
    return id + "," + X + "," + Y + "," + Z + "," + dimension + "," + display;
  }

  public BlockPos toBlockPos() {
    return new BlockPos(X, Y, Z);
  }

  public String coordsDisplay() {
    // "["+id + "] "+
    return Math.round(X) + ", " + Math.round(Y) + ", " + Math.round(Z); // +
    // showName
  }

  @Override
  public String toString() {
    return "[" + dimension + "] (" + (int) X + ", " + (int) Y + ", " + (int) Z + ")";
  }
}