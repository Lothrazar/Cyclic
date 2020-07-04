package com.lothrazar.cyclic.command;

import java.util.List;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.player.PlayerEntity;

public class CommandHealth implements ICyclicCommand {

  @Override
  public String getName() {
    return "health";
  }

  @Override
  public int execute(CommandContext<CommandSource> ctx, List<String> arguments, PlayerEntity player) {
    System.out.println(arguments);
    float newlevel = Float.parseFloat(arguments.get(0));
    player.setHealth(newlevel);
    return 1;
  }
}
