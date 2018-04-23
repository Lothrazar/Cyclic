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
package com.lothrazar.cyclicmagic.core.util;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class UtilFurnace {

  // http://minecraft.gamepedia.com/Furnace
  public final static int SLOT_INPUT = 0;
  public final static int SLOT_FUEL = 1;
  public final static int SLOT_OUTPUT = 2;

  public static void tryMergeStackIntoSlot(TileEntityFurnace furnace, EntityPlayer entityPlayer, int playerSlot, int furnaceSlot) {
    ItemStack current = furnace.getStackInSlot(furnaceSlot);
    ItemStack held = entityPlayer.inventory.getStackInSlot(playerSlot);
    boolean success = false;
    World worldObj = entityPlayer.getEntityWorld();
    if (current.isEmpty()) {
      // just done
      if (worldObj.isRemote == false) {
        furnace.setInventorySlotContents(furnaceSlot, held.copy());
        held = ItemStack.EMPTY;
      }
      success = true;
    }
    else if (held.isItemEqual(current)) {
      //ModMain.logger.info("slot is NOT empty and they match, current old:" + current.stackSize);
      // merging updates the stack size numbers in both furnace and in players
      success = true;
      if (worldObj.isRemote == false) {
        UtilItemStack.mergeItemsBetweenStacks(held, current);
      }
    }
    if (success) {
      if (worldObj.isRemote == false) {
        if (!held.isEmpty() && held.getCount() == 0) {// so now we just fix if something is size zero
          held = ItemStack.EMPTY;
        }
        entityPlayer.inventory.setInventorySlotContents(playerSlot, held);
        entityPlayer.inventory.markDirty();
      }
      UtilSound.playSound(entityPlayer, SoundEvents.ENTITY_ITEM_PICKUP);
    }
  }

  public static void extractFurnaceOutput(TileEntityFurnace furnace, EntityPlayer player) {
    ItemStack current = furnace.removeStackFromSlot(SLOT_OUTPUT);
    if (!current.isEmpty()) {
      BlockPos pos = player.getPosition();
      if (player.getEntityWorld().isRemote == false) {
        player.dropItemAndGetStack(new EntityItem(player.getEntityWorld(), pos.getX(), pos.getY(), pos.getZ(), current));
      }
      UtilSound.playSound(player, SoundEvents.ENTITY_ITEM_PICKUP);
      //UtilEntity.dropItemStackInWorld(furnace.getWorld(), furnace.getPos(), current);
    }
  }

  public static boolean canBeSmelted(ItemStack input) {
    // we literally get the smelt recipe instance to test if it has one
    ItemStack itemstack = FurnaceRecipes.instance().getSmeltingResult(input);
    return (!itemstack.isEmpty());
  }

  public static boolean isFuel(ItemStack input) {
    // how long does it burn for? zero means it isnt fuel
    return TileEntityFurnace.getItemBurnTime(input) > 0;
  }
}
