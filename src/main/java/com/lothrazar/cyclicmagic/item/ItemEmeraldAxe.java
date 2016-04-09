package com.lothrazar.cyclicmagic.item;

import com.lothrazar.cyclicmagic.registry.ItemRegistry;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ItemEmeraldAxe extends ItemAxe implements IHasRecipe{

	public static final String name = "emerald_axe";
	public ItemEmeraldAxe(){

		super(ItemRegistry.MATERIAL_EMERALD);
	}

	@Override
	public boolean getIsRepairable(ItemStack toRepair, ItemStack repair){

		if(net.minecraftforge.oredict.OreDictionary.itemMatches(new ItemStack(ItemRegistry.REPAIR_EMERALD), repair, false)){
			return true;
		}
		return super.getIsRepairable(toRepair, repair);
	}

	@Override
	public void addRecipe(){
		GameRegistry.addShapedRecipe(new ItemStack(this)
		, "ee ","es "," s "
		,'e',new ItemStack(Blocks.emerald_block)
		,'s',new ItemStack(Items.stick));
		
		GameRegistry.addShapedRecipe(new ItemStack(this)
		, " ee"," se"," s "
		,'e',new ItemStack(Blocks.emerald_block)
		,'s',new ItemStack(Items.stick));
	}
}
