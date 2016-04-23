package com.lothrazar.cyclicmagic.command;

import com.lothrazar.cyclicmagic.util.Const;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.server.MinecraftServer;

public class CommandHeal extends BaseCommand{

	public static final String name = "heal";

	public CommandHeal( boolean op) {
		super(name, op);
	}
	
	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {

		if(sender instanceof EntityLivingBase){
			EntityLivingBase living = (EntityLivingBase) sender;
			
			living.setHealth(living.getMaxHealth());
		}
	}
}
