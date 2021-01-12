package com.lothrazar.cyclic.command;

import com.lothrazar.cyclic.ConfigRegistry;
import com.lothrazar.cyclic.util.UtilChat;
import com.mojang.brigadier.context.CommandContext;
import java.util.List;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class CommandNetherping implements ICyclicCommand {

  final static double netherRatio = 8.0;

  @Override
  public String getName() {
    return "pingnether";
  }

  @Override
  public boolean needsOp() {
    return ConfigRegistry.COMMANDPINGNETHER.get();
  }

  @Override
  public int execute(CommandContext<CommandSource> ctx, List<String> arguments, PlayerEntity player) {
    double factor = 1 / netherRatio;//overworld: use 1/8th
    boolean isnether = player.world.getDimensionKey() == World.THE_NETHER;
    if (isnether) {
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
