package com.lothrazar.cyclicmagic.command;
import com.lothrazar.cyclicmagic.registry.CapabilityRegistry;
import com.lothrazar.cyclicmagic.registry.CapabilityRegistry.IPlayerExtendedProperties;
import com.lothrazar.cyclicmagic.util.UtilChat;
import com.lothrazar.cyclicmagic.util.UtilEntity;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;

public class CommandHearts extends BaseCommand implements ICommand {
  public static final String name = "sethearts";
  public CommandHearts(boolean op) {
    super(name, op);
    this.setUsernameIndex(0);
  }
  @Override
  public String getUsage(ICommandSender sender) {
    return "/" + getName() + " <player> <hearts>";
  }
  @Override
  public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
    EntityPlayer ptarget = null;
    int hearts = 0;
    try {
      ptarget = super.getPlayerByUsername(server, args[0]);
      if (ptarget == null) {
        UtilChat.addChatMessage(sender, getUsage(sender));
        return;
      }
    }
    catch (Exception e) {
      UtilChat.addChatMessage(sender, getUsage(sender));
      return;
    }
    try {
      hearts = Integer.parseInt(args[1]);
    }
    catch (Exception e) {
      UtilChat.addChatMessage(sender, getUsage(sender));
      return;
    }
    if (hearts < 1) {
      hearts = 1;
    }
    int health = hearts * 2;
    IPlayerExtendedProperties prop = CapabilityRegistry.getPlayerProperties(ptarget);
    prop.setMaxHealth(health);
    UtilEntity.setMaxHealth(ptarget, health);
  }
}
