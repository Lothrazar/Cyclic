package com.lothrazar.cyclicmagic.util;

import net.minecraft.item.Item;
import net.minecraft.util.math.MathHelper;

public class UtilItem{

	public static int getMaxDmgFraction(Item tool, int d){

		return tool.getMaxDamage() - (int) MathHelper.floor_double(tool.getMaxDamage() / d);
	}
}
