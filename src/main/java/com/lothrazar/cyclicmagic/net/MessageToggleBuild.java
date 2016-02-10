package com.lothrazar.cyclicmagic.net;

import com.lothrazar.cyclicmagic.item.ItemCyclicWand;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;

public class MessageToggleBuild implements IMessage, IMessageHandler<MessageToggleBuild, IMessage> {
  

	public MessageToggleBuild() {
	}


	@Override
	public void fromBytes(ByteBuf buf) {

	}

	@Override
	public void toBytes(ByteBuf buf) {
	
	}

	@Override
	public IMessage onMessage(MessageToggleBuild message, MessageContext ctx) {

		EntityPlayer player = ctx.getServerHandler().playerEntity;
	 
		if(player.getHeldItem()==null || player.getHeldItem().getItem() instanceof ItemCyclicWand == false){
			return null;
		}
		
		ItemCyclicWand.BuildType.toggleBuildType(player.getHeldItem());
		
		return null;
	}
}
