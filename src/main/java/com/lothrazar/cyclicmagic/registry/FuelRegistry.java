package com.lothrazar.cyclicmagic.registry;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.common.registry.GameRegistry;
import com.lothrazar.cyclicmagic.event.FuelHandler;
import com.lothrazar.cyclicmagic.util.Const;

public class FuelRegistry {

	private static boolean enabled;

	public static void register() {

		if (enabled) {
			GameRegistry.registerFuelHandler(new FuelHandler());
		}
	}

	public static void syncConfig(Configuration config) {

		String category = Const.MODCONF + "Misc";

		String msg = "Other Features";

		config.setCategoryComment(category, msg);

		Property prop = config.get(category, "More Fuel Enabled", true, msg);

		enabled = prop.getBoolean();

	}
}
