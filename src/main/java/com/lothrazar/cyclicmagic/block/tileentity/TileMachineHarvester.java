package com.lothrazar.cyclicmagic.block.tileentity;
import com.lothrazar.cyclicmagic.util.UtilHarvestCrops;
import com.lothrazar.cyclicmagic.util.UtilParticle;
import com.lothrazar.cyclicmagic.util.UtilWorld;
import com.lothrazar.cyclicmagic.util.UtilHarvestCrops.HarestCropsConfig;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;

public class TileMachineHarvester extends TileEntityBaseMachineInvo implements ITileRedstoneToggle {
  private int timer;
  public static int TIMER_FULL = 80;
  private HarestCropsConfig conf;
  private int needsRedstone = 1;
  private static final String NBT_TIMER = "Timer";
  public static int HARVEST_RADIUS = 16;
  private static final String NBT_REDST = "redstone";
  public static enum Fields {
    TIMER, REDSTONE
  }
  public TileMachineHarvester(String n) {
    super(n);
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
      //TODO:spit drops out the facing side, just like uncrafter
      // -> to this end, add new conf flag
      if (UtilHarvestCrops.harvestSingle(this.worldObj, harvest, conf)) {
        //        ModMain.logger.info("harvested :" + UtilChat.blockPosToString(harvest));
        UtilParticle.spawnParticle(worldObj, EnumParticleTypes.DRAGON_BREATH, harvest);
        timer = TIMER_FULL;//harvest worked!
      }
      else {
        //        UtilParticle.spawnParticle(worldObj, EnumParticleTypes.SMOKE_NORMAL, harvest);
        timer = 1;//harvest didnt work, try again really quick
      }
    }
    else {
      this.spawnParticlesAbove();
    }
    this.markDirty();
  }
  private BlockPos getHarvestPos() {
    //move center over that much, not including exact horizontal
    BlockPos center = this.getPos().offset(this.getCurrentFacing(), HARVEST_RADIUS + 1);
    return UtilWorld.getRandomPos(this.worldObj.rand, center, HARVEST_RADIUS);
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
      }
  }
  @Override
  public int getFieldCount() {
    return Fields.values().length;
  }
  @Override
  public int getSizeInventory() {
    // TODO Auto-generated method stub
    return 0;
  }
  @Override
  public ItemStack getStackInSlot(int index) {
    // TODO Auto-generated method stub
    return null;
  }
  @Override
  public ItemStack decrStackSize(int index, int count) {
    // TODO Auto-generated method stub
    return null;
  }
  @Override
  public ItemStack removeStackFromSlot(int index) {
    // TODO Auto-generated method stub
    return null;
  }
  @Override
  public void setInventorySlotContents(int index, ItemStack stack) {
    // TODO Auto-generated method stub
  }
  @Override
  public int[] getSlotsForFace(EnumFacing side) {
    // TODO Auto-generated method stub
    return null;
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
