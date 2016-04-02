package com.lothrazar.cyclicmagic.item.tool;

import com.lothrazar.cyclicmagic.registry.ItemRegistry;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;

public class ItemSamAxe extends ItemAxe
{
	public ItemSamAxe(ToolMaterial material) 
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
