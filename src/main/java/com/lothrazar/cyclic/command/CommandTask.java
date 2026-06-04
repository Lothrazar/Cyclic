package com.lothrazar.cyclic.command;

import com.lothrazar.cyclic.capabilities.player.PlayerCyclicAttachment;
import com.lothrazar.cyclic.registry.AttachmentRegistry;
import com.lothrazar.library.util.ChatUtil;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerPlayer;

public class CommandTask {

  public static int add(CommandContext<CommandSourceStack> ctx, String string) throws CommandSyntaxException {
    ServerPlayer player = ctx.getSource().getPlayerOrException();
    PlayerCyclicAttachment data = player.getData(AttachmentRegistry.CYCLIC_PLAYER);
    String extra = String.join(" ", string);
    int j = data.todoTasks.size();
    data.todoTasks.add(extra);
    ChatUtil.addServerChatMessage(player, j + ")" + extra);
    return 0;
  }

  public static int remove(CommandContext<CommandSourceStack> ctx, int integer) throws CommandSyntaxException {
    ServerPlayer player = ctx.getSource().getPlayerOrException();
    PlayerCyclicAttachment data = player.getData(AttachmentRegistry.CYCLIC_PLAYER);
    data.todoTasks.remove(integer);
    ChatUtil.addServerChatMessage(player, "[" + data.todoTasks.size() + "]");
    return 0;
  }

  public static int list(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
    ServerPlayer player = ctx.getSource().getPlayerOrException();
    PlayerCyclicAttachment data = player.getData(AttachmentRegistry.CYCLIC_PLAYER);
    for (int i = 0; i < data.todoTasks.size(); i++) {
      ChatUtil.addServerChatMessage(player, i + ")" + data.todoTasks.get(i));
    }
    return 0;
  }
}
