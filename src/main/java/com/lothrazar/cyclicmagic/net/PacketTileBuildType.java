package com.lothrazar.cyclicmagic.net;

import com.lothrazar.cyclicmagic.block.tileentity.TileEntityBuilder;
import com.lothrazar.cyclicmagic.block.tileentity.TileEntityBuilder.BuildType;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketTileBuildType implements IMessage, IMessageHandler<PacketTileBuildType, IMessage> {

	public static final int ID = 55;
	private BlockPos			pos;
	int newType;

	public PacketTileBuildType() {

	}

	public PacketTileBuildType(BlockPos p, BuildType type) {
		pos = p;
		newType = type.ordinal();
	}

	@Override
	public void fromBytes(ByteBuf buf) {

		NBTTagCompound tags = ByteBufUtils.readTag(buf);

		int x = tags.getInteger("x");
		int y = tags.getInteger("y");
		int z = tags.getInteger("z");
		newType = tags.getInteger("T");
		pos = new BlockPos(x, y, z);
	}

	@Override
	public void toBytes(ByteBuf buf) {

		NBTTagCompound tags = new NBTTagCompound();
		tags.setInteger("x", pos.getX());
		tags.setInteger("y", pos.getY());
		tags.setInteger("z", pos.getZ());
		tags.setInteger("T", newType);

		ByteBufUtils.writeTag(buf, tags);
	}


	@Override
	public IMessage onMessage(PacketTileBuildType message, MessageContext ctx) {

		EntityPlayer player = ctx.getServerHandler().playerEntity;

		TileEntityBuilder tile = (TileEntityBuilder)player.getEntityWorld().getTileEntity(message.pos);
		
		System.out.println("got a tile build type packet: "+message.newType);
		
		if(tile != null){
			
			tile.setBuildType(TileEntityBuilder.BuildType.values()[message.newType]);
		}

		return null;
	}
}
