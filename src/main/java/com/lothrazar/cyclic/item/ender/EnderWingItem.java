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
import com.lothrazar.cyclic.config.ConfigRegistry;
import com.lothrazar.cyclic.item.ItemBaseCyclic;
import com.lothrazar.cyclic.registry.SoundRegistry;
import com.lothrazar.library.core.Const;
import com.lothrazar.library.core.IHasClickToggle;
import com.lothrazar.library.util.ChatUtil;
import com.lothrazar.library.util.EntityUtil;
import com.lothrazar.library.util.ItemStackUtil;
import com.lothrazar.library.util.PlayerUtil;
import com.lothrazar.library.util.SoundUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class EnderWingItem extends ItemBaseCyclic implements IHasClickToggle {


  public EnderWingItem(Properties properties) {
    super(properties);
  }

  @Override
  public InteractionResult use(Level worldIn, Player playerIn, InteractionHand handIn) {
    if (worldIn.isClientSide()
        || playerIn.getCooldowns().isOnCooldown(this)) {
      return super.use(worldIn, playerIn, handIn);
    }
    attemptTeleport(worldIn, playerIn, playerIn.getItemInHand(handIn));
    return super.use(worldIn, playerIn, handIn);
  }

  private void attemptTeleport(Level worldIn, Player playerIn, ItemStack held) {
    if (!(playerIn instanceof ServerPlayer serverPlayerEntity)) {
      return;
    }
    if (serverPlayerEntity.getRespawnPosition() == null) {
      ChatUtil.sendStatusMessage(playerIn, "command.cyclic.home.none");
      return;
    }
    ResourceKey<Level> spawnWorldKey = serverPlayerEntity.getRespawnDimension();
    if (spawnWorldKey == Level.NETHER && worldIn.dimension() != Level.NETHER) {
      ChatUtil.sendStatusMessage(playerIn, "command.cyclic.home.nether");
      return;
    }
    if (spawnWorldKey == Level.OVERWORLD && worldIn.dimension() != Level.OVERWORLD) {
      ChatUtil.sendStatusMessage(playerIn, "command.cyclic.home.overworld");
      return;
    }
    Optional<Vec3> optional = PlayerUtil.getPlayerHome(serverPlayerEntity);
    if (optional.isEmpty()) {
      ChatUtil.sendStatusMessage(playerIn, "command.cyclic.home.obstructed");
      return;
    }
    BlockPos pos = BlockPos.containing(optional.get());
    ItemStackUtil.damageItem(playerIn, held);
    playerIn.getCooldowns().addCooldown(this, ConfigRegistry.CHARM_HOME_COOLDOWN_SECONDS.get() * Const.TICKS_PER_SEC);
    EntityUtil.enderTeleportEvent(playerIn, worldIn, pos);
    SoundUtil.playSound(playerIn, SoundRegistry.WARP_ECHO.get());
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
