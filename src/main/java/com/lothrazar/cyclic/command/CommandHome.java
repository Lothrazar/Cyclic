package com.lothrazar.cyclic.command;

import com.lothrazar.cyclic.config.ConfigRegistry;
import com.lothrazar.cyclic.util.UtilChat;
import com.lothrazar.cyclic.util.UtilEntity;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import java.util.Optional;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;

public class CommandHome {

  public boolean needsOp() {
    return ConfigRegistry.COMMANDHOME.get();
  }

  public static int execute(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
    ServerPlayer player = ctx.getSource().getPlayerOrException();
    BlockPos respawnPos = player.getRespawnPosition();
    Optional<Vec3> optional = Optional.empty();
    if (respawnPos != null) {
      optional = Player.findRespawnPositionAndUseSpawnBlock(player.getLevel(), respawnPos, 0.0F, true, true);
    }
    optional = Player.findRespawnPositionAndUseSpawnBlock(player.getLevel(), respawnPos, 0.0F, true, true);
    if (optional.isPresent()) {
      BlockPos bedLocation = new BlockPos(optional.get());
      UtilEntity.enderTeleportEvent(player, player.level, bedLocation);
    }
    else {
      UtilChat.sendFeedback(ctx, "command.cyclic.gethome.bed");
    }
    return 0;
  }
}
