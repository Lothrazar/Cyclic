package com.lothrazar.cyclicmagic.net;

import com.lothrazar.cyclicmagic.item.ItemCyclicWand;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;

public class MessageTogglePlace implements IMessage, IMessageHandler<MessageTogglePlace, IMessage>{

	public MessageTogglePlace(){

	}

	@Override
	public void fromBytes(ByteBuf buf){

	}

	@Override
	public void toBytes(ByteBuf buf){

	}

	@Override
	public IMessage onMessage(MessageTogglePlace message, MessageContext ctx){

		EntityPlayer player = ctx.getServerHandler().playerEntity;

		if(player.getHeldItem() == null || player.getHeldItem().getItem() instanceof ItemCyclicWand == false){
			return null;
		}

		ItemCyclicWand.PlaceType.toggle(player.getHeldItem());

		return null;
	}
}
