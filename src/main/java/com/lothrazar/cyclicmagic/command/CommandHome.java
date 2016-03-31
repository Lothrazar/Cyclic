package com.lothrazar.samscommands.command;

import java.util.ArrayList;
import java.util.List;

import com.lothrazar.samscommands.ModCommands; 
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentTranslation;  
import net.minecraft.world.World;

public class CommandHome implements ICommand
{
	public static boolean REQUIRES_OP;  
	private ArrayList<String> aliases = new ArrayList<String>();

	public CommandHome()
	{ 
		this.aliases.add("HOME");  
	}
	@Override
	public int compareTo(Object arg0)
	{ 
		return 0;
	}

	@Override
	public String getName()
	{ 
		return "home";
	}

	@Override
	public String getCommandUsage(ICommandSender ic)
	{ 
		return "/" + getName();
	}

	@Override
	public List getAliases()
	{ 
		return aliases;
	}

	@Override
	public void execute(ICommandSender ic, String[] args) 	throws CommandException
	{ 
		World world = ic.getCommandSenderEntity().worldObj;
		 
		EntityPlayer player = world.getClosestPlayer(ic.getPosition().getX(), ic.getPosition().getY(), ic.getPosition().getZ(), 5);

		if(player.dimension != 0)
		{
			 player.addChatMessage(new ChatComponentTranslation("Can only teleport to your home in the overworld"));
			 return;
		}
		
		BlockPos realBedPos = ModCommands.getBedLocationSafe(world, player);
		 
		if(realBedPos != null)
		{ 
			ModCommands.teleportWallSafe(player, world, realBedPos); 
			world.playSoundAtEntity(player, "mob.endermen.portal", 1.0F, 1.0F);
		}
		else
		{
			//spawn point was set, so the coords were not null, but player broke the bed (probably recently)
			player.addChatMessage(new ChatComponentTranslation("Your home bed was missing or obstructed.")); 
		} 
	}
  
	@Override
	public boolean canCommandSenderUse(ICommandSender ic)
	{
		return (REQUIRES_OP) ? ic.canUseCommand(2, this.getName()) : true; 
	}

	@Override
	public List addTabCompletionOptions(ICommandSender ic, String[] args, BlockPos pos)
	{ 
		return null;
	}

	@Override
	public boolean isUsernameIndex(String[] args, int i)
	{ 
		return false;
	} 
}
