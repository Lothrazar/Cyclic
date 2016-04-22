package com.lothrazar.cyclicmagic.command;

import com.lothrazar.cyclicmagic.util.Const;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.server.MinecraftServer;

public class CommandHeal extends BaseCommand{

	public CommandHeal(String n, boolean op) {
		super(n, op);
	}
	
	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {

		//TODO: try to get a number as input
		if(sender instanceof EntityLivingBase){
			EntityLivingBase living = (EntityLivingBase) sender;
			
			living.setHealth(living.getMaxHealth());
		}
		
		System.out.println("Warning: command not implemented " + Const.MODID + ":" + this.getCommandName());

	}
}
