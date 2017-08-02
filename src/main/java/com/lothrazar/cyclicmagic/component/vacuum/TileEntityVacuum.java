package com.lothrazar.cyclicmagic.component.vacuum;
import java.util.ArrayList;
import java.util.List;
import com.lothrazar.cyclicmagic.block.base.TileEntityBaseMachineInvo;
import com.lothrazar.cyclicmagic.gui.ITilePreviewToggle;
import com.lothrazar.cyclicmagic.gui.ITileRedstoneToggle;
import com.lothrazar.cyclicmagic.util.UtilInventoryTransfer;
import com.lothrazar.cyclicmagic.util.UtilShape;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

public class TileEntityVacuum extends TileEntityBaseMachineInvo implements ITickable, ITileRedstoneToggle, ITilePreviewToggle {
  private static final int VRADIUS = 2;
  public static final int TIMER_FULL = 18;
  public final static int RADIUS = 8;
  private static final int[] SLOTS_EXTRACT = new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26 };
  public static enum Fields {
    TIMER, RENDERPARTICLES, REDSTONE;
  }
  private int timer = 0;
  private int needsRedstone = 1;
  private int renderParticles = 0;
  public TileEntityVacuum() {
    super(3 * 9);
  }
  @Override
  public int[] getFieldOrdinals() {
    return super.getFieldArray(Fields.values().length);
  }
  @Override
  public void update() {
    if (!this.isRunning()) { return; }
    this.spawnParticlesAbove();
    if (!this.updateTimerIsZero()) {return;}
    updateCollection();
  }
  @SuppressWarnings("serial")
  private void updateCollection() {
    //expand only goes ONE direction. so expand(3...) goes 3 in + x, but not both ways. for full boc centered at this..!! we go + and -
    BlockPos center = this.getTargetCenter();
    AxisAlignedBB region = new AxisAlignedBB(center).expand(RADIUS, VRADIUS, RADIUS).expand(-1 * RADIUS, -1 * VRADIUS, -1 * RADIUS);//expandXyz
    List<EntityItem> orbs = getWorld().getEntitiesWithinAABB(EntityItem.class, region);
    if (orbs == null) { return; }
    for (EntityItem orb : orbs) {
      if (orb.isDead) {
        continue;
      }
      ItemStack contained = orb.getItem();
      ArrayList<ItemStack> toDrop = UtilInventoryTransfer.dumpToIInventory(new ArrayList<ItemStack>() {
        {
          add(contained);
        }
      }, this, 0);
      if (toDrop.size() > 0) {//JUST in case it did not fit or only half of it fit
        orb.setItem(toDrop.get(0));
      }
      else {
        orb.setDropItemsWhenDead(false);
        orb.setDead();
      }
    }
  }
  public BlockPos getTargetCenter() {
    //move center over that much, not including exact horizontal
    return this.getPos().offset(this.getCurrentFacing(), RADIUS);
  }
  @Override
  public int[] getSlotsForFace(EnumFacing side) {
    return SLOTS_EXTRACT;
  }
  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound tags) {
    tags.setInteger(NBT_TIMER, timer);
    tags.setInteger(NBT_REDST, this.needsRedstone);
    tags.setInteger(NBT_RENDER, renderParticles);
    return super.writeToNBT(tags);
  }
  @Override
  public void readFromNBT(NBTTagCompound tags) {
    super.readFromNBT(tags);
    timer = tags.getInteger(NBT_TIMER);
    this.needsRedstone = tags.getInteger(NBT_REDST);
    this.renderParticles = tags.getInteger(NBT_RENDER);
  }
  @Override
  public int getFieldCount() {
    return Fields.values().length;
  }
  @Override
  public int getField(int id) {
    switch (Fields.values()[id]) {
      case TIMER:
        return timer;
      case RENDERPARTICLES:
        return this.renderParticles;
      case REDSTONE:
        return this.needsRedstone;
    }
    return -1;
  }
  @Override
  public void setField(int id, int value) {
    switch (Fields.values()[id]) {
      case TIMER:
        this.timer = value;
      break;
      case REDSTONE:
        this.needsRedstone = value;
      break;
      case RENDERPARTICLES:
        this.renderParticles = value % 2;
      break;
    }
  }
  public void togglePreview() {
    this.renderParticles = (renderParticles + 1) % 2;
  }
  @Override
  public List<BlockPos> getShape() {
    return UtilShape.squareHorizontalHollow(getTargetCenter(), RADIUS);
  }
  @Override
  public boolean isPreviewVisible() {
    return this.getField(Fields.RENDERPARTICLES.ordinal()) == 1;
  }
  @Override
  public void toggleNeedsRedstone() {
    this.needsRedstone = (this.needsRedstone + 1) % 2;
  }
  @Override
  public boolean onlyRunIfPowered() {
    return this.needsRedstone == 1;
  }
}
