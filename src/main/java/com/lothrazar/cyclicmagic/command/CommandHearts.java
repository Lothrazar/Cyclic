package com.lothrazar.samscommands.command;

import java.util.ArrayList;
import java.util.List;
import com.lothrazar.samscommands.ModCommands; 
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;

public class CommandHearts implements ICommand
{
	private static boolean REQUIRES_OP = true; // always
	private ArrayList<String> aliases = new ArrayList<String>();

	@Override
	public int compareTo(Object arg0)
	{
		return 0;
	}

	@Override
	public String getName()
	{
		return "sethearts";
	}

	@Override
	public String getCommandUsage(ICommandSender sender)
	{
		return "/"+getName()+" <player> <hearts>";
	}

	@Override
	public List getAliases()
	{
		return aliases;
	}

	@Override
	public void execute(ICommandSender sender, String[] args)		throws CommandException
	{ 
		EntityPlayer ptarget  = null;
		
		int hearts = 0;
		try
		{
			ptarget = MinecraftServer.getServer().getConfigurationManager().getPlayerByUsername(args[0]);
			
			if(ptarget == null)
			{
				ModCommands.addChatMessage(sender,getCommandUsage(sender));
				return;
			}
		}
		catch (Exception e)
		{
			ModCommands.addChatMessage(sender,getCommandUsage(sender));
			return;
		}
		try
		{
			hearts = Integer.parseInt(args[1]);
		}
		catch (Exception e)
		{
			ModCommands.addChatMessage(sender,getCommandUsage(sender));
			return;
		}
		
		if(hearts < 1) {hearts = 1;}
		
		ModCommands.setMaxHealth(ptarget, hearts*2);
	}

	@Override
	public boolean canCommandSenderUse(ICommandSender sender)
	{ 
		return (REQUIRES_OP) ? sender.canUseCommand(2, this.getName()) : true; 
	}

	@Override
	public List addTabCompletionOptions(ICommandSender sender, String[] args,	BlockPos pos)
	{
		return null;
	}

	@Override
	public boolean isUsernameIndex(String[] args, int index)
	{
		return false;
	}

}
