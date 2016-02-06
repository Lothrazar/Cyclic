package com.lothrazar.cyclicmagic.net;

//import com.lothrazar.util.Util;

import com.lothrazar.cyclicmagic.util.UtilParticle;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageParticle implements IMessage, IMessageHandler<MessageParticle, IMessage> {
	private int x;
	private int y;
	private int z;
	private int particle;

	public MessageParticle() {
	}

	public MessageParticle(BlockPos p, int part) {
		x = p.getX();
		y = p.getY();
		z = p.getZ();
		particle = part;
	}

	public MessageParticle(int _x, int _y, int _z, int part) {
		x = _x;
		y = _y;
		z = _z;
		particle = part;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		String csv = ByteBufUtils.readUTF8String(buf);

		String[] pts = csv.split(",");
		x = Integer.parseInt(pts[0]);
		y = Integer.parseInt(pts[1]);
		z = Integer.parseInt(pts[2]);
		particle = Integer.parseInt(pts[3]);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		//TODO: could use blockpos instead, might be cleaner
		ByteBufUtils.writeUTF8String(buf, x + "," + y + "," + z + "," + particle);
	}

	@Override
	public IMessage onMessage(MessageParticle message, MessageContext ctx) {
		if (ctx.side.isClient()) {
			// http://www.minecraftforge.net/forum/index.php?topic=21195.0
			if (Minecraft.getMinecraft().thePlayer == null) {
				return null;
			}// i think maybe possibly this was null once? [
			// 20:50:44] [Netty Local Client IO #0/ERROR] [FML]:
			// SimpleChannelHandlerWrapper exception
			// java.lang.NullPointerException
			// at
			// com.lothrazar.samscontent.potion.MessagePotion.onMessage(MessagePotion.java:69)
			// ~[MessagePotion.class:?]
			World world = Minecraft.getMinecraft().thePlayer.worldObj;// Minecraft.getMinecraft().getIntegratedServer().getEntityWorld();

			UtilParticle.spawnParticle(world, EnumParticleTypes.getParticleFromId(message.particle), new BlockPos(message.x, message.y, message.z));
		}

		return null;
	}
}
