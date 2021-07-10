package com.lothrazar.cyclic.command;

import com.lothrazar.cyclic.util.UtilChat;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class CommandNetherping {

  static final double NETHER_RATIO = 8.0;

  public static int execute(CommandContext<CommandSource> ctx) throws CommandSyntaxException {
    ServerPlayerEntity player = ctx.getSource().asPlayer();
    BlockPos pos = player.getPosition();
    UtilChat.sendFeedback(ctx, UtilChat.blockPosToString(pos));
    return 0;
  }

  public static int exeNether(CommandContext<CommandSource> ctx) throws CommandSyntaxException {
    ServerPlayerEntity player = ctx.getSource().asPlayer();
    double factor = 1 / NETHER_RATIO; //overworld: use 1/8th
    boolean isnether = player.world.getDimensionKey() == World.THE_NETHER;
    if (isnether) {
      //you are nether, multiply by it
      factor = NETHER_RATIO;
    }
    BlockPos pos = player.getPosition();
    double x = pos.getX();
    double z = pos.getZ();
    BlockPos netherpos = new BlockPos(x * factor, pos.getY(), z * factor);
    UtilChat.sendFeedback(ctx,
        UtilChat.blockPosToString(pos)
            + " -> " +
            UtilChat.blockPosToString(netherpos));
    return 0;
  }
}
