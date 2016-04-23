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

	public static void register(FMLServerStartingEvent event) {

		if (configToggle.get(CommandEnderChest.name)) {
			event.registerServerCommand(new CommandEnderChest( true));
		}
		if (configToggle.get(CommandGetHome.name)) {
			event.registerServerCommand(new CommandGetHome( true));
		}
		if (configToggle.get(CommandHeal.name)) {
			event.registerServerCommand(new CommandHeal(true));
		}
		if (configToggle.get(CommandHearts.name)) {
			event.registerServerCommand(new CommandHearts( true));
		}
		if (configToggle.get(CommandHome.name)) {
			event.registerServerCommand(new CommandHome( true));
		}
		if (configToggle.get(CommandPing.name)) {
			event.registerServerCommand(new CommandPing( true));
		}
		if (configToggle.get(CommandRecipe.name)) {
			event.registerServerCommand(new CommandRecipe( true));
		}
		if (configToggle.get(CommandSearchItem.name)) {
			event.registerServerCommand(new CommandSearchItem( true));
		}
		if (configToggle.get(CommandSearchSpawner.name)) {
			event.registerServerCommand(new CommandSearchSpawner( true));
		}
		if (configToggle.get(CommandSearchTrades.name)) {
			event.registerServerCommand(new CommandSearchTrades( true));
		}
		if (configToggle.get(CommandSimpleWaypoints.name)) {
			event.registerServerCommand(new CommandSimpleWaypoints( true));
		}
		if (configToggle.get(CommandTodoList.name)) {
			event.registerServerCommand(new CommandTodoList( true));
		}
		if (configToggle.get(CommandUses.name)) {
			event.registerServerCommand(new CommandUses( true));
		}
		if (configToggle.get(CommandVillageInfo.name)) {
			event.registerServerCommand(new CommandVillageInfo( true));
		}
		if (configToggle.get(CommandWorldHome.name)) {
			event.registerServerCommand(new CommandWorldHome( true));
		}
	}

	public static void syncConfig(Configuration config) {
		String category = Const.MODCONF + "Commands";
		config.setCategoryComment(category, "Disable any command that was added");

		Property prop = config.get(category, CommandEnderChest.name, true, " ");
		prop.setRequiresMcRestart(true);
		configToggle.put(CommandEnderChest.name, prop.getBoolean());

		prop = config.get(category, CommandHeal.name, true, " ");
		prop.setRequiresMcRestart(true);
		configToggle.put(CommandHeal.name, prop.getBoolean());
		
		prop = config.get(category, CommandHearts.name, true, " ");
		prop.setRequiresMcRestart(true);
		configToggle.put(CommandHearts.name, prop.getBoolean());

		prop = config.get(category, CommandGetHome.name, true, " ");
		prop.setRequiresMcRestart(true);
		configToggle.put(CommandGetHome.name, prop.getBoolean());

		prop = config.get(category, CommandHome.name, true, " ");
		prop.setRequiresMcRestart(true);
		configToggle.put(CommandHome.name, prop.getBoolean());

		prop = config.get(category, CommandPing.name, true, " ");
		prop.setRequiresMcRestart(true);
		configToggle.put(CommandPing.name, prop.getBoolean());

		prop = config.get(category, CommandRecipe.name, true, " ");
		prop.setRequiresMcRestart(true);
		configToggle.put(CommandRecipe.name, prop.getBoolean());

		prop = config.get(category, CommandSearchItem.name, true, " ");
		prop.setRequiresMcRestart(true);
		configToggle.put(CommandSearchItem.name, prop.getBoolean());

		prop = config.get(category, CommandSearchSpawner.name, true, " ");
		prop.setRequiresMcRestart(true);
		configToggle.put(CommandSearchSpawner.name, prop.getBoolean());

		prop = config.get(category, CommandSearchTrades.name, true, " ");
		prop.setRequiresMcRestart(true);
		configToggle.put(CommandSearchTrades.name, prop.getBoolean());

		prop = config.get(category, CommandSimpleWaypoints.name, true, " ");
		prop.setRequiresMcRestart(true);
		configToggle.put(CommandSimpleWaypoints.name, prop.getBoolean());

		prop = config.get(category, CommandTodoList.name, true, " ");
		prop.setRequiresMcRestart(true);
		configToggle.put(CommandTodoList.name, prop.getBoolean());

		prop = config.get(category, CommandUses.name, true, " ");
		prop.setRequiresMcRestart(true);
		configToggle.put(CommandUses.name, prop.getBoolean());

		prop = config.get(category, CommandVillageInfo.name, true, " ");
		prop.setRequiresMcRestart(true);
		configToggle.put(CommandVillageInfo.name, prop.getBoolean());

		prop = config.get(category, CommandWorldHome.name, true, " ");
		prop.setRequiresMcRestart(true);
		configToggle.put(CommandWorldHome.name, prop.getBoolean());

		prop = config.get(category, CommandWorldHome.name, true, " ");
		prop.setRequiresMcRestart(true);
		configToggle.put(CommandWorldHome.name, prop.getBoolean());

		prop = config.get(category, CommandHeal.name, true, " ");
		prop.setRequiresMcRestart(true);
		configToggle.put(CommandHeal.name, prop.getBoolean());
		
	}
}
