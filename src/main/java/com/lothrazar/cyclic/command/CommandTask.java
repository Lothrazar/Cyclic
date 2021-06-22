package com.lothrazar.cyclic.command;

import com.lothrazar.cyclic.event.PlayerDataEvents;
import com.lothrazar.cyclic.event.PlayerDataEvents.CyclicFile;
import com.lothrazar.cyclic.util.UtilChat;
import com.mojang.brigadier.context.CommandContext;
import java.util.List;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.player.PlayerEntity;

public class CommandTask implements ICyclicCommand {

  @Override
  public String getName() {
    return "todo";
  }

  @Override
  public boolean needsOp() {
    return false;
  }

  @Override
  public int execute(CommandContext<CommandSource> ctx, List<String> arguments, PlayerEntity player) {
    String subtask = arguments.get(0);
    CyclicFile file = PlayerDataEvents.getOrCreate(player);
    if (subtask.equalsIgnoreCase("add")) {
      String extra = String.join(" ", arguments.subList(1, arguments.size()));
      int j = file.tasks.size();
      file.tasks.add(extra);
      UtilChat.addServerChatMessage(player, j + ")" + extra);
    }
    else if (subtask.equalsIgnoreCase("remove")) {
      Integer target = Integer.parseInt(arguments.get(1));
      file.tasks.remove(target.intValue());
      UtilChat.addServerChatMessage(player, "[" + file.tasks.size() + "]");
    }
    else if (subtask.equalsIgnoreCase("list")) {
      int i = 0;
      for (String t : file.tasks) {
        UtilChat.addServerChatMessage(player, i + ")" + t);
        i++;
      }
    }
    else if (subtask.equalsIgnoreCase("show")) {
      //toggle showhide
    }
    return 1;
  }
}
