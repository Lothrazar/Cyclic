package com.lothrazar.cyclicmagic.block.tileentity;
import java.util.ArrayList;
import java.util.List;
import com.lothrazar.cyclicmagic.util.UtilItemStack;
import com.lothrazar.cyclicmagic.util.UtilParticle;
import com.lothrazar.cyclicmagic.util.UtilPlaceBlocks;
import com.lothrazar.cyclicmagic.util.UtilShape;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TileMachineStructureBuilder extends TileEntityBaseMachineInvo implements ITileRedstoneToggle, ITileSizeToggle, ITickable {
  final static int spotsSkippablePerTrigger = 50;
  private static final int maxSpeed = 1;
  public static final int TIMER_FULL = 10;// 100;//one day i will add fuel AND/OR speed upgrades. till then make very slow
  private static final int[] hopperInput = { 0, 1, 2, 3, 4, 5, 6, 7, 8 };// all slots
  private static final String NBT_TIMER = "Timer";
  private static final String NBT_BUILDTYPE = "build";
  private static final String NBT_SPEED = "speed";
  private static final String NBT_SIZE = "size";
  private static final String NBT_REDST = "redstone";
  private static final String NBT_SHAPEINDEX = "shapeindex";
  private int timer;
  private int buildType;
  private int buildSpeed;
  private int buildSize = 3;
  private int buildHeight = 3;
  private int needsRedstone = 1;
  private int shapeIndex = 0;// current index of shape array
  public static int maxSize;
  public static int maxHeight = 10;
  public static enum Fields {
    TIMER, BUILDTYPE, SPEED, SIZE, HEIGHT, REDSTONE
  }
  public enum BuildType {
    FACING, SQUARE, CIRCLE, SOLID, STAIRWAY, SPHERE;
    public static BuildType getNextType(BuildType btype) {
      int type = btype.ordinal();
      type++;
      if (type > SPHERE.ordinal()) {
        type = FACING.ordinal();
      }
      return BuildType.values()[type];
    }
    public boolean hasHeight() {
      if (this == STAIRWAY || this == SPHERE)
        return false;
      return true;
    }
  }
  public TileMachineStructureBuilder() {
    super(9);
  }
  public List<BlockPos> rebuildShape() {
    BuildType buildType = getBuildTypeEnum();
    List<BlockPos> shape = new ArrayList<BlockPos>();
    // only rebuild shapes if they are different
    switch (buildType) {
      case CIRCLE:
        shape = UtilShape.circleHorizontal(this.getPos(), this.getSize() * 2);
      break;
      case FACING:
        shape = UtilShape.line(this.getPos(), this.getCurrentFacing(), this.getSize());
      break;
      case SQUARE:
        shape = UtilShape.squareHorizontalHollow(this.getPos(), this.getSize());
      break;
      case SOLID:
        shape = UtilShape.squareHorizontalFull(this.getTargetCenter(), this.getSize());
      break;
      case STAIRWAY:
        shape = UtilShape.stairway(this.getPos(), this.getCurrentFacing(), this.getSize() * 2, true);
      break;
      case SPHERE:
        shape = UtilShape.sphere(this.getPos(), this.getSize());
      break;
    }
    if (buildType.hasHeight() && this.buildHeight > 1) { //first layer is already done, add remaining
      shape = UtilShape.repeatShapeByHeight(shape, buildHeight - 1);
    }
    return shape;
  }
  public BlockPos getTargetCenter() {
    //move center over that much, not including exact horizontal
    return this.getPos().offset(this.getCurrentFacing(), this.getSize() + 1);
  }
  @Override
  public boolean isItemValidForSlot(int index, ItemStack stack) {
    return Block.getBlockFromItem(stack.getItem()) != null;
  }
  @Override
  public int getField(int id) {
    if (id >= 0 && id < this.getFieldCount())
      switch (Fields.values()[id]) {
      case TIMER:
      return timer;
      case BUILDTYPE:
      return this.buildType;
      case SPEED:
      return this.buildSpeed;
      case SIZE:
      return this.buildSize;
      case HEIGHT:
      return this.buildHeight;
      case REDSTONE:
      return this.needsRedstone;
      }
    return -1;
  }
  @Override
  public void setField(int id, int value) {
    if (id >= 0 && id < this.getFieldCount())
      switch (Fields.values()[id]) {
      case TIMER:
      this.timer = value;
      break;
      case BUILDTYPE:
      this.buildType = value;
      break;
      case SPEED:
      this.buildSpeed = value;
      break;
      case SIZE:
      this.buildSize = value;
      break;
      case HEIGHT:
      if (value > maxHeight) {
      value = maxHeight;
      }
      this.buildHeight = value;
      break;
      case REDSTONE:
      this.needsRedstone = value;
      break;
      default:
      break;
      }
  }
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
  public void setSpeed(int s) {
    if (s <= 0) {
      s = 1;
    }
    if (s >= maxSpeed) {
      s = maxSpeed;
    }
    this.setField(Fields.SPEED.ordinal(), s);
  }
  public int getSpeed() {
    int s = this.getField(Fields.SPEED.ordinal());
    if (s <= 0) {
      s = 1;
    }
    return s;
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
    this.buildSpeed = tagCompound.getInteger(NBT_SPEED);
    this.buildSize = tagCompound.getInteger(NBT_SIZE);
  }
  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound tagCompound) {
    tagCompound.setInteger(NBT_TIMER, timer);
    tagCompound.setInteger(NBT_REDST, this.needsRedstone);
    tagCompound.setInteger(NBT_SHAPEINDEX, this.shapeIndex);
    tagCompound.setInteger(NBT_BUILDTYPE, this.getBuildType());
    tagCompound.setInteger(NBT_SPEED, this.getSpeed());
    tagCompound.setInteger(NBT_SIZE, this.getSize());
    return super.writeToNBT(tagCompound);
  }
  public boolean isBurning() {
    return this.timer > 0 && this.timer < TIMER_FULL;
  }
  public boolean isRunning() {
    return !this.onlyRunIfPowered() || this.isPowered();
  }
  @Override
  public void update() {
    shiftAllUp();
    boolean trigger = false;
    if (!isRunning()) { return; }
    this.spawnParticlesAbove();
    World world = getWorld();
    ItemStack stack = getStackInSlot(0);
    if (stack != ItemStack.EMPTY) {
      timer -= this.getSpeed();
      if (timer <= 0) {
        timer = TIMER_FULL;
        trigger = true;
      }
    }
    if (trigger) {
      Block stuff = Block.getBlockFromItem(stack.getItem());
      if (stuff != null) {
        List<BlockPos> shape = this.rebuildShape();
        if (shape.size() == 0) { return; }
        if (this.shapeIndex < 0 || this.shapeIndex >= shape.size()) {
          this.shapeIndex = 0;
        }
        BlockPos nextPos = shape.get(this.shapeIndex);//start at current position and validate
        for (int i = 0; i < spotsSkippablePerTrigger; i++) {
          //true means bounding box is null in the check. entit falling sand uses true
          if (world.isAirBlock(nextPos) &&
              stuff.canPlaceBlockAt(world, nextPos) &&
              world.mayPlace(stuff, nextPos, true, EnumFacing.UP, null)) { // check if this spot is even valid
            IBlockState placeState = UtilItemStack.getStateFromMeta(stuff, stack.getMetadata());
            if (world.isRemote == false && world.isAirBlock(nextPos) && UtilPlaceBlocks.placeStateSafe(world, null, nextPos, placeState)) {
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
    //    this.markDirty();
  }
  @Override
  public int[] getSlotsForFace(EnumFacing side) {
    return hopperInput;
  }
  @Override
  public void toggleNeedsRedstone() {
    int val = this.needsRedstone + 1;
    if (val > 1) {
      val = 0;//hacky lazy way
    }
    this.setField(Fields.REDSTONE.ordinal(), val);
  }
  @Override
  public void toggleSizeShape() {
    TileMachineStructureBuilder.BuildType old = this.getBuildTypeEnum();
    TileMachineStructureBuilder.BuildType next = TileMachineStructureBuilder.BuildType.getNextType(old);
    this.setBuildType(next.ordinal());
  }
  @Override
  public void displayPreview() {
    List<BlockPos> shape;
    if (this.buildType == BuildType.SPHERE.ordinal()) {//spheres of bigger sizes just literally only render part then get cut off so
      shape = UtilShape.circleHorizontal(this.getPos(), this.getSize() * 2);
      shape.addAll(UtilShape.circleVertical(this.getPos(), this.getSize() * 2));
    }
    else {
      shape = this.rebuildShape();
    }
    for (BlockPos pos : shape) {
      UtilParticle.spawnParticle(getWorld(), EnumParticleTypes.DRAGON_BREATH, pos, 2);
    }
  }
}
