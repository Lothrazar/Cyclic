package com.lothrazar.cyclicmagic.net;
 
import com.lothrazar.cyclicmagic.item.ItemCyclicWand;
import com.lothrazar.cyclicmagic.util.UtilSpellCaster;

import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class PacketBuildSize implements IMessage, IMessageHandler<PacketBuildSize, IMessage>
{
	public static final int ID = 34;
	public PacketBuildSize()	{ 	}
	private int size;
	public PacketBuildSize(int s)	{ 	
		size = s;
	}
	@Override
	public void fromBytes(ByteBuf buf)	{	
		NBTTagCompound tags = ByteBufUtils.readTag(buf);
		size = tags.getInteger("size");
	}
	
	@Override
	public void toBytes(ByteBuf buf)	{	

		NBTTagCompound tags = new NBTTagCompound();
		tags.setInteger("size", size);
		ByteBufUtils.writeTag(buf, tags);
	}
	
	@Override
	public IMessage onMessage(PacketBuildSize message, MessageContext ctx)
	{  
		EntityPlayer player = ctx.getServerHandler().playerEntity; 

		ItemStack wand = UtilSpellCaster.getPlayerWandIfHeld(player);
		
		if(wand != null){
			ItemCyclicWand.BuildType.setBuildSize(wand,message.size);
		}
		
		return null;
	}
}
 
