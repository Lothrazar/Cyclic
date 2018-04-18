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
package com.lothrazar.cyclicmagic.entity;

import net.minecraft.entity.MoverType;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class EntityGoldMinecart extends EntityMinecart {

  private static final double DRAG_RIDDEN = 0.95D;//vanilla reduces this 0.75
  public static Item dropItem = Items.MINECART;//override with gold minecart on registry, this is here just for nonnull

  public EntityGoldMinecart(World worldIn) {
    super(worldIn);
  }

  public EntityGoldMinecart(World worldIn, double x, double y, double z) {
    super(worldIn, x, y, z);
  }

  /**
   * Get's the maximum speed for a minecart vanilla is 0.4D default
   */
  @Override
  protected double getMaximumSpeed() {
    return super.getMaximumSpeed() + 0.1D;
  }

  @Override
  protected double getMaxSpeed() {
    return this.getMaximumSpeed();
    //the railway block has a 0.4 hardcoded, but we want our miencart to ingnore this rail property
    //      float railMaxSpeed = ((BlockRailBase)state.getBlock()).getRailMaxSpeed(world, this, pos);
    //      return Math.min(railMaxSpeed, getCurrentCartSpeedCapOnRail());
  }

  /**
   * 
   * Returns the carts max speed when traveling on rails. Carts going faster than 1.1 cause issues with chunk loading. Carts cant traverse slopes or corners at greater than 0.5 - 0.6. This value is
   * compared with the rails max speed and the carts current speed cap to determine the carts current max speed. A normal rail's max speed is 0.4.
   *
   * @return Carts max speed.
   */
  @Override
  public float getMaxCartSpeedOnRail() {
    return super.getMaxCartSpeedOnRail() + 0.1f;//super is 1.2
  }

  /**
   * Moved to allow overrides. This code handles minecart movement and speed capping when on a rail.
   */
  @Override
  public void moveMinecartOnRail(BlockPos pos) {
    double mX = this.motionX;
    double mZ = this.motionZ;
    if (this.isBeingRidden()) {
      mX *= DRAG_RIDDEN;
      mZ *= DRAG_RIDDEN;
    }
    double max = this.getMaxSpeed();
    mX = MathHelper.clamp(mX, -max, max);
    mZ = MathHelper.clamp(mZ, -max, max);
    this.move(MoverType.SELF, mX, 0.0D, mZ);
  }

  @Override
  public void killMinecart(DamageSource source) {
    this.setDead();
    if (this.world.getGameRules().getBoolean("doEntityDrops")) {
      ItemStack itemstack = new ItemStack(dropItem);
      if (this.hasCustomName()) {
        itemstack.setStackDisplayName(this.getCustomNameTag());
      }
      this.entityDropItem(itemstack, 0.0F);
    }
  }

  @Override
  public ItemStack getCartItem() {
    return new ItemStack(dropItem);
  }

  /**
   * Called every tick the minecart is on an activator rail.
   */
  @Override
  public void onActivatorRailPass(int x, int y, int z, boolean receivingPower) {
    if (receivingPower) {
      if (this.isBeingRidden()) {
        this.removePassengers();
      }
      if (this.getRollingAmplitude() == 0) {
        this.setRollingDirection(-this.getRollingDirection());
        this.setRollingAmplitude(10);
        this.setDamage(50.0F);
        this.setBeenAttacked();
      }
    }
  }

  @Override
  public boolean processInitialInteract(EntityPlayer player, EnumHand hand) {
    if (net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new net.minecraftforge.event.entity.minecart.MinecartInteractEvent(this, player, hand))) return true;
    if (player.isSneaking()) {
      return false;
    }
    else if (this.isBeingRidden()) {
      return true;
    }
    else {
      if (!this.world.isRemote) {
        player.startRiding(this);
      }
      return true;
    }
  }

  @Override
  public Type getType() {
    return EntityMinecart.Type.RIDEABLE;
  }
}
