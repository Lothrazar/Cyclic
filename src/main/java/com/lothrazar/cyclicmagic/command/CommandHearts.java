package com.lothrazar.cyclicmagic.command;

import com.lothrazar.cyclicmagic.util.UtilChat;
import com.lothrazar.cyclicmagic.util.UtilEntity;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;

public class CommandHearts extends BaseCommand implements ICommand
{
	public CommandHearts(String n, boolean op){

		super(n, op);
	}

	@Override
	public String getCommandUsage(ICommandSender sender)
	{
		return "/"+getCommandName()+" <player> <hearts>";
	}

	@Override
	public void execute(MinecraftServer server,ICommandSender sender, String[] args)		throws CommandException
	{ 
		EntityPlayer ptarget  = null;
		
		int hearts = 0;
		try
		{
			ptarget = super.getPlayerByUsername(server,args[0]);
			
			if(ptarget == null)
			{
				UtilChat.addChatMessage(sender,getCommandUsage(sender));
				return;
			}
		}
		catch (Exception e)
		{
			UtilChat.addChatMessage(sender,getCommandUsage(sender));
			return;
		}
		try
		{
			hearts = Integer.parseInt(args[1]);
		}
		catch (Exception e)
		{
			UtilChat.addChatMessage(sender,getCommandUsage(sender));
			return;
		}
		
		if(hearts < 1) {hearts = 1;}
		
		UtilEntity.setMaxHealth(ptarget, hearts*2);
	}
}
