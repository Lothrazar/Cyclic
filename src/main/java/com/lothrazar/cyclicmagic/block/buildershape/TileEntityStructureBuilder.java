/*******************************************************************************
 * The MIT License (MIT)
 * 
 * Copyright (C) 2014-2018 Sam Bassett (aka Lothrazar)
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 ******************************************************************************/
package com.lothrazar.cyclicmagic.block.buildershape;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import com.lothrazar.cyclicmagic.core.block.TileEntityBaseMachineInvo;
import com.lothrazar.cyclicmagic.core.util.UtilItemStack;
import com.lothrazar.cyclicmagic.core.util.UtilPlaceBlocks;
import com.lothrazar.cyclicmagic.core.util.UtilShape;
import com.lothrazar.cyclicmagic.gui.ITilePreviewToggle;
import com.lothrazar.cyclicmagic.gui.ITileRedstoneToggle;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntityStructureBuilder extends TileEntityBaseMachineInvo implements ITileRedstoneToggle, ITilePreviewToggle, ITickable {

  private static final int spotsSkippablePerTrigger = 50;
  public static int TIMER_FULL = 25;
  private static final String NBT_BUILDTYPE = "build";
  private static final String NBT_SHAPEINDEX = "shapeindex";
  private int buildType;
  private int buildSize = 3;
  private int height;
  private int needsRedstone = 1;
  private int shapeIndex = 0;// current index of shape array
  private int renderParticles = 1;
  private int rotations = 0;
  public static int maxSize;
  public static int maxHeight = 10;
  private int offsetX = 0;
  private int offsetY = 0;
  private int offsetZ = 0;

  public static enum Fields {
    TIMER, BUILDTYPE, SPEED, SIZE, HEIGHT, REDSTONE, RENDERPARTICLES, ROTATIONS, OX, OY, OZ, FUEL;
  }

  public enum BuildType {
    FACING, SQUARE, CIRCLE, SOLID, SPHERE, DIAGONAL, DOME, CUP, PYRAMID;

    public static BuildType getNextType(BuildType btype) {
      int type = btype.ordinal();
      type++;
      if (type > SPHERE.ordinal()) {
        type = FACING.ordinal();
      }
      return BuildType.values()[type];
    }

    public boolean hasHeight() {
      if (this == SPHERE || this == DIAGONAL || this == DOME || this == CUP)
        return false;
      return true;
    }

    public String shortcode() {
      switch (this) {
        case CIRCLE:
          return "CI";
        case DIAGONAL:
          return "DI";
        case FACING:
          return "FA";
        case SOLID:
          return "SO";
        case SPHERE:
          return "SP";
        case SQUARE:
          return "SQ";
        case DOME:
          return "DO";
        case CUP:
          return "CU";
        case PYRAMID:
          return "PY";
      }
      return "";
    }
  }

  public TileEntityStructureBuilder() {
    super(9);
    this.initEnergy(BlockStructureBuilder.FUEL_COST);
    this.setSlotsForInsert(Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8));
  }

  @Override
  @SideOnly(Side.CLIENT)
  public AxisAlignedBB getRenderBoundingBox() {
    return TileEntity.INFINITE_EXTENT_AABB;
  }

  @Override
  public int[] getFieldOrdinals() {
    return super.getFieldArray(Fields.values().length);
  }

  @Override
  public List<BlockPos> getShape() {
    BuildType buildType = getBuildTypeEnum();
    List<BlockPos> shape = new ArrayList<BlockPos>();
    // only rebuild shapes if they are different
    switch (buildType) {
      case CIRCLE:
        shape = UtilShape.circleHorizontal(this.getPosTarget(), this.getSize() * 2);
        shape = UtilShape.repeatShapeByHeight(shape, getHeight() - 1);
      break;
      case FACING:
        shape = UtilShape.line(this.getPosTarget(), this.getCurrentFacing(), this.getSize());
        shape = UtilShape.repeatShapeByHeight(shape, getHeight() - 1);
      break;
      case SQUARE:
        shape = UtilShape.squareHorizontalHollow(this.getPosTarget(), this.getSize());
        shape = UtilShape.repeatShapeByHeight(shape, getHeight() - 1);
      break;
      case SOLID:
        shape = UtilShape.squareHorizontalFull(this.getTargetFacing(), this.getSize());
        shape = UtilShape.repeatShapeByHeight(shape, getHeight() - 1);
      break;
      case SPHERE:
        shape = UtilShape.sphere(this.getPosTarget(), this.getSize());
      break;
      case DOME:
        shape = UtilShape.sphereDome(this.getPosTarget(), this.getSize());
      break;
      case CUP:
        shape = UtilShape.sphereCup(this.getPosTarget().up(this.getSize()), this.getSize());
      break;
      case DIAGONAL:
        shape = UtilShape.diagonal(this.getPosTarget(), this.getCurrentFacing(), this.getSize() * 2, true);
      break;
      case PYRAMID:
        shape = UtilShape.squarePyramid(this.getPosTarget(), this.getSize(), getHeight());
      break;
    }
    return shape;
  }

  private BlockPos getPosTarget() {
    return this.getPos().add(this.offsetX, this.offsetY, this.offsetZ);
  }

  public BlockPos getTargetFacing() {
    //move center over that much, not including exact horizontal
    return this.getPosTarget().offset(this.getCurrentFacing(), this.getSize() + 1);
  }

  @Override
  public boolean isItemValidForSlot(int index, ItemStack stack) {
    return Block.getBlockFromItem(stack.getItem()) != null;
  }

  @Override
  public int getField(int id) {
    if (id >= 0 && id < this.getFieldCount()) {
      switch (Fields.values()[id]) {
        case TIMER:
          return timer;
        case BUILDTYPE:
          return this.buildType;
        case SPEED:
          return this.speed;
        case SIZE:
          return this.buildSize;
        case HEIGHT:
          return this.height;
        case REDSTONE:
          return this.needsRedstone;
        case RENDERPARTICLES:
          return this.renderParticles;
        case ROTATIONS:
          return this.rotations;
        case OX:
          return this.offsetX;
        case OY:
          return this.offsetY;
        case OZ:
          return this.offsetZ;
        case FUEL:
          return this.getEnergyCurrent();
      }
    }
    return -1;
  }

  @Override
  public void setField(int id, int value) {
    if (id >= 0 && id < this.getFieldCount()) {
      switch (Fields.values()[id]) {
        case FUEL:
          this.setEnergyCurrent(value);
        break;
        case TIMER:
          this.timer = value;
        break;
        case BUILDTYPE:
          if (value >= BuildType.values().length) {
            value = 0;
          }
          this.buildType = value;
        break;
        case SPEED:
          this.speed = value;
        break;
        case SIZE:
          this.buildSize = value;
        break;
        case HEIGHT:
          if (value > maxHeight) {
            value = maxHeight;
          }
          this.height = Math.max(1, value);
        break;
        case REDSTONE:
          this.needsRedstone = value;
        break;
        case RENDERPARTICLES:
          this.renderParticles = value;
        break;
        case ROTATIONS:
          this.rotations = Math.max(0, value);
        break;
        case OX:
          this.offsetX = value;
        break;
        case OY:
          this.offsetY = value;
        break;
        case OZ:
          this.offsetZ = value;
        break;
      }
    }
  }

  @Override
  public boolean onlyRunIfPowered() {
    return this.needsRedstone == 1;
  }

  public int getTimer() {
    return this.getField(Fields.TIMER.ordinal());
  }

  public int getHeight() {
    return this.getField(Fields.HEIGHT.ordinal());
  }

  public void setHeight(int value) {
    this.setField(Fields.HEIGHT.ordinal(), value);
  }

  public int getBuildType() {
    return this.getField(Fields.BUILDTYPE.ordinal());
  }

  public void setBuildType(int value) {
    this.setField(Fields.BUILDTYPE.ordinal(), value);
  }

  public BuildType getBuildTypeEnum() {
    int bt = Math.min(this.getBuildType(), BuildType.values().length - 1);
    return BuildType.values()[bt];
  }

  public void setSize(int s) {
    if (s <= 0) {
      s = 1;
    }
    if (s >= maxSize) {
      s = maxSize;
    }
    this.setField(Fields.SIZE.ordinal(), s);
  }

  public int getSize() {
    int s = this.getField(Fields.SIZE.ordinal());
    if (s <= 0) {
      s = 1;
    }
    return s;
  }

  @Override
  public int getFieldCount() {
    return Fields.values().length;
  }

  @Override
  public void readFromNBT(NBTTagCompound tagCompound) {
    super.readFromNBT(tagCompound);
    this.needsRedstone = tagCompound.getInteger(NBT_REDST);
    timer = tagCompound.getInteger(NBT_TIMER);
    shapeIndex = tagCompound.getInteger(NBT_SHAPEINDEX);
    this.buildType = tagCompound.getInteger(NBT_BUILDTYPE);
    this.buildSize = tagCompound.getInteger(NBT_SIZE);
    this.renderParticles = tagCompound.getInteger(NBT_RENDER);
    this.rotations = tagCompound.getInteger("rotations");
    this.setHeight(tagCompound.getInteger("buildHeight"));
    //ModCyclic.logger.info("HEIGHT IS READ : " + this.getHeight());
    // ModCyclic.logger.info("HEIGHT IS RAW : " + tagCompound.getInteger("buildHeight"));
    this.offsetX = tagCompound.getInteger("ox");
    this.offsetY = tagCompound.getInteger("oy");
    this.offsetZ = tagCompound.getInteger("oz");
  }

  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound tagCompound) {
    tagCompound.setInteger("buildHeight", height);
    //  ModCyclic.logger.info("HEIGHT IS WRITTEN : " + tagCompound.getInteger("buildHeight") + "????" + this.getHeight());
    tagCompound.setInteger(NBT_TIMER, timer);
    tagCompound.setInteger(NBT_REDST, this.needsRedstone);
    tagCompound.setInteger(NBT_SHAPEINDEX, this.shapeIndex);
    tagCompound.setInteger(NBT_BUILDTYPE, this.getBuildType());
    tagCompound.setInteger(NBT_SIZE, this.buildSize);
    tagCompound.setInteger(NBT_RENDER, renderParticles);
    tagCompound.setInteger("rotations", rotations);
    tagCompound.setInteger("ox", this.offsetX);
    tagCompound.setInteger("oy", this.offsetY);
    tagCompound.setInteger("oz", this.offsetZ);
    // ModCyclic.logger.info("buildSize  : " + tagCompound.getInteger(NBT_SIZE) + "????" + buildSize);
    return super.writeToNBT(tagCompound);
  }

  @Override
  public void update() {
    if (this.isRunning() == false || this.isInventoryEmpty()) {
      return;
    }
    this.shiftAllUp(1);
    if (this.updateEnergyIsBurning() == false) {
      return;
    }
    if (this.updateTimerIsZero()) {
      timer = TIMER_FULL;
      ItemStack stack = getStackInSlot(0);
      if (stack.isEmpty()) {
        return;
      }
      Block stuff = Block.getBlockFromItem(stack.getItem());
      if (stuff != null) {
        List<BlockPos> shape = this.getShape();
        if (shape.size() == 0) {
          return;
        }
        if (this.shapeIndex < 0 || this.shapeIndex >= shape.size()) {
          this.shapeIndex = 0;
        }
        BlockPos nextPos = shape.get(this.shapeIndex);//start at current position and validate
        for (int i = 0; i < spotsSkippablePerTrigger; i++) {
          //true means bounding box is null in the check. entit falling sand uses true
          //used to be exact air world.isAirBlock(nextPos)
          if (stuff.canPlaceBlockAt(world, nextPos) && //sutf checks isReplaceable for us, all AIR checks removed
              world.mayPlace(stuff, nextPos, true, EnumFacing.UP, null)) { // check if this spot is even valid
            IBlockState placeState = UtilItemStack.getStateFromMeta(stuff, stack.getMetadata());
            if (world.isRemote == false && UtilPlaceBlocks.placeStateSafe(world, null, nextPos, placeState)) {
              //rotations if any
              for (int j = 0; j < this.rotations; j++) {
                UtilPlaceBlocks.rotateBlockValidState(world, null, nextPos, this.getCurrentFacing());
              }
              //decrement and sound
              this.decrStackSize(0, 1);
            }
            break;//ok , target position is valid, we can build only into air
          }
          else {//cant build here. move up one
            nextPos = shape.get(this.shapeIndex);
            this.incrementPosition(shape);
          } //but after inrementing once, we may not yet be valid so skip at most ten spots per tick
        }
      }
    }
  }

  private void incrementPosition(List<BlockPos> shape) {
    if (shape == null || shape.size() == 0) {
      return;
    }
    else {
      int c = shapeIndex + 1;
      if (c < 0 || c >= shape.size()) {
        c = 0;
      }
      shapeIndex = c;
    }
  }

  @Override
  public void toggleNeedsRedstone() {
    int val = (this.needsRedstone + 1) % 2;
    this.setField(Fields.REDSTONE.ordinal(), val);
  }

  public void toggleSizeShape() {
    TileEntityStructureBuilder.BuildType old = this.getBuildTypeEnum();
    TileEntityStructureBuilder.BuildType next = TileEntityStructureBuilder.BuildType.getNextType(old);
    this.setBuildType(next.ordinal());
  }

  @Override
  public boolean isPreviewVisible() {
    return this.getField(Fields.RENDERPARTICLES.ordinal()) == 1;
  }
}
