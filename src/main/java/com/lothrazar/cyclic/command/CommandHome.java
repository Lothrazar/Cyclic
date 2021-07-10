package com.lothrazar.cyclic.command;

import com.lothrazar.cyclic.ConfigRegistry;
import com.lothrazar.cyclic.util.UtilChat;
import com.lothrazar.cyclic.util.UtilEntity;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import java.util.Optional;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;

public class CommandHome {

  public boolean needsOp() {
    return ConfigRegistry.COMMANDHOME.get();
  }

  public static int execute(CommandContext<CommandSource> ctx) throws CommandSyntaxException {
    ServerPlayerEntity player = ctx.getSource().asPlayer();
    BlockPos respawnPos = player.func_241140_K_();
    Optional<Vector3d> optional = Optional.empty();
    if (respawnPos != null) {
      optional = PlayerEntity.func_242374_a(player.getServerWorld(), respawnPos, 0.0F, true, true);
    }
    optional = PlayerEntity.func_242374_a(player.getServerWorld(), respawnPos, 0.0F, true, true);
    if (optional.isPresent()) {
      BlockPos bedLocation = new BlockPos(optional.get());
      UtilEntity.enderTeleportEvent(player, player.world, bedLocation);
    }
    else {
      UtilChat.sendFeedback(ctx, "command.cyclic.gethome.bed");
    }
    return 0;
  }
}
