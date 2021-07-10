package com.lothrazar.cyclic.command;

import com.mojang.brigadier.context.CommandContext;
import java.util.Collection;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.player.ServerPlayerEntity;

public class CommandHunger {

  public static int execute(CommandContext<CommandSource> ctx, Collection<ServerPlayerEntity> players, int newlevel) {
    for (ServerPlayerEntity player : players) {
      player.getFoodStats().setFoodLevel(newlevel);
    }
    return 0;
  }
}
