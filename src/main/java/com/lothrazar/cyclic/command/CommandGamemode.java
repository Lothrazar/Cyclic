package com.lothrazar.cyclic.command;

import java.util.Collection;
import com.lothrazar.cyclic.util.ChatUtil;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.GameType;

public class CommandGamemode {

  public static int executeGamemode(CommandContext<CommandSourceStack> x, Collection<ServerPlayer> players, int integer) {
    for (ServerPlayer p : players) {
      switch (integer) {
        case 0:
          p.setGameMode(GameType.SURVIVAL);
        break;
        case 1:
          p.setGameMode(GameType.CREATIVE);
        break;
        case 2:
          p.setGameMode(GameType.ADVENTURE);
        break;
        case 3:
          p.setGameMode(GameType.SPECTATOR);
        break;
        default:
          ChatUtil.sendFeedback(x, integer + " = ?!");
        break;
      }
    }
    return 0;
  }
}
