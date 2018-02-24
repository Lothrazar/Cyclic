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
package com.lothrazar.cyclicmagic.component.magnetanti;
import com.lothrazar.cyclicmagic.block.base.TileEntityBaseMachine;
import com.lothrazar.cyclicmagic.util.UtilEntity;
import com.lothrazar.cyclicmagic.util.UtilParticle;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ITickable;
import net.minecraft.util.text.ITextComponent;

public class TileEntityMagnetAnti extends TileEntityBaseMachine implements ITickable {
  private static final String NBT_TIMER = "Timer";
  public final static int TIMER_FULL = 10;
  public final static int ITEM_VRADIUS = 3;
  public final static int ITEM_HRADIUS = 32;
  private final static float SPEED = 1.4F;//was 0.9
  private int timer;
  public TileEntityMagnetAnti() {
    this.timer = TIMER_FULL;
  }
  @Override
  public ITextComponent getDisplayName() {
    return null;
  }
  @Override
  public void readFromNBT(NBTTagCompound tagCompound) {
    super.readFromNBT(tagCompound);
    timer = tagCompound.getInteger(NBT_TIMER);
  }
  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound tagCompound) {
    tagCompound.setInteger(NBT_TIMER, timer);
    return super.writeToNBT(tagCompound);
  }
  public boolean isBurning() {
    return this.timer > 0 && this.timer < TIMER_FULL;
  }
  @Override
  public void update() {
    boolean trigger = false;
    timer--;
    if (timer <= 0) {
      timer = TIMER_FULL;
      trigger = true;
    }
    // center of the block
    double x = this.getPos().getX() + 0.5;
    double y = this.getPos().getY();
    double z = this.getPos().getZ() + 0.5;
    if (trigger) {
      UtilEntity.moveEntityLivingNonplayers(this.getWorld(), x, y, z, ITEM_HRADIUS, ITEM_VRADIUS, false, SPEED);
      timer = TIMER_FULL;
      spawnParticles();
    }
  }
  protected void spawnParticles() {
    if (this.getWorld().isRemote) {
      double x = this.getPos().getX() + 0.5; //center of the block;
      double y = this.getPos().getY() + 0.5;
      double z = this.getPos().getZ() + 0.5;
      UtilParticle.spawnParticle(this.getWorld(), EnumParticleTypes.CRIT_MAGIC, x, y, z);
      UtilParticle.spawnParticle(this.getWorld(), EnumParticleTypes.CRIT_MAGIC, x, y + 1, z);
      UtilParticle.spawnParticle(this.getWorld(), EnumParticleTypes.CRIT_MAGIC, x, y + 2, z);
      UtilParticle.spawnParticle(this.getWorld(), EnumParticleTypes.CRIT_MAGIC, x, y + 3, z);
    }
  }
}
