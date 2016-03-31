package com.lothrazar.samscommands.command;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.lothrazar.samscommands.ModCommands;

import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentTranslation; 
import net.minecraftforge.common.DimensionManager;

public class CommandTodoList implements ICommand
{   
	public static boolean REQUIRES_OP = false; 

	private ArrayList<String> aliases = new ArrayList<String>(); 

	public CommandTodoList()
	{  
	    this.aliases.add(getName().toUpperCase());   
	}

	private static String MODE_ADD = "add"; 
	private static String MODE_REMOVE = "delete"; 
	private static String MODE_SET = "set";
	private static String MODE_GET = "get";
	private static String NBT_KEY = ModCommands.MODID+"_todo";

	public static boolean PERSIST_DEATH;

	@Override
	public String getCommandUsage(ICommandSender s) 
	{ 
		return "/" + getName()+" <"+MODE_GET +"|"+MODE_SET + "|" +MODE_ADD + "|" +MODE_REMOVE+  "> <text>";
	}
   
	
	public static String getTodoForPlayer(EntityPlayer player)
	{
		String todoCurrent = player.getEntityData().getString(NBT_KEY);

		if(todoCurrent == null) todoCurrent = "";
		
		return todoCurrent;
	}
	
	public static void setTodoForPlayer(EntityPlayer player, String todoCurrent)
	{
		player.getEntityData().setString(NBT_KEY,todoCurrent);
	}
	
	@Override
	public void execute(ICommandSender icommandsender, String[] args)
	{ 
		EntityPlayer player = (EntityPlayer)icommandsender; 
  
		String todoCurrent = getTodoForPlayer(player );
 
		 //is the first argument empty
		 if(args == null || args.length == 0 || args[0] == null || args[0].isEmpty())
		 {
			// player.addChatMessage(new ChatComponentTranslation(getCommandUsage(icommandsender))); 

			 ModCommands.addChatMessage(player,getCommandUsage(icommandsender));
			 
			 return; 
		 }
		 
		 String message = "";

		 if(args[0].equals(MODE_GET))
		 { 
			//just display current in chat
			 ModCommands.addChatMessage(player,getTodoForPlayer(player));
		 } 
		 else if(args[0].equals(MODE_REMOVE))
		 { 
			 todoCurrent = "";
			 args[0] = "";//remove the plus sign 
		 } 
		 else if(args[0].equals(MODE_ADD))
		 {
			 for(int i = 1; i < args.length; i++)
			 {
				 message += " " + args[i];
			 } 
			 
			 if(todoCurrent.isEmpty()) todoCurrent = message;
			 else todoCurrent += " " + message;//so append
		 }
		 else if(args[0].equals(MODE_SET)) 
		 {
			 //they just did /todo blah blah
			 for(int i = 1; i < args.length; i++)
			 {
				 message += " " + args[i];
			 } 
			 
			 todoCurrent = message;//so replace
		 }
 
		 setTodoForPlayer(player, todoCurrent); 
	}
	 
 
	@Override
	public List getAliases() 
	{ 
		return aliases;
	}
	 

	@Override
	public String getName() 
	{ 
		return "todo";
	}

	@Override
	public int compareTo(Object arg0)
	{ 
		return 0;
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
}



