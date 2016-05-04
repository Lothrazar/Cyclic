package com.lothrazar.cyclicmagic.registry;

import java.util.HashMap;
import java.util.Map;
import com.lothrazar.cyclicmagic.command.*;
import com.lothrazar.cyclicmagic.util.Const;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

public class CommandRegistry {

	private static Map<String, Boolean> configToggle = new HashMap<String, Boolean>();
	private static Map<String, Boolean> commandNeedsOp = new HashMap<String, Boolean>();
	private static String category;

	public static void register(FMLServerStartingEvent event) {

		if (configToggle.get(CommandEnderChest.name)) {
			event.registerServerCommand(new CommandEnderChest(commandNeedsOp.get(CommandEnderChest.name)));
		}
		if (configToggle.get(CommandGetHome.name)) {
			event.registerServerCommand(new CommandGetHome(commandNeedsOp.get(CommandGetHome.name)));
		}
		if (configToggle.get(CommandHeal.name)) {
			event.registerServerCommand(new CommandHeal(commandNeedsOp.get(CommandHeal.name)));
		}
		if (configToggle.get(CommandHearts.name)) {
			event.registerServerCommand(new CommandHearts(commandNeedsOp.get(CommandHearts.name)));
		}
		if (configToggle.get(CommandHome.name)) {
			event.registerServerCommand(new CommandHome(commandNeedsOp.get(CommandHome.name)));
		}
		if (configToggle.get(CommandPing.name)) {
			event.registerServerCommand(new CommandPing(commandNeedsOp.get(CommandPing.name)));
		}
		if (configToggle.get(CommandRecipe.name)) {
			event.registerServerCommand(new CommandRecipe(commandNeedsOp.get(CommandRecipe.name)));
		}
		if (configToggle.get(CommandSearchItem.name)) {
			event.registerServerCommand(new CommandSearchItem(commandNeedsOp.get(CommandSearchItem.name)));
		}
		if (configToggle.get(CommandSearchSpawner.name)) {
			event.registerServerCommand(new CommandSearchSpawner(commandNeedsOp.get(CommandSearchSpawner.name)));
		}
		if (configToggle.get(CommandSearchTrades.name)) {
			event.registerServerCommand(new CommandSearchTrades(commandNeedsOp.get(CommandSearchTrades.name)));
		}
		if (configToggle.get(CommandSimpleWaypoints.name)) {
			event.registerServerCommand(new CommandSimpleWaypoints(commandNeedsOp.get(CommandSimpleWaypoints.name)));
		}
		if (configToggle.get(CommandTodoList.name)) {
			event.registerServerCommand(new CommandTodoList(commandNeedsOp.get(CommandTodoList.name)));
		}
		if (configToggle.get(CommandUses.name)) {
			event.registerServerCommand(new CommandUses(commandNeedsOp.get(CommandUses.name)));
		}
		if (configToggle.get(CommandVillageInfo.name)) {
			event.registerServerCommand(new CommandVillageInfo(commandNeedsOp.get(CommandVillageInfo.name)));
		}
		if (configToggle.get(CommandWorldHome.name)) {
			event.registerServerCommand(new CommandWorldHome(commandNeedsOp.get(CommandWorldHome.name)));
		}
	}

	private static void syncCommandConfig(Configuration config, String name, boolean defaultNeedsOp) {

		Property prop = config.get(category, name, true, " ");
		prop.setRequiresMcRestart(true);
		configToggle.put(name, prop.getBoolean());

		prop = config.get(category, name, defaultNeedsOp, " ");
		prop.setRequiresMcRestart(true);
		commandNeedsOp.put(name + ".needs_op", prop.getBoolean());
	}

	public static void syncConfig(Configuration config) {
		category = Const.MODCONF + "Commands";
		config.setCategoryComment(category, "Disable any command that was added");

		syncCommandConfig(config, CommandEnderChest.name, false);

		syncCommandConfig(config, CommandHeal.name, false);

		syncCommandConfig(config, CommandHearts.name, true);

		syncCommandConfig(config, CommandGetHome.name, false);

		syncCommandConfig(config, CommandHome.name, false);

		syncCommandConfig(config, CommandPing.name, false);

		syncCommandConfig(config, CommandRecipe.name, false);

		syncCommandConfig(config, CommandSearchItem.name, false);

		syncCommandConfig(config, CommandSearchSpawner.name, true);

		syncCommandConfig(config, CommandSearchTrades.name, false);

		syncCommandConfig(config, CommandSimpleWaypoints.name, false);

		syncCommandConfig(config, CommandTodoList.name, false);

		syncCommandConfig(config, CommandUses.name, false);

		syncCommandConfig(config, CommandVillageInfo.name, false);

		syncCommandConfig(config, CommandWorldHome.name, false);

		syncCommandConfig(config, CommandHeal.name, true);
	}
}
