package com.lothrazar.cyclic.command;

import java.util.Collection;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerPlayer;

public class CommandGlowing {

  public static int executeGlowing(CommandContext<CommandSourceStack> x, Collection<ServerPlayer> players, boolean bool) {
    for (ServerPlayer p : players) {
      p.setGlowingTag(bool);
    }
    return 0;
  }
}
