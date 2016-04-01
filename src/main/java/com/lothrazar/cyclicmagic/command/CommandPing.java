package com.lothrazar.cyclicmagic.command;

import java.util.ArrayList;
import java.util.List;
import com.lothrazar.cyclicmagic.util.UtilChat;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

public class CommandPing extends BaseCommand implements ICommand
{
	public CommandPing(String n, boolean op){

		super(n, op);
	}
 
	@Override
	public String getCommandUsage(ICommandSender sender)
	{
		return "/"+getCommandName() + "[nether]";
	}

	@Override
	public void execute(MinecraftServer server,ICommandSender sender, String[] args)	throws CommandException
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
			
			//TODO: ModCommands.posToString(n)
			UtilChat.addChatMessage(player, n.toString());
			
			return;
		}
		
		UtilChat.addChatMessage(player, player.getPosition().toString());
	}
}
