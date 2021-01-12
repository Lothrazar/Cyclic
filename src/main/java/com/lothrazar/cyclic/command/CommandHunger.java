package com.lothrazar.cyclic.command;

import com.lothrazar.cyclic.ConfigRegistry;
import com.mojang.brigadier.context.CommandContext;
import java.util.List;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.player.PlayerEntity;

public class CommandHunger implements ICyclicCommand {

  @Override
  public String getName() {
    return "hunger";
  }

  @Override
  public boolean needsOp() {
    return ConfigRegistry.COMMANDHUNGER.get();
  }

  @Override
  public int execute(CommandContext<CommandSource> ctx, List<String> arguments, PlayerEntity player) {
    int newlevel = Integer.parseInt(arguments.get(0));
    player.getFoodStats().setFoodLevel(newlevel);
    return 1;
  }
}
