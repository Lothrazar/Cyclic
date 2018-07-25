/*******************************************************************************
 * The MIT License (MIT)
 * 
 * Copyright (C) 2014-2018 Sam Bassett (aka Lothrazar)
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 ******************************************************************************/
package com.lothrazar.cyclicmagic.block.collector;

import java.util.ArrayList;
import java.util.List;
import com.lothrazar.cyclicmagic.core.ITileStackWrapper;
import com.lothrazar.cyclicmagic.core.block.TileEntityBaseMachineInvo;
import com.lothrazar.cyclicmagic.core.gui.StackWrapper;
import com.lothrazar.cyclicmagic.core.util.UtilInventoryTransfer;
import com.lothrazar.cyclicmagic.core.util.UtilShape;
import com.lothrazar.cyclicmagic.gui.ITilePreviewToggle;
import com.lothrazar.cyclicmagic.gui.ITileRedstoneToggle;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

public class TileEntityVacuum extends TileEntityBaseMachineInvo implements ITileStackWrapper, ITickable, ITileRedstoneToggle, ITilePreviewToggle {

  private NonNullList<StackWrapper> stacksWrapped = NonNullList.withSize(10, new StackWrapper());
  private static final int VRADIUS = 2;
  private static final int MAX_SIZE = 9;//7 means 15x15
  public static final int TIMER_FULL = 20;
  public final static int ROWS = 4;
  public final static int COLS = 9;

  public static enum Fields {
    TIMER, RENDERPARTICLES, REDSTONE, SIZE;
  }

  private int timer = 0;
  private int needsRedstone = 1;
  private int renderParticles = 0;
  private int size = 4;//center plus 4 in each direction = 9x9

  public TileEntityVacuum() {
    super(ROWS * COLS);
    this.setSetRenderGlobally(true);
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

  private List<ItemStack> getFilterNonempty() {
    List<ItemStack> filt = new ArrayList<ItemStack>();
    for (StackWrapper wrap : this.stacksWrapped) {
      if (wrap.isEmpty() == false) {
        filt.add(wrap.getStack().copy());
      }
    }
    return filt;
  }

  private boolean canPickup(EntityItem itemOnGround) {
    if (itemOnGround.isDead) {
      return false;//nope
    }
    List<ItemStack> filt = this.getFilterNonempty();
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
    writeStackWrappers(stacksWrapped, tags);
    tags.setInteger(NBT_TIMER, timer);
    tags.setInteger(NBT_REDST, this.needsRedstone);
    tags.setInteger(NBT_RENDER, renderParticles);
    tags.setInteger(NBT_SIZE, size);
    return super.writeToNBT(tags);
  }

  @Override
  public void readFromNBT(NBTTagCompound tags) {
    super.readFromNBT(tags);
    readStackWrappers(stacksWrapped, tags);
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
  public StackWrapper getStackWrapper(int i) {
    return stacksWrapped.get(i);
  }

  @Override
  public void setStackWrapper(int i, StackWrapper stack) {
    stacksWrapped.set(i, stack);
  }

  @Override
  public int getWrapperCount() {
    return stacksWrapped.size();
  }
}
