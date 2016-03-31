package com.lothrazar.samscommands.command;

import java.util.ArrayList;
import java.util.List; 

import com.lothrazar.samscommands.ModCommands; 
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentTranslation;
//import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;

public class CommandWorldHome  implements ICommand
{
	public static boolean REQUIRES_OP; 
	
	private ArrayList<String> aliases = new ArrayList<String>();
	public CommandWorldHome()
	{
		aliases.add("WORLDHOME");
	}
	@Override
	public int compareTo(Object o)
	{ 
		return 0;
	}

	@Override
	public String getName()
	{ 
		return "worldhome";
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
	public void execute(ICommandSender ic, String[] args)
	{
		World world = ic.getCommandSenderEntity().worldObj; 
		EntityPlayer player = world.getClosestPlayer(ic.getPosition().getX(), ic.getPosition().getY(), ic.getPosition().getZ(), 5);
 
		if(player.dimension != 0)
		{
			//TODO:"Can only teleport to worldhome in the overworld"
			 player.addChatMessage(new ChatComponentTranslation("command.worldhome.dim"));
			 return;
		}
		
		//this tends to always get something at y=64, regardless if there is AIR or not 
		//so we need to safely push the player up out of any blocks they are in
		
		ModCommands.teleportWallSafe(player, world, world.getSpawnPoint()); 
		ModCommands.playSoundAt(player,  "mob.endermen.portal");
	}
 
	@Override
	public boolean canCommandSenderUse(ICommandSender ic)
	{
		return (REQUIRES_OP) ? ic.canUseCommand(2, this.getName()) : true; 
	}
 
	@Override
	public boolean isUsernameIndex(String[] ic, int args)
	{ 
		return false;
	}

	@Override
	public List addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos)
	{ 
		return null;
	} 
}
