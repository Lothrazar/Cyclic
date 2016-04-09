package com.lothrazar.cyclicmagic.net;

import com.lothrazar.cyclicmagic.gui.ContainerFakeWorkbench;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketOpenWindow;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class OpenCraftingPacket implements IMessage, IMessageHandler<OpenCraftingPacket, IMessage> {
	public static final int ID = 33;
	NBTTagCompound tags = new NBTTagCompound();

	public OpenCraftingPacket() {
	}

	public OpenCraftingPacket(NBTTagCompound ptags) {
		tags = ptags;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		tags = ByteBufUtils.readTag(buf);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		ByteBufUtils.writeTag(buf, this.tags);
	}

	@Override
	public IMessage onMessage(OpenCraftingPacket message, MessageContext ctx) {
		EntityPlayer p = ctx.getServerHandler().playerEntity;

		if (p.openContainer != null) {
			p.closeScreen();
		}

		EntityPlayerMP player = (EntityPlayerMP) p;

		player.getNextWindowId();

		player.playerNetServerHandler.sendPacket(new SPacketOpenWindow(player.currentWindowId,
				"minecraft:crafting_table", p.getDisplayName(), 0, player.getEntityId()));
		player.openContainer = new ContainerFakeWorkbench(player.inventory, player.worldObj);
		player.openContainer.windowId = player.currentWindowId;
		player.openContainer.onCraftGuiOpened(player);

		return null;
	}
}
