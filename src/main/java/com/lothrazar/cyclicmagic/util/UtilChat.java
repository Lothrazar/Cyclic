package com.lothrazar.cyclicmagic.util;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentTranslation;


public class UtilChat{

	public static void message(EntityPlayer player,String text){
		player.addChatMessage(new ChatComponentTranslation(text));
	}
}
