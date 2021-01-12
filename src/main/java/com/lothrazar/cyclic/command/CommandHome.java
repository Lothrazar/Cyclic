package com.lothrazar.cyclic.command;

import com.lothrazar.cyclic.ConfigRegistry;
import com.lothrazar.cyclic.util.UtilChat;
import com.lothrazar.cyclic.util.UtilEntity;
import com.mojang.brigadier.context.CommandContext;
import java.util.List;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;

public class CommandHome implements ICyclicCommand {

  @Override
  public String getName() {
    return "home";
  }

  @Override
  public boolean needsOp() {
    return ConfigRegistry.COMMANDHOME.get();
  }

  @Override
  public int execute(CommandContext<CommandSource> ctx, List<String> arguments, PlayerEntity player) {
    BlockPos bedLocation = player.getBedPosition().orElse(null);
    if (bedLocation == null) {
      UtilChat.sendFeedback(ctx, "command.cyclic.gethome.bed");
      return 0;
    }
    UtilEntity.enderTeleportEvent(player, player.world, bedLocation);
    return 1;
  }
}
