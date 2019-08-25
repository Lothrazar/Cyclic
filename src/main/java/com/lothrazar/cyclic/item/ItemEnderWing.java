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

public class ItemEnderWing extends ItemBase {

  public ItemEnderWing(Properties properties) {
    super(properties);
  }

  private static final int cooldown = 600;//ticks not seconds
  private static final int durability = 16;

  public static enum WarpType {
    BED, SPAWN
  }

  private WarpType warpType;
  //
  //  @Override
  //  public String getContentName() {
  //    return warpType == WarpType.SPAWN ? "tool_warp_spawn" : "tool_warp_home";
  //  }
  //  private boolean tryActivate(EntityPlayer player, ItemStack held) {
  //    if (player.getCooldownTracker().hasCooldown(this)) {
  //      return false;
  //    }
  //    World world = player.getEntityWorld();
  //    if (player.dimension != 0) {
  //      UtilChat.sendStatusMessage(player, "command.worldhome.dim");
  //      return false;
  //    }
  //    //boolean success = false;
  //    BlockPos target = null;
  //    switch (warpType) {
  //      case BED:
  //        target = player.getBedLocation(0);
  //        // success = UtilWorld.tryTpPlayerToBed(world, player);
  //        if (target == null) {
  //          UtilChat.sendStatusMessage(player, "command.gethome.bed");
  //          return false;
  //        }
  //      break;
  //      case SPAWN:
  //        target = world.getSpawnPoint();
  //      //UtilEntity.teleportWallSafe(player, world, world.getSpawnPoint());
  //      //success = true;
  //      break;
  //    }
  //    if (target == null) {
  //      return false;
  //    }
  //    boolean success = UtilEntity.enderTeleportEvent(player, world, target);
  //    if (success) {
  //      UtilItemStack.damageItem(player, held);
  //      UtilSound.playSound(player, SoundRegistry.warp);
  //      UtilEntity.setCooldownItem(player, this, cooldown);
  //    }
  //    return success;
  //  }
}
