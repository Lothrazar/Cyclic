package com.lothrazar.cyclicmagic.item;

import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.registry.ItemRegistry;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ItemEmeraldPickaxe extends ItemPickaxe implements IHasRecipe {
	public static final String name = "emerald_pickaxe";

	public ItemEmeraldPickaxe() {
		super(ItemRegistry.MATERIAL_EMERALD);
	}

	@Override
	public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
		if (net.minecraftforge.oredict.OreDictionary.itemMatches(new ItemStack(ItemRegistry.REPAIR_EMERALD), repair, false)) { return true; }
		return super.getIsRepairable(toRepair, repair);
	}

	@Override
	public void addRecipe() {

		GameRegistry.addShapedRecipe(new ItemStack(this), "eee", " s ", " s ", 'e', new ItemStack(Blocks.emerald_block), 's', new ItemStack(Items.stick));
	}
}
