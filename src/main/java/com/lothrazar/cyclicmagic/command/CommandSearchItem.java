package com.lothrazar.cyclicmagic.command;

import java.util.ArrayList;
import java.util.HashMap;
import com.lothrazar.cyclicmagic.util.UtilChat;
import com.lothrazar.cyclicmagic.util.UtilSearchWorld;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

public class CommandSearchItem extends BaseCommand implements ICommand {
	public CommandSearchItem(String n, boolean op) {

		super(n, op);
	}

	@Override
	public String getCommandUsage(ICommandSender arg0) {
		return "/" + getCommandName() + " <itemname> [radius]";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) {
		if (!(sender instanceof EntityPlayerMP)) { return; }

		EntityPlayerMP player = (EntityPlayerMP) sender;
		if (args.length < 1) {
			UtilChat.addChatMessage(player, getCommandUsage(sender));
			return;
		}

		int radius = 0;
		if (args.length > 1) {
			radius = Integer.parseInt(args[1]);
		}

		if (radius > 128) {
			radius = 128;
		}// Maximum //
		if (radius <= 0) {
			radius = 64;
		}// default

		String searchQuery = args[0].trim().toLowerCase(); // args[0] is the command
		                                                   // name or alias used
		                                                   // such as "is"
		ArrayList<IInventory> tilesToSearch = new ArrayList<IInventory>();
		HashMap<IInventory, BlockPos> dictionary = new HashMap<IInventory, BlockPos>();
		IInventory tile;

		ArrayList<BlockPos> foundChests = UtilSearchWorld.findBlocks(player, Blocks.chest, radius);
		for (BlockPos pos : foundChests) {
			tile = (IInventory) player.worldObj.getTileEntity(pos);
			tilesToSearch.add(tile);
			dictionary.put(tile, pos);
		}
		ArrayList<BlockPos> foundTrapChests = UtilSearchWorld.findBlocks(player, Blocks.trapped_chest, radius);
		for (BlockPos pos : foundTrapChests) {
			tile = (IInventory) player.worldObj.getTileEntity(pos);
			tilesToSearch.add(tile);
			dictionary.put(tile, pos);
		}
		ArrayList<BlockPos> foundDisp = UtilSearchWorld.findBlocks(player, Blocks.dispenser, radius);
		for (BlockPos pos : foundDisp) {
			tile = (IInventory) player.worldObj.getTileEntity(pos);
			tilesToSearch.add(tile);
			dictionary.put(tile, pos);
		}

		int foundQtyTotal;
		ArrayList<String> foundMessages = new ArrayList<String>();

		for (IInventory inventory : tilesToSearch) {
			foundQtyTotal = 0;
			foundQtyTotal = UtilSearchWorld.searchTileInventory(searchQuery, inventory);

			if (foundQtyTotal > 0) {
				String totalsStr = foundQtyTotal + " found : ";

				// TODO we COULD have configs for each of these, that is, one config
				// flag for SEND_CHAT
				// and one for SEND_PARTICLES
				// ModCommands.spawnParticlePacketByID(dictionary.get(inventory),EnumParticleTypes.CRIT_MAGIC.getParticleID());

				foundMessages.add(totalsStr + getCoordsOrReduced(player, dictionary.get(inventory)));
			}
		}

		int ifound = foundMessages.size();

		if (ifound == 0) {
			UtilChat.addChatMessage(player, "No items found within search radius : " + radius);
		}
		else {
			for (int i = 0; i < ifound; i++) {
				UtilChat.addChatMessage(player, foundMessages.get(i));
			}
		}
	}

	public static String getCoordsOrReduced(EntityPlayer player, BlockPos pos) {
		// boolean showCoords =
		// !player.worldObj.getGameRules().getGameRuleBooleanValue("reducedDebugInfo");

		// if(showCoords)
		return pos.getX() + ", " + pos.getY() + ", " + pos.getZ();
		// else
		// return ModCommands.getDirectionsString(player, pos);
	}

}
