package com.lothrazar.cyclicmagic.net;
 
import com.lothrazar.cyclicmagic.util.UtilPlayerInventory;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;

public class MessageBarDown implements IMessage, IMessageHandler<MessageBarDown, IMessage>
{
	public MessageBarDown()	{ 	}
	
	@Override
	public void fromBytes(ByteBuf buf)	{	}
	
	@Override
	public void toBytes(ByteBuf buf)	{	}
	
	@Override
	public IMessage onMessage(MessageBarDown message, MessageContext ctx)
	{  
		EntityPlayer player = ctx.getServerHandler().playerEntity; 
		
		UtilPlayerInventory.shiftBarDown(player);
 	
		return null;
	}
}
 
