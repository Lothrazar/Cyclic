package com.lothrazar.cyclic.command;

import com.lothrazar.cyclic.ConfigRegistry;
import com.mojang.brigadier.context.CommandContext;
import java.util.List;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.player.PlayerEntity;

public class CommandHealth implements ICyclicCommand {

  @Override
  public String getName() {
    return "health";
  }

  @Override
  public boolean needsOp() {
    return ConfigRegistry.COMMANDHEALTH.get();
  }

  @Override
  public int execute(CommandContext<CommandSource> ctx, List<String> arguments, PlayerEntity player) {
    float newlevel = Float.parseFloat(arguments.get(0));
    player.setHealth(newlevel);
    return 1;
  }
}
