package com.lothrazar.cyclicmagic.util;

import net.minecraft.entity.player.EntityPlayer; 
import net.minecraft.util.text.TextComponentTranslation;


public class UtilChat{

	public static void message(EntityPlayer player,String text){
		player.addChatMessage(new TextComponentTranslation(text));
	}
}
