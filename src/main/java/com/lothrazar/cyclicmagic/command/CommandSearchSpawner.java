package com.lothrazar.cyclicmagic.command;
import java.util.ArrayList;
import com.lothrazar.cyclicmagic.util.UtilChat;
import com.lothrazar.cyclicmagic.util.UtilWorld;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.init.Blocks;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

public class CommandSearchSpawner extends BaseCommand implements ICommand {
  public static final String name = "searchspawner";
  public CommandSearchSpawner(boolean op) {
    super(name, op);
  }
  public static final int MAXRADIUS = 128;
  // yes no?
  public static final int DEFAULTRADIUS = 64;
  @Override
  public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
    int radius = 0;
    if (args.length > 0) {
      radius = Integer.parseInt(args[0]);
    }
    if (radius > MAXRADIUS) {
      radius = MAXRADIUS;
    }
    if (radius <= 0) {
      radius = DEFAULTRADIUS;
    }
    ArrayList<BlockPos> founds = UtilWorld.findBlocks(sender.getEntityWorld(),
        sender.getPosition(), Blocks.MOB_SPAWNER, radius);
    if (founds.size() == 0) {
      UtilChat.addChatMessage(sender, UtilChat.lang("command.searchspawner.none") + radius);
    }
    else {
      for (BlockPos found : founds)
        if (found != null) {
          UtilChat.addChatMessage(sender, UtilChat.blockPosToString(found));
        }
    }
  }
}
