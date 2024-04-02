package com.lothrazar.cyclic.data;

import com.lothrazar.cyclic.util.UtilWorld;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.server.ServerWorld;

public class BlockPosDim {

  private double x;
  private double y;
  private double z;
  private BlockPos pos;
  private String dimension;
  private String name;
  private Vector3d hitVec = Vector3d.ZERO;
  private Direction side;
  private Direction sidePlayerFacing;

  public BlockPosDim(BlockPos pos, String dimension, CompoundNBT stackTag) {
    setX(pos.getX());
    setY(pos.getY());
    setZ(pos.getZ());
    this.setPos(pos);
    this.setDimension(dimension);
    if (stackTag != null && stackTag.contains("display")) {
      //
      CompoundNBT displayTag = stackTag.getCompound("display");
      if (displayTag != null && displayTag.contains("Name", 8)) {
        //
        ITextComponent namec = ITextComponent.Serializer.getComponentFromJson(displayTag.getString("Name"));
        this.name = namec.getString();
      }
    }
  }

  @Override
  public String toString() {
    return getDisplayString();
  }

  public String getDisplayString() {
    String prefix = "";
    if (name != null && !name.isEmpty()) {
      prefix = name + " ";
    }
    String d = dimension == null ? "" : dimension.replace("minecraft:", "");
    return prefix + d + " (" + (int) getX() + ", " + (int) getY() + ", " + (int) getZ() + ")";
  }

  public String getDimension() {
    return dimension;
  }

  public void setDimension(String dimension) {
    this.dimension = dimension;
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

  public Direction getSide() {
    return side == null ? Direction.UP : side;
  }

  public void setSide(Direction side) {
    this.side = side;
  }

  public Vector3d getHitVec() {
    return hitVec;
  }

  public void setHitVec(Vector3d hitVec) {
    this.hitVec = hitVec;
  }

  public Direction getSidePlayerFacing() {
    return sidePlayerFacing;
  }

  public void setSidePlayerFacing(Direction sidePlayerFacing) {
    this.sidePlayerFacing = sidePlayerFacing;
  }

  public BlockPos getPos() {
    return pos;
  }

  public void setPos(BlockPos pos) {
    this.pos = pos;
  }

  public ServerWorld getServerLevel(MinecraftServer server) {
    return server.getWorld(UtilWorld.stringToDimension(this.getDimension()));
  }
}
