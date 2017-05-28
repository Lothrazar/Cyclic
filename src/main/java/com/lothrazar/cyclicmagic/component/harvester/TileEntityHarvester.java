package com.lothrazar.cyclicmagic.component.harvester;
import java.util.List;
import com.lothrazar.cyclicmagic.ITilePreviewToggle;
import com.lothrazar.cyclicmagic.ITileRedstoneToggle;
import com.lothrazar.cyclicmagic.ITileSizeToggle;
import com.lothrazar.cyclicmagic.block.tileentity.TileEntityBaseMachineInvo;
import com.lothrazar.cyclicmagic.component.autouser.TileEntityUser.Fields;
import com.lothrazar.cyclicmagic.util.UtilHarvestCrops;
import com.lothrazar.cyclicmagic.util.UtilHarvestCrops.HarvestSetting;
import com.lothrazar.cyclicmagic.util.UtilParticle;
import com.lothrazar.cyclicmagic.util.UtilShape;
import com.lothrazar.cyclicmagic.util.UtilWorld;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;

public class TileEntityHarvester extends TileEntityBaseMachineInvo implements ITileRedstoneToggle, ITileSizeToggle, ITilePreviewToggle, ITickable {
  private static final int MAX_SIZE = 7;//radius 7 translates to 15x15 area (center block + 7 each side)
  private int size = MAX_SIZE;//default to the old fixed size, backwards compat
  public final static int TIMER_FULL = 100;
  private HarvestSetting conf;
  private int needsRedstone = 1;
  private int renderParticles = 0;
  public static enum Fields {
    TIMER, REDSTONE, SIZE, RENDERPARTICLES, FUEL, FUELMAX;
  }
  public TileEntityHarvester() {
    super(1);
    this.setFuelSlot(0);
    this.timer = TIMER_FULL;
    conf = new HarvestSetting();
    conf.doesCrops = true;
    conf.doesMushroom = true;
    conf.doesPumpkinBlocks = true;
    conf.doesMelonBlocks = true;
  }
  @Override
  public int[] getFieldOrdinals() {
    return super.getFieldArray(Fields.values().length);
  }
//  public void setHarvestConf(HarvestSetting c) {
//    conf = c;
//  }
  public HarvestSetting getHarvestConf() {
    return conf;
  }
  @Override
  public void readFromNBT(NBTTagCompound tagCompound) {
    super.readFromNBT(tagCompound);
    this.needsRedstone = tagCompound.getInteger(NBT_REDST);
    size = tagCompound.getInteger(NBT_SIZE);
    this.renderParticles = tagCompound.getInteger(NBT_RENDER);
  }
  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound tagCompound) {
    tagCompound.setInteger(NBT_REDST, this.needsRedstone);
    tagCompound.setInteger(NBT_SIZE, size);
    tagCompound.setInteger(NBT_RENDER, renderParticles);
    return super.writeToNBT(tagCompound);
  }
  public boolean isFuelBurning() {
    return this.timer > 0 && this.timer < TIMER_FULL;
  }
  @Override
  public void update() {
    if (!isRunning()) { return; }
    this.spawnParticlesAbove();
    this.updateFuelIsBurning();
    if (this.updateTimerIsZero()) {
      timer = TIMER_FULL;//harvest worked!
      this.spawnParticlesAbove();
      BlockPos harvest = getTargetPos();
      if (UtilHarvestCrops.harvestSingle(getWorld(), harvest, conf)) {
        UtilParticle.spawnParticle(getWorld(), EnumParticleTypes.DRAGON_BREATH, harvest);
      }
      //      else {
      //        timer = 1;//harvest didnt work, try again really quick
      //      }
      this.markDirty();
    }
  }
  public BlockPos getTargetCenter() {
    //move center over that much, not including exact horizontal
    return this.getPos().offset(this.getCurrentFacing(), this.size + 1);
  }
  private BlockPos getTargetPos() {
    return UtilWorld.getRandomPos(getWorld().rand, getTargetCenter(), this.size);
  }
  @Override
  public int getField(int id) {
    switch (Fields.values()[id]) {
      case TIMER:
        return timer;
      case REDSTONE:
        return this.needsRedstone;
      case SIZE:
        return this.size;
      case RENDERPARTICLES:
        return this.renderParticles;
      case FUEL:
        return this.getFuelCurrent();
      case FUELMAX:
        return this.getFuelMax();
      default:
      break;
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
        case SIZE:
          this.size = value;
        break;
        case RENDERPARTICLES:
          this.renderParticles = value % 2;
        break;
        case FUEL:
          this.setFuelCurrent(value);
        break;
        case FUELMAX:
          this.setFuelMax(value);
        break;
        default:
        break;
      }
    }
  }
  @Override
  public int getFieldCount() {
    return Fields.values().length;
  }
  @Override
  public int[] getSlotsForFace(EnumFacing side) {
    return new int[] {};
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
  public boolean onlyRunIfPowered() {
    return this.needsRedstone == 1;
  }
  @Override
  public void togglePreview() {
    this.renderParticles = (renderParticles + 1) % 2;
  }
  @Override
  public List<BlockPos> getShape() {
    return UtilShape.squareHorizontalHollow(getTargetCenter(), this.size);
  }
  @Override
  public boolean isPreviewVisible() {
    return this.getField(Fields.RENDERPARTICLES.ordinal()) == 1;
  }
}
