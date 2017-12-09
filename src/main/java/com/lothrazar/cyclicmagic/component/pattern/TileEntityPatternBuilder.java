package com.lothrazar.cyclicmagic.component.pattern;
import java.util.ArrayList;
import java.util.List;
import com.lothrazar.cyclicmagic.block.base.TileEntityBaseMachineInvo;
import com.lothrazar.cyclicmagic.gui.ITilePreviewToggle;
import com.lothrazar.cyclicmagic.gui.ITileRedstoneToggle;
import com.lothrazar.cyclicmagic.util.UtilItemStack;
import com.lothrazar.cyclicmagic.util.UtilParticle;
import com.lothrazar.cyclicmagic.util.UtilShape;
import com.lothrazar.cyclicmagic.util.UtilSound;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ITickable;
import net.minecraft.util.Rotation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TileEntityPatternBuilder extends TileEntityBaseMachineInvo implements ITickable, ITilePreviewToggle, ITileRedstoneToggle {
  private final static int MAXIMUM = 32;
  private static final String NBT_REDST = "redstone";
  private static final int TIMER_FULL = 20;
  private static final int TIMER_SKIP = 1;
  private int height = 5;
  private int offsetTargetX = -10;
  private int offsetTargetY = 0;
  private int offsetTargetZ = 1;
  private int offsetSourceX = 6;
  private int offsetSourceY = 0;
  private int offsetSourceZ = 1;
  private int sizeRadius = 5;
  private int timer = 1;
  private int needsRedstone = 1;
  private int renderParticles = 1;
  private int rotation = 0;//enum value of Rotation
  public static enum Fields {
    OFFTARGX, OFFTARGY, OFFTARGZ, SIZER, OFFSRCX, OFFSRCY, OFFSRCZ, HEIGHT, TIMER, REDSTONE, RENDERPARTICLES, ROTATION;
  }
  public TileEntityPatternBuilder() {
    super(18);
    this.setSlotsForBoth();
  }
  @Override
  public int[] getFieldOrdinals() {
    return super.getFieldArray(Fields.values().length);
  }
  @Override
  public int getFieldCount() {
    return Fields.values().length;
  }
  private BlockPos getCenterTarget() {
    return this.getPos().add(offsetTargetX, offsetTargetY, offsetTargetZ);
  }
  private BlockPos getCenterSrc() {
    return this.getPos().add(offsetSourceX, offsetSourceY, offsetSourceZ);
  }
  private int findSlotForMatch(IBlockState stateToMatch) {
    int slot = -1;
    if (stateToMatch == null || stateToMatch.getBlock() == null) {
      return slot;
    }
    ItemStack is;
    Item itemFromState;
    for (int i = 0; i < this.getSizeInventory(); i++) {
      is = this.getStackInSlot(i);
      if (UtilItemStack.isEmpty(is)) {
        continue;
      }
      itemFromState = Item.getItemFromBlock(stateToMatch.getBlock());
      if (itemFromState == is.getItem()) {
        slot = i;//yep it matches
        break;
      }
    }
    return slot;
  }
  public boolean onlyRunIfPowered() {
    return this.needsRedstone == 1;
  }
  private BlockPos convertPosSrcToTarget(BlockPos posSrc) {
    BlockPos centerSrc = this.getCenterSrc();
    int xOffset = posSrc.getX() - centerSrc.getX();
    int yOffset = posSrc.getY() - centerSrc.getY();
    int zOffset = posSrc.getZ() - centerSrc.getZ();
    BlockPos centerTarget = this.getCenterTarget();
    return centerTarget.add(xOffset, yOffset, zOffset);
  }
  @Override
  public void update() {
    if (!isRunning()) { // it works ONLY if its powered
      return;
    }
    timer -= 1;
    if (timer <= 0) { //try build one block
      timer = 0;
      List<BlockPos> shapeSrc = this.getSourceShape();
      if (shapeSrc.size() <= 0) {
        return;
      }
      int pTarget = world.rand.nextInt(shapeSrc.size());
      BlockPos posSrc = shapeSrc.get(pTarget);
      BlockPos posTarget = convertPosSrcToTarget(posSrc);
      if (this.renderParticles == 1) {
        UtilParticle.spawnParticle(this.getWorld(), EnumParticleTypes.CRIT_MAGIC, posSrc);
        UtilParticle.spawnParticle(this.getWorld(), EnumParticleTypes.CRIT_MAGIC, posTarget);
      }
      IBlockState stateToMatch;
      int slot;
      if (world.isAirBlock(posSrc) == false) {
        stateToMatch = world.getBlockState(posSrc);
        slot = this.findSlotForMatch(stateToMatch);
        if (slot < 0) {
          return;
        } //EMPTY
        if (world.isAirBlock(posTarget)) { //now we want target to be air
          timer = TIMER_FULL;//now start over
          world.setBlockState(posTarget, stateToMatch);
          this.decrStackSize(slot, 1);
          SoundType type = UtilSound.getSoundFromBlockstate(stateToMatch, world, posTarget);
          if (type != null && type.getPlaceSound() != null) {
            int dim = this.getDimension();
            int range = 18;
            UtilSound.playSoundFromServer(type.getPlaceSound(), SoundCategory.BLOCKS, posTarget, dim, range);
          }
        }
        else { //does NOT MATCH, so skip ahead
          timer = TIMER_SKIP;
        }
      }
      else { //src IS air, so skip ahead
        timer = TIMER_SKIP;
      }
    }
  }
  public BlockPos getSourceCenter() {
    return this.getPos().add(offsetSourceX, offsetSourceY, offsetSourceZ);
  }
  public BlockPos getTargetCenter() {
    return this.getPos().add(offsetTargetX, offsetTargetY, offsetTargetZ);
  }
  public List<BlockPos> getSourceFrameOutline() {
    BlockPos centerSrc = getSourceCenter();
    List<BlockPos> shapeSrc = UtilShape.cubeFrame(centerSrc, this.sizeRadius, this.height);
    return shapeSrc;
  }
  public List<BlockPos> getTargetFrameOutline() {
    return UtilShape.cubeFrame(getTargetCenter(), this.sizeRadius, this.height);
  }
  public List<BlockPos> getSourceShape() {
    BlockPos centerSrc = this.getSourceCenter();
    return UtilShape.readAllSolid(world, centerSrc, this.sizeRadius, this.height);
  }
  public List<BlockPos> getTargetShape() {
    List<BlockPos> shapeSrc = getSourceShape();
    List<BlockPos> shapeTarget = new ArrayList<BlockPos>();
    for (BlockPos p : shapeSrc) {
      shapeTarget.add(this.convertPosSrcToTarget(new BlockPos(p)));
    }
    return UtilShape.rotateShape(this.getCenterTarget(), shapeTarget, this.getRotation());
  }
  @Override
  public int[] getSlotsForFace(EnumFacing side) {
    return new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17 };
  }
  @Override
  public void readFromNBT(NBTTagCompound compound) {
    super.readFromNBT(compound);
    this.offsetTargetX = compound.getInteger("ox");
    this.offsetTargetY = compound.getInteger("oy");
    this.offsetTargetZ = compound.getInteger("oz");
    this.offsetSourceX = compound.getInteger("sx");
    this.offsetSourceY = compound.getInteger("sy");
    this.offsetSourceZ = compound.getInteger("sz");
    this.sizeRadius = compound.getInteger("r");
    this.height = compound.getInteger("height");
    this.timer = compound.getInteger("timer");
    this.renderParticles = compound.getInteger("render");
    this.needsRedstone = compound.getInteger(NBT_REDST);
  }
  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound compound) {
    compound.setInteger("ox", offsetTargetX);
    compound.setInteger("oy", offsetTargetY);
    compound.setInteger("oz", offsetTargetZ);
    compound.setInteger("sx", offsetSourceX);
    compound.setInteger("sy", offsetSourceY);
    compound.setInteger("sz", offsetSourceZ);
    compound.setInteger("r", sizeRadius);
    compound.setInteger("height", height);
    compound.setInteger("timer", timer);
    compound.setInteger("render", renderParticles);
    compound.setInteger(NBT_REDST, this.needsRedstone);
    return super.writeToNBT(compound);
  }
  public int getHeight() {
    return height;
  }
  public Rotation getRotation() {
    return Rotation.values()[this.rotation];
  }
  public String getRotationName() {
    switch (this.getRotation()) {
      case CLOCKWISE_180:
        return "180";
      case CLOCKWISE_90:
        return "90";
      case COUNTERCLOCKWISE_90:
        return "270";
      case NONE:
      break;
    }
    return "None";
  }
  public int getField(Fields f) {
    switch (f) {
      case OFFTARGX:
        return this.offsetTargetX;
      case OFFTARGY:
        return this.offsetTargetY;
      case OFFTARGZ:
        return this.offsetTargetZ;
      case SIZER:
        return this.sizeRadius;
      case OFFSRCX:
        return this.offsetSourceX;
      case OFFSRCY:
        return this.offsetSourceY;
      case OFFSRCZ:
        return this.offsetSourceZ;
      case HEIGHT:
        return this.getHeight();
      case TIMER:
        return this.timer;
      case REDSTONE:
        return this.needsRedstone;
      case RENDERPARTICLES:
        return this.renderParticles;
      case ROTATION:
        return this.rotation;
    }
    return 0;
  }
  public void setField(Fields f, int value) {
    //max applies to all fields
    if (value > MAXIMUM) {
      value = MAXIMUM;
    }
    switch (f) {
      case OFFTARGX:
        this.offsetTargetX = value;
      break;
      case OFFTARGY:
        this.offsetTargetY = value;
      break;
      case OFFTARGZ:
        this.offsetTargetZ = value;
      break;
      case SIZER:
        this.sizeRadius = value;
      break;
      case OFFSRCX:
        this.offsetSourceX = value;
      break;
      case OFFSRCY:
        this.offsetSourceY = value;
      break;
      case OFFSRCZ:
        this.offsetSourceZ = value;
      break;
      case HEIGHT:
        this.height = value;
      break;
      case TIMER:
        this.timer = value;
      break;
      case REDSTONE:
        this.needsRedstone = value;
      break;
      case RENDERPARTICLES:
        this.renderParticles = value;
      break;
      case ROTATION:
        this.rotation = value % Rotation.values().length;
      break;
    }
  }
  @Override
  public int getField(int id) {
    return getField(Fields.values()[id]);
  }
  @Override
  public void setField(int id, int value) {
    setField(Fields.values()[id], value);
  }
  public void swapTargetSource() {
    int srcX = this.offsetSourceX;
    int srcY = this.offsetSourceY;
    int srcZ = this.offsetSourceZ;
    this.offsetSourceX = this.offsetTargetX;
    this.offsetSourceY = this.offsetTargetY;
    this.offsetSourceZ = this.offsetTargetZ;
    this.offsetTargetX = srcX;
    this.offsetTargetY = srcY;
    this.offsetTargetZ = srcZ;
  }
  @Override
  public void toggleNeedsRedstone() {
    int val = (this.needsRedstone + 1) % 2;
    this.setField(Fields.REDSTONE.ordinal(), val);
  }
  @Override
  public void togglePreview() {
    int val = (this.renderParticles + 1) % 2;
    this.setField(Fields.RENDERPARTICLES.ordinal(), val);
  }
  @Override
  public boolean isPreviewVisible() {
    return renderParticles == 1;
  }
  @Override
  public List<BlockPos> getShape() {
    return getTargetShape();//special case for this block, not used here
  }
}
