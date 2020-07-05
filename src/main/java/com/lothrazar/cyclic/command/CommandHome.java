package com.lothrazar.cyclic.command;

import java.util.List;
import com.lothrazar.cyclic.ConfigManager;
import com.lothrazar.cyclic.util.UtilChat;
import com.lothrazar.cyclic.util.UtilEntity;
import com.mojang.brigadier.context.CommandContext;
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
    return ConfigManager.COMMANDHOME.get();
  }

  @Override
  public int execute(CommandContext<CommandSource> ctx, List<String> arguments, PlayerEntity player) {
    BlockPos bedLocation = player.getBedLocation(player.dimension);
    if (bedLocation == null) {
      UtilChat.sendFeedback(ctx, "command.cyclic.gethome.bed");
      return 0;
    }
    UtilEntity.teleportWallSafe(player, player.world, bedLocation);
    return 1;
  }
}
