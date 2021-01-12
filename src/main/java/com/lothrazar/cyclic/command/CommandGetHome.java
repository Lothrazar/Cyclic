package com.lothrazar.cyclic.command;

import com.lothrazar.cyclic.ConfigRegistry;
import com.lothrazar.cyclic.util.UtilChat;
import com.mojang.brigadier.context.CommandContext;
import java.util.List;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;

public class CommandGetHome implements ICyclicCommand {

  @Override
  public String getName() {
    return "gethome";
  }

  @Override
  public boolean needsOp() {
    return ConfigRegistry.COMMANDGETHOME.get();
  }

  @Override
  public int execute(CommandContext<CommandSource> ctx, List<String> arguments, PlayerEntity player) {
    BlockPos bedLocation = player.getBedPosition().orElse(null); // player.getBedLocation(player.dimension);
    if (bedLocation == null) {
      UtilChat.sendFeedback(ctx, "command.cyclic.gethome.bed");
      return 0;
    }
    UtilChat.sendFeedback(ctx, UtilChat.lang("command.cyclic.gethome.yours")
        + " " + UtilChat.blockPosToString(bedLocation));
    return 1;
  }
}
