package com.lothrazar.cyclicmagic.data;

import javax.annotation.Nullable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class BlockPosDim {

  private double x;
  private double y;
  private double z;
  public int id;
  private int dimension;
  private String display;
  private Vec3d hitVec = Vec3d.ZERO;
  @Nullable
  private EnumFacing side = null;

  public BlockPosDim(int idx, BlockPos pos, int dimension, String d) {
    setX(pos.getX());
    setY(pos.getY());
    setZ(pos.getZ());
    id = idx;
    this.setDimension(dimension);
    setDisplay(d);
  }

  public BlockPosDim(int idx, EntityPlayer p, String d) {
    setX(p.posX);
    setY(p.posY);
    setZ(p.posZ);
    id = idx;
    setDimension(p.dimension);
    setDisplay(d);
  }

  public BlockPosDim(String csv) {
    String[] pts = csv.split(",");
    id = Integer.parseInt(pts[0]);
    setX(Double.parseDouble(pts[1]));
    setY(Double.parseDouble(pts[2]));
    setZ(Double.parseDouble(pts[3]));
    setDimension(Integer.parseInt(pts[4]));
    if (pts.length > 5)
      setDisplay(pts[5]);
  }

  public BlockPosDim(BlockPos pos, int dimension) {
    this.x = pos.getX();
    this.y = pos.getY();
    this.z = pos.getZ();
    this.dimension = dimension;
  }

  public String toCSV() {
    return id + "," + getX() + "," + getY() + "," + getZ() + "," + getDimension() + "," + getDisplay();
  }

  public BlockPos toBlockPos() {
    return new BlockPos(getX(), getY(), getZ());
  }

  public String coordsDisplay() {
    // "["+id + "] "+
    return Math.round(getX()) + ", " + Math.round(getY()) + ", " + Math.round(getZ()); // +
    // showName
  }

  @Override
  public String toString() {
    return "[" + getDimension() + "] (" + (int) getX() + ", " + (int) getY() + ", " + (int) getZ() + ")";
  }

  public int getDimension() {
    return dimension;
  }

  public void setDimension(int dimension) {
    this.dimension = dimension;
  }

  public String getDisplay() {
    return display;
  }

  public void setDisplay(String display) {
    this.display = display;
  }

  public double getZ() {
    return z;
  }

  public void setZ(double z) {
    this.z = z;
  }

  public double getY() {
    return y;
  }

  public void setY(double y) {
    this.y = y;
  }

  public double getX() {
    return x;
  }

  public void setX(double x) {
    this.x = x;
  }

  public @Nullable EnumFacing getSide() {
    return side;
  }

  public void setSide(EnumFacing side) {
    this.side = side;
  }

  public Vec3d getHitVec() {
    return hitVec;
  }

  public void setHitVec(Vec3d hitVec) {
    this.hitVec = hitVec;
  }
}