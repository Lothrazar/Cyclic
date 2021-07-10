package com.lothrazar.cyclic.command;

import com.mojang.brigadier.context.CommandContext;
import java.util.Collection;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.player.ServerPlayerEntity;

public class CommandHealth {

  public static int execute(CommandContext<CommandSource> ctx, Collection<ServerPlayerEntity> players, float newlevel) {
    for (ServerPlayerEntity player : players) {
      player.setHealth(newlevel);
    }
    return 0;
  }
}
