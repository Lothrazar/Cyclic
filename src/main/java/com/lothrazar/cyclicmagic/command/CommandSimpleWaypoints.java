package com.lothrazar.cyclicmagic.command;

import java.util.ArrayList;
import java.util.Arrays;
import com.lothrazar.cyclicmagic.util.Const;
import com.lothrazar.cyclicmagic.util.UtilChat;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextFormatting;

public class CommandSimpleWaypoints extends BaseCommand implements ICommand {
	// public static boolean ENABLE_TP = false;
	public static final String name = "waypoint";
	private static String NBT_KEY = Const.MODID + "_swp";

	public CommandSimpleWaypoints( boolean op) {

		super(name, op);
	}

	@Override
	public String getCommandUsage(ICommandSender p_71518_1_) {
		// String tp = ENABLE_TP ? "|" + MODE_TP : "";
		// + "|" + MODE_HIDEDISPLAY
		return "/" + getCommandName() + " <" + MODE_LIST + "|" + MODE_SAVE + "|" + MODE_CLEAR + "> [name | index]";
	}

	// private static String MODE_TP = "tp";
	// private static String MODE_DISPLAY = "get";
	// private static String MODE_HIDEDISPLAY = "hide";
	private final static String	MODE_LIST			= "list";
	private final static String	MODE_SAVE			= "save";
	private final static String	MODE_CLEAR		= "delete";
	// private static String KEY_CURRENT = "simplewp_current";
	public static boolean				PERSIST_DEATH	= true;			// TODO: mayb econfig

	@Override
	public void execute(MinecraftServer server, ICommandSender icommandsender, String[] args) {
		EntityPlayer p = (EntityPlayer) icommandsender;

		if (args == null || args.length == 0 || args[0] == null || args[0].length() == 0) {
			UtilChat.addChatMessage(icommandsender, getCommandUsage(icommandsender));

			return;// not enough args
		}

		if (args[0].equals(MODE_LIST)) {
			executeList(p);
			return;
		}

		if (args[0].equals(MODE_SAVE)) {
			String n = "";
			if (args.length > 1)
				n = args[1];
			executeSave(p, n);
			return;
		}

		// so its outside the scope of commands that do not have a number
		int index = -1;

		try {
			index = Integer.parseInt(args[1]);// TODO: trycatch on this , it might not
			                                  // be integer
		} catch (Exception e) {
			UtilChat.addChatMessage(icommandsender, getCommandUsage(icommandsender));
			return;
		}
		if (index <= 0) // invalid number, or int parse failed
		{
			// ZERO NOT ALLOWED
			UtilChat.addChatMessage(icommandsender, getCommandUsage(icommandsender));
			return;
		}

		if (args[0].equals(MODE_CLEAR)) {
			executeClear(p, index);
			return;
		}

		// if nothing else, as not matched anything:
		// then this is like the default case in a switch statement
		UtilChat.addChatMessage(icommandsender, getCommandUsage(icommandsender));
	}

	private void executeSave(EntityPlayer p, String name) {
		ArrayList<String> lines = getForPlayer(p);

		if (name == null)
			name = "";

		// never putr a loc in index zero

		if (lines.size() == 0)
			lines.add("0");

		Location here = new Location(lines.size(), p, name);

		lines.add(here.toCSV());

		overwriteForPlayer(p, lines);
	}

	private void executeClear(EntityPlayer p, int index) {
		ArrayList<String> lines = getForPlayer(p);

		ArrayList<String> newLines = new ArrayList<String>();

		int i = 0;
		for (String line : lines) {
			if (i == index) {
				i++;
				continue;// skip this line
			}
			i++;

			newLines.add(line);
		}

		newLines.set(0, "0");

		overwriteForPlayer(p, newLines);
	}

	private void executeList(EntityPlayer p) {
		boolean showCoords = true;// !p.worldObj.getGameRules().getGameRuleBooleanValue("reducedDebugInfo");

		ArrayList<String> lines = getForPlayer(p);

		int i = 0;
		String msg = "";
		Location loc;

		for (String line : lines) {
			if (i == 0) {
				i++;
				continue;
			}// just a weird bug that happens, since we index by 1

			if (line == null || line.isEmpty()) {
				continue;
			}
			msg = TextFormatting.WHITE + "";// overworld and all other dimensions
			loc = new Location(line);

			if (loc.dimension == 1)// END
				msg = TextFormatting.DARK_PURPLE + "";
			else if (loc.dimension == -1)// NETHER
				msg = TextFormatting.RED + "";

			msg += "<" + i + "> ";

			if (showCoords)
				msg += loc.toDisplay();
			else
				msg += loc.name;

			UtilChat.addChatMessage(p, msg);

			i++;
		}
	}

	@Override
	public boolean isUsernameIndex(String[] p_82358_1_, int p_82358_2_) {
		return false;
	}

	public static void overwriteForPlayer(EntityPlayer player, ArrayList<String> lines) {
		String csv = "";
		for (String line : lines) {
			csv += line + System.lineSeparator();
		}
		player.getEntityData().setString(NBT_KEY, csv);

	}

	public static Location getSingleForPlayer(EntityPlayer player, int index) {
		if (index <= 0) { return null; }

		ArrayList<String> saved = getForPlayer(player);// .getDisplayName().getUnformattedText()

		if (saved.size() <= index) { return null; }

		// loc = getSingleForPlayer(p,index);

		String sloc = saved.get(index);

		if (sloc == null || sloc.isEmpty()) { return null; }

		Location loc = null;

		if (index < saved.size() && saved.get(index) != null) {
			loc = new Location(sloc);// this still might be null..?
		}

		return loc;
	}

	public static ArrayList<String> getForPlayer(EntityPlayer player) {
		ArrayList<String> lines = new ArrayList<String>();

		String csv = player.getEntityData().getString(NBT_KEY);

		lines = new ArrayList<String>(Arrays.asList(csv.split(System.lineSeparator())));

		return lines;
	}

}
