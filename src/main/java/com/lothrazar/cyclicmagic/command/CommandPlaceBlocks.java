package com.lothrazar.samscommands.command;

import java.util.ArrayList;
import java.util.List; 

import com.lothrazar.samscommands.ModCommands; 
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;

public class CommandPlaceBlocks implements ICommand
{
	public static boolean REQUIRES_OP=false;   
	private ArrayList<String> aliases = new ArrayList<String>();
	public static int VERTICAL_MAX = 5; //TODO from config file 
	
	public CommandPlaceBlocks()
	{
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
		return "place";
	}

	@Override
	public String getCommandUsage(ICommandSender sender) 
	{
		return "/"+getName() + "<line|stair|floor|circle> <dist|radius> [skip] [voffset]";
	}

	@Override
	public List getAliases() 
	{
		return aliases;
	}
	
	@Override
	public void execute(ICommandSender sender, String[] args)	throws CommandException 
	{
		if(PlaceLib.canSenderPlace(sender) == false) {return;}

		EntityPlayer player = (EntityPlayer)sender;
		ItemStack held = player.inventory.getCurrentItem();
		
		IBlockState placing = Block.getBlockFromItem(held.getItem()).getStateFromMeta(held.getMetadata());

		if(args.length == 0)
		{ 
			ModCommands.addChatMessage(player, getCommandUsage(sender));
			return;
		}
		
		String type = args[0];

		int distOrRadius = 0;//is required
       
        try
		{
        	distOrRadius = Integer.parseInt(args[1]);// Math.min(Integer.parseInt(args[1]), held.stackSize);
		}
		catch (Exception e)
		{
			ModCommands.addChatMessage(player, getCommandUsage(sender));
			return;
		}

        int skip = 1;
        if(args.length > 2 && args[2] != null)
        {
            try
    		{
            	skip =  Math.max(Integer.parseInt(args[2]), 1);
    		}
    		catch (NumberFormatException e)
    		{
    			ModCommands.addChatMessage(player, getCommandUsage(sender));
    			return;
    		}
        }
		int vertOffset = 0;//is required
		if(args.length > 3 && args[3] != null)
        {
	        try
			{
	        	vertOffset =  Math.min(Integer.parseInt(args[3]), held.stackSize);
			}
			catch (NumberFormatException e)
			{
				ModCommands.addChatMessage(player, getCommandUsage(sender));
    			return;
			}
        }
		
		BlockPos startPos = player.getPosition();

		if( vertOffset > VERTICAL_MAX ||
			vertOffset < VERTICAL_MAX*-1) 
		{
			ModCommands.addChatMessage(player, ModCommands.lang("command.place.vertical"));
			return;
		}
		
		if(vertOffset > 0) startPos = startPos.up(vertOffset);
		if(vertOffset < 0) startPos = startPos.down(vertOffset*-1);//so use puts -2 means go down 2
		
		if(type.equalsIgnoreCase("line"))
		{
			PlaceLib.line(player.worldObj, player, startPos, placing, distOrRadius, skip);//,vertOffset
		}
		if(type.equalsIgnoreCase("stair"))
		{ 
			PlaceLib.stairway(player.worldObj, player, startPos, placing, distOrRadius);//, skip,vertOffset
		}
		if(type.equalsIgnoreCase("floor"))
		{
			PlaceLib.square(player.worldObj, player, startPos, placing, distOrRadius);//, skip,vertOffset
		}
		if(type.equalsIgnoreCase("circle"))
		{
			PlaceLib.circle(player.worldObj, player, startPos, placing, distOrRadius);//, skip,vertOffset
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
}
