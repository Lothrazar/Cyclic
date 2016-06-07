package com.lothrazar.cyclicmagic.command;

import com.lothrazar.cyclicmagic.util.UtilChat;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;

public class CommandHeal extends BaseCommand{

	public static final String name = "heal";

	public CommandHeal( boolean op) {
		super(name, op);
		this.setUsernameIndex(0);
	}

	@Override
	public String getCommandUsage(ICommandSender sender) {
		return "/" + getCommandName() + " <player>";
	}
	
	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {

		if(args.length == 0 && sender instanceof EntityLivingBase){
	 
			EntityLivingBase living = (EntityLivingBase) sender;
			
			living.setHealth(living.getMaxHealth()); 
		}
		
		EntityPlayer ptarget = null;

		try {
			ptarget = super.getPlayerByUsername(server, args[0]);

			if (ptarget == null) {
				UtilChat.addChatMessage(sender, getCommandUsage(sender));
				return;
			}

			ptarget.setHealth(ptarget.getMaxHealth()); 
			
		} catch (Exception e) {
			UtilChat.addChatMessage(sender, getCommandUsage(sender));
			return;
		}
	}
}
