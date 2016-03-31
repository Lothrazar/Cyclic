package com.lothrazar.samscommands.command;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;     

import com.lothrazar.samscommands.*; 

import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer; 
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.DimensionManager;

public class CommandSimpleWaypoints  implements ICommand
{
	public static boolean REQUIRES_OP; 
	public static boolean ENABLE_TP;
	private static String NBT_KEY = ModCommands.MODID+"_swp";
	private ArrayList<String> aliases = new ArrayList<String>();

	public CommandSimpleWaypoints()
	{  
		this.aliases.add("simplewaypoint"); 
		this.aliases.add("swp"); 
		this.aliases.add("SWP");
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
		return "simplewp";
	}

	@Override
	public String getCommandUsage(ICommandSender p_71518_1_) 
	{  
		String tp = ENABLE_TP ?  "|" + MODE_TP : "";
		// + "|" + MODE_HIDEDISPLAY
		return "/" + getName()+" <"+MODE_LIST + "|" + MODE_SAVE + "|"  +MODE_CLEAR + tp + "> [displayname | showindex]";
	}

	@Override
	public List getAliases() 
	{ 
		return this.aliases;
	}

	private static String MODE_TP = "tp"; 
	//private static String MODE_DISPLAY = "get"; 
	//private static String MODE_HIDEDISPLAY = "hide";
	private static String MODE_LIST = "list";
	private static String MODE_SAVE = "save";
	private static String MODE_CLEAR = "delete"; 
	//private static String KEY_CURRENT = "simplewp_current";
	public static boolean PERSIST_DEATH;
	
	@Override
	public void execute(ICommandSender icommandsender, String[] args) 
	{  
		EntityPlayer p = (EntityPlayer)icommandsender;
		
		if(args == null || args.length == 0 || args[0] == null || args[0].length() == 0)
		{ 
			p.addChatMessage(new ChatComponentTranslation(getCommandUsage(icommandsender))); 
	 
			return;//not enough args
		}
 
		if(args[0].equals(MODE_LIST))
		{
			executeList(p);
			return;
		} 
 
		if(args[0].equals(MODE_SAVE))
		{
			String n = "";
			if(args.length > 1) n = args[1];
			executeSave(p, n);
			return;
		} 
 
		 //so its outside the scope of commands that do not have a number
		int index = -1;
		
		try{
		index = Integer.parseInt(args[1]);//TODO: trycatch on this , it might not be integer
		}
		catch(Exception e)
		{
			p.addChatMessage(new ChatComponentTranslation(getCommandUsage(icommandsender))); 
			return;
		}
		if(index <= 0 ) //invalid number, or int parse failed
		{
			// ZERO NOT ALLOWED
			p.addChatMessage(new ChatComponentTranslation(getCommandUsage(icommandsender))); 
			return;
		}
		
		if(args[0].equals(MODE_CLEAR))
		{
			executeClear(p, index);
			return;
		} 
		
		if(ENABLE_TP && args[0].equals(MODE_TP))
		{
			executeTp(p, index);
			return;
		} 

		//if nothing else, as not matched anything:
		//then this is like the default case in a switch statement
		p.addChatMessage(new ChatComponentTranslation(getCommandUsage(icommandsender))); 
	}
	
	private void executeSave(EntityPlayer p, String name) 
	{ 
		ArrayList<String> lines = getForPlayer(p);
		
		
		if(name == null) name = "";
		
		//never putr a loc in index zero
		
		if(lines.size() == 0) lines.add("0");
		
		Location here = new Location(lines.size(), p , name);
		
		lines.add( here.toCSV());
		 
		overwriteForPlayer(p,lines);
	} 
/*
	private void executeHide(EntityPlayer p) 
	{
		ArrayList<String> lines = getForPlayer(p);
		
		if(lines.size() < 1){return;}
		lines.set(0,"0");
		overwriteForPlayer(p,lines); 
	}*/
//	public static int EXP_COST_TP = 100;//TODO: CONFIG
	private void executeTp(EntityPlayer player,int index) 
	{
		/*if(ModMain.getExpTotal(player) < EXP_COST_TP)
		{
			ModMain.addChatMessage(player, ModMain.lang("waypoints.tp.exp")+EXP_COST_TP);
			return;
		}
		*/
		Location loc = getSingleForPlayer(player,index);

		//System.out.println("try and teleport to loc "+index);
		
		
		if(loc == null)
		{
			ModCommands.addChatMessage(player, "waypoints.tp.notfound");
		}
		else
		{
			if(player.dimension != loc.dimension)
			{
				ModCommands.addChatMessage(player, "waypoints.tp.dimension");
			}
			else
			{
				ModCommands.teleportWallSafe(player, player.worldObj, new BlockPos(loc.X,loc.Y,loc.Z));
				//TODO:
				//ModMain.drainExp(player, EXP_COST_TP);
			}
		}
	}
	private void executeClear(EntityPlayer p, int index) 
	{
		ArrayList<String> lines = getForPlayer(p);

		ArrayList<String> newLines = new ArrayList<String>();
		
		int i = 0;
		for(String line : lines)
		{
			if(i == index)
			{
				i++;
				continue;//skip this line
			}
			i++;

			
			newLines.add(line);
		}
	 
		newLines.set(0,"0");
		
		overwriteForPlayer(p,newLines);
	}

	private void executeList(EntityPlayer p) 
	{ 
		boolean showCoords = !p.worldObj.getGameRules().getGameRuleBooleanValue("reducedDebugInfo");
		
		ArrayList<String> lines = getForPlayer(p);
		
		int i = 0;
		String msg = "";
		Location loc;
		
		for(String line : lines)
		{ 
			if(i == 0){i++;continue;}//just a weird bug that happens, since we index by 1
			
			if(line == null || line.isEmpty()) {continue;}
			msg = EnumChatFormatting.WHITE+"";//overworld and all other dimensions
			loc = new Location(line);
			
		 
			if(loc.dimension == 1)//END
				msg = EnumChatFormatting.DARK_PURPLE+"";
			else if(loc.dimension == -1)//NETHER
				msg = EnumChatFormatting.RED+"";
			
			msg += "<" + i + "> ";
			
			if(showCoords)  
				msg += loc.toDisplay();
			else
				msg += loc.name;
				
			p.addChatMessage(new ChatComponentTranslation(msg)); 
			
			i++;
		}
	}
	 
	@Override
	public boolean isUsernameIndex(String[] p_82358_1_, int p_82358_2_) 
	{ 
		return false;
	} 
	 /* 
	private void SetCurrentForPlayer(EntityPlayer player, int current)
	{
		//String playerName = player.getDisplayName().getUnformattedText();
		
		ArrayList<String> lines = getForPlayer(player);
		
		lines.set(0, current+"");//overwrite the current index
 
		overwriteForPlayer(player, lines);
	}*/
 
	public static void overwriteForPlayer(EntityPlayer player, ArrayList<String> lines)
	{ 
		String csv = "";
		for(String line : lines) 
		{ 
			csv += line + System.lineSeparator();
		}
		player.getEntityData().setString(NBT_KEY,csv);
		 
	}
	public static Location getSingleForPlayer(EntityPlayer player, int index)
	{
		if(index <= 0){return null;}
		
		
		ArrayList<String> saved = getForPlayer(player);//.getDisplayName().getUnformattedText()


		if(saved.size() <= index) {return null;}

    	//loc = getSingleForPlayer(p,index);
    	
		String sloc = saved.get(index);
		
		if(sloc == null || sloc.isEmpty()) {return null;}

		Location loc = null;
		
		if( index < saved.size() && saved.get(index) != null) 
		{
			loc = new Location(sloc);//this still might be null..?
		}
		
		
		return loc;
	}
	public static ArrayList<String> getForPlayer(EntityPlayer player)
	{ 
		ArrayList<String> lines = new ArrayList<String>();
 
		String csv = player.getEntityData().getString(NBT_KEY);
	 
		lines = new ArrayList<String>(Arrays.asList(csv.split(System.lineSeparator())));
	 
		return lines;
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
