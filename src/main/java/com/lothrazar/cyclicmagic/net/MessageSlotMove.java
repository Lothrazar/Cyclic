package com.lothrazar.cyclicmagic.net;
 
import com.lothrazar.cyclicmagic.util.UtilInventory;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

public class MessageSlotMove implements IMessage, IMessageHandler<MessageSlotMove, IMessage>
{
	public static final int ID = 24;
	public MessageSlotMove()	{ 	}

	private int slot;
	private boolean isDown;
	public MessageSlotMove(int slotnum, boolean upordown)	{ 	
		slot = slotnum;
		isDown = upordown;
	}
	
	
	@Override
	public void fromBytes(ByteBuf buf)	{	
		NBTTagCompound tags = ByteBufUtils.readTag(buf);
		slot = tags.getInteger("slot");
		isDown = tags.getBoolean("isDown");
	}
	
	@Override
	public void toBytes(ByteBuf buf)	{	

		NBTTagCompound tags = new NBTTagCompound();
		tags.setInteger("slot", slot);
		tags.setBoolean("isDown", isDown);
		ByteBufUtils.writeTag(buf, tags);
	}
	
	@Override
	public IMessage onMessage(MessageSlotMove message, MessageContext ctx)
	{  
		EntityPlayer player = ctx.getServerHandler().playerEntity; 
 
		if(message.isDown){
			UtilInventory.shiftSlotUp(player, message.slot); 
		}
		else{
			UtilInventory.shiftSlotDown(player, message.slot); 
		}

		return null;
	}
}
 
