package com.lothrazar.samscommands.command;

import java.util.ArrayList;
import java.util.List; 

import com.lothrazar.samscommands.ModCommands;

import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.IMerchant;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;

public class CommandSearchTrades  implements ICommand
{
	public static boolean REQUIRES_OP;  
	private ArrayList<String> aliases = new ArrayList<String>();

	public CommandSearchTrades()
	{
		aliases.add("searcht");
		aliases.add("SEARCHTRADE"); 
		aliases.add("SEARCHT");   
	}
	@Override
	public int compareTo(Object arg0) 
	{ 
		return 0;
	}

	@Override
	public String getName() 
	{ 
		return "searchtrade";
	}

	@Override
	public String getCommandUsage(ICommandSender ic) 
	{ 
		return  "/" + getName()+" <item name> <qty>";
	}

	@Override
	public List getAliases() 
	{ 
		return aliases;
	}

	@Override
	public void execute(ICommandSender ic, String[] args) 
	{
		EntityPlayer p = (EntityPlayer)ic;
		if(args.length == 0)
		{  
			p.addChatMessage(new ChatComponentTranslation(getCommandUsage(ic))); 
			return;
		}
		
		String searching = args[0].toLowerCase();
		int searchingQty = -1;
		if(args.length > 1)
		{
			searchingQty = Integer.parseInt(args[1]);
			
			if(searchingQty < 0) {searchingQty  = 0;}
		}
		//step 1: get list of nearby villagers, seearch entities nearby in world
		
		double X = ic.getPosition().getX();//ic.getPlayerCoordinates().posX;
		double Z = ic.getPosition().getZ();//ic.getPlayerCoordinates().posZ;
		
		double range = 64;
		
		AxisAlignedBB searchRange = AxisAlignedBB.fromBounds(
				X + 0.5D - range, 0.0D, 
				Z + 0.5D - range, 
				X + 0.5D + range, 255.0D, 
				Z + 0.5D + range);
		 

		 List<Entity> merchants = ic.getEntityWorld().getEntitiesWithinAABB(EntityVillager.class, searchRange);

		 //List merchants = ic.getEntityWorld().getEntitiesWithinAABB(IMerchant.class, searchRange);
		 List<EntityLiving> villagers = new ArrayList();
		 
		 //double check that it should be an adult villager
		 //recall that 
		 // public class EntityVillager extends EntityAgeable implements INpc, IMerchant
		 for (Entity m : merchants) 
		 {
		     if(m instanceof EntityLiving && ((EntityLiving)m).isChild() == false && (IMerchant)m != null)
		     { 
		    	 villagers.add((EntityLiving)m);
		     }
		 }
		 
		 MerchantRecipeList list;
		 MerchantRecipe rec;
		 ItemStack buy;
		 ItemStack buySecond;
		 ItemStack sell;
		 String disabled, m;
		 
		 ArrayList<String> messages = new ArrayList<String>();
		 boolean match = false;
		 IMerchant v_merch;
		 EntityLiving v_entity;
		 for(int i = 0; i < villagers.size(); i++)
		 { 
			 v_entity = villagers.get(i);
			 v_merch = (IMerchant)villagers.get(i);///not null for sure based on how we constructed the list
			 
			 list = v_merch.getRecipes(p); 
			 
			 for(int r = 0; r < list.size(); r++)
			 {
				 match = false;
				 rec = (MerchantRecipe)list.get(r); 
				 disabled = (rec.isRecipeDisabled()) ? "[x] " : "";
				 
				 buy = rec.getItemToBuy();
				 buySecond = rec.getSecondItemToBuy();
				 
				 sell = rec.getItemToSell();

				 //match to any of the three possible items
				 //only match quantity if they enter one
				 
				 if(buy.getDisplayName().toLowerCase().contains(searching))
				 {
					 if(searchingQty < 0 || searchingQty == buy.stackSize)
						 match = true;
				 }
				 
				 if(buySecond != null && buySecond.getDisplayName().contains(searching))
				 {
					 if(searchingQty < 0 || searchingQty == buySecond.stackSize)
						 match = true;
				 }
				 
				 if(sell.getDisplayName().contains(searching))
				 {
					 if(searchingQty < 0 || searchingQty == sell.stackSize)
						 match = true;
				 }
				 
				 if(match)
				 {
					 m =  disabled  +
							 ModCommands.posToString(v_entity.getPosition()) + " " + 
							 sell.stackSize +" "+sell.getDisplayName()+
							 " :: "+
							 buy.stackSize+" "+buy.getDisplayName();
					 messages.add(m); 
					 
					 
					 ModCommands.spawnParticlePacketByID(((Entity)villagers.get(i)).getPosition()
							 ,EnumParticleTypes.CRIT_MAGIC.getParticleID());
					
				 }
			 } 
		 }
 
		 for(int j = 0; j < messages.size();j++)
		 {
			p.addChatMessage(new ChatComponentTranslation(messages.get(j))); 
		 }
		 
		 if(messages.size() == 0)
		 {
			p.addChatMessage(new ChatComponentTranslation("No matching trades found in nearby villagers ("+range+"m).")); 
		 } 
	}
 
	@Override
	public boolean isUsernameIndex(String[] args, int i) 
	{ 
		return false;
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
