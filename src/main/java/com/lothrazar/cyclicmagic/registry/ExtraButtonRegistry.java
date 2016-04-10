package com.lothrazar.cyclicmagic.registry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import com.lothrazar.cyclicmagic.util.Const;
import net.minecraftforge.common.config.Configuration;

public class ExtraButtonRegistry {
	public static void register() {

	}

	public static String				position;
	public static boolean				restockLeaveOne;

	public static String				posLeft		= "topleft";
	public static String				posRight	= "topright";
	public static String				posBottom	= "bottomleft";

	public static List<String>	blacklistGuis;

	public static void syncConfig(Configuration config) {
		String category = Const.MODCONF + "Inventory Buttons";

		List<String> valid = new ArrayList<String>();
		valid.add(posLeft);
		valid.add(posRight);
		valid.add(posBottom);
		
		
		restockLeaveOne = config.getBoolean("Restock Leave One",category, false, "By default (false) the Restock feature will empty your chests if possible.  If you change this to true, then using Restock will leave one behind of each item stack");

		position = config.getString("Button Location", category, posRight, "Location of the buttons, " + "valid entries are: " + String.join(",", valid));

		if (valid.contains(position) == false) {
			position = posRight;// default
		}

		// config.addCustomCategoryComment(category, "Here you can blacklist any
		// container, vanilla or modded. Mostly for creating modpacks, if some
		// containers shouldnt have these buttons showing up.");
		// the default
		String blacklistDefault = "net.minecraft.client.gui.inventory.GuiBrewingStand," + "net.minecraft.client.gui.inventory.GuiBeacon," + "net.minecraft.client.gui.inventory.GuiCrafting," + "net.minecraft.client.gui.inventory.GuiFurnace," + "net.minecraft.client.gui.inventory.GuiScreenHorseInventory," + "net.minecraft.client.gui.inventory.GuiContainerCreative";

		String csv = config.getString("Blacklist Container CSV", category, blacklistDefault, "FOR MODPACK DEVS: These containers are blocked from getting the buttons.  By default, anything that extends 'GuiContainer' will get the buttons.  ");
		// blacklistGuis = new ArrayList<String>();
		blacklistGuis = (List<String>) Arrays.asList(csv.split(","));
		if (blacklistGuis == null)
			blacklistGuis = new ArrayList<String>();// just being extra safe

		// if(config.hasChanged()){config.save();}
	}
}
