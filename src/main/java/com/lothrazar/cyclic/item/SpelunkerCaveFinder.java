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

import com.lothrazar.cyclic.util.UtilChat;
import com.lothrazar.cyclic.util.UtilEntity;
import com.lothrazar.cyclic.util.UtilItemStack;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;

public class SpelunkerCaveFinder extends ItemBaseCyclic {

  public SpelunkerCaveFinder(Properties properties) {
    super(properties.durability(10));
  }

  private static final int COOLDOWN = 12;
  private static final int RANGE = 64;

  @Override
  public InteractionResult useOn(UseOnContext context) {
    Player player = context.getPlayer();
    //
    ItemStack stack = context.getItemInHand();
    BlockPos pos = context.getClickedPos();
    Direction direction = context.getClickedFace();
    if (direction == null) {
      return super.useOn(context);
    }
    Level worldObj = context.getLevel();
    //    boolean showOdds = player.isSneaking();
    boolean found = false;
    //    if (!worldObj.isRemote) {
    BlockPos current = pos;
    for (int i = 1; i <= RANGE; i++) {
      current = current.relative(direction.getOpposite());
      if (context.getLevel().isEmptyBlock(current)) {
        UtilChat.addChatMessage(player, UtilChat.lang("tool.spelunker.cave") + i);
        found = true;
      }
      else if (worldObj.getBlockState(current) == Blocks.WATER.defaultBlockState()) {
        UtilChat.addChatMessage(player, UtilChat.lang("tool.spelunker.water") + i);
        found = true;
      }
      else if (worldObj.getBlockState(current) == Blocks.LAVA.defaultBlockState()) {
        UtilChat.addChatMessage(player, UtilChat.lang("tool.spelunker.lava") + i);
        found = true;
      }
      if (found) {
        break;
      }
    }
    if (found == false) {
      UtilChat.addChatMessage(player, UtilChat.lang("tool.spelunker.none") + RANGE);
    }
    UtilItemStack.damageItem(player, stack);
    UtilEntity.setCooldownItem(player, this, COOLDOWN);
    return super.useOn(context);
  }
}
