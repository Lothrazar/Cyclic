package com.lothrazar.samscommands.command;

import java.util.ArrayList;
import java.util.List; 

import com.lothrazar.samscommands.ModCommands; 

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentTranslation;  

public class CommandSearchSpawner implements ICommand
{ 
	public static boolean REQUIRES_OP;  
	
	public CommandSearchSpawner()
	{
		aliases.add("SEARCHSPAWNER");
		aliases.add("searchdungeon"); 
		aliases.add("SEARCHDUNGEON");  
		aliases.add("searchs"); 
		aliases.add("searchd"); 
	}

	@Override
	public int compareTo(Object arg0) 
	{ 
		return 0;
	}

	@Override
	public String getName() 
	{ 
		return "searchspawner";
	}

	@Override
	public String getCommandUsage(ICommandSender sender) 
	{ 
		return "/" + getName() + " [radius]";
	}

	private ArrayList<String> aliases = new ArrayList<String>();
	@Override
	public List getAliases() 
	{ 
		return aliases;
	}

	public static final int MAXRADIUS = 128;// TODO: config file for these? yes no?
	public static final int DEFAULTRADIUS = 64;
	@Override
	public void execute(ICommandSender sender, String[] args) throws CommandException 
	{ 
		EntityPlayer player = (EntityPlayer)sender;
		int radius = 0;
		if(args.length > 0)
		{
			radius = Integer.parseInt(args[0]);
		}
		
		if(radius > MAXRADIUS) { radius = MAXRADIUS; }
		if(radius <= 0 ) { radius = DEFAULTRADIUS;  }
		
		//BlockPos found = Util.findClosestBlock(player, Blocks.mob_spawner, radius);
		
		ArrayList<BlockPos> founds = ModCommands.findBlocks(player, Blocks.mob_spawner, radius);
		
		if(founds.size() == 0)
		{
			//TODO: lang file for this string
			player.addChatMessage(new ChatComponentTranslation( "None Found with radius "+radius )); 
		}
		else
		{
			String m;
			for(BlockPos found : founds) //if(found != null)
			{ 
				//"Found at : "+
				m = ModCommands.getCoordsOrReduced(player, found);//TODO: lang file?
				player.addChatMessage(new ChatComponentTranslation( m )); 
			}
		}
	}

	@Override
	public boolean canCommandSenderUse(ICommandSender ic) 
	{ 
		return (REQUIRES_OP) ? ic.canUseCommand(2, this.getName()) : true; 
	}

	@Override
	public List addTabCompletionOptions(ICommandSender sender, String[] args,BlockPos pos) 
	{ 
		return null;
	}

	@Override
	public boolean isUsernameIndex(String[] args, int index) 
	{ 
		return false;
	} 
}
