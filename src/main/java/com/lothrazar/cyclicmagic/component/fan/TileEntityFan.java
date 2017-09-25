package com.lothrazar.cyclicmagic.component.fan;
import java.util.List;
import com.lothrazar.cyclicmagic.block.base.TileEntityBaseMachineInvo;
import com.lothrazar.cyclicmagic.gui.ITilePreviewToggle;
import com.lothrazar.cyclicmagic.gui.ITileRedstoneToggle;
import com.lothrazar.cyclicmagic.util.UtilEntity;
import com.lothrazar.cyclicmagic.util.UtilParticle;
import com.lothrazar.cyclicmagic.util.UtilShape;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

public class TileEntityFan extends TileEntityBaseMachineInvo implements ITickable, ITileRedstoneToggle, ITilePreviewToggle {
  private static final int MIN_RANGE = 1;
  private static final int TIMER_FULL = 30;
  protected static final int MAX_SPEED = 10;//unused mostly
  private static final String NBT_PART = "particles";
  private static final String NBT_PUSH = "pushpull";
  private static final String NBT_RANGE = "range";
  private static final int MAX_RANGE = 32;
  public static enum Fields {
    TIMER, REDSTONE, PARTICLES, PUSHPULL, RANGE, SPEED;
  }
  private int timer;
  private int needsRedstone = 1;
  private int pushIfZero = 0;//else pull. 0 as default
  private int showParticles = 0;// 0 as default
  private int range = 16;
  public TileEntityFan() {
    super(0);
    this.speed = 5;
  }
  @Override
  public int[] getFieldOrdinals() {
    return super.getFieldArray(Fields.values().length);
  }
  @Override
  public void update() {
    if (this.isRunning() == false) {
      this.timer = 0;
      return;
    }
    if (this.timer == 0) {
      this.timer = TIMER_FULL;
      //rm this its ugly, keep in case i add a custom particle
      if (isPreviewVisible()) {
        doParticles();
      }
    }
    else {
      this.timer--;
    }
    pushEntities();
  }
  public List<BlockPos> getShape() {
    //UtilShape.line(this.getPos(), this.getCurrentFacing(), this.getSize());
    return UtilShape.line(getPos(), getCurrentFacing(), getCurrentRange());
  }
  private int pushEntities() {
    List<BlockPos> shape = getShape();
    if (shape.size() == 0) {
      // sometimes is empty on changing dimension or tile load/unload
      return 0;
    }
    BlockPos start = shape.get(0);
    BlockPos end = shape.get(shape.size() - 1);//without this hotfix, fan works only on the flatedge of the band, not the 1x1 area
    switch (getCurrentFacing().getAxis()) {
      case X:
        end = end.add(0, 0, 1);//X means EASTorwest. adding +1z means GO 1 south
      break;
      case Y:
      break;
      case Z:
        end = end.add(1, 0, 0);
      break;
      default:
      break;
    }
    end = end.add(0, 1, 0);///and of course go up one space. so we have a 3D range selected not a flat slice (ex: height 66 to 67)
    //ok now we have basically teh 3d box we wanted
    //problem: NORTH and WEST are skipping first blocks right at fan, but shouldnt.
    //EAST and SOUTH are skiping LAST blocks, but shouldnt
    //just fix it. root cause seems fine esp with UtilShape used
    EnumFacing face = getCurrentFacing();
    switch (face) {
      case NORTH:
        start = start.south();
      break;
      case SOUTH:
        end = end.south();
      break;
      case EAST:
        end = end.east();
      break;
      case WEST:
        start = start.east();
      break;
      default:
      break;
    }
    AxisAlignedBB region = new AxisAlignedBB(start, end);
    List<Entity> entitiesFound = this.getWorld().getEntitiesWithinAABB(Entity.class, region);//UtilEntity.getLivingHostile(, region);
    // center of the block
    double x = this.getPos().getX() + 0.5;
    double y = this.getPos().getY() + 2;//was 0.7; dont move them up, move down. let them fall!
    double z = this.getPos().getZ() + 0.5;
    float SPEED = this.getSpeedCalc();
    boolean pushIfFalse = (pushIfZero != 0);
    UtilEntity.pullEntityList(x, y, z, pushIfFalse, entitiesFound, SPEED, SPEED);
    return entitiesFound.size();
  }
  private float getSpeedCalc() {
    return ((float) this.speed) / 15F;
  }
  private void doParticles() {
    List<BlockPos> shape = getShape();
    for (BlockPos pos : shape) {
      UtilParticle.spawnParticle(this.getWorld(), EnumParticleTypes.CLOUD, pos, 1);
    }
  }
  private int getCurrentRange() {
    EnumFacing facing = getCurrentFacing();
    BlockPos tester;
    for (int i = MIN_RANGE; i <= this.getRange(); i++) {//if we start at fan, we hit MYSELF (the fan)
      tester = this.getPos().offset(facing, i);
      if (canBlowThrough(tester) == false) {
        return i; //cant pass thru
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
    tags.setInteger(NBT_PART, this.showParticles);
    tags.setInteger(NBT_PUSH, this.pushIfZero);
    tags.setInteger(NBT_RANGE, this.range);
    return super.writeToNBT(tags);
  }
  @Override
  public void readFromNBT(NBTTagCompound tags) {
    super.readFromNBT(tags);
    timer = tags.getInteger(NBT_TIMER);
    needsRedstone = tags.getInteger(NBT_REDST);
    this.showParticles = tags.getInteger(NBT_PART);
    this.pushIfZero = tags.getInteger(NBT_PUSH);
    this.range = tags.getInteger(NBT_RANGE);
  }
  @Override
  public void toggleNeedsRedstone() {
    int val = this.needsRedstone + 1;
    this.setField(Fields.REDSTONE.ordinal(), val % 2);
  }
  private void setShowParticles(int value) {
    this.showParticles = value % 2;
  }
  private void setPushPull(int value) {
    this.pushIfZero = value % 2;
    this.markDirty();
    this.world.markBlockRangeForRenderUpdate(pos, pos);
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
          return this.showParticles;
        case PUSHPULL:
          return this.pushIfZero;
        case RANGE:
          return this.range;
        case SPEED:
          return this.speed;
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
        case SPEED:
          this.setSpeed(value);
        break;
      }
    }
  }
  @Override
  public void setSpeed(int value) {
    if (value < 1) {
      value = 1;
    }
    speed = Math.min(value, MAX_SPEED);
  }
  @Override
  public void togglePreview() {
    this.showParticles = (this.showParticles + 1) % 2;
  }
  @Override
  public boolean isPreviewVisible() {
    return this.showParticles == 1;
  }
}
