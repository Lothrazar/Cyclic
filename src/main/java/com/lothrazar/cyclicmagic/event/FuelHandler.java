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
		int stick = TileEntityFurnace.getItemBurnTime(new ItemStack(Items.STICK));
		int log = TileEntityFurnace.getItemBurnTime(new ItemStack(Blocks.LOG));
		// int coal = TileEntityFurnace.getItemBurnTime(new ItemStack(Items.coal));
		// blazerod, coalblock, lava bucket are above these

		fuelMap.put(Items.SIGN, log);
		fuelMap.put(Items.ACACIA_BOAT, log);
		fuelMap.put(Items.JUNGLE_BOAT, log);
		fuelMap.put(Items.BIRCH_BOAT, log);
		fuelMap.put(Items.BOAT, log);//oak
		fuelMap.put(Items.DARK_OAK_BOAT, log);
		fuelMap.put(Items.SPRUCE_BOAT, log);
		fuelMap.put(Item.getItemFromBlock(Blocks.LADDER), log);
		fuelMap.put(Items.PAINTING, log);
		fuelMap.put(Items.WHEAT_SEEDS, stick);
		fuelMap.put(Items.PUMPKIN_SEEDS, stick);
		fuelMap.put(Items.MELON_SEEDS, stick);
		fuelMap.put(Item.getItemFromBlock(Blocks.VINE), stick);
		fuelMap.put(Item.getItemFromBlock(Blocks.TALLGRASS), stick);
		fuelMap.put(Item.getItemFromBlock(Blocks.WATERLILY), stick);
		fuelMap.put(Item.getItemFromBlock(Blocks.DEADBUSH), stick);
		fuelMap.put(Item.getItemFromBlock(Blocks.LEAVES), stick);
		fuelMap.put(Item.getItemFromBlock(Blocks.LEAVES2), stick);
	}

	@Override
	public int getBurnTime(ItemStack fuel) {

		if (fuelMap.containsKey(fuel.getItem())) { return fuelMap.get(fuel.getItem()); }

		return 0;
	}

}
