package com.lothrazar.cyclicmagic.command;

import com.lothrazar.cyclicmagic.util.UtilChat;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

public class CommandGetHome extends BaseCommand implements ICommand {
	public static boolean REQUIRES_OP;

	public CommandGetHome(String n, boolean op) {
		super(n, op);
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender ic, String[] args) {
		EntityPlayer player = ((EntityPlayer) ic);
		// World world = player.worldObj;

		if (player.dimension != 0) {
			UtilChat.addChatMessage(player, "No home outside the overworld");
			return;
		}

		BlockPos coords = player.getBedLocation(0);

		if (coords == null) {
			// has not been sent in a bed
			// TODO: get the ID for this chat for translation purposes
			UtilChat.addChatMessage(player, "Your home bed was missing or obstructed.");

		}
		else {
			String pos = coords.getX() + ", " + coords.getY() + ", " + coords.getZ();

			UtilChat.addChatMessage(player, "Your home bed is at " + pos);

		}
	}
}