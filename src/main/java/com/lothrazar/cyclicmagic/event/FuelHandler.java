package com.lothrazar.cyclicmagic.event;

import java.util.HashMap;
import java.util.Map;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraftforge.fml.common.IFuelHandler;

public class FuelHandler implements IFuelHandler {

	Map<Item, Integer> fuelMap = new HashMap<Item, Integer>();

	/*
	 * THE PLAN:
	 * FUEL REGISTRY, make more stuff burn implements IFuelHandler {
	 * wood doors
	 * BOATS
	 * ferns, grass, vines, lillypad
	 * leaf blocks
	 * ladder
	 * dead bush
	 * sign, painting
	 * Wheat seeds
	 */
	public FuelHandler() {

		// http://minecraft.gamepedia.com/Smelting
		int stick = TileEntityFurnace.getItemBurnTime(new ItemStack(Items.stick));
		int log = TileEntityFurnace.getItemBurnTime(new ItemStack(Blocks.log));
		// int coal = TileEntityFurnace.getItemBurnTime(new ItemStack(Items.coal));
		// blazerod, coalblock, lava bucket are above these

		fuelMap.put(Items.sign, log);
		fuelMap.put(Items.acacia_boat, log);
		fuelMap.put(Items.jungle_boat, log);
		fuelMap.put(Items.birch_boat, log);
		fuelMap.put(Items.boat, log);//oak
		fuelMap.put(Items.dark_oak_boat, log);
		fuelMap.put(Items.spruce_boat, log);
		fuelMap.put(Item.getItemFromBlock(Blocks.ladder), log);
		fuelMap.put(Items.painting, log);
		fuelMap.put(Items.wheat_seeds, stick);
		fuelMap.put(Items.pumpkin_seeds, stick);
		fuelMap.put(Items.melon_seeds, stick);
		fuelMap.put(Item.getItemFromBlock(Blocks.vine), stick);
		fuelMap.put(Item.getItemFromBlock(Blocks.tallgrass), stick);
		fuelMap.put(Item.getItemFromBlock(Blocks.waterlily), stick);
		fuelMap.put(Item.getItemFromBlock(Blocks.deadbush), stick);
		fuelMap.put(Item.getItemFromBlock(Blocks.leaves), stick);
		fuelMap.put(Item.getItemFromBlock(Blocks.leaves2), stick);
	}

	@Override
	public int getBurnTime(ItemStack fuel) {

		if (fuelMap.containsKey(fuel.getItem())) { return fuelMap.get(fuel.getItem()); }

		return 0;
	}

}
