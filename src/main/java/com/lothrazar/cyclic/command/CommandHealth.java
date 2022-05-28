package com.lothrazar.cyclic.command;

import java.util.Collection;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerPlayer;

public class CommandHealth {

  public static int executeHealth(CommandContext<CommandSourceStack> ctx, Collection<ServerPlayer> players, float newlevel) {
    for (ServerPlayer player : players) {
      player.setHealth(newlevel);
    }
    return 0;
  }
}
