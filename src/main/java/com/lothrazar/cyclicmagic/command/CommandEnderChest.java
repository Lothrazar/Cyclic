package com.lothrazar.cyclicmagic.command;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;

public class CommandEnderChest implements ICommand
{
	public static boolean REQUIRES_OP; 
	private ArrayList<String> aliases = new ArrayList<String>();

	public CommandEnderChest()
	{ 
		this.aliases.add("ec"); 
		this.aliases.add(getName().toUpperCase());
	}

	@Override
	public String getName()
	{
		return "enderchest";
	}

	@Override
	public String getCommandUsage(ICommandSender icommandsender)
	{
		return "/" + getName();
	}
  
	@Override
	public void execute(ICommandSender sender, String[] args) throws CommandException
	{
		EntityPlayer p = (EntityPlayer) sender;
		p.displayGUIChest(p.getInventoryEnderChest());
	}
 
	@Override
	public boolean isUsernameIndex(String[] astring, int i)
	{
		return false;
	}

	@Override
	public int compareTo(Object o)
	{
		return 0;
	}
  
	@Override
	public List getAliases()
	{
		return this.aliases;
	}
  
	@Override
	public boolean canCommandSenderUse(ICommandSender ic)
	{
		return (REQUIRES_OP) ? ic.canUseCommand(2, this.getName()) : true; 
	}

	@Override
	public List addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos)
	{ 
		return null;
	}
}
