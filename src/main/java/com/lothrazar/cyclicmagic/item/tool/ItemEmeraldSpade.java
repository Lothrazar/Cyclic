package com.lothrazar.cyclicmagic.item.tool;

import com.lothrazar.cyclicmagic.item.IHasRecipe;
import com.lothrazar.cyclicmagic.registry.ItemRegistry;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ItemEmeraldSpade extends ItemSpade implements IHasRecipe{

	public ItemEmeraldSpade(){

		super(ItemRegistry.MATERIAL_EMERALD);
	}

	@Override
	public void addRecipe(){
		GameRegistry.addShapedRecipe(new ItemStack(this)
		, " e "," s "," s "
		,'e',new ItemStack(Blocks.emerald_block)
		,'s',new ItemStack(Items.stick));
	}
}
