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
import com.lothrazar.cyclic.data.Const;
import com.lothrazar.cyclic.registry.SoundRegistry;
import com.lothrazar.cyclic.util.UtilChat;
import com.lothrazar.cyclic.util.UtilEntity;
import com.lothrazar.cyclic.util.UtilItemStack;
import com.lothrazar.cyclic.util.UtilSound;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class EnderWingItem extends ItemBase {

  public EnderWingItem(Properties properties) {
    super(properties);
  }

  private static final int cooldown = 600;//ticks not seconds 

  @Override
  public ActionResultType onItemUse(ItemUseContext context) {
    PlayerEntity player = context.getPlayer();
    World world = context.getWorld();
    if (player.getCooldownTracker().hasCooldown(this)) {
      return super.onItemUse(context);
    }
    if (player.dimension.getId() != Const.Dimension.overworld) {
      UtilChat.sendStatusMessage(player, "command.home.overworld");
    }
    else {
      BlockPos pos = player.getBedLocation(player.dimension);
      if (pos == null) {
        UtilChat.sendStatusMessage(player, "command.gethome.bed");
      }
      else {
        UtilSound.playSound(player, SoundRegistry.warp_echo);
        if (!world.isRemote()) {
          UtilItemStack.damageItem(player, context.getItem());
          player.getCooldownTracker().setCooldown(this, cooldown);
          UtilEntity.teleportWallSafe(player, world, pos);
        }
      }
    }
    return super.onItemUse(context);
  }
}
