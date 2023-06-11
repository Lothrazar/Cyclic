package com.lothrazar.cyclic.command;

import com.lothrazar.cyclic.util.ChatUtil;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;

public class CommandNetherping {

  static final double NETHER_RATIO = 8.0;

  public static int execute(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
    ServerPlayer player = ctx.getSource().getPlayerOrException();
    BlockPos pos = player.blockPosition();
    ChatUtil.sendFeedback(ctx, ChatUtil.blockPosToString(pos));
    return 0;
  }

  public static int exeNether(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
    ServerPlayer player = ctx.getSource().getPlayerOrException();
    double factor = 1 / NETHER_RATIO; //overworld: use 1/8th
    boolean isnether = player.level().dimension() == Level.NETHER;
    if (isnether) {
      //you are nether, multiply by it
      factor = NETHER_RATIO;
    }
    BlockPos pos = player.blockPosition();
    double x = pos.getX();
    double z = pos.getZ();
    BlockPos netherpos = new BlockPos((int) (x * factor), pos.getY(), (int) (z * factor));
    ChatUtil.sendFeedback(ctx,
        ChatUtil.blockPosToString(pos)
            + " -> " +
            ChatUtil.blockPosToString(netherpos));
    return 0;
  }
}
