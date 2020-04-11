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

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.block.core.TileEntityBaseMachineInvo;
import com.lothrazar.cyclicmagic.capability.EnergyStore;
import com.lothrazar.cyclicmagic.data.BlockPosDim;
import com.lothrazar.cyclicmagic.data.ITilePreviewToggle;
import com.lothrazar.cyclicmagic.data.ITileRedstoneToggle;
import com.lothrazar.cyclicmagic.item.locationgps.ItemLocationGps;
import com.lothrazar.cyclicmagic.util.UtilFakePlayer;
import com.lothrazar.cyclicmagic.util.UtilPlaceBlocks;
import com.lothrazar.cyclicmagic.util.UtilShape;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntityStructureBuilder extends TileEntityBaseMachineInvo implements ITileRedstoneToggle, ITilePreviewToggle, ITickable {

  private static final int spotsSkippablePerTrigger = 50;
  private static final String NBT_BUILDTYPE = "build";
  private static final String NBT_SHAPEINDEX = "shapeindex";
  private int buildType;
  private int buildSize = 3;
  private int height;
  private int shapeIndex = 0;// current index of shape array
  public static int maxSize;
  public static int maxHeight = 10;
  public static final int SLOT_GPS = 9;

  public static enum Fields {
    TIMER, BUILDTYPE, SPEED, SIZE, HEIGHT, REDSTONE, RENDERPARTICLES;
  }

  public TileEntityStructureBuilder() {
    super(9 + 1);
    this.initEnergy(new EnergyStore(MENERGY), BlockStructureBuilder.FUEL_COST);
    this.setSlotsForInsert(Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8));
    this.needsRedstone = 1;
    this.renderParticles = 1;
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
    StructureBuilderType buildType = getBuildTypeEnum();
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
    ItemStack gps = this.getStackInSlot(SLOT_GPS);
    BlockPosDim target = ItemLocationGps.getPosition(gps);
    BlockPos myTarget = null;
    if (target == null) {
      myTarget = this.getPos();
    }
    else {
      myTarget = target.toBlockPos();
    }
    return myTarget;
  }

  public BlockPos getTargetFacing() {
    //move center over that much, not including exact horizontal
    return this.getPosTarget().offset(this.getCurrentFacing(), this.getSize() + 1);
  }

  @Override
  public boolean isItemValidForSlot(int index, ItemStack stack) {
    if (index == SLOT_GPS) {
      return stack.getItem() instanceof ItemLocationGps;
    }
    return true;//dont check for "is item block" stuff like dank null 
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
      }
    }
    return -1;
  }

  @Override
  public void setField(int id, int value) {
    if (id >= 0 && id < this.getFieldCount()) {
      switch (Fields.values()[id]) {
        case TIMER:
          this.timer = value;
        break;
        case BUILDTYPE:
          //??toggleSizeShape
          if (value >= StructureBuilderType.values().length) {
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
          ModCyclic.logger.info("OLD VALUE " + this.renderParticles);
          this.renderParticles = value % 2;
          ModCyclic.logger.info("renderPart toggle" + this.renderParticles);
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

  public StructureBuilderType getBuildTypeEnum() {
    int bt = Math.min(this.getBuildType(), StructureBuilderType.values().length - 1);
    return StructureBuilderType.values()[bt];
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
    shapeIndex = tagCompound.getInteger(NBT_SHAPEINDEX);
    this.buildType = tagCompound.getInteger(NBT_BUILDTYPE);
    this.buildSize = tagCompound.getInteger(NBT_SIZE);
    this.setHeight(tagCompound.getInteger("buildHeight"));
  }

  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound tagCompound) {
    tagCompound.setInteger("buildHeight", height);
    tagCompound.setInteger(NBT_SHAPEINDEX, this.shapeIndex);
    tagCompound.setInteger(NBT_BUILDTYPE, this.getBuildType());
    tagCompound.setInteger(NBT_SIZE, this.buildSize);
    return super.writeToNBT(tagCompound);
  }

  private void verifyFakePlayer(WorldServer w) {
    if (uuid == null) {
      uuid = UUID.randomUUID();
    }
    if (fakePlayer == null) {
      fakePlayer = UtilFakePlayer.initFakePlayer(w, this.uuid, this.getBlockType().getTranslationKey());
      if (fakePlayer == null) {
        ModCyclic.logger.error("Fake player failed to init ");
      }
    }
  }

  private WeakReference<FakePlayer> fakePlayer;
  private UUID uuid;

  @Override
  public void update() {
    if (this.isRunning() == false || this.isInventoryEmpty()) {
      return;
    }
    if (world instanceof WorldServer) {
      verifyFakePlayer((WorldServer) world);
    }
    this.shiftAllUp(1);
    if (this.updateEnergyIsBurning() == false) {
      return;
    }
    ItemStack stack = getStackInSlot(0);
    if (stack.isEmpty()) {
      return;
    }
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
      if (world.isAirBlock(nextPos) && fakePlayer != null) { // check if this spot is even valid
        ItemStack gps = this.getStackInSlot(SLOT_GPS);
        BlockPosDim target = ItemLocationGps.getPosition(gps);
        EnumFacing targetSide = (target == null) ? EnumFacing.UP : target.getSide();
        Vec3d tarVec = (target == null) ? Vec3d.ZERO : target.getHitVec();
        if (UtilPlaceBlocks.buildStackAsPlayer(world, fakePlayer.get(),
            nextPos, stack, targetSide, tarVec)) {
          stack.shrink(1); //          this.decrStackSize(0, 1);
        }
        break;//ok , target position is valid, we can build only into air
      }
      else {//cant build here. move up one
        nextPos = shape.get(this.shapeIndex);
        this.incrementPosition(shape);
      } //but after inrementing once, we may not yet be valid so skip at most ten spots per tick
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

  @Override
  public boolean isPreviewVisible() {
    return this.renderParticles == 1;
  }
}
