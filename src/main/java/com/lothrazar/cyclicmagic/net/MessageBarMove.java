package com.lothrazar.cyclicmagic.net;
 
import com.lothrazar.cyclicmagic.util.UtilPlayerInventory;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

public class MessageBarMove implements IMessage, IMessageHandler<MessageBarMove, IMessage>
{
	public static final int ID = 25;
	public MessageBarMove()	{ 	}
	private boolean isDown;
	public MessageBarMove(  boolean upordown)	{ 	
	 
		isDown = upordown;
	}
	@Override
	public void fromBytes(ByteBuf buf)	{	
		NBTTagCompound tags = ByteBufUtils.readTag(buf);
		isDown = tags.getBoolean("isDown");
	}
	
	@Override
	public void toBytes(ByteBuf buf)	{	

		NBTTagCompound tags = new NBTTagCompound();
		tags.setBoolean("isDown", isDown);
		ByteBufUtils.writeTag(buf, tags);
	}
	
	@Override
	public IMessage onMessage(MessageBarMove message, MessageContext ctx)
	{  
		EntityPlayer player = ctx.getServerHandler().playerEntity; 

		if(message.isDown){
			UtilPlayerInventory.shiftBarDown(player);
		}
		else{
			UtilPlayerInventory.shiftBarUp(player);
		}
 	
		return null;
	}
}
 
