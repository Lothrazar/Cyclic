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
package com.lothrazar.cyclic.item.wing;

import com.lothrazar.cyclic.base.IHasClickToggle;
import com.lothrazar.cyclic.base.ItemBase;
import com.lothrazar.cyclic.registry.SoundRegistry;
import com.lothrazar.cyclic.util.UtilEntity;
import com.lothrazar.cyclic.util.UtilItemStack;
import com.lothrazar.cyclic.util.UtilSound;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.InteractionHand;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.LevelData;

import net.minecraft.world.item.Item.Properties;

public class EnderWingSp extends ItemBase implements IHasClickToggle {

  public EnderWingSp(Properties properties) {
    super(properties);
  }

  private static final int cooldown = 600;

  @Override
  public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {
    if (playerIn.getCooldowns().isOnCooldown(this)) {
      return super.use(worldIn, playerIn, handIn);
    }
    attemptTeleport(worldIn, playerIn, playerIn.getItemInHand(handIn));
    return super.use(worldIn, playerIn, handIn);
  }

  private void attemptTeleport(Level worldIn, Player playerIn, ItemStack held) {
    LevelData worldInfo = worldIn.getLevelData();
    if (worldInfo != null) {
      BlockPos spawn = new BlockPos(worldInfo.getXSpawn(), worldInfo.getYSpawn(), worldInfo.getZSpawn());
      if (spawn != null) {
        UtilEntity.enderTeleportEvent(playerIn, worldIn, spawn);
        UtilSound.playSound(playerIn, SoundRegistry.WARP_ECHO);
        UtilItemStack.damageItem(playerIn, held);
        playerIn.getCooldowns().addCooldown(this, cooldown);
      }
    }
  }

  @Override
  public void toggle(Player player, ItemStack held) {
    this.attemptTeleport(player.level, player, held);
  }

  @Override
  public boolean isOn(ItemStack held) {
    return false;
  }
}
