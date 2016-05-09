package com.lothrazar.cyclicmagic.item;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraftforge.fml.common.registry.GameRegistry;

import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.registry.ItemRegistry;

public class ItemEmeraldSword extends ItemSword implements IHasRecipe {
	public static final String name = "emerald_sword";

	public ItemEmeraldSword() {

		super(ItemRegistry.MATERIAL_EMERALD);
	}

	@Override
	public void addRecipe() {

		GameRegistry.addShapedRecipe(new ItemStack(this), " e ", " e ", " s ", 'e', new ItemStack(Blocks.emerald_block), 's', new ItemStack(Items.stick));

	}
}
