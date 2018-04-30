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

import net.minecraft.block.BlockRailBase;
import net.minecraft.block.BlockSourceImpl;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityMinecartChest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerDispenser;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class EntityMinecartDropper extends EntityMinecartChest {

  private static final int TIME_BTW_DROPS = 20;
  private int timeSinceDropped = 0;
  public static Item dropItem = Items.MINECART;//override with gold minecart on registry, this is here just for nonnull
  BehaviorMinecartDropItem drop = new BehaviorMinecartDropItem();

  public EntityMinecartDropper(World worldIn) {
    super(worldIn);
  }

  public EntityMinecartDropper(World worldIn, double x, double y, double z) {
    super(worldIn, x, y, z);
  }

  public int getSizeInventory() {
    return 9;
  }

  public IBlockState getDefaultDisplayTile() {
    return Blocks.DROPPER.getDefaultState();//.withProperty(BlockChest.FACING, EnumFacing.NORTH);
  }

  public String getGuiID() {
    //minecraft:dropper
    return "minecraft:dropper";
  }

  @Override
  public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn) {
    this.addLoot(playerIn);
    return new ContainerDispenser(playerInventory, this);
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
      this.dispense(this.world, new BlockPos(x, y, z));
    }
  }

  protected void dispense(World worldIn, BlockPos pos) {
    if (this.timeSinceDropped > 0) {
      this.timeSinceDropped--;
      return;
    }
    this.timeSinceDropped = TIME_BTW_DROPS;
    BlockSourceImpl source = new BlockSourceImpl(worldIn, pos);
    int i = this.getDispenseSlot();
    if (i < 0) {
      worldIn.playEvent(1001, pos, 0);
    }
    else {
      ItemStack itemstack = this.getStackInSlot(i);
      this.setInventorySlotContents(i, this.drop.dispense(source, itemstack));
    }
  }

  @Override
  protected void moveAlongTrack(BlockPos pos, IBlockState state) {
    BlockRailBase blockrailbase = (BlockRailBase) state.getBlock();
    if (blockrailbase != Blocks.ACTIVATOR_RAIL) {
      this.timeSinceDropped = 0;//for passing thru fast reset the counter when we leave eh
    }
    super.moveAlongTrack(pos, state);
  }

  /**
   * from TileEntityDispenser
   * 
   * @return
   */
  public int getDispenseSlot() {
    int i = -1;
    int j = 1;
    for (int k = 0; k < this.getSizeInventory(); ++k) {
      if (!((ItemStack) this.getStackInSlot(k)).isEmpty() && world.rand.nextInt(j++) == 0) {
        i = k;
      }
    }
    return i;
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
