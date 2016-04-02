package com.lothrazar.cyclicmagic.net;

import com.lothrazar.cyclicmagic.SpellCaster;
import com.lothrazar.cyclicmagic.item.ItemCyclicWand;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class MessageToggleBuild implements IMessage, IMessageHandler<MessageToggleBuild, IMessage>{

	public static final int ID = 17;

	public MessageToggleBuild(){

	}

	@Override
	public void fromBytes(ByteBuf buf){

	}

	@Override
	public void toBytes(ByteBuf buf){

	}

	@Override
	public IMessage onMessage(MessageToggleBuild message, MessageContext ctx){

		EntityPlayer player = ctx.getServerHandler().playerEntity;
		ItemStack wand = SpellCaster.getPlayerWandIfHeld(player);

		if(wand == null){
			return null;
		}

		ItemCyclicWand.BuildType.toggle(wand);

		return null;
	}
}
