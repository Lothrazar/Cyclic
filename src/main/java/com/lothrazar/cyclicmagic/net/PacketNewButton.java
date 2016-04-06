package com.lothrazar.cyclicmagic.net;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.NetHandlerPlayServer;

import com.lothrazar.cyclicmagic.item.ItemEnderBook;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketNewButton  implements IMessage, IMessageHandler<PacketNewButton, IMessage>
{
	public PacketNewButton(){}
	private String name;
	public PacketNewButton(String n)
	{
		name = n;
	}
	@Override
	public void fromBytes(ByteBuf buf) 
	{
		name = ByteBufUtils.readUTF8String(buf);
	}

	@Override
	public void toBytes(ByteBuf buf) 
	{
		 ByteBufUtils.writeUTF8String(buf, name);
	}
	
	@Override
	public IMessage onMessage(PacketNewButton message, MessageContext ctx)
	{
		//since we are on the server right now:
		EntityPlayer player = ((NetHandlerPlayServer)ctx.netHandler).playerEntity;
		//otherwise, on the client we would use  Minecraft.getMinecraft().thePlayer;
	/*
		//this shouldnt happen anyway but just in case
		if(player.getHeldItem() == null || 
				!(player.getHeldItem().getItem() instanceof ItemEnderBook))
		{
			//then cancel
			return null;
		}
		*/
		//it now passes the stack, in case the players hand becomes null/empty at some point during process
		
		
		ItemEnderBook.saveCurrentLocation(player,message.name);
		
		
		return null;
	}
}
