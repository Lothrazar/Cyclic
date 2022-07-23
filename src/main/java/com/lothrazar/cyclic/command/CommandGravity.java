package com.lothrazar.cyclic.command;

import java.util.Collection;
import com.lothrazar.cyclic.registry.CommandRegistry;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerPlayer;

public class CommandGravity {

  private static void set(boolean bool, ServerPlayer p) {
    p.setNoGravity(bool);
  }

  private static boolean get(ServerPlayer p) {
    return p.isNoGravity();
  }

  public static int executeGravity(CommandContext<CommandSourceStack> x, Collection<ServerPlayer> players, boolean bool) {
    for (ServerPlayer p : players) {
      set(bool, p);
    }
    return 0;
  }

  public static int executeToggle(CommandContext<CommandSourceStack> x, Collection<ServerPlayer> players) {
    for (ServerPlayer p : players) {
      set(!get(p), p);
    }
    return 0;
  }

  public static int executeRandom(CommandContext<CommandSourceStack> x, Collection<ServerPlayer> players) {
    for (ServerPlayer p : players) {
      set(CommandRegistry.RAND.nextBoolean(), p);
    }
    return 0;
  }
}
