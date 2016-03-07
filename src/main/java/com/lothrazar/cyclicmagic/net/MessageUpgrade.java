package com.lothrazar.cyclicmagic.net;

import com.lothrazar.cyclicmagic.item.ItemCyclicWand;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageUpgrade implements IMessage, IMessageHandler<MessageUpgrade, IMessage>{

	public static final int upgrade = 50;
	public MessageUpgrade(){

	}

	@Override
	public void fromBytes(ByteBuf buf){

	}

	@Override
	public void toBytes(ByteBuf buf){

	}

	@Override
	public IMessage onMessage(MessageUpgrade message, MessageContext ctx){

		EntityPlayer player = ctx.getServerHandler().playerEntity;
		if(player.getHeldItem() != null && player.getHeldItem().getItem() instanceof ItemCyclicWand){
			ItemCyclicWand.Energy.increaseMaximum(player.getHeldItem(), upgrade);
		}
		return null;
	}
}
