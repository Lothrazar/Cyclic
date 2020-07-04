package com.lothrazar.cyclic.command;

import java.util.List;
import com.lothrazar.cyclic.util.UtilChat;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;

public class CommandNetherping implements ICyclicCommand {

  final static double netherRatio = 8.0;

  @Override
  public String getName() {
    return "pingnether";
  }

  @Override
  public int execute(CommandContext<CommandSource> ctx, List<String> arguments, PlayerEntity player) {
    double factor = 1 / netherRatio;//overworld: use 1/8th
    if (player.dimension.getId() == -1) {
      //you are nether, multiply by it
      factor = netherRatio;
    }
    BlockPos pos = player.getPosition();
    double x = pos.getX();
    double z = pos.getZ();
    BlockPos netherpos = new BlockPos(x * factor, pos.getY(), z * factor);
    UtilChat.sendFeedback(ctx,
        UtilChat.blockPosToString(pos)
            + " -> " +
            UtilChat.blockPosToString(netherpos));
    return 1;
  }
}
