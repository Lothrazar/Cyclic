package com.lothrazar.cyclic.command;

import com.lothrazar.library.util.ChatUtil;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;

public class CommandGetHome {

  public static int execute(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
    ServerPlayer player = ctx.getSource().getPlayerOrException();
    /* get the player's respawn point. This will be one of the following:
     *
     * -- null: Player has not slept in a bed, or their bed has been destr oyed, and they are not tied to a Respawn Anchor
     *
     * -- the location of their bed, if they've set the respawn point with a bed
     *
     * -- the location of their Respawn Anchor in the Nether */
    // 26.1: ServerPlayer#getRespawnPosition() removed - respawn data is now bundled into a RespawnConfig record
    var respawnConfig = player.getRespawnConfig();
    BlockPos respawnPos = respawnConfig == null ? null : respawnConfig.respawnData().pos();
    if (respawnPos != null) {
      ChatUtil.sendFeedback(ctx, ChatUtil.lang("command.cyclic.gethome.yours") + " " + ChatUtil.blockPosToString(respawnPos));
    }
    else {
      ChatUtil.sendFeedback(ctx, "command.cyclic.gethome.bed");
    }
    return 0;
  }
}
