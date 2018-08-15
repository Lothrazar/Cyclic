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
package com.lothrazar.cyclicmagic.block.placer;

import java.lang.ref.WeakReference;
import java.util.UUID;
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.core.block.TileEntityBaseMachineInvo;
import com.lothrazar.cyclicmagic.core.util.UtilEntity;
import com.lothrazar.cyclicmagic.core.util.UtilFakePlayer;
import com.lothrazar.cyclicmagic.core.util.UtilItemStack;
import com.lothrazar.cyclicmagic.core.util.UtilPlaceBlocks;
import com.lothrazar.cyclicmagic.gui.ITileRedstoneToggle;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ITickable;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.FakePlayer;

public class TileEntityPlacer extends TileEntityBaseMachineInvo implements ITileRedstoneToggle, ITickable {

  private static final int buildSpeed = 1;
  public static final int TIMER_FULL = 1;//one day i will add fuel AND/OR speed upgrades. till then make very slow
  private static final String NBT_TIMER = "Timer";
  private static final String NBT_REDST = "redstone";
  private WeakReference<FakePlayer> fakePlayer;

  public static enum Fields {
    TIMER, REDSTONE;
  }

  private int timer;
  private int needsRedstone = 1;
  private UUID uuid;

  public TileEntityPlacer() {
    super(9);
    this.setSlotsForBoth();
  }

  @Override
  public int[] getFieldOrdinals() {
    return super.getFieldArray(Fields.values().length);
  }

  @Override
  public boolean isItemValidForSlot(int index, ItemStack stack) {
    return Block.getBlockFromItem(stack.getItem()) != null;
  }

  @Override
  public int getField(int id) {
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

  public int getTimer() {
    return this.getField(Fields.TIMER.ordinal());
  }

  @Override
  public void readFromNBT(NBTTagCompound tagCompound) {
    super.readFromNBT(tagCompound);
    this.needsRedstone = tagCompound.getInteger(NBT_REDST);
    timer = tagCompound.getInteger(NBT_TIMER);
  }

  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound tagCompound) {
    tagCompound.setInteger(NBT_REDST, this.needsRedstone);
    tagCompound.setInteger(NBT_TIMER, timer);
    return super.writeToNBT(tagCompound);
  }

  public boolean isFuelBurning() {
    return this.timer > 0 && this.timer < TIMER_FULL;
  }

  private void verifyFakePlayer(WorldServer w) {
    if (fakePlayer == null) {
      fakePlayer = UtilFakePlayer.initFakePlayer(w, this.uuid, this.getBlockType().getUnlocalizedName());
      if (fakePlayer == null) {
        ModCyclic.logger.error("Fake player failed to init ");
      }
    }
  }

  private void verifyUuid(World world) {
    if (uuid == null) {
      uuid = UUID.randomUUID();
    }
  }

  @Override
  public void update() {
    shiftAllUp();
    boolean trigger = false;
    if (!isRunning()) {
      // it works ONLY if its powered
      markDirty();
      return;
    }
    if (world instanceof WorldServer) {
      verifyUuid(world);
      verifyFakePlayer((WorldServer) world);
    }
    ItemStack stack = getStackInSlot(0);
    if (stack == null) {
      timer = TIMER_FULL;// reset just like you would in a  furnace
    }
    else {
      timer -= buildSpeed;
      if (timer <= 0) {
        timer = TIMER_FULL;
        trigger = true;
      }
    }
    if (trigger) {
      if (stack.getItem() instanceof ItemBlock && fakePlayer != null) {
        UtilEntity.setEntityFacing(fakePlayer.get(), this.getCurrentFacing());
        try {
          UtilPlaceBlocks.placeItemblock(world, pos.offset(this.getCurrentFacing()), stack, fakePlayer.get());
        }
        catch (Throwable e) {
          ModCyclic.logger.error("Block could be not be placed : " + stack.getItem().getRegistryName(), e);
        }
      }
      else {
        Block stuff = Block.getBlockFromItem(stack.getItem());
        if (stuff != null && stuff != Blocks.AIR) {
          if (UtilPlaceBlocks.placeStateSafe(getWorld(), null, pos.offset(this.getCurrentFacing()),
              UtilItemStack.getStateFromMeta(stuff, stack.getMetadata()))) {
            this.decrStackSize(0, 1);
          }
        }
      }
    }
    this.markDirty();
  }

  @Override
  public boolean receiveClientEvent(int id, int value) {
    if (id >= 0 && id < this.getFieldCount()) {
      this.setField(id, value);
      return true;
    }
    else {
      return super.receiveClientEvent(id, value);
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

  @Override
  public boolean onlyRunIfPowered() {
    return this.needsRedstone == 1;
  }
}
