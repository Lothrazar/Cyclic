package com.lothrazar.cyclicmagic.registry;

import com.lothrazar.cyclicmagic.command.*;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

public class CommandRegistry {

	public static void register(FMLServerStartingEvent event) {
		event.registerServerCommand(new CommandEnderChest("enderchest", true));
		event.registerServerCommand(new CommandGetHome("gethome", true));
		event.registerServerCommand(new CommandHearts("sethearts", true));
		event.registerServerCommand(new CommandHome("home", false));
		event.registerServerCommand(new CommandPing("ping", false));
		event.registerServerCommand(new CommandRecipe("searchrecipe", false));
		event.registerServerCommand(new CommandSearchItem("searchitem", false));
		event.registerServerCommand(new CommandSearchSpawner("searchspawner", true));
		event.registerServerCommand(new CommandSearchTrades("searchtrade", false));
		event.registerServerCommand(new CommandSimpleWaypoints("waypoint", false));
		event.registerServerCommand(new CommandTodoList("todo", false));
		event.registerServerCommand(new CommandUses("searchuses", false));
		event.registerServerCommand(new CommandVillageInfo("villageinfo", false));
		event.registerServerCommand(new CommandWorldHome("worldhome", false));
	}
}
