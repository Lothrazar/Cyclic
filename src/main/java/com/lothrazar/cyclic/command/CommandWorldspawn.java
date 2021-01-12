package com.lothrazar.cyclic.command;

import com.lothrazar.cyclic.ConfigRegistry;
import com.mojang.brigadier.context.CommandContext;
import java.util.List;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.player.PlayerEntity;

public class CommandWorldspawn implements ICyclicCommand {

  @Override
  public String getName() {
    return "worldspawn";
  }

  @Override
  public boolean needsOp() {
    return ConfigRegistry.COMMANDWORLDSPAWN.get();
  }

  @Override
  public int execute(CommandContext<CommandSource> ctx, List<String> arguments, PlayerEntity player) {
    //TODO: 1.16 World Spawn 
    //    UtilEntity.teleportWallSafe(player, player.world, player.world.getSpawnPoint());
    return 1;
  }
}
