package com.lothrazar.cyclic.command;

import java.util.List;
import com.lothrazar.cyclic.ConfigManager;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.player.PlayerEntity;

public class CommandHunger implements ICyclicCommand {

  @Override
  public String getName() {
    return "hunger";
  }

  @Override
  public boolean needsOp() {
    return ConfigManager.COMMANDHUNGER.get();
  }

  @Override
  public int execute(CommandContext<CommandSource> ctx, List<String> arguments, PlayerEntity player) {
    int newlevel = Integer.parseInt(arguments.get(0));
    player.getFoodStats().setFoodLevel(newlevel);
    return 1;
  }
}
