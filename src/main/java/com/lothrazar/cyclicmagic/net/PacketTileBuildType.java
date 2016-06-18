package com.lothrazar.cyclicmagic.net;

import com.lothrazar.cyclicmagic.block.tileentity.TileEntityBuilder;
import com.lothrazar.cyclicmagic.block.tileentity.TileEntityBuilder.BuildType;
import com.lothrazar.cyclicmagic.util.UtilChat;

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

	public PacketTileBuildType() {

	}

	public PacketTileBuildType(BlockPos p) {
		pos = p;
	}

	@Override
	public void fromBytes(ByteBuf buf) {

		NBTTagCompound tags = ByteBufUtils.readTag(buf);

		int x = tags.getInteger("x");
		int y = tags.getInteger("y");
		int z = tags.getInteger("z");
		pos = new BlockPos(x, y, z);
	}

	@Override
	public void toBytes(ByteBuf buf) {

		NBTTagCompound tags = new NBTTagCompound();
		tags.setInteger("x", pos.getX());
		tags.setInteger("y", pos.getY());
		tags.setInteger("z", pos.getZ());

		ByteBufUtils.writeTag(buf, tags);
	}


	@Override
	public IMessage onMessage(PacketTileBuildType message, MessageContext ctx) {

		EntityPlayer player = ctx.getServerHandler().playerEntity;

		TileEntityBuilder container = (TileEntityBuilder)player.getEntityWorld().getTileEntity(message.pos);
		
		if(container != null){

			TileEntityBuilder.BuildType old = container.getBuildTypeEnum();
			TileEntityBuilder.BuildType next = TileEntityBuilder.BuildType.getNextType(old);
			container.setBuildType(next);

			container.setBuildType(next);
			
			UtilChat.addChatMessage(player, next.name());
		}

		return null;
	}
}
