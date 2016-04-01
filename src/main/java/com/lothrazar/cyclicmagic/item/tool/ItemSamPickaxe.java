package com.lothrazar.cyclicmagic.item.tool;

import com.lothrazar.cyclicmagic.ItemRegistry;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;

public class ItemSamPickaxe extends ItemPickaxe
{
	public ItemSamPickaxe(ToolMaterial material) 
	{
		super(material); 
	}
	
	

	@Override
	public boolean getIsRepairable(ItemStack toRepair, ItemStack repair)
    {
        if (net.minecraftforge.oredict.OreDictionary.itemMatches(new ItemStack(ItemRegistry.REPAIR_EMERALD), repair, false)) {
        	return true;
        }
        return super.getIsRepairable(toRepair, repair);
    }
}
