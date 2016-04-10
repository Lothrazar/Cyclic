package com.lothrazar.cyclicmagic.command;

import java.util.ArrayList;
import com.lothrazar.cyclicmagic.util.UtilChat;
import com.lothrazar.cyclicmagic.util.UtilSound;
import com.lothrazar.cyclicmagic.util.UtilTeleport;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
//import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;

public class CommandWorldHome extends BaseCommand implements ICommand {
	public static boolean			REQUIRES_OP;

	private ArrayList<String>	aliases	= new ArrayList<String>();

	public CommandWorldHome(String n, boolean op) {

		super(n, op);
		aliases.add(n.toUpperCase());
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender ic, String[] args) {
		World world = ic.getCommandSenderEntity().worldObj;

		EntityPlayer player = (EntityPlayer) ic;// world.getClosestPlayer(ic.getPosition().getX(),
		                                        // ic.getPosition().getY(),
		                                        // ic.getPosition().getZ(), 5);

		if (player.dimension != 0) {
			// TODO:"Can only teleport to worldhome in the overworld"
			UtilChat.addChatMessage(player, "command.worldhome.dim");
			return;
		}

		// this tends to always get something at y=64, regardless if there is AIR or
		// not
		// so we need to safely push the player up out of any blocks they are in

		UtilTeleport.teleportWallSafe(player, world, world.getSpawnPoint());
		UtilSound.playSound(player, "mob.endermen.portal");
	}
}
