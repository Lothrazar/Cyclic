package com.lothrazar.cyclicmagic.util;

import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer; 
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;


public class UtilChat{

	public static void addChatMessage(EntityPlayer player,String text){
		player.addChatMessage(new TextComponentTranslation(text));
	}
	public static void addChatMessage(ICommandSender sender,String text){
		sender.addChatMessage(new TextComponentTranslation(text));
	}
	public static void addChatMessage(EntityPlayer player, ITextComponent textComponentTranslation){

		player.addChatMessage(textComponentTranslation);
	}
	

	public static String blockPosToString(BlockPos pos)
	{
		//boolean showCoords = !player.worldObj.getGameRules().getGameRuleBooleanValue("reducedDebugInfo");
		
		//if(showCoords)
			return pos.getX() + ", " + pos.getY() + ", " + pos.getZ();
		//else
			//return ModCommands.getDirectionsString(player, pos); 
	}
	public static String lang(String string){
		return I18n.translateToLocal(string);
	}
	public static void addChatMessage(World worldObj, ITextComponent textComponentTranslation){

		if(worldObj.getMinecraftServer() != null){
			worldObj.getMinecraftServer().addChatMessage(textComponentTranslation);
		}
		//else it is a client side world; cant do it
	}

}
