package com.lothrazar.cyclicmagic.command;
/*
import java.util.ArrayList;
import com.lothrazar.cyclicmagic.util.UtilChat;
import com.lothrazar.cyclicmagic.util.UtilPlaceBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
*/
public class CommandPlaceBlocks //extends BaseCommand implements ICommand
{
	/*
	public static int VERTICAL_MAX = 5; //TODO from config file 
	
	public CommandPlaceBlocks(String n, boolean op){

		super(n, op);//"place"
	}

	@Override
	public String getCommandUsage(ICommandSender sender) 
	{
		return "/"+getCommandName() + "<line|stair|floor|circle> <dist|radius> [skip] [voffset]";
	}

	@Override
	public void execute(MinecraftServer server,ICommandSender sender, String[] args)	throws CommandException 
	{
		if(canSenderPlace(sender) == false) {return;}

		EntityPlayer player = (EntityPlayer)sender;
		ItemStack held = player.inventory.getCurrentItem();
		
		IBlockState placing = Block.getBlockFromItem(held.getItem()).getStateFromMeta(held.getMetadata());

		if(args.length == 0)
		{ 
			UtilChat.addChatMessage(player, getCommandUsage(sender));
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
			UtilChat.addChatMessage(player, getCommandUsage(sender));
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
    			UtilChat.addChatMessage(player, getCommandUsage(sender));
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
				UtilChat.addChatMessage(player, getCommandUsage(sender));
    			return;
			}
        }
		
		BlockPos startPos = player.getPosition();

		if( vertOffset > VERTICAL_MAX ||
			vertOffset < VERTICAL_MAX*-1) 
		{
			UtilChat.addChatMessage(player, "command.place.vertical");
			return;
		}
		
		if(vertOffset > 0) startPos = startPos.up(vertOffset);
		if(vertOffset < 0) startPos = startPos.down(vertOffset*-1);//so use puts -2 means go down 2
		
		if(type.equalsIgnoreCase("line"))
		{
			//UtilPlaceBlocks.line(player.worldObj, player, startPos, placing, distOrRadius, skip);//,vertOffset
		}
		if(type.equalsIgnoreCase("stair"))
		{ 
			UtilPlaceBlocks.stairway(player.worldObj, player, startPos, placing, distOrRadius);//, skip,vertOffset
		}
		if(type.equalsIgnoreCase("floor"))
		{
			UtilPlaceBlocks.square(player.worldObj, player, startPos, placing, distOrRadius);//, skip,vertOffset
		}
		if(type.equalsIgnoreCase("circle"))
		{
			UtilPlaceBlocks.circle(player.worldObj, player, startPos, placing, distOrRadius);//, skip,vertOffset
		}
    }
	
	
	//library of functions/configs that apply to all /place[] commands
		//for all of these, we allow the player to be null
		
		//TODO: should xp cost be here as well?
		public static ArrayList<Block> allowed = new ArrayList<Block>();
		public static String allowedFromConfig = "";
		//public static int XP_COST_PER_PLACE;

		public static boolean canSenderPlace(ICommandSender sender)
		{
			EntityPlayer player = (EntityPlayer)sender;
			
			if(player == null){return false;}//was sent by command block or something, ignore it
			
			if(player.inventory.getCurrentItem() == null || player.inventory.getCurrentItem().stackSize == 0)
			{
				UtilChat.addChatMessage(player, "command.place.empty"); 
				return false;
			}
			Block pblock = Block.getBlockFromItem(player.inventory.getCurrentItem().getItem());

			if(pblock == null)
			{
				UtilChat.addChatMessage(player, "command.place.empty"); 
				return false;
			}
				
			if(isAllowed(pblock) == false)
			{ 
				UtilChat.addChatMessage(player, "command.place.notallowed"); 
				return false;
			}
			
			return true;
		}
		
		public static void translateCSV()
		{
			//do this on the fly, could be items not around yet during config change
			System.out.println("placelib getBlockListFromCSV");
			
			//if(PlaceLib.allowed.size() == 0)
			//	PlaceLib.allowed = ModCommands.getBlockListFromCSV(PlaceLib.allowedFromConfig); 
			
		}         

		public static boolean isAllowed(Block pblock)
		{
			translateCSV();
			
			return allowed.size() == 0 || allowed.contains(pblock);
		}
	*/
	
}
