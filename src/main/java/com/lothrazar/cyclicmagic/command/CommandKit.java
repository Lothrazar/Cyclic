package com.lothrazar.samscommands.command;

import java.util.ArrayList;
import java.util.List; 
import com.lothrazar.samscommands.ModCommands; 
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentTranslation;

public class CommandKit implements ICommand
{ 
	public static boolean REQUIRES_OP;
	//public static ArrayList<String> contents = new ArrayList<String>();
	private static ArrayList<Item> giveItems = new ArrayList<Item>();
	private static String giveItemsFromConfig = "";
	
	private ArrayList<String> aliases = new ArrayList<String>();

	public CommandKit()
	{ 
		this.aliases.add("KIT");  
	}
	@Override
	public int compareTo(Object arg0)
	{ 
		return 0;
	}

	@Override
	public String getName()
	{ 
		return "kit";
	}

	@Override
	public String getCommandUsage(ICommandSender sender)
	{ 
		return "/" + getName();
	}

	@Override
	public List getAliases()
	{ 
		return aliases;
	}

	@Override
	public void execute(ICommandSender sender, String[] args) throws CommandException
	{ 
		EntityPlayer p = (EntityPlayer)sender;
		  
		int kitsUsed = p.getEntityData().getInteger(getName()); 
		 
		if(kitsUsed == 0) //has the player used this already (in this life)
		{
			translateCSV();
			for(Item item : giveItems) //these were decided by the config file
			{
				p.dropItem(new ItemStack( item ), true, false);
			}
			
			//set the flag so we cannot run this again (unless we die)
			ModCommands.incrementPlayerIntegerNBT(p, getName(),1);
		}
		else
		{
			 p.addChatMessage(new ChatComponentTranslation("You can only get one kit each time you die."));
		} 
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

	@Override
	public boolean isUsernameIndex(String[] args, int index)
	{ 
		return false;
	}

	private static void translateCSV()
	{
		//do this on the fly, could be items not around yet during config change
		if(giveItems.size() == 0)
			giveItems = ModCommands.getItemListFromCSV(giveItemsFromConfig); 
	}
	
	public static void setItemsFromString(String csv)
	{ 
		giveItemsFromConfig = csv;
	} 
}
