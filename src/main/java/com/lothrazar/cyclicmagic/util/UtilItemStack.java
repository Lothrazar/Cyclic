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
package com.lothrazar.cyclicmagic.util;

import java.util.List;
import javax.annotation.Nonnull;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public class UtilItemStack {

  public static ItemStack tryDepositToHandler(World world, BlockPos posSide, EnumFacing sideOpp, ItemStack stackToExport) {
    TileEntity tileTarget = world.getTileEntity(posSide);
    if (tileTarget == null ||
        tileTarget.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, sideOpp) == false) {
      return stackToExport;
    }
    IItemHandler itemHandlerDeposit = tileTarget.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, sideOpp);
    for (int i = 0; i < itemHandlerDeposit.getSlots(); i++) {
      //      i thought i needed this, but bug was on other end
      // https://github.com/BluSunrize/ImmersiveEngineering/issues/3044
      //      if (tileTarget instanceof IInventory &&
      //          ((IInventory) tileTarget).isItemValidForSlot(i, stackToExport) == false) {
      //        continue;
      //      }
      ItemStack leftBehindAfterInsert = itemHandlerDeposit.insertItem(i, stackToExport, false).copy();
      //so toExport is 60. leftbehind is 50, this means 10 were deposited. success
      if (leftBehindAfterInsert.getCount() < stackToExport.getCount()) {
        return leftBehindAfterInsert;
      }
    }
    return stackToExport;
  }

  /**
   * match item, damage, and NBT
   * 
   * @param chestItem
   * @param bagItem
   * @return
   */
  public static boolean canMerge(ItemStack chestItem, ItemStack bagItem) {
    if (chestItem.isEmpty() || bagItem.isEmpty()) {
      return false;
    }
    return (bagItem.getItem().equals(chestItem.getItem())
        && bagItem.getItemDamage() == chestItem.getItemDamage()
        && ItemStack.areItemStackTagsEqual(bagItem, chestItem));
  }

  public static int mergeItemsBetweenStacks(ItemStack takeFrom, ItemStack moveTo) {
    int room = moveTo.getMaxStackSize() - moveTo.getCount();
    int moveover = 0;
    if (room > 0) {
      moveover = Math.min(takeFrom.getCount(), room);
      moveTo.grow(moveover);
      takeFrom.shrink(moveover);
    }
    return moveover;
  }

  public static boolean isEmpty(ItemStack is) {
    return is == null || is.isEmpty() || is == ItemStack.EMPTY;
  }

  public static void damageItem(EntityLivingBase p, ItemStack s) {
    if (p instanceof EntityPlayer) {
      damageItem((EntityPlayer) p, s);
    }
    else {
      s.damageItem(1, p);
    }
  }

  public static void damageItem(EntityPlayer p, ItemStack s) {
    damageItem(p, s, 1);
  }

  public static void repairItem(EntityPlayer p, ItemStack s) {
    s.setItemDamage(s.getItemDamage() - 1);
  }
  public static void damageItem(EntityPlayer p, ItemStack s, int num) {
    if (p.capabilities.isCreativeMode == false && p.world.isRemote == false) {
      s.damageItem(num, p);
    }
  }

  /**
   * Created becuase getStateFromMeta is deprecated, and its used everywhere so tons of warnings, and i have no idea how simple/complex the solution will be
   * 
   * @param b
   * @param meta
   * @return
   */
  @SuppressWarnings("deprecation")
  public static IBlockState getStateFromMeta(Block b, int meta) {
    return b.getStateFromMeta(meta);
  }

  /**
   * created to wrap up deprecated calls
   * 
   * @param b
   * @param state
   * @param player
   * @param worldIn
   * @param pos
   * @return
   */
  @SuppressWarnings("deprecation")
  public static float getPlayerRelativeBlockHardness(Block b, IBlockState state, EntityPlayer player, World worldIn, BlockPos pos) {
    return b.getPlayerRelativeBlockHardness(state, player, worldIn, pos);
  }

  @SuppressWarnings("deprecation")
  public static float getBlockHardness(IBlockState state, World worldIn, BlockPos pos) {
    //no way the forge hooks one has a stupid thing where <0 returns 0
    return state.getBlock().getBlockHardness(state, worldIn, pos);
    //    return b.getPlayerRelativeBlockHardness(state, player, worldIn, pos);
  }

  public static EntityItem dropItemStackInWorld(World worldObj, BlockPos pos, Block block) {
    return dropItemStackInWorld(worldObj, pos, new ItemStack(block));
  }

  public static EntityItem dropItemStackInWorld(World worldObj, BlockPos pos, Item item) {
    return dropItemStackInWorld(worldObj, pos, new ItemStack(item));
  }

  public static EntityItem dropItemStackInWorld(World world, BlockPos pos, ItemStack stack) {
    if (pos == null || world == null || stack.isEmpty()) {
      return null;
    }
    EntityItem entityItem = new EntityItem(world, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, stack);
    if (world.isRemote == false) {
      // do not spawn a second 'ghost' one onclient side
      world.spawnEntity(entityItem);
    }
    return entityItem;
  }

  public static void dropItemStackMotionless(World world, BlockPos pos, @Nonnull ItemStack stack) {
    if (stack.isEmpty()) {
      return;
    }
    EntityItem entityItem = new EntityItem(world, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, stack);
    if (world.isRemote == false) {
      // do not spawn a second 'ghost' one onclient side
      entityItem.motionX = entityItem.motionY = entityItem.motionZ = 0;
      world.spawnEntity(entityItem);
    }
  }

  public static void dropItemStacksInWorld(World world, BlockPos pos, List<ItemStack> stacks) {
    for (ItemStack s : stacks) {
      UtilItemStack.dropItemStackInWorld(world, pos, s);
    }
  }

  public static @Nonnull String getStringForItemStack(ItemStack itemStack) {
    Item item = itemStack.getItem();
    return item.getRegistryName().getResourceDomain() + ":" + item.getRegistryName().getResourcePath() + "/" + itemStack.getMetadata();
  }

  public static String getStringForItem(Item item) {
    if (item == null || item.getRegistryName() == null) {
      return "";
    }
    return item.getRegistryName().getResourceDomain() + ":" + item.getRegistryName().getResourcePath();
  }

  public static String getStringForBlock(Block b) {
    return b.getRegistryName().getResourceDomain() + ":" + b.getRegistryName().getResourcePath();
  }

  public static void dropBlockState(World world, BlockPos position, IBlockState current) {
    if (world.isRemote == false && current.getBlock() != Blocks.AIR) {
      dropItemStackInWorld(world, position, getSilkTouchDrop(current));
    }
  }

  public static IBlockState getStateFromStack(ItemStack stack) {
    Block stuff = Block.getBlockFromItem(stack.getItem());
    return UtilItemStack.getStateFromMeta(stuff, stack.getMetadata());
  }

  /* stupid Block class has this not public */
  public static ItemStack getSilkTouchDrop(IBlockState state) {
    Item item = Item.getItemFromBlock(state.getBlock());
    int i = 0;
    if (item.getHasSubtypes()) {
      i = state.getBlock().getMetaFromState(state);
    }
    return new ItemStack(item, 1, i);
  }
}
