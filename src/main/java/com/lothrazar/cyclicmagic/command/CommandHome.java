package com.lothrazar.cyclicmagic.command;

import java.util.ArrayList;
import java.util.List;
import com.lothrazar.cyclicmagic.util.UtilChat;
import com.lothrazar.cyclicmagic.util.UtilSound;
import com.lothrazar.cyclicmagic.util.UtilTeleport;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

public class CommandHome extends BaseCommand implements ICommand
{
	private ArrayList<String> aliases;
	public static boolean REQUIRES_OP; 
	public CommandHome(String n, boolean op){

		super(n, op);
		aliases = new ArrayList<String>();
	}

	@Override
	public boolean checkPermission(MinecraftServer server,ICommandSender sender)
	{
		return (REQUIRES_OP) ? super.checkPermission(server, sender) : true; 
	}
 
	@Override
	public String getCommandName()
	{ 
		return "home";
	}

	@Override
	public String getCommandUsage(ICommandSender ic)
	{ 
		return "/"+getCommandName();
	}

	@Override
	public List<String> getCommandAliases()
	{ 
		return aliases;
	}

	@Override
	public void execute(MinecraftServer server,ICommandSender ic, String[] args)
	{
		EntityPlayer player = ((EntityPlayer)ic); 
		World world = player.worldObj;
 
		if(player.dimension != 0)
		{
			 UtilChat.addChatMessage(player,new TextComponentTranslation("Can only teleport to your home in the overworld"));
			 return;
		}
		
		 BlockPos pos = player.getBedLocation(0);
		 
		 if(pos == null)
		 {
			 //has not been sent in a bed
			 //TODO: get the ID for this chat for translation purposes
			 UtilChat.addChatMessage(ic,"Your home bed was missing or obstructed.");
			 return;
		 }
		 IBlockState state = world.getBlockState(pos);
		 Block block = (state == null)?null: world.getBlockState(pos).getBlock();
		 
		 if (block != null && block.isBed(state, world, pos, player))// (block.equals(Blocks.bed)  ))//?? block.isBed(state, world, pos, player)
		 {
			 //then move over according to how/where the bed wants me to spawn
			 pos = block.getBedSpawnPosition(state, world, pos, null);
		 }
		 else
		 {
			 //spawn point was set, so the coords were not null, but player broke the bed (probably recently)
			 UtilChat.addChatMessage(player,"Your home bed was missing or obstructed.");
			 return;
		 }
		 
		  
		 /*
		player.setPositionAndUpdate(coords.getX(),  coords.getY(),  coords.getZ()); 
		while (player.getEntityBoundingBox() != null && world.getCollidingBoundingBoxes(player, player.getEntityBoundingBox()) != null && 
			  !world.getCollidingBoundingBoxes(player, player.getEntityBoundingBox()).isEmpty())
		{
			player.setPositionAndUpdate(player.posX, player.posY + 1.0D, player.posZ);
		}*/
		
		UtilTeleport.teleportWallSafe(player, world, pos);
		 
		UtilSound.playSound(player, "mob.endermen.portal"); 
	}
}
