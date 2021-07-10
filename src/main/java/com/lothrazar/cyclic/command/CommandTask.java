package com.lothrazar.cyclic.command;

import com.lothrazar.cyclic.data.CyclicFile;
import com.lothrazar.cyclic.event.PlayerDataEvents;
import com.lothrazar.cyclic.util.UtilChat;
import com.mojang.brigadier.context.CommandContext;
import java.util.List;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.player.PlayerEntity;

public class CommandTask {

  //TODO:
  public int execute(CommandContext<CommandSource> ctx, List<String> arguments, PlayerEntity player) {
    String subtask = arguments.get(0);
    CyclicFile file = PlayerDataEvents.getOrCreate(player);
    if (subtask.equalsIgnoreCase("add")) {
      String extra = String.join(" ", arguments.subList(1, arguments.size()));
      int j = file.todoTasks.size();
      file.todoTasks.add(extra);
      UtilChat.addServerChatMessage(player, j + ")" + extra);
    }
    else if (subtask.equalsIgnoreCase("remove")) {
      Integer target = Integer.parseInt(arguments.get(1));
      file.todoTasks.remove(target.intValue());
      UtilChat.addServerChatMessage(player, "[" + file.todoTasks.size() + "]");
    }
    else if (subtask.equalsIgnoreCase("list")) {
      int i = 0;
      for (String t : file.todoTasks) {
        UtilChat.addServerChatMessage(player, i + ")" + t);
        i++;
      }
    }
    else if (subtask.equalsIgnoreCase("hidden")) {
      file.todoVisible = !file.todoVisible;
    }
    return 1;
  }
}
