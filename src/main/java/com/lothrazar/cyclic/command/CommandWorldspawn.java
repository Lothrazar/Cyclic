package com.lothrazar.cyclic.command;

import java.util.List;
import com.lothrazar.cyclic.ConfigManager;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.player.PlayerEntity;

public class CommandWorldspawn implements ICyclicCommand {

  @Override
  public String getName() {
    return "worldspawn";
  }

  @Override
  public boolean needsOp() {
    return ConfigManager.COMMANDWORLDSPAWN.get();
  }

  @Override
  public int execute(CommandContext<CommandSource> ctx, List<String> arguments, PlayerEntity player) {
    //TODO: 1.16
    //    UtilEntity.teleportWallSafe(player, player.world, player.world.getSpawnPoint());
    return 1;
  }
}
