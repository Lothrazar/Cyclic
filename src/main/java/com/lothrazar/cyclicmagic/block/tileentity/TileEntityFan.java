package com.lothrazar.cyclicmagic.block.tileentity;
import java.util.List;
import com.lothrazar.cyclicmagic.util.UtilEntity;
import com.lothrazar.cyclicmagic.util.UtilParticle;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

public class TileEntityFan extends TileEntityBaseMachineInvo implements ITickable, ITileRedstoneToggle {
  private static final int MIN_RANGE = 1;
  private static final int TIMER_FULL = 30;
  private static final String NBT_TIMER = "Timer";
  private static final String NBT_REDST = "redstone";
  private static final String NBT_PART = "particles";
  private static final String NBT_PUSH = "pushpull";
  private static final String NBT_RANGE = "range";
  private static final int MAX_RANGE = 32;
  public static enum Fields {
    TIMER, REDSTONE, PARTICLES, PUSHPULL, RANGE;
  }
  private int timer;
  private int needsRedstone = 1;
  private int pushIfZero = 0;//else pull. 0 as default
  private int particlesIfZero = 0;// 0 as default
  private int speedBase = 13;//divide by 100 for real speed. bigger=faster
  private int range = 16;
  public TileEntityFan() {
    super(0);
  }
  @Override
  public void update() {
    if (this.isRunning() == false) {
      this.timer = 0;
      return;
    }
    EnumFacing facing = getCurrentFacing();
    int rangeFixed = getCurrentRange(); //can go up to max range unless hits a solid
    if (this.timer == 0) {
      this.timer = TIMER_FULL;
      //rm this its ugly, keep in case i add a custom particle
      if (particlesIfZero == 0) {
        doParticles(rangeFixed);
      }
    }
    else {
      this.timer--;
    }
    pushEntities(facing, rangeFixed);//int pushed = 
  }
  private int pushEntities(EnumFacing facing, int rangeFixed) {
    BlockPos start = this.getPos();
    BlockPos end = this.getCurrentFacingPos().offset(facing, rangeFixed).up();//.up()
    //without this hotfix, fan works only on the flatedge of the band, not the 1x1 area
    switch (facing.getAxis()) {
      case X:
        end = end.add(0, 0, 1);
      break;
      case Y:
      break;
      case Z:
        end = end.add(1, 0, 0);
      break;
      default:
      break;
    }
    AxisAlignedBB region = new AxisAlignedBB(start, end);
    List<Entity> nonPlayer = this.getWorld().getEntitiesWithinAABB(Entity.class, region);//UtilEntity.getLivingHostile(, region);
    // center of the block
    double x = this.getPos().getX() + 0.5;
    double y = this.getPos().getY() + 0.7;
    double z = this.getPos().getZ() + 0.5;
    float SPEED = this.getSpeedCalc();
    boolean pushIfFalse = (pushIfZero != 0);
    UtilEntity.pullEntityList(x, y, z, pushIfFalse, nonPlayer, SPEED, SPEED);
    return nonPlayer.size();
  }
  private float getSpeedCalc() {
    return ((float) speedBase) / 100F;
  }
  private void doParticles(int rangeFixed) {
    EnumFacing facing = getCurrentFacing();
    for (int i = 1; i <= rangeFixed; i++) {
      UtilParticle.spawnParticle(this.getWorld(), EnumParticleTypes.CLOUD, this.getPos().offset(facing, i), 1);
    }
  }
  private int getCurrentRange() {
    EnumFacing facing = getCurrentFacing();
    BlockPos tester;
    for (int i = MIN_RANGE; i <= this.getRange(); i++) {//if we start at fan, we hit MYSELF (the fan)
      tester = this.getPos().offset(facing, i);
      if (canBlowThrough(tester) == false) { return i; //cant pass thru
      }
    }
    return getRange();
  }
  public int getRange() {
    return this.range;
  }
  private void setRange(int value) {
    this.range = Math.min(value, MAX_RANGE);
    if (range < MIN_RANGE) {
      range = MIN_RANGE;
    }
  }
  private boolean canBlowThrough(BlockPos tester) {
    //passes through air, and anything NOT a full block
    return this.getWorld().isAirBlock(tester) || !this.getWorld().getBlockState(tester).isFullBlock();
  }
  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound tags) {
    tags.setInteger(NBT_TIMER, timer);
    tags.setInteger(NBT_REDST, this.needsRedstone);
    tags.setInteger(NBT_PART, this.particlesIfZero);
    tags.setInteger(NBT_PUSH, this.pushIfZero);
    tags.setInteger(NBT_RANGE, this.range);
    return super.writeToNBT(tags);
  }
  @Override
  public void readFromNBT(NBTTagCompound tags) {
    super.readFromNBT(tags);
    timer = tags.getInteger(NBT_TIMER);
    needsRedstone = tags.getInteger(NBT_REDST);
    this.particlesIfZero = tags.getInteger(NBT_PART);
    this.pushIfZero = tags.getInteger(NBT_PUSH);
    this.range = tags.getInteger(NBT_RANGE);
  }
  @Override
  public void toggleNeedsRedstone() {
    int val = this.needsRedstone + 1;
    this.setField(Fields.REDSTONE.ordinal(), val % 2);
  }
  private void setShowParticles(int value) {
    this.particlesIfZero = value % 2;
  }
  private void setPushPull(int value) {
    this.pushIfZero = value % 2;
  }
  public boolean onlyRunIfPowered() {
    return this.needsRedstone == 1;
  }
  @Override
  public int getFieldCount() {
    return Fields.values().length;
  }
  @Override
  public int getField(int id) {
    if (id >= 0 && id < this.getFieldCount()) {
      switch (Fields.values()[id]) {
        case TIMER:
          return timer;
        case REDSTONE:
          return this.needsRedstone;
        case PARTICLES:
          return this.particlesIfZero;
        case PUSHPULL:
          return this.pushIfZero;
        case RANGE:
          return this.range;
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
        case REDSTONE:
          this.needsRedstone = value;
        break;
        case PARTICLES:
          this.setShowParticles(value);
        break;
        case PUSHPULL:
          this.setPushPull(value);
        break;
        case RANGE:
          this.setRange(value);
        break;
      }
    }
  }
}
