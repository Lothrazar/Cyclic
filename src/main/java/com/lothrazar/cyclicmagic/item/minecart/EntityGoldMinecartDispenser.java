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

import java.util.Random;
import com.lothrazar.cyclicmagic.ModCyclic;
import net.minecraft.block.BlockDispenser;
import net.minecraft.block.BlockRailBase;
import net.minecraft.block.BlockSourceImpl;
import net.minecraft.block.state.IBlockState;
import net.minecraft.dispenser.IBehaviorDispenseItem;
import net.minecraft.entity.item.EntityMinecartChest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerDispenser;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class EntityGoldMinecartDispenser extends EntityMinecartChest {

  private static final int TIME_BTW_DROPS = 40;
  private int timeSinceDropped = 0;
  private FakeWorld fakeWorld;

  public EntityGoldMinecartDispenser(World worldIn) {
    super(worldIn);
    fakeWorld = new FakeWorld(worldIn, this);
    this.setDisplayTile(getDefaultDisplayTile());
  }

  public EntityGoldMinecartDispenser(World worldIn, double x, double y, double z) {
    super(worldIn, x, y, z);
    fakeWorld = new FakeWorld(worldIn, this);
    this.setDisplayTile(getDefaultDisplayTile());
  }

  public int getSizeInventory() {
    return 9;
  }

  public IBlockState getDefaultDisplayTile() {
    return Blocks.DISPENSER.getDefaultState();//.withProperty(BlockChest.FACING, EnumFacing.NORTH);
  }

  @Override
  public IBlockState getDisplayTile() {
    IBlockState s = super.getDisplayTile();
    //    ModCyclic.logger.info("getDisplayTile " + s);
    //    ModCyclic.logger.info("getDisplayTile facing " + s.getValue(BlockDispenser.FACING));
    return s;
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

  public String getGuiID() {
    return "minecraft:dispenser";
  }

  @Override
  public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn) {
    this.addLoot(playerIn);
    return new ContainerDispenser(playerInventory, this);
  }

  @Override
  public void onActivatorRailPass(int x, int y, int z, boolean receivingPower) {
    //    EnumFacing fac = this.getAdjustedHorizontalFacing();//.getOpposite();
    //    this.setDisplayTile(getDefaultDisplayTile().withProperty(BlockDispenser.FACING, fac));
    //ModCyclic.logger.info("this.getAdjustedHorizontalFacing()"+fac);
    if (receivingPower) {
      this.dispense(this.fakeWorld, new BlockPos(x, y, z));
    }
  }

  /**
   * pulled from BlockDispenser
   * 
   * @param worldIn
   * @param pos
   */
  protected void dispense(World worldIn, BlockPos pos) {
    if (this.timeSinceDropped > 0) {
      this.timeSinceDropped--;
      return;
    }
    BlockSourceImpl source = new BlockSourceImpl(worldIn, pos);
    int i = this.getDispenseSlot(worldIn.rand);
    if (i < 0) {
      world.playEvent(1001, pos, 0);
      this.timeSinceDropped = TIME_BTW_DROPS;
    }
    else {
      ItemStack itemstack = this.getStackInSlot(i);
      IBehaviorDispenseItem ibehaviordispenseitem = this.getBehavior(itemstack);
      //    ModCyclic.logger.info("BEHAVIOR "+ibehaviordispenseitem);
      try {
        ItemStack result = ibehaviordispenseitem.dispense(source, itemstack);
        this.setInventorySlotContents(i, result);
        this.timeSinceDropped = TIME_BTW_DROPS;
      }
      catch (Exception e) {
        ModCyclic.logger.error(e.getMessage());
      }
    }
  }

  //  @Override
  //  public boolean attackEntityFrom(DamageSource source, float amount) {
  //    if (source.getEntity() == this || source.getEntity() instanceof EntityArrow) {
  //      amount = 0;
  //      return false;
  //    }
  //    return true;
  //  }
  //  @Override
  //  public AxisAlignedBB getCollisionBox(Entity entityIn) {
  //    if (entityIn instanceof EntityArrow) { return new AxisAlignedBB(this.getPosition(), this.getPosition()); }
  //    return super.getCollisionBox(entityIn);
  //  }
  /**
   * from TileEntityDispenser
   * 
   * @return
   */
  public int getDispenseSlot(Random rand) {
    int i = -1;
    int j = 1;
    for (int k = 0; k < this.getSizeInventory(); ++k) {
      if (!((ItemStack) this.getStackInSlot(k)).isEmpty() && rand.nextInt(j++) == 0) {
        i = k;
      }
    }
    return i;
  }

  /**
   * 
   * pulled from BlockDispenser
   * 
   * @param stack
   * @return
   */
  protected IBehaviorDispenseItem getBehavior(ItemStack stack) {
    //    ModCyclic.logger.info("BEHAVIOR??? "+ stack);
    return BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.getObject(stack.getItem());
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
      //        fac = (this.motionX < 0) ? EnumFacing.WEST : EnumFacing.EAST;
      break;
      case NORTH_SOUTH:
        //        fac = (this.motionZ > 0) ? EnumFacing.SOUTH : EnumFacing.NORTH;
        fac = (this.motionZ < 0) ? EnumFacing.WEST : EnumFacing.EAST;
      break;
      default:
      break;
    }
    super.moveAlongTrack(pos, state);
    if (fac != null) {
      //      ModCyclic.logger.info(raildirection+" setDisplayTile  "+fac);
      this.setDisplayTile(getDefaultDisplayTile().withProperty(BlockDispenser.FACING, fac));
    }
  }
}
