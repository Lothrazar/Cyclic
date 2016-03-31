package com.lothrazar.samscommands.command;

import java.util.ArrayList;
import java.util.List; 

import com.lothrazar.samscommands.ModCommands; 
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class CommandUses implements ICommand
{
	public static boolean REQUIRES_OP;  
	private ArrayList<String> aliases = new ArrayList<String>();
	public CommandUses()
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
		return "uses";
	}

	@Override
	public String getCommandUsage(ICommandSender sender)
	{
		return "/"+getName();
	}

	@Override
	public List getAliases()
	{
		return aliases;
	}

	@Override
	public void execute(ICommandSender sender, String[] args)		throws CommandException
	{ 
		World world = sender.getEntityWorld();
		if(! (sender instanceof EntityPlayer)){return;}//does not work from command blocks and such
		
		EntityPlayer player = (EntityPlayer)sender;
		ItemStack held = player.inventory.getCurrentItem();
				
		if(held == null && world.isRemote)
		{
			ModCommands.addChatMessage(player, "command.recipes.empty");
			return;
		}

		List<IRecipe> recipes = CraftingManager.getInstance().getRecipeList();
		ItemStack recipeResult;
		boolean foundSomething = false;
		for (IRecipe recipe : recipes)
		{
		    recipeResult = recipe.getRecipeOutput();
		   
		    //compare ignoring stack size. not null, and the same item
		    
			if( recipeResult == null || recipeResult.getItem() == null){continue;} 

		    ItemStack[] ingred = CommandRecipe.getRecipeInput(recipe);
			
			for(ItemStack is : ingred)
			{
				if(is != null && held.getItem() == is.getItem() && held.getMetadata() == is.getMetadata()) 
				{ 
					ModCommands.addChatMessage(player, recipeResult.getDisplayName());
				
					foundSomething = true;
					//break only the inner loop, keep looking for other recipes
					break;
				}
			} 
		}//end loop
		
		if(foundSomething == false)
		{
			ModCommands.addChatMessage(player, "command.recipes.notfound");
		}
	}

	@Override
	public boolean canCommandSenderUse(ICommandSender sender)
	{ 
		return (REQUIRES_OP) ? sender.canUseCommand(2, this.getName()) : true; 
	}

	@Override
	public List addTabCompletionOptions(ICommandSender sender, String[] args,			BlockPos pos)
	{ 
		return null;
	}

	@Override
	public boolean isUsernameIndex(String[] args, int index)
	{ 
		return false;
	}

}
