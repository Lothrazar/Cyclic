package com.lothrazar.cyclicmagic.command;

import java.util.ArrayList;
import java.util.List; 
import com.lothrazar.cyclicmagic.util.UtilChat;
import com.lothrazar.cyclicmagic.util.UtilNBT;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

public class CommandKit extends BaseCommand implements ICommand
{ 
	public static boolean REQUIRES_OP;
	//public static ArrayList<String> contents = new ArrayList<String>();
	private static ArrayList<Item> giveItems = new ArrayList<Item>();
	private static String giveItemsFromConfig = "";
	
	private ArrayList<String> aliases = new ArrayList<String>();

	public CommandKit(String n, boolean op){

		super(n, op);
		this.aliases.add("KIT");  
		giveItems.add(Items.wooden_axe);
		giveItems.add(Items.wooden_shovel);
	}

	@Override
	public List getCommandAliases()
	{ 
		return aliases;
	}

	@Override
	public void execute(MinecraftServer server,ICommandSender sender, String[] args) throws CommandException
	{ 
		EntityPlayer p = (EntityPlayer)sender;
		  
		int kitsUsed = p.getEntityData().getInteger(getCommandName()); 
		 
		if(kitsUsed == 0) //has the player used this already (in this life)
		{
			System.out.println("TODO: pull item list from config");
			//translateCSV();
			for(Item item : giveItems) //these were decided by the config file
			{
				p.dropItem(new ItemStack( item ), true, false);
			}
			
			//set the flag so we cannot run this again (unless we die)
			UtilNBT.incrementPlayerIntegerNBT(p, getCommandName(),1);
		}
		else
		{
			 UtilChat.addChatMessage(p,"You can only get one kit each time you die.");
		} 
	}
 /*
  //TODO: CONFIG
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
	*/
}
