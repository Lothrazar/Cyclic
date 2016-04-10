package com.lothrazar.cyclicmagic.registry;

import net.minecraft.block.BlockDispenser;
import net.minecraft.item.Item;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import com.lothrazar.cyclicmagic.dispenser.*;
import com.lothrazar.cyclicmagic.util.Const;

public class DispenserBehaviorRegistry {

	private static boolean seedsEnabled;

	public static void register() {

		if (seedsEnabled) {

			// if I could, i would just make a list of items, and register one single
			// BehaviorPlantSeed object
			// to all those items, but it doesnt work that way I guess

			// NOTE: currently does not support things like:
			// cocoa beans
			// chorus flower
			// reeds
			// tree saplings
			// becuase they are not considered 'plantable growing things' in the
			// normal way
			// something to TODO look in the future
			// DOES work with all plants even netherwart

			for (Item item : Item.itemRegistry) { // GameData.getBlockItemMap().entrySet()){
				if (item == null) {
					continue;
				}

				if (item instanceof IPlantable) {

					// System.out.println("BehaviorPlantSeed :
					// "+item.getUnlocalizedName());

					BlockDispenser.dispenseBehaviorRegistry.putObject(item, new BehaviorPlantSeed());
				}
			}
		}
	}

	public static void syncConfig(Configuration config) {
		String category = Const.MODCONF + "Misc";

		Property prop = config.get(category, "DispenserSeedsEnabled", true, "Dispensers can plant any growable seed");
		prop.setRequiresWorldRestart(true);
		seedsEnabled = prop.getBoolean();
	}
}
