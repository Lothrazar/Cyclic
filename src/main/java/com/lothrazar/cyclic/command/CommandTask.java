package com.lothrazar.cyclic.command;

import com.lothrazar.cyclic.data.CyclicFile;
import com.lothrazar.cyclic.event.PlayerDataEvents;
import com.lothrazar.cyclic.util.UtilChat;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.player.ServerPlayerEntity;

public class CommandTask {

  public static int add(CommandContext<CommandSource> ctx, String string) throws CommandSyntaxException {
    ServerPlayerEntity player = ctx.getSource().asPlayer();
    CyclicFile file = PlayerDataEvents.getOrCreate(player);
    String extra = String.join(" ", string);
    int j = file.todoTasks.size();
    file.todoTasks.add(extra);
    UtilChat.addServerChatMessage(player, j + ")" + extra);
    return 0;
  }

  public static int remove(CommandContext<CommandSource> ctx, int integer) throws CommandSyntaxException {
    ServerPlayerEntity player = ctx.getSource().asPlayer();
    CyclicFile file = PlayerDataEvents.getOrCreate(player);
    file.todoTasks.remove(integer);
    UtilChat.addServerChatMessage(player, "[" + file.todoTasks.size() + "]");
    return 0;
  }

  public static int toggle(CommandContext<CommandSource> ctx) throws CommandSyntaxException {
    ServerPlayerEntity player = ctx.getSource().asPlayer();
    CyclicFile file = PlayerDataEvents.getOrCreate(player);
    file.todoVisible = !file.todoVisible;
    return 0;
  }

  public static int list(CommandContext<CommandSource> ctx) throws CommandSyntaxException {
    ServerPlayerEntity player = ctx.getSource().asPlayer();
    CyclicFile file = PlayerDataEvents.getOrCreate(player);
    for (int i = 0; i < file.todoTasks.size(); i++) {
      UtilChat.addServerChatMessage(player, i + ")" + file.todoTasks.get(i));
    }
    return 0;
  }
}
