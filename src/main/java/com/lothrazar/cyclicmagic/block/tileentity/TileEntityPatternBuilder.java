package com.lothrazar.cyclicmagic.block.tileentity;
import java.util.List;
import com.lothrazar.cyclicmagic.util.UtilItemStack;
import com.lothrazar.cyclicmagic.util.UtilParticle;
import com.lothrazar.cyclicmagic.util.UtilShape;
import com.lothrazar.cyclicmagic.util.UtilSound;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TileEntityPatternBuilder extends TileEntityBaseMachineInvo implements ITickable, ITileRedstoneToggle {
  private static final EnumParticleTypes PARTICLE_TARGET = EnumParticleTypes.CLOUD;
  private static final EnumParticleTypes PARTICLE_SRC = EnumParticleTypes.DRAGON_BREATH;
  private static final String NBT_REDST = "redstone";
  private static final String NBT_INV = "Inventory";
  private static final String NBT_SLOT = "Slot";
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
  private static final int TIMER_FULL = 20;
  private static final int TIMER_SKIP = 1;
  private ItemStack[] inv;
  public static enum Fields {
    OFFTARGX, OFFTARGY, OFFTARGZ, SIZER, OFFSRCX, OFFSRCY, OFFSRCZ, HEIGHT, TIMER, REDSTONE
  }
  public TileEntityPatternBuilder() {
    inv = new ItemStack[18];
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
  private boolean onlyRunIfPowered() {
    return this.needsRedstone == 1;
  }
  @Override
  public void update() {
    if (this.onlyRunIfPowered() && this.isPowered() == false) {
      // it works ONLY if its powered
      return;
    }
    this.renderBoundingBoxes();
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
  private void renderBoundingBoxes() {
    //targ
    BlockPos centerTarget = this.getPos().add(offsetTargetX, offsetTargetY, offsetTargetZ);
    List<BlockPos> shapeTarget = UtilShape.cubeFrame(centerTarget, this.sizeRadius, this.height);
    if (this.getWorld().rand.nextDouble() < 0.1) {
      for (BlockPos p : shapeTarget) {
        UtilParticle.spawnParticleNarrow(this.getWorld(), PARTICLE_TARGET, p);
      }
    }
    //src
    BlockPos centerSrc = this.getPos().add(offsetSourceX, offsetSourceY, offsetSourceZ);
    List<BlockPos> shapeSrc = UtilShape.cubeFrame(centerSrc, this.sizeRadius, this.height);
    if (this.getWorld().rand.nextDouble() < 0.1) {
      for (BlockPos p : shapeSrc) {
        UtilParticle.spawnParticleNarrow(this.getWorld(), PARTICLE_SRC, p);
      }
    }
  }
  @Override
  public int getSizeInventory() {
    return inv.length;
  }
  @Override
  public ItemStack getStackInSlot(int index) {
    return inv[index];
  }
  @Override
  public ItemStack decrStackSize(int index, int count) {
    ItemStack stack = getStackInSlot(index);
    if (stack != null) {
      if (stack.stackSize <= count) {
        setInventorySlotContents(index, null);
      }
      else {
        stack = stack.splitStack(count);
        if (stack.stackSize == 0) {
          setInventorySlotContents(index, null);
        }
      }
    }
    return stack;
  }
  @Override
  public ItemStack removeStackFromSlot(int index) {
    ItemStack stack = getStackInSlot(index);
    if (stack != null) {
      setInventorySlotContents(index, null);
    }
    return stack;
  }
  @Override
  public void setInventorySlotContents(int index, ItemStack stack) {
    inv[index] = stack;
    if (stack != null && stack.stackSize > getInventoryStackLimit()) {
      stack.stackSize = getInventoryStackLimit();
    }
  }
  @Override
  public int[] getSlotsForFace(EnumFacing side) {
    return new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17 };
  }
  @Override
  public void readFromNBT(NBTTagCompound tagCompound) {
    super.readFromNBT(tagCompound);
    this.offsetTargetX = tagCompound.getInteger("ox");
    this.offsetTargetY = tagCompound.getInteger("oy");
    this.offsetTargetZ = tagCompound.getInteger("oz");
    this.offsetSourceX = tagCompound.getInteger("sx");
    this.offsetSourceY = tagCompound.getInteger("sy");
    this.offsetSourceZ = tagCompound.getInteger("sz");
    this.sizeRadius = tagCompound.getInteger("r");
    this.height = tagCompound.getInteger("height");
    this.timer = tagCompound.getInteger("timer");
    this.needsRedstone = tagCompound.getInteger(NBT_REDST);
    NBTTagList tagList = tagCompound.getTagList(NBT_INV, 10);
    for (int i = 0; i < tagList.tagCount(); i++) {
      NBTTagCompound tag = (NBTTagCompound) tagList.getCompoundTagAt(i);
      byte slot = tag.getByte(NBT_SLOT);
      if (slot >= 0 && slot < inv.length) {
        inv[slot] = ItemStack.loadItemStackFromNBT(tag);
      }
    }
  }
  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound tagCompound) {
    tagCompound.setInteger("ox", offsetTargetX);
    tagCompound.setInteger("oy", offsetTargetY);
    tagCompound.setInteger("oz", offsetTargetZ);
    tagCompound.setInteger("sx", offsetSourceX);
    tagCompound.setInteger("sy", offsetSourceY);
    tagCompound.setInteger("sz", offsetSourceZ);
    tagCompound.setInteger("r", sizeRadius);
    tagCompound.setInteger("height", height);
    tagCompound.setInteger("timer", timer);
    tagCompound.setInteger(NBT_REDST, this.needsRedstone);
    NBTTagList itemList = new NBTTagList();
    for (int i = 0; i < inv.length; i++) {
      ItemStack stack = inv[i];
      if (stack != null) {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setByte(NBT_SLOT, (byte) i);
        stack.writeToNBT(tag);
        itemList.appendTag(tag);
      }
    }
    tagCompound.setTag(NBT_INV, itemList);
    return super.writeToNBT(tagCompound);
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
    default:
      break;
    }
    return 0;
  }
  public void setField(Fields f, int value) {
    this.renderBoundingBoxes();
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
  @Override
  public void toggleNeedsRedstone() {
    int val = this.needsRedstone + 1;
    if (val > 1) {
      val = 0;//hacky lazy way
    }
    this.setField(Fields.REDSTONE.ordinal(), val);
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
    this.renderBoundingBoxes();
  }
}
