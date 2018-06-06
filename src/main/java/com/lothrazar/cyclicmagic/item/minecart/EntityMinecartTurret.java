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
package com.lothrazar.cyclicmagic.item.minecart;

import net.minecraft.block.BlockObserver;
import net.minecraft.block.BlockRailBase;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionType;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class EntityMinecartTurret extends EntityGoldMinecart {

  private static final float YAW = 0.1F;
  private static final float VELOCITY = 1.1F;
  private static final float INACCRACY = 2.0F;
  private static final int TIME_BTW_DROPS = 40;
  public static Item dropItem = Items.MINECART;//override with gold minecart on registry, this is here just for nonnull
  private int timeSinceDropped = 0;

  public EntityMinecartTurret(World worldIn) {
    super(worldIn);
    this.setDisplayTile(getDefaultDisplayTile());
  }

  public EntityMinecartTurret(World worldIn, double x, double y, double z) {
    super(worldIn, x, y, z);
    this.setDisplayTile(getDefaultDisplayTile());
  }

  public int getSizeInventory() {
    return 0;
  }

  public IBlockState getDefaultDisplayTile() {
    return Blocks.OBSERVER.getDefaultState();//.withProperty(BlockChest.FACING, EnumFacing.NORTH);
  }

  @Override
  protected void writeEntityToNBT(NBTTagCompound compound) {
    compound.setInteger("tdr", timeSinceDropped);
    super.writeEntityToNBT(compound);
  }

  @Override
  protected void readEntityFromNBT(NBTTagCompound compound) {
    timeSinceDropped = compound.getInteger("tdr");
    super.readEntityFromNBT(compound);
  }

  @Override
  public void onActivatorRailPass(int x, int y, int z, boolean receivingPower) {
    if (receivingPower) {
      this.dispense(new BlockPos(x, y, z));
    }
  }

  /**
   * pulled from BlockDispenser
   * 
   * @param worldIn
   * @param pos
   */
  protected void dispense(BlockPos pos) {
    if (this.timeSinceDropped > 0) {
      this.timeSinceDropped--;
      return;
    }
    this.timeSinceDropped = TIME_BTW_DROPS;
    EnumFacing enumfacing = this.getDisplayTile().getValue(BlockObserver.FACING);
    shootThisDirection(enumfacing);
    shootThisDirection(enumfacing.getOpposite());
  }

  public void shootThisDirection(EnumFacing enumfacing) {
    BlockPos position = this.getPosition().up().offset(enumfacing, 2);
    EntityTippedArrow entitytippedarrow = new EntityTippedArrow(world, position.getX(), position.getY(), position.getZ());
    entitytippedarrow.setPotionEffect(PotionUtils.addPotionToItemStack(new ItemStack(Items.TIPPED_ARROW), PotionType.getPotionTypeForName("slowness")));
    entitytippedarrow.pickupStatus = EntityArrow.PickupStatus.CREATIVE_ONLY;
    entitytippedarrow.setThrowableHeading((double) enumfacing.getFrontOffsetX(), YAW, (double) enumfacing.getFrontOffsetZ(), VELOCITY, INACCRACY);
    world.spawnEntity(entitytippedarrow);
  }

  @Override
  protected void moveAlongTrack(BlockPos pos, IBlockState state) {
    BlockRailBase blockrailbase = (BlockRailBase) state.getBlock();
    if (blockrailbase != Blocks.ACTIVATOR_RAIL) {
      this.timeSinceDropped = 0;
    }
    //force DISPENSER to face sime direction as my movemene
    //      double slopeAdjustment = getSlopeAdjustment();
    BlockRailBase.EnumRailDirection raildirection = blockrailbase.getRailDirection(world, pos, state, this);
    EnumFacing fac = null;
    switch (raildirection) {
      case ASCENDING_EAST:
        fac = EnumFacing.EAST;
      break;
      case ASCENDING_WEST:
        fac = EnumFacing.WEST;
      break;
      case ASCENDING_NORTH:
        fac = EnumFacing.NORTH;
      break;
      case ASCENDING_SOUTH:
        fac = EnumFacing.SOUTH;
      case EAST_WEST:
        fac = (this.motionX > 0) ? EnumFacing.SOUTH : EnumFacing.NORTH;
      break;
      case NORTH_SOUTH:
        fac = (this.motionZ < 0) ? EnumFacing.WEST : EnumFacing.EAST;
      break;
      default:
      break;
    }
    super.moveAlongTrack(pos, state);
    if (fac != null) {
      this.setDisplayTile(getDefaultDisplayTile().withProperty(BlockObserver.FACING, fac));
    }
  }

  @Override
  public void killMinecart(DamageSource source) {
    this.setDead();
    if (this.world.getGameRules().getBoolean("doEntityDrops")) {
      ItemStack itemstack = getCartItem();
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
}
