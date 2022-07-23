package com.lothrazar.cyclic.command;

import java.util.Collection;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerPlayer;

public class CommandGravity {

  public static int executeGravity(CommandContext<CommandSourceStack> x, Collection<ServerPlayer> players, boolean bool) {
    for (ServerPlayer p : players) {
      p.setNoGravity(bool);
    }
    return 0;
  }
}
