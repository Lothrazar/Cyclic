package com.lothrazar.cyclicmagic.command;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;

public class CommandEnderChest extends BaseCommand implements ICommand
{
	public static boolean REQUIRES_OP; 
	private ArrayList<String> aliases = new ArrayList<String>();

	public CommandEnderChest(String n, boolean op)
	{ 
		super(n, op);
		this.aliases.add("ec"); 
		this.aliases.add(getCommandName().toUpperCase());
	}

	@Override
	public String getCommandName()
	{
		return "enderchest";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
	{
		EntityPlayer p = (EntityPlayer) sender;
		p.displayGUIChest(p.getInventoryEnderChest());
	}
 
	@Override
	public List<String> getCommandAliases()
	{
		return this.aliases;
	}
  
	@Override
	public boolean checkPermission(MinecraftServer server, ICommandSender ic)
	{
		return (REQUIRES_OP) ? super.checkPermission(server, ic) : true; 
	} 
}
