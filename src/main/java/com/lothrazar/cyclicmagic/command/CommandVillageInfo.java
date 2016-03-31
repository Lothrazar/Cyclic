package com.lothrazar.samscommands.command;

import java.util.ArrayList;
import java.util.List; 

import com.lothrazar.samscommands.ModCommands;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.village.Village;
import net.minecraft.world.World;

public class CommandVillageInfo implements ICommand
{
	public static boolean REQUIRES_OP; 
	private ArrayList<String> aliases = new ArrayList<String>();
//5852309458819775221
	//is a seed with village at spawn; for testing
	public CommandVillageInfo()
	{ 
		this.aliases.add("ec"); 
		this.aliases.add(getName().toUpperCase());
	}

	@Override
	public String getName()
	{
		return "villageinfo";
	}

	@Override
	public String getCommandUsage(ICommandSender icommandsender)
	{
		return "/" + getName();
	}
  
	@Override
	public void execute(ICommandSender sender, String[] args) throws CommandException
	{
		BlockPos pos = sender.getPosition();
		World world = sender.getEntityWorld();
		
		int dX,dZ;
		 
		int range = 64;

		Village closest = world.villageCollectionObj.getNearestVillage(pos, range);
		 
		if(closest == null)
		{
			ModCommands.addChatMessage(sender,ModCommands.lang("debug.novillage"));
		}
		else
		{ 
			int doors = closest.getNumVillageDoors();
		    int villagers = closest.getNumVillagers();
		    
		    ModCommands.addChatMessage(sender,ModCommands.lang("debug.villagepop")+"  "+String.format("%d",villagers));
		    ModCommands.addChatMessage(sender,ModCommands.lang("debug.villagedoors")+"  "+String.format("%d",doors));
		   
		    if(sender instanceof EntityPlayer)
		    {
		    	//command blocks/server controllers do not have reputation
		    	EntityPlayer player = (EntityPlayer)sender;
			    int rep = closest.getReputationForPlayer(player.getName());
			    
			    ModCommands.addChatMessage(sender,player.getName()+" "+ModCommands.lang("debug.villagerep")+"  "+String.format("%d",rep));
		    }
            
		    dX = pos.getX() - closest.getCenter().getX();
		    dZ = pos.getZ() - closest.getCenter().getZ();
		    
		    int dist = MathHelper.floor_double(Math.sqrt( dX*dX + dZ*dZ));
            
		    ModCommands.addChatMessage(sender,ModCommands.lang("debug.villagedistcenter")+"  "+String.format("%d", dist)); 
		}
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
