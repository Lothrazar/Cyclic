package com.lothrazar.cyclic.command;

import java.util.Collection;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerPlayer;

public class CommandHunger {

  public static int execute(CommandContext<CommandSourceStack> ctx, Collection<ServerPlayer> players, int newlevel) {
    for (ServerPlayer player : players) {
      player.getFoodData().setFoodLevel(newlevel);
    }
    return 0;
  }
}
