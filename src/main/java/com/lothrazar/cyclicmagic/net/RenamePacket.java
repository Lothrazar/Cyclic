package com.lothrazar.cyclicmagic.net;

import io.netty.buffer.ByteBuf; 
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
 
public class RenamePacket  implements IMessage , IMessageHandler<RenamePacket, IMessage>
{
	NBTTagCompound tags = new NBTTagCompound(); 
	public RenamePacket(){}
	public RenamePacket(NBTTagCompound ptags)
	{
		tags = ptags;
	}

	@Override
	public void fromBytes(ByteBuf buf) 
	{
		tags = ByteBufUtils.readTag(buf);
	}

	@Override
	public void toBytes(ByteBuf buf) 
	{
		ByteBufUtils.writeTag(buf, this.tags);
	}
	@Override
	public IMessage onMessage(RenamePacket message, MessageContext ctx)
	{
		//EntityPlayer p = ctx.getServerHandler().playerEntity;
	 
		return null;
	}
 
}
