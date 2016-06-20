package com.lothrazar.cyclicmagic.net;
import com.lothrazar.cyclicmagic.block.tileentity.TileEntityBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketTileBuildSize implements IMessage, IMessageHandler<PacketTileBuildSize, IMessage> {
	public static final int ID = 55;
	private BlockPos pos;
	private int size;
	public PacketTileBuildSize() {
	}
	public PacketTileBuildSize(BlockPos p,int s) {
		pos = p;
		size = s;
	}
	@Override
	public void fromBytes(ByteBuf buf) {
		NBTTagCompound tags = ByteBufUtils.readTag(buf);
		int x = tags.getInteger("x");
		int y = tags.getInteger("y");
		int z = tags.getInteger("z");
		pos = new BlockPos(x, y, z);
		size = tags.getInteger("size");
	}
	@Override
	public void toBytes(ByteBuf buf) {
		NBTTagCompound tags = new NBTTagCompound();
		tags.setInteger("x", pos.getX());
		tags.setInteger("y", pos.getY());
		tags.setInteger("z", pos.getZ());
		tags.setInteger("size", size);
		ByteBufUtils.writeTag(buf, tags);
	}
	@Override
	public IMessage onMessage(PacketTileBuildSize message, MessageContext ctx) {
		EntityPlayerMP player = ctx.getServerHandler().playerEntity;
		TileEntityBuilder tile = (TileEntityBuilder) player.getEntityWorld().getTileEntity(message.pos);
		if (tile != null) {
			tile.setSize(tile.getSize() + message.size);
			tile.rebuildShape();
			tile.markDirty();
			if (player.openContainer != null) {
				player.openContainer.detectAndSendChanges();
				player.sendAllWindowProperties(player.openContainer, tile);
			}
		}
		return null;
	}
}
