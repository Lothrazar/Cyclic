package com.lothrazar.cyclicmagic.block.tileentity;
import java.util.List;
import com.lothrazar.cyclicmagic.util.UtilHarvestCrops;
import com.lothrazar.cyclicmagic.util.UtilParticle;
import com.lothrazar.cyclicmagic.util.UtilShape;
import com.lothrazar.cyclicmagic.util.UtilWorld;
import com.lothrazar.cyclicmagic.util.UtilHarvestCrops.HarestCropsConfig;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;

public class TileMachineHarvester extends TileEntityBaseMachineInvo implements ITileRedstoneToggle, ITileSizeToggle {
  private int timer;
  public static int TIMER_FULL = 80;
  private HarestCropsConfig conf;
  private int needsRedstone = 1;
  private static final String NBT_TIMER = "Timer";
  private static final String NBT_REDST = "redstone";
  private static final int MAX_SIZE = 7;//radius 7 translates to 15x15 area (center block + 7 each side)
  private int size = MAX_SIZE;//default to the old fixed size, backwards compat
  public static enum Fields {
    TIMER, REDSTONE, SIZE
  }
  public TileMachineHarvester() {
    this.timer = TIMER_FULL;
    conf = new HarestCropsConfig();
    conf.doesCrops = true;
    conf.doesMushroom = true;
    conf.doesPumpkinBlocks = true;
    conf.doesMelonBlocks = true;
  }
  public void setHarvestConf(HarestCropsConfig c) {
    conf = c;
  }
  public HarestCropsConfig getHarvestConf() {
    return conf;
  }
  @Override
  public void readFromNBT(NBTTagCompound tagCompound) {
    super.readFromNBT(tagCompound);
    this.needsRedstone = tagCompound.getInteger(NBT_REDST);
    timer = tagCompound.getInteger(NBT_TIMER);
  }
  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound tagCompound) {
    tagCompound.setInteger(NBT_TIMER, timer);
    tagCompound.setInteger(NBT_REDST, this.needsRedstone);
    return super.writeToNBT(tagCompound);
  }
  public boolean isBurning() {
    return this.timer > 0 && this.timer < TIMER_FULL;
  }
  @Override
  public void update() {
    if (this.onlyRunIfPowered() && this.isPowered() == false) {
      // it works ONLY if its powered
      this.markDirty();
      return;
    }
    this.spawnParticlesAbove();
    boolean trigger = false;
    // center of the block
    timer -= this.getSpeed();
    if (timer <= 0) {
      timer = TIMER_FULL;
      trigger = true;
    }
    if (trigger) {
      BlockPos harvest = getHarvestPos();
      if (UtilHarvestCrops.harvestSingle(getWorld(), harvest, conf)) {
        UtilParticle.spawnParticle(getWorld(), EnumParticleTypes.DRAGON_BREATH, harvest);
        timer = TIMER_FULL;//harvest worked!
      }
      else {
        timer = 1;//harvest didnt work, try again really quick
      }
    }
    else {
      this.spawnParticlesAbove();
    }
    this.markDirty();
  }
  public BlockPos getTargetCenter() {
    //move center over that much, not including exact horizontal
    return this.getPos().offset(this.getCurrentFacing(), this.size + 1);
  }
  private BlockPos getHarvestPos() {
    return UtilWorld.getRandomPos(getWorld().rand, getTargetCenter(), this.size);
  }
  public void displayPreview() {
    List<BlockPos> allPos = UtilShape.squareHorizontalHollow(getTargetCenter(), this.size);
    for (BlockPos pos : allPos) {
      UtilParticle.spawnParticle(getWorld(), EnumParticleTypes.DRAGON_BREATH, pos);
    }
  }
  private int getSpeed() {
    return 1;
  }
  @Override
  public int getField(int id) {
    if (id >= 0 && id < this.getFieldCount())
      switch (Fields.values()[id]) {
      case TIMER:
        return timer;
      case REDSTONE:
        return this.needsRedstone;
      case SIZE:
        return this.size;
      default:
        break;
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
      case REDSTONE:
        this.needsRedstone = value;
        break;
      case SIZE:
        this.size = value;
        break;
      default:
        break;
      }
  }
  @Override
  public int getFieldCount() {
    return Fields.values().length;
  }
  @Override
  public int getSizeInventory() {
    return 0;
  }
  @Override
  public ItemStack getStackInSlot(int index) {
    return null;
  }
  @Override
  public ItemStack decrStackSize(int index, int count) {
    return null;
  }
  @Override
  public ItemStack removeStackFromSlot(int index) {
    return null;
  }
  @Override
  public void setInventorySlotContents(int index, ItemStack stack) {
  }
  @Override
  public int[] getSlotsForFace(EnumFacing side) {
    return null;
  }
  public void toggleSizeShape() {
    this.size++;
    if (this.size > MAX_SIZE) {
      this.size = 0;
    }
  }
  @Override
  public void toggleNeedsRedstone() {
    int val = this.needsRedstone + 1;
    if (val > 1) {
      val = 0;//hacky lazy way
    }
    this.setField(Fields.REDSTONE.ordinal(), val);
  }
  private boolean onlyRunIfPowered() {
    return this.needsRedstone == 1;
  }
}
