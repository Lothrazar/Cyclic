package com.lothrazar.cyclic.command;

import java.util.Collection;
import com.lothrazar.cyclic.registry.CommandRegistry;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerPlayer;

public class CommandHunger {

  private static void set(int newlevel, ServerPlayer player) {
    player.getFoodData().setFoodLevel(newlevel);
  }

  private static int get(ServerPlayer player) {
    return player.getFoodData().getFoodLevel();
  }

  public static int executeSet(CommandContext<CommandSourceStack> ctx, Collection<ServerPlayer> players, int newlevel) {
    for (ServerPlayer player : players) {
      set(newlevel, player);
    }
    return 0;
  }

  public static int executeRandom(CommandContext<CommandSourceStack> ctx, Collection<ServerPlayer> players, int min, int max) {
    for (ServerPlayer player : players) {
      set(CommandRegistry.RAND.nextInt(min, max), player);
    }
    return 0;
  }

  public static int executeFactor(CommandContext<CommandSourceStack> x, Collection<ServerPlayer> players, double fact) {
    for (ServerPlayer player : players) {
      set((int) (get(player) * fact), player);
    }
    return 0;
  }

  public static int executeAdd(CommandContext<CommandSourceStack> x, Collection<ServerPlayer> players, int more) {
    for (ServerPlayer player : players) {
      set(get(player) + more, player);
    }
    return 0;
  }
}
