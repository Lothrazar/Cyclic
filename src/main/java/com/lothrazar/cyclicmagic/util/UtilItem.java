package com.lothrazar.cyclicmagic.util;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;

public class UtilItem {

	
	public static int getMaxDmgFraction(Item tool, int d) {

		return tool.getMaxDamage() - (int) MathHelper.floor_double(tool.getMaxDamage() / d);
	}
	public static void damageItem(EntityPlayer p, ItemStack s) {

		if(p.capabilities.isCreativeMode == false){
			s.damageItem(1, p);
		}
	}
	public static String getRawName(Item item) { 
		return item.getUnlocalizedName().replaceAll("item.", "");
	}
}
