package com.lothrazar.cyclicmagic.registry;

import java.util.HashMap;
import java.util.Map;
import com.lothrazar.cyclicmagic.command.*;
import com.lothrazar.cyclicmagic.util.Const;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

public class CommandRegistry {

	private static Map<String, Boolean>	configToggle	= new HashMap<String, Boolean>();
	
	public static void register(FMLServerStartingEvent event) {
		event.registerServerCommand(new CommandEnderChest(CommandEnderChest.name, true));
		event.registerServerCommand(new CommandGetHome(CommandGetHome.name, true));
		event.registerServerCommand(new CommandHearts(CommandHearts.name, true));
		event.registerServerCommand(new CommandHome(CommandHome.name, false));
		event.registerServerCommand(new CommandPing(CommandPing.name, false));
		event.registerServerCommand(new CommandRecipe(CommandRecipe.name, false));
		event.registerServerCommand(new CommandSearchItem(CommandSearchItem.name, false));
		event.registerServerCommand(new CommandSearchSpawner(CommandSearchSpawner.name, true));
		event.registerServerCommand(new CommandSearchTrades(CommandSearchTrades.name, false));
		event.registerServerCommand(new CommandSimpleWaypoints(CommandSimpleWaypoints.name, false));
		event.registerServerCommand(new CommandTodoList(CommandTodoList.name, false));
		event.registerServerCommand(new CommandUses(CommandUses.name, false));
		event.registerServerCommand(new CommandVillageInfo(CommandVillageInfo.name, false));
		event.registerServerCommand(new CommandWorldHome(CommandWorldHome.name, false));
	}

	public static void syncConfig(Configuration config) {
		String category = Const.MODCONF + "Commands";
		config.setCategoryComment(category, "Disable any command that was added");

		Property prop = config.get(category, CommandEnderChest.name, true, " ");
		prop.setRequiresMcRestart(true);
		configToggle.put(CommandEnderChest.name, prop.getBoolean());

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
		
		
	}
}
