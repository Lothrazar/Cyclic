package com.lothrazar.cyclicmagic.command;

import java.util.ArrayList;
import java.util.List;
import com.lothrazar.cyclicmagic.util.Const;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

public class BaseCommand implements ICommand {
	// https://github.com/LothrazarMinecraftMods/MinecraftSearchCommands/blob/master/src/main/java/com/lothrazar/searchcommands/command/CommandSearchTrades.java
	// https://github.com/PrinceOfAmber/SamsPowerups/tree/master/Commands/src/main/java/com/lothrazar/samscommands/command
	// CommandSimpleWaypoints removed TP feature -> we have ender book already

	private String						name;
	private boolean						requiresOP;
	private final static int	OP	= 2;
	protected ArrayList<String>	aliases;

	public BaseCommand(String n, boolean op) {
		this(n,op,null);
	}

	public BaseCommand(String n, boolean op, ArrayList<String> paliases) {
		name = n;
		requiresOP = op;
		aliases = (paliases == null) ? new ArrayList<String>() : paliases;
		aliases.add(name.toUpperCase());
	}

	@Override
	public List<String> getCommandAliases() {
		return this.aliases;
	}

	@Override
	public String getCommandName() {
		return name;
	}

	@Override
	public int compareTo(ICommand arg0) {
		return this.equals(arg0) ? 1 : 0;
	}

	/**
	 * Override to specify any arguments or info
	 */
	@Override
	public String getCommandUsage(ICommandSender sender) {
		return "/" + this.getCommandName();
	}

	/**
	 * unless you override, all commands require 'op'
	 * override to change
	 */
	@Override
	public boolean checkPermission(MinecraftServer server, ICommandSender sender) {

		return (requiresOP) ? sender.canCommandSenderUseCommand(OP, this.getCommandName()) : true;
	}

	@Override
	public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos) {

		return new ArrayList<String>();
	}

	@Override
	public boolean isUsernameIndex(String[] args, int index) {
		return false;
	}

	public EntityPlayerMP getPlayerByUsername(MinecraftServer server, String name) {
		return server.getPlayerList().getPlayerByUsername(name);
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {

		System.out.println("Warning: command not implemented " + Const.MODID + " -> " + this.getCommandName());

	}
}
