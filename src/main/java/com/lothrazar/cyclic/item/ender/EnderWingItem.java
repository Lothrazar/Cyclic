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
package com.lothrazar.cyclic.item.ender;

import java.util.Optional;
import com.lothrazar.cyclic.api.IHasClickToggle;
import com.lothrazar.cyclic.item.ItemBaseCyclic;
import com.lothrazar.cyclic.registry.SoundRegistry;
import com.lothrazar.cyclic.util.ChatUtil;
import com.lothrazar.cyclic.util.EntityUtil;
import com.lothrazar.cyclic.util.ItemStackUtil;
import com.lothrazar.cyclic.util.SoundUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class EnderWingItem extends ItemBaseCyclic implements IHasClickToggle {

  private static final int COOLDOWN = 600; //ticks not seconds 

  public EnderWingItem(Properties properties) {
    super(properties);
  }

  @Override
  public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {
    if (worldIn.isClientSide
        || playerIn.getCooldowns().isOnCooldown(this)) {
      return super.use(worldIn, playerIn, handIn);
    }
    attemptTeleport(worldIn, playerIn, playerIn.getItemInHand(handIn));
    return super.use(worldIn, playerIn, handIn);
  }

  private void attemptTeleport(Level worldIn, Player playerIn, ItemStack held) {
    ServerLevel serverWorld = worldIn.getServer().getLevel(Level.OVERWORLD);
    ServerPlayer serverPlayerEntity = playerIn instanceof ServerPlayer ? (ServerPlayer) playerIn : null;
    if (serverWorld != null && serverPlayerEntity != null) {
      /* get the player's respawn point. This will be one of the following: -- null: Player has not slept in a bed, or their bed has been destroyed, and they are not tied to a Respawn Anchor -- the
       * location of their bed, if they've set the respawn point with a bed -- the location of their Respawn Anchor in the Nether */
      BlockPos respawnPos = serverPlayerEntity.getRespawnPosition();
      if (respawnPos != null) {
        //This Optional checks that the player has a valid respawn point, and that it's safe to spawn there
        Optional<Vec3> optional = Player.findRespawnPositionAndUseSpawnBlock(serverWorld, respawnPos, 0.0F, true, true);
        BlockPos pos;
        boolean needsTeleport = false;
        if (optional.isPresent()) {
          pos = new BlockPos((int) optional.get().x(), (int) optional.get().y(), (int) optional.get().z());
          ResourceKey<Level> spawnWorldKey = serverPlayerEntity.getRespawnDimension();
          ServerLevel spawnWorld = worldIn.getServer().getLevel(spawnWorldKey);
          if (spawnWorld != null && spawnWorldKey == Level.NETHER) {
            if (worldIn.dimension() == Level.NETHER) {
              needsTeleport = true;
            }
            else {
              ChatUtil.sendStatusMessage(playerIn, "command.cyclic.home.nether");
            }
          }
          else if (spawnWorld != null && spawnWorldKey == Level.OVERWORLD) {
            if (worldIn.dimension() == Level.OVERWORLD) {
              needsTeleport = true;
            }
            else {
              ChatUtil.sendStatusMessage(playerIn, "command.cyclic.home.overworld");
            }
          }
          if (needsTeleport) {
            ItemStackUtil.damageItem(playerIn, held);
            playerIn.getCooldowns().addCooldown(this, COOLDOWN);
            EntityUtil.enderTeleportEvent(playerIn, spawnWorld, pos);
            SoundUtil.playSound(playerIn, SoundRegistry.WARP_ECHO.get());
          }
        }
        else {
          ChatUtil.sendStatusMessage(playerIn, "command.cyclic.home.obstructed");
        }
      }
    }
  }

  @Override
  public void toggle(Player player, ItemStack held) {
    this.attemptTeleport(player.level(), player, held);
  }

  @Override
  public boolean isOn(ItemStack held) {
    return false;
  }
}
