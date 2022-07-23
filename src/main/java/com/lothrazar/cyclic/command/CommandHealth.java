package com.lothrazar.cyclic.command;

import java.util.Collection;
import com.lothrazar.cyclic.registry.CommandRegistry;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerPlayer;

public class CommandHealth {

  private static void set(float newlevel, ServerPlayer player) {
    player.setHealth(newlevel);
  }

  private static float get(ServerPlayer player) {
    return player.getHealth();
  }

  public static int executeSet(CommandContext<CommandSourceStack> ctx, Collection<ServerPlayer> players, float newlevel) {
    for (ServerPlayer player : players) {
      set(newlevel, player);
    }
    return 0;
  }

  public static int executeAdd(CommandContext<CommandSourceStack> x, Collection<ServerPlayer> players, int more) {
    for (ServerPlayer player : players) {
      set(get(player) + more, player);
    }
    return 0;
  }

  public static int executeFactor(CommandContext<CommandSourceStack> x, Collection<ServerPlayer> players, double factor) {
    for (ServerPlayer player : players) {
      set(get(player) * (float) factor, player);
    }
    return 0;
  }

  public static int addRandom(CommandContext<CommandSourceStack> x, Collection<ServerPlayer> players, int min, int max) {
    for (ServerPlayer player : players) {
      set(get(player) + CommandRegistry.RAND.nextInt(min, max), player);
    }
    return 0;
  }
}
