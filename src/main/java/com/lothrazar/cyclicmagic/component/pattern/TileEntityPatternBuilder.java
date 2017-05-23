package com.lothrazar.cyclicmagic.component.pattern;
import java.util.List;
import com.lothrazar.cyclicmagic.ITileRedstoneToggle;
import com.lothrazar.cyclicmagic.block.tileentity.TileEntityBaseMachineInvo;
import com.lothrazar.cyclicmagic.util.UtilItemStack;
import com.lothrazar.cyclicmagic.util.UtilParticle;
import com.lothrazar.cyclicmagic.util.UtilShape;
import com.lothrazar.cyclicmagic.util.UtilSound;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TileEntityPatternBuilder extends TileEntityBaseMachineInvo implements ITickable, ITileRedstoneToggle {
  private final static int MAXIMUM = 32;
  private static final String NBT_REDST = "redstone";
  private static final int TIMER_FULL = 20;
  private static final int TIMER_SKIP = 1;
  private int height = 5;
  private int offsetTargetX = -4;
  private int offsetTargetY = 0;
  private int offsetTargetZ = 1;
  private int offsetSourceX = 4;
  private int offsetSourceY = 0;
  private int offsetSourceZ = 1;
  private int sizeRadius = 3;
  private int timer = 1;
  private int needsRedstone = 1;
  private int renderParticles = 1;
  public static enum Fields {
    OFFTARGX, OFFTARGY, OFFTARGZ, SIZER, OFFSRCX, OFFSRCY, OFFSRCZ, HEIGHT, TIMER, REDSTONE, RENDERPARTICLES;
  }
  public TileEntityPatternBuilder() {
    super(18);
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
    if (stateToMatch == null || stateToMatch.getBlock() == null) { return slot; }
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
  @Override
  public void update() {
    if (!isRunning()) { // it works ONLY if its powered
      return;
    }
    timer -= 1;
    if (timer <= 0) { //try build one block
      timer = TIMER_FULL;
      BlockPos centerSrc = this.getCenterSrc();
      List<BlockPos> shapeSrc = UtilShape.cubeFilled(centerSrc, this.sizeRadius, this.height);
      if (shapeSrc.size() <= 0) { return; }
      World world = this.getWorld();
      int pTarget = world.rand.nextInt(shapeSrc.size());
      BlockPos posSrc = shapeSrc.get(pTarget);
      int xOffset, yOffset, zOffset;
      xOffset = posSrc.getX() - centerSrc.getX();
      yOffset = posSrc.getY() - centerSrc.getY();
      zOffset = posSrc.getZ() - centerSrc.getZ();
      BlockPos centerTarget = this.getCenterTarget();
      BlockPos posTarget = centerTarget.add(xOffset, yOffset, zOffset);
      UtilParticle.spawnParticle(this.getWorld(), EnumParticleTypes.CRIT_MAGIC, posSrc);
      UtilParticle.spawnParticle(this.getWorld(), EnumParticleTypes.CRIT_MAGIC, posTarget);
      IBlockState stateToMatch;
      int slot;
      if (!world.isAirBlock(posSrc)) {
        stateToMatch = world.getBlockState(posSrc);
        slot = this.findSlotForMatch(stateToMatch);
        if (slot < 0) { return; } //EMPTY
        if (world.isAirBlock(posTarget)) { //now we want target to be air
          world.setBlockState(posTarget, stateToMatch);
          this.decrStackSize(slot, 1);
          UtilSound.playSoundPlaceBlock(world, posTarget, stateToMatch.getBlock());
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
  public List<BlockPos> getSourceShape() {
    BlockPos centerSrc = this.getPos().add(offsetSourceX, offsetSourceY, offsetSourceZ);
    List<BlockPos> shapeSrc = UtilShape.cubeFrame(centerSrc, this.sizeRadius, this.height);
    return shapeSrc;
  }
  public List<BlockPos> getTargetShape() {
    BlockPos centerTarget = this.getPos().add(offsetTargetX, offsetTargetY, offsetTargetZ);
    List<BlockPos> shapeTarget = UtilShape.cubeFrame(centerTarget, this.sizeRadius, this.height);
    return shapeTarget;
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
        return this.height;
      case TIMER:
        return this.timer;
      case REDSTONE:
        return this.needsRedstone;
      case RENDERPARTICLES:
        return this.renderParticles;
      default:
      break;
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
      default:
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
  public void swapShowRender() {
    int val = (this.renderParticles + 1) % 2;
    this.setField(Fields.RENDERPARTICLES.ordinal(), val);
  }
  public boolean isPreviewOn() { // sed by TESR
    return renderParticles == 1;
  }
}
