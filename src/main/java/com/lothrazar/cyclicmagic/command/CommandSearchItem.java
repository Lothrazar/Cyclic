package com.lothrazar.samscommands.command;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;    

import com.lothrazar.samscommands.ModCommands; 
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumParticleTypes;

public class CommandSearchItem  implements ICommand
{
	private ArrayList<String> aliases = new ArrayList<String>();  
	public static boolean REQUIRES_OP; 
	
	public CommandSearchItem()
	{
		aliases.add(getName().toUpperCase());
		aliases.add("searchi");
		aliases.add("SEARCHI");
	}
	 
	@Override
	public boolean canCommandSenderUse(ICommandSender ic)
	{
		return (REQUIRES_OP) ? ic.canUseCommand(2, this.getName()) : true; 
	}
	  
	@Override
	public List getAliases() 
	{  
		return aliases;
	}

	@Override
	public String getName() 
	{ 
		return "searchitem";
	}

	@Override
	public String getCommandUsage(ICommandSender arg0) 
	{ 
		return "/" + getName()+" <itemname> [radius]"; 
	}

	@Override
	public boolean isUsernameIndex(String[] arg0, int arg1) 
	{ 
		return false;
	}
  
	@Override
	public void execute(ICommandSender sender, String[] args) 
	{ 
		if (!(sender instanceof EntityPlayerMP)) {return;}
			
		EntityPlayerMP player = (EntityPlayerMP) sender;
		if (args.length < 1)
		{
			player.addChatMessage(new ChatComponentTranslation(getCommandUsage(sender))); 
			return;
		}
		
		int radius = 0;
		if(args.length > 1)
		{
			radius = Integer.parseInt(args[1]);
		}
		
		if(radius > 128) { radius = 128; }//Maximum // 
		if(radius <= 0 ) { radius = 64;  }//default
		
		String searchQuery = args[0].trim().toLowerCase(); // args[0] is the command name or alias used such as "is"
		ArrayList<IInventory> tilesToSearch = new ArrayList<IInventory>();
		HashMap<IInventory,BlockPos> dictionary = new HashMap<IInventory,BlockPos> ();
		IInventory tile;
		
		ArrayList<BlockPos> foundChests = ModCommands.findBlocks(player, Blocks.chest, radius); 
		for (BlockPos pos : foundChests)
		{  	
			tile = (IInventory)player.worldObj.getTileEntity(pos);  
			tilesToSearch.add(tile); 
			dictionary.put(tile, pos);
		}
		ArrayList<BlockPos> foundTrapChests = ModCommands.findBlocks(player, Blocks.trapped_chest, radius); 
		for (BlockPos pos : foundTrapChests)
		{  	
			tile = (IInventory)player.worldObj.getTileEntity(pos);  
			tilesToSearch.add(tile); 
			dictionary.put(tile, pos);
		}
		ArrayList<BlockPos> foundDisp = ModCommands.findBlocks(player, Blocks.dispenser, radius); 
		for (BlockPos pos : foundDisp)
		{  	
			tile = (IInventory)player.worldObj.getTileEntity(pos);  
			tilesToSearch.add(tile); 
			dictionary.put(tile, pos);
		}
		
		int foundQtyTotal;
		ArrayList<String> foundMessages = new ArrayList<String>();
		
		for(IInventory inventory : tilesToSearch)
		{ 
			foundQtyTotal = 0;
			foundQtyTotal = searchTileInventory(searchQuery, inventory);
			
			if(foundQtyTotal > 0)
			{ 
				String totalsStr = foundQtyTotal + " found : "; 
				
				//TODO we COULD have configs for each of these, that is, one config flag for SEND_CHAT
				//and one for SEND_PARTICLES
				ModCommands.spawnParticlePacketByID(dictionary.get(inventory),EnumParticleTypes.CRIT_MAGIC.getParticleID());
				
				foundMessages.add(totalsStr + ModCommands.getCoordsOrReduced(player,dictionary.get(inventory)));
			}  
		}
  
		int ifound = foundMessages.size();
		
		if(ifound == 0)  
		{ 
			player.addChatMessage(new ChatComponentTranslation("No items found within " + radius + " blocks of you."));
		}
		else
		{ 
			for (int i = 0; i < ifound; i++) 
			{  
				player.addChatMessage(new ChatComponentTranslation(foundMessages.get(i)));
		    }
		}
	}

	public static String getCoordsOrReduced(EntityPlayer player, BlockPos pos)
	{
		boolean showCoords = !player.worldObj.getGameRules().getGameRuleBooleanValue("reducedDebugInfo");
		
		if(showCoords)
			return pos.getX() + ", " + pos.getY() + ", " + pos.getZ();
		else
			return ModCommands.getDirectionsString(player, pos); 
	}
	private int searchTileInventory(String search, IInventory inventory) 
	{
		int foundQty;
		foundQty = 0; 
		
		for (int slot = 0; slot < inventory.getSizeInventory(); slot++)//a break; will cancel this loop
		{
			ItemStack invItem = inventory.getStackInSlot(slot);
			if(invItem == null){continue;} //empty slot in chest (or container)
			 
			String invItemName = invItem.getDisplayName().toLowerCase(); 
			
			//find any overlap: so if x ==y , or if x substring of y, or y substring of x 
			if(search.equals(invItemName) 
				|| search.contains(invItemName)
				|| invItemName.contains(search))
			{  
				foundQty += invItem.stackSize; 
			} 
		} //end loop on current tile entity
		return foundQty;
	}
	
	@Override
	public int compareTo(Object arg0)
	{ 
		return 0;
	}
 
	@Override
	public List addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos)
	{ 
		return null;
	}
}
