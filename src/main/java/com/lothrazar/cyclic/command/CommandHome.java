package com.lothrazar.cyclic.command;

import java.util.Collection;
import java.util.Optional;
import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.util.ChatUtil;
import com.lothrazar.cyclic.util.EntityUtil;
import com.lothrazar.cyclic.util.PlayerUtil;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class CommandHome {

  public static void teleport(ServerPlayer player) {
    Optional<Vec3> optional = PlayerUtil.getPlayerHome(player);
    if (optional.isPresent()) {
      BlockPos bedLocation = new BlockPos(optional.get());
      EntityUtil.enderTeleportEvent(player, player.level, bedLocation);
      ModCyclic.LOGGER.info("[home]" + bedLocation + " | " + player.getUUID());
    }
    else {
      ChatUtil.addServerChatMessage(player, Component.translatable("command.cyclic.gethome.bed"));
      //      ModCyclic.LOGGER.error(ChatUtil.lang("command.cyclic.gethome.bed"));
    }
  }

  public static int executeTp(CommandContext<CommandSourceStack> x, Collection<ServerPlayer> players) {
    for (ServerPlayer sp : players) {
      teleport(sp);
    }
    return 0;
  }

  public static int executeSetHome(CommandContext<CommandSourceStack> x, Collection<ServerPlayer> players, BlockPos blockPos) {
    for (ServerPlayer sp : players) {
      set(sp, x.getSource().getLevel().dimension(), blockPos);
    }
    return 0;
  }

  private static void set(ServerPlayer sp, ResourceKey<Level> resourceKey, BlockPos blockPosition) {
    //respawn anchor uses ,false, true)
    //setspawn command uses true, false)
    sp.setRespawnPosition(resourceKey, blockPosition, 0, true, false);
  }

  public static int executeSaveHome(CommandContext<CommandSourceStack> x, Collection<ServerPlayer> players) {
    for (ServerPlayer sp : players) {
      set(sp, x.getSource().getLevel().dimension(), sp.blockPosition());
    }
    return 0;
  }

  public static int executeReset(CommandContext<CommandSourceStack> x, Collection<ServerPlayer> players) {
    for (ServerPlayer sp : players) {
      set(sp, x.getSource().getLevel().dimension(), null);
    }
    return 0;
  }
}
