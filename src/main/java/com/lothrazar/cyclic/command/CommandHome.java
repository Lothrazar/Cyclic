package com.lothrazar.cyclic.command;

import java.util.Optional;
import com.lothrazar.cyclic.util.ChatUtil;
import com.lothrazar.cyclic.util.EntityUtil;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public class CommandHome {

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
      EntityUtil.enderTeleportEvent(player, player.level, bedLocation);
    }
    else {
      ChatUtil.sendFeedback(ctx, "command.cyclic.gethome.bed");
    }
    return 0;
  }
}
