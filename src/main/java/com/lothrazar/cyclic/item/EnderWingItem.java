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
import com.lothrazar.cyclic.registry.SoundRegistry;
import com.lothrazar.cyclic.util.UtilChat;
import com.lothrazar.cyclic.util.UtilEntity;
import com.lothrazar.cyclic.util.UtilItemStack;
import com.lothrazar.cyclic.util.UtilSound;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import java.util.Optional;

public class EnderWingItem extends ItemBase {

  public EnderWingItem(Properties properties) {
    super(properties);
  }

  private static final int cooldown = 600;//ticks not seconds 

  @Override
  public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
    if (worldIn.isRemote
          || playerIn.getCooldownTracker().hasCooldown(this))
      return super.onItemRightClick(worldIn, playerIn, handIn);

    ServerWorld serverWorld = worldIn.getServer().getWorld(World.OVERWORLD);
    ServerPlayerEntity serverPlayerEntity = playerIn instanceof ServerPlayerEntity ? (ServerPlayerEntity) playerIn : null;
    if (serverWorld != null && serverPlayerEntity != null) {
      /*
      get the player's respawn point. This will be one of the following:
        -- null: Player has not slept in a bed, or their bed has been destroyed, and they are not tied to a Respawn Anchor
        -- the location of their bed, if they've set the respawn point with a bed
        -- the location of their Respawn Anchor in the Nether
       */
      BlockPos respawnPos = serverPlayerEntity.func_241140_K_();
      if (respawnPos != null) {
        //This Optional checks that the player has a valid respawn point, and that it's safe to spawn there
        Optional<Vector3d> optional = PlayerEntity.func_242374_a(serverWorld, respawnPos, 0.0F,true, true);
        BlockPos pos;
        boolean needsTeleport = false;
        if (optional.isPresent()) {
          pos = new BlockPos(optional.get().getX(), optional.get().getY(), optional.get().getZ());
          RegistryKey<World> spawnWorldKey = serverPlayerEntity.func_241141_L_();
          ServerWorld spawnWorld = worldIn.getServer().getWorld(spawnWorldKey);

          if (spawnWorld != null && spawnWorldKey == World.THE_NETHER) {
            if (worldIn.getDimensionKey() == World.THE_NETHER) {
              needsTeleport = true;
            }
            else {
              UtilChat.sendStatusMessage(playerIn, "command.cyclic.home.nether");
            }
          }
          else if (spawnWorld != null && spawnWorldKey == World.OVERWORLD) {
            if (worldIn.getDimensionKey() == World.OVERWORLD) {
              needsTeleport = true;
            }
            else {
              UtilChat.sendStatusMessage(playerIn, "command.cyclic.home.overworld");
            }
          }
          if (needsTeleport) {
            UtilItemStack.damageItem(playerIn, playerIn.getHeldItem(handIn));
            playerIn.getCooldownTracker().setCooldown(this, cooldown);
            UtilEntity.teleportWallSafe(playerIn, spawnWorld, pos);
            UtilSound.playSound(playerIn, SoundRegistry.warp_echo);
          }
        }
        else {
          UtilChat.sendStatusMessage(playerIn, "command.cyclic.home.obstructed");
        }
      }
    }
    return super.onItemRightClick(worldIn, playerIn, handIn);
  }
}
