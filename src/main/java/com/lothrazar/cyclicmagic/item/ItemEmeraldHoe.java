package com.lothrazar.cyclicmagic.item;

import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.registry.ItemRegistry;
import net.minecraft.init.Items;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ItemEmeraldHoe extends ItemHoe implements IHasRecipe {

	public static final String name = "emerald_hoe";

	public ItemEmeraldHoe() {

		super(ItemRegistry.TOOL_MATERIAL_EMERALD);
	}

	@Override
	public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {

		if (net.minecraftforge.oredict.OreDictionary.itemMatches(new ItemStack(ItemRegistry.REPAIR_EMERALD), repair, false)) { return true; }
		return super.getIsRepairable(toRepair, repair);
	}

	@Override
	public void addRecipe() {
		GameRegistry.addShapedRecipe(new ItemStack(this), "ee ", " s ", " s ", 'e', new ItemStack(ItemRegistry.REPAIR_EMERALD), 's', new ItemStack(Items.STICK));
		GameRegistry.addShapedRecipe(new ItemStack(this), " ee", " s ", " s ", 'e', new ItemStack(ItemRegistry.REPAIR_EMERALD), 's', new ItemStack(Items.STICK));
	}
}
