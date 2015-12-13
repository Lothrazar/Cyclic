package com.lothrazar.samsmagic.potion;

//import com.lothrazar.util.Util;

import com.lothrazar.samsmagic.ModSpells;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessagePotion implements IMessage, IMessageHandler<MessagePotion, IMessage>
{
	private int x;
	private int y;
	private int z;
	private int particle;
	
	public static final int ID = 4;
	
	public MessagePotion()
	{ 
	}
	public MessagePotion(BlockPos p, int part)
	{ 
		x = p.getX();
		y = p.getY();
		z = p.getZ(); 
		particle = part;
	}
	public MessagePotion(int _x,int _y,int _z, int part)
	{ 
		x = _x;
		y = _y;
		z = _z; 
		particle = part;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) 
	{ 
		 String csv = ByteBufUtils.readUTF8String(buf);

		 String[] pts = csv.split(",");
		 x = Integer.parseInt(pts[0]);
		 y = Integer.parseInt(pts[1]);
		 z = Integer.parseInt(pts[2]);   
		 particle = Integer.parseInt(pts[3]);   
	}

	@Override
	public void toBytes(ByteBuf buf) 
	{
		 ByteBufUtils.writeUTF8String(buf, x+","+y+","+z+","+particle); 
	}

	@Override
	public IMessage onMessage(MessagePotion message, MessageContext ctx)
	{ 
		if(ctx.side.isClient()) 
		{ 
			//  http://www.minecraftforge.net/forum/index.php?topic=21195.0
			if(Minecraft.getMinecraft().thePlayer ==null){return null;}//i think maybe possibly this was null once? [
			//20:50:44] [Netty Local Client IO #0/ERROR] [FML]: SimpleChannelHandlerWrapper exception
			//java.lang.NullPointerException
			//at com.lothrazar.samscontent.potion.MessagePotion.onMessage(MessagePotion.java:69) ~[MessagePotion.class:?]
			World world = Minecraft.getMinecraft().thePlayer.worldObj;//Minecraft.getMinecraft().getIntegratedServer().getEntityWorld();
  
			ModSpells.spawnParticle(world, EnumParticleTypes.getParticleFromId(message.particle), new BlockPos(message.x,message.y,message.z));
		}
		 
		return null;
	}
}
