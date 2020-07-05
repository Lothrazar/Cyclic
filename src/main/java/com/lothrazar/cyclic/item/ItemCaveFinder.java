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
package com.lothrazar.cyclic.item;

import com.lothrazar.cyclic.base.ItemBase;
import com.lothrazar.cyclic.util.UtilChat;
import com.lothrazar.cyclic.util.UtilEntity;
import com.lothrazar.cyclic.util.UtilItemStack;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemCaveFinder extends ItemBase {

  public ItemCaveFinder(Properties properties) {
    super(properties.maxDamage(10));
  }

  private static final int COOLDOWN = 12;
  private static int range = 48;

  @Override
  public ActionResultType onItemUse(ItemUseContext context) {
    PlayerEntity player = context.getPlayer();
    //
    ItemStack stack = context.getItem();
    BlockPos pos = context.getPos();
    Direction direction = context.getFace();
    if (direction == null) {
      return super.onItemUse(context);
    }
    World worldObj = context.getWorld();
    //    boolean showOdds = player.isSneaking();
    boolean found = false;
    //    if (!worldObj.isRemote) {
    BlockPos current = pos;
    for (int i = 1; i <= range; i++) {
      current = current.offset(direction.getOpposite());
      if (context.getWorld().isAirBlock(current)) {
        UtilChat.addChatMessage(player, UtilChat.lang("tool.spelunker.cave") + i);
        found = true;
      }
      else if (worldObj.getBlockState(current) == Blocks.WATER.getDefaultState()) {
        UtilChat.addChatMessage(player, UtilChat.lang("tool.spelunker.water") + i);
        found = true;
      }
      else if (worldObj.getBlockState(current) == Blocks.LAVA.getDefaultState()) {
        UtilChat.addChatMessage(player, UtilChat.lang("tool.spelunker.lava") + i);
        found = true;
      }
      if (found) {
        break;//stop looping
      }
    }
    if (found == false) {
      UtilChat.addChatMessage(player, UtilChat.lang("tool.spelunker.none") + range);
    }
    //    }
    UtilItemStack.damageItem(player, stack);
    UtilEntity.setCooldownItem(player, this, COOLDOWN);
    return super.onItemUse(context);
  }
}
