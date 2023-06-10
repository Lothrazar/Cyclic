package com.lothrazar.cyclic.command;

import java.util.Optional;
import com.lothrazar.cyclic.util.ChatUtil;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

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
    BlockPos respawnPos = player.getRespawnPosition();
    Optional<Vec3> optional = Optional.empty();
    if (respawnPos != null) {
      optional = Player.findRespawnPositionAndUseSpawnBlock(player.getLevel(), respawnPos, 0.0F, true, true);
    }
    if (optional.isPresent()) {
      Vec3 thanksMojang = optional.get();
      BlockPos bedLocation = new BlockPos((int) thanksMojang.x, (int) thanksMojang.y, (int) thanksMojang.z);
      ChatUtil.sendFeedback(ctx, ChatUtil.lang("command.cyclic.gethome.yours") + " " + ChatUtil.blockPosToString(bedLocation));
    }
    else {
      ChatUtil.sendFeedback(ctx, "command.cyclic.gethome.bed");
    }
    return 0;
  }
}
