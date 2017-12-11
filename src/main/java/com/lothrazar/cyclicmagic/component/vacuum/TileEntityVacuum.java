package com.lothrazar.cyclicmagic.component.vacuum;
import java.util.ArrayList;
import java.util.List;
import com.lothrazar.cyclicmagic.block.base.TileEntityBaseMachineInvo;
import com.lothrazar.cyclicmagic.gui.ITilePreviewToggle;
import com.lothrazar.cyclicmagic.gui.ITileRedstoneToggle;
import com.lothrazar.cyclicmagic.gui.ITileSizeToggle;
import com.lothrazar.cyclicmagic.util.UtilInventoryTransfer;
import com.lothrazar.cyclicmagic.util.UtilShape;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

public class TileEntityVacuum extends TileEntityBaseMachineInvo implements ITickable, ITileRedstoneToggle, ITilePreviewToggle, ITileSizeToggle {
  private static final int VRADIUS = 2;
  private static final int MAX_SIZE = 9;//7 means 15x15
  public static final int TIMER_FULL = 20;
  public final static int ROWS = 4;
  public final static int COLS = 9;
  public final static int FILTERSLOTS = 5 * 2;
  public static enum Fields {
    TIMER, RENDERPARTICLES, REDSTONE, SIZE;
  }
  private int timer = 0;
  private int needsRedstone = 1;
  private int renderParticles = 0;
  private int size = 4;//center plus 4 in each direction = 9x9
  public TileEntityVacuum() {
    super(ROWS * COLS + FILTERSLOTS);
    this.setSlotsForExtract(0, ROWS * COLS);
  }
  @Override
  public int[] getFieldOrdinals() {
    return super.getFieldArray(Fields.values().length);
  }
  @Override
  public void update() {
    if (!this.isRunning()) {
      return;
    }
    this.spawnParticlesAbove();
    if (!this.updateTimerIsZero()) {
      return;
    }
    updateCollection();
  }
  private void updateCollection() {
    //expand only goes ONE direction. so expand(3...) goes 3 in + x, but not both ways. for full boc centered at this..!! we go + and -
    BlockPos center = this.getTargetCenter();
    AxisAlignedBB region = new AxisAlignedBB(center).expand(size, VRADIUS, size).expand(-1 * size, -1 * VRADIUS, -1 * size);//expandXyz
    List<EntityItem> items = getWorld().getEntitiesWithinAABB(EntityItem.class, region);
    if (items == null) {
      return;
    }
    for (EntityItem itemOnGround : items) {
      processItemOnGround(itemOnGround);
    }
  }
  @SuppressWarnings("serial")
  private void processItemOnGround(EntityItem itemOnGround) {
    if (this.canPickup(itemOnGround) == false) {
      return;
    } //its dead, or its filtered out
    ItemStack contained = itemOnGround.getItem();
    //making it a list not  a single is a superhack
    ArrayList<ItemStack> toDrop = UtilInventoryTransfer.dumpToIInventory(new ArrayList<ItemStack>() {
      {
        add(contained);
      }
    }, this, 0, ROWS * COLS);//end slot is here, after that is item filter so dont put them there
    if (toDrop.size() > 0) {//JUST in case it did not fit or only half of it fit
      itemOnGround.setItem(toDrop.get(0));
    }
    else {
      itemOnGround.setDropItemsWhenDead(false);
      itemOnGround.setDead();
    }
  }
  private List<ItemStack> getFilterCopy() {
    List<ItemStack> filt = new ArrayList<ItemStack>();
    int start = TileEntityVacuum.ROWS * TileEntityVacuum.COLS;
    ItemStack s;
    for (int k = 0; k < TileEntityVacuum.FILTERSLOTS; k++) {
      s = this.getStackInSlot(k + start);
      if (s.isEmpty() == false) {
        filt.add(s.copy());
      }
    }
    return filt;
  }
  private boolean canPickup(EntityItem itemOnGround) {
    if (itemOnGround.isDead) {
      return false;//nope
    }
    List<ItemStack> filt = this.getFilterCopy();
    if (filt.size() == 0) {
      return true;//filter is empty, no problem eh
    }
    //filter is not empty. so if its not found we return false
    for (ItemStack f : filt) {
      if (f.isItemEqualIgnoreDurability(itemOnGround.getItem())) {
        return true;
      }
    }
    return false;
  }
  private BlockPos getTargetCenter() {
    //move center over that much, not including exact horizontal
    return this.getPos().offset(this.getCurrentFacing(), size + 1);
  }
  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound tags) {
    tags.setInteger(NBT_TIMER, timer);
    tags.setInteger(NBT_REDST, this.needsRedstone);
    tags.setInteger(NBT_RENDER, renderParticles);
    tags.setInteger(NBT_SIZE, size);
    return super.writeToNBT(tags);
  }
  @Override
  public void readFromNBT(NBTTagCompound tags) {
    super.readFromNBT(tags);
    timer = tags.getInteger(NBT_TIMER);
    this.needsRedstone = tags.getInteger(NBT_REDST);
    this.renderParticles = tags.getInteger(NBT_RENDER);
    this.size = tags.getInteger(NBT_SIZE);
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
      case SIZE:
        return this.size;
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
      case SIZE:
        this.size = value;
      break;
    }
  }
  public void togglePreview() {
    this.renderParticles = (renderParticles + 1) % 2;
  }
  @Override
  public List<BlockPos> getShape() {
    //vertical radius goes both up and down. so to draw shape, start below and push up
    BlockPos bottmCenter = getTargetCenter().offset(EnumFacing.DOWN, VRADIUS);
    return UtilShape.repeatShapeByHeight(
        UtilShape.squareHorizontalHollow(bottmCenter, size),
        VRADIUS * 2);
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
  @Override
  public void toggleSizeShape() {
    this.size++;
    if (this.size > MAX_SIZE) {
      this.size = 0;//size zero means a 1x1 area
    }
  }
}
