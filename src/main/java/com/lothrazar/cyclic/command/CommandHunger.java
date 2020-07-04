package com.lothrazar.cyclic.command;

import java.util.List;
import com.lothrazar.cyclic.util.UtilChat;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.player.PlayerEntity;

public class CommandHunger implements ICyclicCommand {

  @Override
  public String getName() {
    return "hunger";
  }

  @Override
  public int execute(CommandContext<CommandSource> ctx, List<String> arguments, PlayerEntity player) {
    boolean isOp = ctx.getSource().hasPermissionLevel(1);
    if (!isOp) {
      UtilChat.sendFeedback(ctx, "commands.help.failed");
      return 1;
    }
    int newlevel = Integer.parseInt(arguments.get(0));
    player.getFoodStats().setFoodLevel(newlevel);
    return 1;
  }
}
