package com.lothrazar.samscommands.command;

import java.util.ArrayList;
import java.util.List;

import com.lothrazar.samscommands.ModCommands; 
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;

public class CommandPing implements ICommand
{
	public static boolean REQUIRES_OP;  //TODO:CONFIG
	private ArrayList<String> aliases = new ArrayList<String>();

	public CommandPing()
	{
		this.aliases.add(getName().toUpperCase());
	}

	@Override
	public int compareTo(Object arg0)
	{
		return 0;
	}

	@Override
	public String getName()
	{
		return "ping";
	}

	@Override
	public String getCommandUsage(ICommandSender sender)
	{
		return "/"+getName() + "[nether]";
	}

	@Override
	public List getAliases()
	{
		return aliases;
	}

	@Override
	public void execute(ICommandSender sender, String[] args)	throws CommandException
	{
		EntityPlayer player = (EntityPlayer)sender;
		if(player == null){return;}
		
		if(args.length > 0 && args[0] != null && args[0].equalsIgnoreCase("nether"))
		{
			BlockPos p = player.getPosition();
			//force doubles, otherwise int rounding makes it act like _/10
			double netherRatio = 8.0;
			double x = p.getX();
			double z = p.getZ();
			BlockPos n = new BlockPos(x/netherRatio,p.getY(),z/netherRatio);
			
			ModCommands.addChatMessage(player, ModCommands.posToString(n));
			
			return;
		}
		
		ModCommands.addChatMessage(player, ModCommands.posToString(player.getPosition()));
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
