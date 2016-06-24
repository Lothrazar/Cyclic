package com.lothrazar.cyclicmagic.command;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;

public class CommandEnderChest extends BaseCommand implements ICommand {
  public static final String name = "enderchest";
  public CommandEnderChest(boolean op) {
    super(name, op);
    this.aliases.add("ec");
  }
  @Override
  public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
    if (sender instanceof EntityPlayer == false) { return; }
    EntityPlayer p = (EntityPlayer) sender;
    p.displayGUIChest(p.getInventoryEnderChest());
  }
}
