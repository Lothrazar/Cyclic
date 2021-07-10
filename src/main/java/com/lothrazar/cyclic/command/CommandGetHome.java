package com.lothrazar.cyclic.command;

import com.lothrazar.cyclic.ConfigRegistry;
import com.lothrazar.cyclic.util.UtilChat;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import java.util.Optional;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;

public class CommandGetHome {

  public boolean needsOp() {
    return ConfigRegistry.COMMANDGETHOME.get();
  }

  public static int execute(CommandContext<CommandSource> ctx) throws CommandSyntaxException {
    ServerPlayerEntity player = ctx.getSource().asPlayer();
    /* get the player's respawn point. This will be one of the following:
     * 
     * -- null: Player has not slept in a bed, or their bed has been destr oyed, and they are not tied to a Respawn Anchor
     * 
     * -- the location of their bed, if they've set the respawn point with a bed
     * 
     * -- the location of their Respawn Anchor in the Nether */
    BlockPos respawnPos = player.func_241140_K_();
    Optional<Vector3d> optional = PlayerEntity.func_242374_a(player.getServerWorld(), respawnPos, 0.0F, true, true);
    if (optional.isPresent()) {
      BlockPos bedLocation = new BlockPos(optional.get());
      UtilChat.sendFeedback(ctx, UtilChat.lang("command.cyclic.gethome.yours") + " " + UtilChat.blockPosToString(bedLocation));
    }
    else {
      UtilChat.sendFeedback(ctx, "command.cyclic.gethome.bed");
    }
    return 0;
  }
}
